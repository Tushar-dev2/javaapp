package com.ebos.ServiceImplimentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebos.Service.BookService;
import com.ebos.repository.BookRepository;
import com.ebos.tables.Book;
import com.ebos.tables.User;

import jakarta.persistence.Id;

@Service
public class BookServiceImpl implements BookService{
	
	
	@Autowired
	private BookRepository bookRepository;

	@Override
	public Map<String, Object> findAllBooks() {
		
		Map<String, Object> map = new HashMap<>();
		
		List<Map<String, Object>> dataList = new ArrayList<>();
		
		try {
			
			List<Book> list = bookRepository.findAll();
			
			list.forEach(books->{
				
				Map<String, Object> bookMap = new HashMap<>();
				
				bookMap.put("BookId", books.getId());
				bookMap.put("BookTitle", books.getTitle());
				bookMap.put("BookAuthor", books.getAuthor());
				
				dataList.add(bookMap);
				
				});
			
			
				map.put("Books", dataList);
				map.put("status", true);
				map.put("message", "Success");
			
		} catch (Exception e) {
				map.put("status", false);
				map.put("message", "Failed");
		}
			return map;
	}


	@Override
	public Map<String, Object> addMultipleBooks(List<Book> books) {
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			
			List<Book> savedBooks = bookRepository.saveAll(books);
			
			response.put("status", true);
	        response.put("message", "Books added successfully");
	        response.put("data", savedBooks);
	        
			
		} catch (Exception e) {
		
			response.put("status", false);
			response.put("message", "Failed");
		}
		
		return response;
	}

	@Override
	public Map<String, Object> updateBook(Book book) {
		
		 Map<String, Object> response = new HashMap<>();
		    
		    try {
		        // Extract the ID from the Book object
		        Long bookId = book.getId();
		        
		        // Find the Book in the database by ID
		        Optional<Book> optionalBook = bookRepository.findById(bookId);
		        
		        if (optionalBook.isPresent()) {
		            // Book exists, update its fields
		            Book existingBook = optionalBook.get();
		            
		            existingBook.setTitle(book.getTitle());
		            existingBook.setAuthor(book.getAuthor());
		           
		            
		            // Save the updated book
		            Book updatedBook = bookRepository.save(existingBook);
		            
		            // Prepare the response
		            response.put("success", true);
		            response.put("message", "Book updated successfully");
		            response.put("data", updatedBook);
		        } else {
		            // Book not found
		            response.put("success", false);
		            response.put("message", "Book not found");
		        }
		    } catch (Exception e) {
		        // Handle exceptions
		        response.put("success", false);
		        response.put("message", "Error updating book: " + e.getMessage());
		    }
		    
		    return response;
		}
	@Override
	public Map<String, Object> findByTitleOrAuthor(String title, String author) {
	    
	    Map<String, Object> response = new HashMap<>();
	    
	    try {
	       
	        List<Book> books = bookRepository.findByTitleOrAuthor(title, author);
	    	
	        // Prepare response data
	        List<Map<String, Object>> bookList = new ArrayList<>();
	        
	        for (Book book : books) {
	            Map<String, Object> bookMap = new HashMap<>();
	            bookMap.put("BookId", book.getId());
	            bookMap.put("BookTitle", book.getTitle());
	            bookMap.put("BookAuthor", book.getAuthor());
	            
	            bookList.add(bookMap);
	        }
	        
	        response.put("Books", bookList);
	        response.put("status", true);
	        response.put("message", "Success");
	        
	    } catch (Exception e) {
	        response.put("status", false);
	        response.put("message", "Failed");
	    }
	    
	    return response;
	}


	@Override
	public Map<String, Object> getBookWithUsers(Long bookId) {
		
		 Map<String, Object> response = new HashMap<>();
		    
		    try {
		        // Find the book by ID
		        Optional<Book> optionalBook = bookRepository.findById(bookId);
		        
		        if (optionalBook.isPresent()) {
		            Book book = optionalBook.get();
		            
		            // Prepare book details
		            Map<String, Object> bookMap = new HashMap<>();
		            bookMap.put("id", book.getId());
		            bookMap.put("title", book.getTitle());
		            bookMap.put("author", book.getAuthor());
		            
		            // Prepare users list
		            List<Map<String, Object>> usersList = new ArrayList<>();
		            for (User user : book.getUsers()) {
		                Map<String, Object> userMap = new HashMap<>();
		                userMap.put("id", user.getId());
		                userMap.put("name", user.getName());
		                userMap.put("username", user.getUsername());
		                userMap.put("email", user.getEmail());
		                usersList.add(userMap);
		            }
		            
		            // Add book and users to response
		            response.put("success", true);
		            response.put("book", bookMap);
		            response.put("users", usersList);
		        } else {
		            // Book not found
		            response.put("success", false);
		            response.put("message", "Book not found");
		        }
		    } catch (Exception e) {
		        // Handle exceptions
		        response.put("success", false);
		        response.put("message", "Error retrieving book: " + e.getMessage());
		    }
		    
		    return response;
		}

}
