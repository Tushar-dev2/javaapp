package com.ebos.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ebos.Service.BookService;
import com.ebos.tables.Book;

@RestController
@RequestMapping("/books")
public class BookController {

	@Autowired
	private BookService bookService;
	
	  @GetMapping("/getAllBooks")
	    public ResponseEntity<?> getAllBooks() {
	        try {
	        	
	            return new ResponseEntity<Map<String,Object>>(bookService.findAllBooks(), HttpStatus.OK);
	       
	        } catch (Exception e) {
	        	
	            return new ResponseEntity<Map<String,Object>>(bookService.findAllBooks(), HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
	  
	  @PostMapping("/addBooks")
	    public ResponseEntity<?> addBooks(@RequestBody List<Book> books) {
	        try {
	            return new ResponseEntity<Map<String,Object>>(bookService.addMultipleBooks(books), HttpStatus.OK);
	        } catch (Exception e) {
	            return new ResponseEntity<Map<String,Object>>(bookService.addMultipleBooks(books), HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
	  
	  @PutMapping("/updateBook")
	    public ResponseEntity<?> updateBook(@RequestBody Book book) {
	        try {
	            return new ResponseEntity<Map<String,Object>>(bookService.updateBook(book), HttpStatus.OK);
	        } catch (Exception e) {
	            return new ResponseEntity<Map<String,Object>>(bookService.updateBook(book), HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
	  
	  @GetMapping("/findBookByTitleOrAuthor/{title}/{author}")
	    public ResponseEntity<?> findByTitleOrAuthor(@PathVariable String title, @PathVariable String author) {
	        try {
	            return new ResponseEntity<Map<String,Object>>(bookService.findByTitleOrAuthor(title, author), HttpStatus.OK);
	        } catch (Exception e) {
	            return new ResponseEntity<Map<String,Object>>(bookService.findByTitleOrAuthor(title, author), HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
	  
	  @GetMapping("/getBookWithUsers/{bookId}")
	    public ResponseEntity<?> getBookWithUsers(@PathVariable Long bookId) {
	        try {
	            return new ResponseEntity<Map<String,Object>>(bookService.getBookWithUsers(bookId), HttpStatus.OK);
	        } catch (Exception e) {
	            return new ResponseEntity<Map<String,Object>>(bookService.getBookWithUsers(bookId), HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
	
}
