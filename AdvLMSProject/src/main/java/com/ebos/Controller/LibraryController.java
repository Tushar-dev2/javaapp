package com.ebos.Controller;

import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.event.PublicInvocationEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ebos.Service.LibraryService;

@RestController
@RequestMapping("/library")
public class LibraryController {
	
	@Autowired
	private LibraryService libraryService;
	
	@GetMapping("/calculateDueFees")
	public ResponseEntity<?> calculateDueFees(@RequestParam Long userId){
		
		try {
			
            return new ResponseEntity<Map<String,Object>>(libraryService.calculateDueFees(userId), HttpStatus.OK);
		
		} catch (Exception e) {
			
            return new ResponseEntity<Map<String,Object>>(libraryService.calculateDueFees(userId), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@PostMapping("/payDueFees")
	public ResponseEntity<?> payDueFees(@RequestParam Long bookIssueId, @RequestParam double amount){
		
		try {
			
            return new ResponseEntity<Map<String,Object>>(libraryService.payDueFees(bookIssueId, amount), HttpStatus.OK);
		
		} catch (Exception e) {
            
			return new ResponseEntity<Map<String,Object>>(libraryService.payDueFees(bookIssueId, amount), HttpStatus.INTERNAL_SERVER_ERROR);
		
		}
	}
	
	
	@PostMapping("/clearDueFeesOnReturn")
	public ResponseEntity<?> clearDueFeesOnReturn(@RequestParam Long bookIssueId){
		
		try {
			
            return new ResponseEntity<Map<String,Object>>(libraryService.clearDueFeesOnReturn(bookIssueId), HttpStatus.OK);

		} catch (Exception e) {
			
            return new ResponseEntity<Map<String,Object>>(libraryService.clearDueFeesOnReturn(bookIssueId), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	@GetMapping("/dailyLateFee")
	public ResponseEntity<?> applyDailyLateFee(){
		
		try {
			
            return new ResponseEntity<Map<String,Object>>(libraryService.applyDailyLateFee(), HttpStatus.OK);

			
		} catch (Exception e) {

            return new ResponseEntity<Map<String,Object>>(libraryService.applyDailyLateFee(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	
	
	
	@PostMapping("/issueBook")
	public ResponseEntity<?> issueBookToUser(@RequestParam Long userId, @RequestParam Long bookId){
		
		try {
			
            return new ResponseEntity<Map<String,Object>>(libraryService.issueBookToUser(userId, bookId), HttpStatus.OK);

		} catch (Exception e) {

            return new ResponseEntity<Map<String,Object>>(libraryService.issueBookToUser(userId, bookId), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	@PostMapping("/archiveOverdueBooks")
	public ResponseEntity<?> archiveOverdueBooks(){
		
		try {
			
            return new ResponseEntity<Map<String,Object>>(libraryService.archiveOverdueBooks(), HttpStatus.OK);

		} catch (Exception e) {

            return new ResponseEntity<Map<String,Object>>(libraryService.archiveOverdueBooks(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
}
