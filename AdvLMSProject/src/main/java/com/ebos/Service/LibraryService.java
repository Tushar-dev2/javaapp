package com.ebos.Service;

import java.util.Map;

public interface LibraryService {

	 public Map<String, Object> calculateDueFees(Long userId);
	
	 public Map<String, Object> payDueFees(Long bookIssueId, double amount); 
		    
	 public Map<String, Object> clearDueFeesOnReturn(Long bookIssueId);
		  	   
	 public Map<String, Object> applyDailyLateFee();
  
     public Map<String, Object> archiveOverdueBooks();
     
     Map<String, Object> issueBookToUser(Long userId, Long bookId);
     
}
