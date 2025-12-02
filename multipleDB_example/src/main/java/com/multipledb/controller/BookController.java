package com.multipledb.controller;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.multipledb.Entity.ShortURLEntity;
import com.multipledb.Entity.ShortURLModel;
import com.multipledb.ExceptionHandler.BookNotFoundException;
import com.multipledb.bookModel.Book;
import com.multipledb.service.BookService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/book")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

	@Autowired
	private BookService bookService; 
	@PostMapping("/save")
	public ResponseEntity<Book> addBook(@RequestBody Book book) {
		System.out.println(book);
		Book addedBook =bookService.addBook(book);
//		return new ResponseEntity<>(addedBook,HttpStatus.CREATED);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(addedBook);

	}
	
//	@GetMapping("/get/{bookId}")
//	public ResponseEntity<Book> getBook(@PathVariable("bookId") int id) { //when pathvariable and parameter name does not match
	@GetMapping("/get/{bookId}")
	public ResponseEntity<Book> getBook(@PathVariable int bookId) {	//when pathvariable and parameter name match no need to write @PathVariable(name="bookId")
		Book addedBook =bookService.getBook(bookId);
//		return new ResponseEntity<>(addedBook,HttpStatus.CREATED);
		
		return ResponseEntity.status(HttpStatus.OK).body(addedBook);

	}
	
	@GetMapping("/getAll")
	public ResponseEntity<List<Book>> getAllBooks() {
		List<Book> listOfBooks =bookService.getAllBooks();
//		return new ResponseEntity<>(addedBook,HttpStatus.CREATED);
		
		return ResponseEntity.status(HttpStatus.OK).body(listOfBooks);

	}
	
	@PutMapping("/update/{bookId}")
	public ResponseEntity<Book> updateBook(@RequestBody Book book,@PathVariable int bookId) {
		
		Book updatedBook = bookService.updateBook(book,bookId);
		System.out.println("updatedBook in db -"+updatedBook);
		return ResponseEntity.status(HttpStatus.OK).body(updatedBook);
	}
	
	@DeleteMapping("/delete/{bookId}")
	public ResponseEntity<Book> deleteBook(@PathVariable(name="bookId") int bookId) {
		
//		@DeleteMapping("/delete/{bookId}")
//		public ResponseEntity<Book> deleteBook(@PathVariable(name="bookId") int id) { //when pathvariable and parameter name does not match
		
		Book deletedBook = bookService.deleteBook(bookId);
		System.out.println("deletedBook from db -"+deletedBook);
		return ResponseEntity.status(HttpStatus.OK).body(deletedBook);
	}
	
	@GetMapping("/getByParam/{bookId}")
	public ResponseEntity<Book> getBookByIdNameAuthor(@PathVariable int bookId,@RequestParam(name = "name",required = true) String bookName
			,@RequestParam(name = "author",required = true) String bookAuthor) {
		
		Book book = bookService.getBookByIdNameAuthor(bookId,bookName,bookAuthor);
	
		return ResponseEntity.status(HttpStatus.OK).body(book);
	}
	
	@GetMapping("/getByOrParam/{bookId}")
	public ResponseEntity<Book> getBookByIdOrNameOrAuthor(@PathVariable int bookId,@RequestParam(name = "name",required = true) String bookName
			,@RequestParam(name = "author",required = true) String bookAuthor) {
		
		Book book = bookService.getBookByIdOrNameOrAuthor(bookId,bookName,bookAuthor);
	
		return ResponseEntity.status(HttpStatus.OK).body(book);
	}
	
	
//xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx BELOW ARE CALLS FOR OTHER TINYURL MICROSERVICE XXXXXXXXXXXXXXXXXXXXXXXX

	@GetMapping("/getShortUrl/{shortKey}")
	@CircuitBreaker(name="tinyUrlCircuit" , fallbackMethod = "fallBackMethodForGet")
	public ResponseEntity<String> getShortUrl(@PathVariable String shortKey) {
		
		String originalUrl = bookService.getOriginalUrl(shortKey);
		HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("http://"+originalUrl));
		return new ResponseEntity<>(headers, HttpStatus.FOUND);
	}
	public ResponseEntity<String> fallBackMethodForGet(String shortKey, Throwable e){
		logger.info("fallback method is executed because service is down: {}",e.getMessage());
		return new ResponseEntity<>("cannot found original url with short key "+shortKey+ 
				" .As service is down", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@PutMapping("/updateShortUrlEntity/{shortKey}")
	@CircuitBreaker(name="tinyUrlCircuit2" , fallbackMethod = "fallBackMethodForUpdate")
	public ResponseEntity<?> updateShortURLEntity(@RequestBody ShortURLModel shortURLModel,
			@PathVariable String shortKey) {
		try {
		ShortURLEntity shortURLEntity = bookService.updateShortURLEntity(shortURLModel,shortKey);
		
		return new ResponseEntity<>(shortURLEntity, HttpStatus.CREATED);
		
		}catch (HttpClientErrorException  ex) {
	        System.out.println("Error body1 = " + ex.getResponseBodyAsString());
	        throw new BookNotFoundException(HttpStatus.NOT_FOUND.toString()
					,"failed","Short url not found with ShortKey 1: " + shortKey);
	    }catch (HttpServerErrorException  ex) {
	        System.out.println("Error body2 = " + ex.getResponseBodyAsString());
	        throw new BookNotFoundException(HttpStatus.NOT_FOUND.toString()
					,"failed","Short url not found with ShortKey2: " + shortKey);
	    }catch (Exception ex) {
	    	throw new BookNotFoundException(HttpStatus.NOT_FOUND.toString()
					,"failed","Short url not found with ShortKey3: " + shortKey);
	    }
	}
	
	public ResponseEntity<?> fallBackMethodForUpdate(ShortURLModel shortURLModel, String shortKey, Throwable e){
		logger.info("fallback method for update is executed because service is down: {}",e.getMessage());
		return new ResponseEntity<>("cannot update original url with short key- "+shortKey+ 
				" .As service is down", HttpStatus.INTERNAL_SERVER_ERROR);
	}

//	@PutMapping("/updateShortUrlEntity/{shortKey}")
//	public Mono<ResponseEntity<?>> updateShortURLEntity(
//	        @RequestBody ShortURLModel shortURLModel,
//	        @PathVariable String shortKey) throws InterruptedException {
//
//	    return bookService.updateShortURLEntity(shortURLModel, shortKey)
//	            .map(entity -> new ResponseEntity<>(entity, HttpStatus.CREATED));
//	}

}
