package com.ebos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ebos.tables.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{
	
	
   
    List<Book> findByTitleOrAuthor(String title, String author);

    Optional<Book> findById(Long id);

	List<Book> findByTitle(String title);

	List<Book> findByAuthor(String author);
    
 
}
