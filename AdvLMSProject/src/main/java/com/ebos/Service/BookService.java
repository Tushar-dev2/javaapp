package com.ebos.Service;

import java.util.List;
import java.util.Map;

import com.ebos.tables.Book;

public interface BookService {
	
	public Map<String, Object> findAllBooks();
	
	public Map<String, Object> addMultipleBooks(List<Book> books);
	
	public Map<String, Object> updateBook(Book book);
	
	public Map<String, Object> findByTitleOrAuthor(String title, String author);
	
	public Map<String, Object> getBookWithUsers(Long bookId);
}
