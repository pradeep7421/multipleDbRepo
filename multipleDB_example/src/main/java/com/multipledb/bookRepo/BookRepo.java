package com.multipledb.bookRepo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.multipledb.bookModel.Book;

@Repository
public interface BookRepo extends JpaRepository<Book, Integer> {

//	Book findByIdAndNameAndAuthor(int bookId, String bookName, String bookAuthor);

	//By JPQL QUERRY 
	@Query("SELECT b FROM Book b WHERE b.id = :id AND b.name = :name AND b.author = :author")
    Optional<Book> findByIdAndNameAndAuthor(@Param("id") int bookId,
                  @Param("name") String bookName,
                  @Param("author") String author);
	
//	Book findByIdOrNameOrAuthor(int bookId, String bookName, String bookAuthor);

	//By JPQL QUERRY 
	@Query("SELECT b FROM Book b WHERE b.id = :id Or b.name = :name Or b.author = :author")
    Optional<Book> findByIdOrNameOrAuthor(@Param("id") int bookId,
                  @Param("name") String bookName,
                  @Param("author") String author);
}
