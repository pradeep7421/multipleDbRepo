package com.multipledb.service;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import com.multipledb.Entity.ShortURLEntity;
import com.multipledb.Entity.ShortURLModel;
import com.multipledb.ExceptionHandler.BookNotFoundException;
import com.multipledb.bookModel.Book;
import com.multipledb.bookRepo.BookRepo;
import com.multipledb.feign.BookUserInterface;
import com.multipledb.transactional.OverallTransactionalModel;
import com.multipledb.userModel.Users;
import com.multipledb.userRepo.UserRepo;
import com.multipledb.utils.OtherUtility;

import jakarta.transaction.Transactional;
import reactor.core.publisher.Mono;

@Service
public class BookService {
	@Autowired
	private BookRepo bookRepo;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private RestTemplate restTemplate;
	@Value("${tinyUrl.get.url}")
	private String tinyUrlServiceBaseUrl;
	@Autowired
	private BookUserInterface interf;
	@Autowired
	private WebClient.Builder builder;
	@Autowired
	private OtherUtility otherUtility;
	@Autowired
	private CacheManager cacheManager;
	public Book addBook(Book book) {
		Users user = new Users();
		user.setUserName("ravi");
		user.setUserEmail("ravi@gmail.com");
		userRepo.save(user);
		return bookRepo.save(book);
	}

	//unless = "#result.id < 1000  means do not cache if condition is true
//	@Cacheable(key = "#bookId", value = "books", unless = "#result.id < 1000") //condition is checked after getting result from db will cache while result.Id>1000
	@Cacheable(value = "books", key = "#bookId", condition = "#bookId > 1000") //condition is checked before method execution will cache if bookId >1000
	public Book getBook(int bookId) {
		System.out.println("Getting result from DB call with - "+bookId+" ---------------------");
		return bookRepo.findById(bookId).get();
	}

	public Book getBook2(int bookId) {

	    Cache cache = cacheManager.getCache("books");

	    // 🔹 CACHE LOOKUP
	    if (cache != null) {
	        Book cachedBook = cache.get(bookId, Book.class);
	        if (cachedBook != null) {
	            System.out.println("Getting result from CACHE ---------------------");
	            return cachedBook;
	        }
	    }

	    // 🔹 CACHE MISS → DB CALL
	    System.out.println("Cache MISS for bookId=" + bookId + " → DB call");
	    Book book = bookRepo.findById(bookId)
	            .orElseThrow(() -> new RuntimeException("Book not found"));

	    // 🔹 UNLESS CONDITION (after DB call)
	    if (book.getId() < 1000) {
	        System.out.println("Skipping cache (unless condition: while bookId < 1000 skipping the cache storage)");
	        return book;
	    }

	    // 🔹 UPDATE CACHE
	    if (cache != null && book.getId() > 1000) {
	        System.out.println("Updating cache after DB call if bookId >1000------------------");
	        cache.put(bookId, book);
	    }

	    return book;
	}


	public List<Book> getAllBooks() {
		return bookRepo.findAll();
	}
	
	@CachePut(value = "books",key = "#bookId")
	public Book updateBook(Book book, int bookId) {

        // fetch the existing book or throw error
//        Book existingBook = bookRepo.findById(bookId)
//                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));
        
//        Book existingBook = bookRepo.findById(bookId)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Book not found with Id"+bookId));
       
        Book existingBook = bookRepo.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(HttpStatus.NOT_FOUND.toString(),"failed","Book not found with id: " + bookId));

        System.out.println("existing book in db "+existingBook);
        // update fields
        existingBook.setName(book.getName());
        existingBook.setAuthor(book.getAuthor());

        // save updated object
        Book updatedBook = bookRepo.save(existingBook);
        
        // 🔹 UPDATE CACHE
        Cache cache = cacheManager.getCache("books");
	    if (cache != null && book.getId() > 1000) {
	        System.out.println("Updating cache after DB call if bookId >1000------------------");
	        cache.put(bookId, updatedBook);
	    }
	    return updatedBook;
    }
	
	@CacheEvict(key = "#bookId",value = "books")
	public Book deleteBook(int bookId) {

        // Find book by id or throw 404
        Book existingBook = bookRepo.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Book not found with id: " + bookId
                ));

        // Delete the record
        bookRepo.delete(existingBook);
        
        Cache cache = cacheManager.getCache("books");

		if (cache != null) {
		    System.out.println("Deleting entry from CACHE ---------------------");
		    cache.evict(bookId);
		}
        // Return deleted object as response
        return existingBook;
    }

	public Book getBookByIdNameAuthor(int bookId, String bookName, String bookAuthor) {
		Book book = bookRepo.findByIdAndNameAndAuthor(bookId,bookName,bookAuthor)
				.orElseThrow(() -> new BookNotFoundException(HttpStatus.NOT_FOUND.toString()
						,"failed","Book not found with id: " + bookId+", bookName -"+bookName+", author - "+ bookAuthor));
		return book;
	}
	
	public Book getBookByIdOrNameOrAuthor(int bookId, String bookName, String bookAuthor) {
		Book book = bookRepo.findByIdOrNameOrAuthor(bookId,bookName,bookAuthor)
				.orElseThrow(() -> new BookNotFoundException(HttpStatus.NOT_FOUND.toString()
						,"failed","Book not found with id: " + bookId+",or bookName -"+bookName+",or author - "+ bookAuthor));
		return book;
	}
	
	
	
	
//xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx BELOW ARE CALLS FOR OTHER TINYURL MICROSERVICE XXXXXXXXXXXXXXXXXXXXXXXX
	
	
	
	public String getOriginalUrl(String shortKey) {
		String apiUrl = tinyUrlServiceBaseUrl+shortKey;
		String originalUrl =restTemplate.getForObject(apiUrl, String.class);
		
//      Calling Get Request of tinyUrl service By Feign Client 	
//		ResponseEntity<String> feignResponse = interf.getShortURL(shortKey);
//		String originalUrl = feignResponse.getBody();
//		System.out.println("OriginalUrl -  "+feignResponse.getBody());
		
		return originalUrl;
	}

	public ShortURLEntity updateShortURLEntity(ShortURLModel body , String shortKey) {
//	public Mono<ShortURLEntity> updateShortURLEntity(ShortURLModel body , String shortKey) throws InterruptedException {
//		String apiUrl = tinyUrlServiceBaseUrl+shortUrl+"?isActive=true&length=2";
//		String apiUrl = UriComponentsBuilder.fromUriString(tinyUrlServiceBaseUrl + shortKey)
//		        .queryParam("isActive", true)
//		        .queryParam("length", 2)
//		        .toUriString();
	
//		HttpHeaders reqHeaders = new  HttpHeaders();
//		reqHeaders.setContentType(MediaType.APPLICATION_JSON);
//		HttpEntity<ShortURLModel> httpEntity = new HttpEntity<>(body, reqHeaders);
		
//		.put method can send body and header but does not give response whereas 
//		.exchange method sends req with body & headers and also provides response
//		restTemplate.put(apiUrl, httpEntity); //it does not return anything
//		ResponseEntity<ShortURLEntity> response =restTemplate.
//				exchange(apiUrl, HttpMethod.PUT,httpEntity,ShortURLEntity.class);
//		System.out.println("shortURLEntity -  "+response.getBody());
//		return response.getBody();

//By using Map object as params in put and exchange method
//		String url = tinyUrlServiceBaseUrl + "{shortKey}?isActive={isActive}&length={length}";
//		Map<String, Object> params = new HashMap<>();
//		params.put("shortKey", shortKey);
//		params.put("isActive", true);
//		params.put("length", 2);
//		restTemplate.put(url, httpEntity,params); //it does not return anything
//		ResponseEntity<ShortURLEntity> response2 =restTemplate.
//				exchange(apiUrl, HttpMethod.PUT,httpEntity,ShortURLEntity.class,params);
//		System.out.println("shortURLEntity -  "+response2.getBody());
//		
//		return response2.getBody();
		
		
//      Calling Put Request of tinyUrl service By Feign Client 		
		ResponseEntity<ShortURLEntity> feignResponse =interf.updateShortURLEntity(body, shortKey, true, 2);
		System.out.println("shortURLEntity -  "+feignResponse.getBody());
		return feignResponse.getBody();

		//BY USING WEBCLIENT 
//		String baseUrl = tinyUrlServiceBaseUrl;
//		System.out.println(baseUrl);
//		Mono<ShortURLEntity> shortURLEntity =builder.build()
//		.put()
//		.uri( //baseUrl+shortKey+"?isActive=true&length=2"
//				uriBuilder->uriBuilder
//				.scheme("http")
//				.host("localhost")
//				.port(9090)
//				.path("/tinyurl/{shortKey}")
//				.queryParam("isActive", true)
//				.queryParam("length", 2)
//				.build(shortKey)
//				)
//		.contentType(MediaType.APPLICATION_JSON)
//		.bodyValue(body)
//		.retrieve()
//		.bodyToMono(ShortURLEntity.class)
////	.block();  //Add If wanted synchronous waiting call and remove below code doOnSubscribe() , doOnSuccess()
//		.doOnSubscribe(s->{
//		System.out.println("Request started");
//		int count =0;
//		while(count <10) {
//			System.out.println("count value - "+ count);
//			count++;
//		}
//		})
//		.doOnSuccess(r->System.out.println("got async response - "+ r));
//		
//		return shortURLEntity;
	}
	
	public void requestsWithDifferentHttpMethodsByExchangeMethod(ShortURLModel body , String shortKey) {	
		 otherUtility.requestsWithDifferentHttpMethodsByExchangeMethod(body,shortKey);
		 otherUtility.requestsWithDifferentHttpMethodsWithoutExchange(body,shortKey);	
	}

	@Transactional
	public Book saveBookAndUserTransactionDemo(OverallTransactionalModel overallTransactionalModel) {
	
		Book addedBook = bookRepo.save(overallTransactionalModel.getBook());
		
		if(StringUtils.isBlank(overallTransactionalModel.getUser().getUserName())) {
			throw new BookNotFoundException(HttpStatus.INTERNAL_SERVER_ERROR.toString()
					,"failed","Book Name should not be blank");
		}
		userRepo.save(overallTransactionalModel.getUser());
		return addedBook;
	}
	
}

