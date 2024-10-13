package com.ebos.ServiceImplimentation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebos.Service.LibraryService;
import com.ebos.repository.BookIssueRepository;
import com.ebos.repository.BookRepository;
import com.ebos.repository.UserRepository;
import com.ebos.tables.Book;
import com.ebos.tables.BookIssue;
import com.ebos.tables.User;

import jakarta.persistence.EntityManager;



@Service
public class LibraryServiceImpl implements LibraryService{

	@Autowired
	private BookIssueRepository bookIssueRepository;
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
	private BookRepository bookRepository;
	
		@Autowired
	    private EntityManager entityManager;
		
		
		@Transactional
		@Override
	    public Map<String, Object> calculateDueFees(Long userId) {
		     Map<String, Object> response = new HashMap<>();
		        
		        try {
		            // Fetch all book issues for the given user ID
		            List<BookIssue> bookIssues = bookIssueRepository.findByUserId(userId);
		            
		            if (bookIssues.isEmpty()) {
		                response.put("status", false);
		                response.put("message", "No book issues found for the user.");
		                return response;
		            }
		            
		            double totalDueFees = 0.0;
		            List<Map<String, Object>> dataList = new ArrayList<>();
		            
		            // Iterate over each book issue to calculate the due fees
		            for (BookIssue bookIssue : bookIssues) {
		                Map<String, Object> dueMap = new HashMap<>();
		                
		                dueMap.put("BookId", bookIssue.getBook().getId());
		                dueMap.put("UserId", bookIssue.getUser().getId());
		                dueMap.put("dueDate", bookIssue.getDueDate());
		                dueMap.put("IssueDate", bookIssue.getIssueDate());
		                
		                if (bookIssue.getDueDate().isBefore(LocalDate.now())) {
		                    long daysOverdue = ChronoUnit.DAYS.between(bookIssue.getDueDate(), LocalDate.now());
		                    double dueFees = daysOverdue * bookIssue.getDueFees();
		                    totalDueFees += dueFees;
		                    dueMap.put("DueFees", dueFees);
		                } else {
		                    dueMap.put("DueFees", 0.0);
		                }
		                
		                dataList.add(dueMap);
		            }
		            
		            response.put("CalculateDue", dataList);
		            response.put("totalDueFees", totalDueFees);
		            response.put("status", true);
		            response.put("message", "Success");
		            
		        } catch (Exception e) {
		            response.put("status", false);
		            response.put("message", "Failed: " + e.getMessage());
		        }
		        
		        return response;
		    }
		
	@Transactional
	@Override
	public Map<String, Object> payDueFees(Long bookIssueId, double amount) {

    Map<String, Object> response = new HashMap<>();
        
        try {
            BookIssue bookIssue = bookIssueRepository.findById(bookIssueId)
                    .orElseThrow(() -> new RuntimeException("Book Issue not Found"));
            
            double newDueFees = bookIssue.getDueFees() - amount;
            bookIssue.setDueFees(newDueFees);
            
            // Get the associated User instance
	        User user = bookIssue.getUser();
	        
	        // Subtract the due fees of this book issue from the user's total due fees
	        double bookIssueDueFees = bookIssue.getDueFees();
	        double userDueFees = user.getDueFees();
	        
	        user.setDueFees(userDueFees - bookIssueDueFees);
	        
            bookIssueRepository.save(bookIssue);
            userRepository.save(user);
            
            
            // Collecting details of the updated book issue
            Map<String, Object> issueDetails = new HashMap<>();
            issueDetails.put("bookId", bookIssue.getBook().getId());
            issueDetails.put("bookTitle", bookIssue.getBook().getTitle());
            issueDetails.put("userId", bookIssue.getUser().getId());
            issueDetails.put("userName", bookIssue.getUser().getName());
            issueDetails.put("dueDate", bookIssue.getDueDate());
            issueDetails.put("remainingDueFees", newDueFees);
            
            response.put("status", true);
            response.put("message", "Due fees paid successfully.");
            response.put("issueDetails", issueDetails);
        } catch (Exception e) {
            response.put("status", false);
            response.put("message", "Error paying due fees: " + e.getMessage());
        }
        
        return response;
    }

	
	@Transactional
	@Override
	public Map<String, Object> clearDueFeesOnReturn(Long bookIssueId) {
		
		 Map<String, Object> response = new HashMap<>();
		    
		    try {
		        // Fetch the BookIssue instance by its ID
		        BookIssue bookIssue = bookIssueRepository.findById(bookIssueId)
		                .orElseThrow(() -> new RuntimeException("Book Issue Not Found"));
		        
		        // Get the associated User instance
		        User user = bookIssue.getUser();
		        
		        // Subtract the due fees of this book issue from the user's total due fees
		        double bookIssueDueFees = bookIssue.getDueFees();
		        double userDueFees = user.getDueFees();
		        
		        user.setDueFees(userDueFees - bookIssueDueFees);
		        
		        // Set the due fees of the book issue to zero
		        bookIssue.setDueFees(0);
		        
		        // Save the updated BookIssue and User instances
		        bookIssueRepository.save(bookIssue);
		        userRepository.save(user); // Ensure you have a userRepository bean defined
		        
		        // Prepare the response
		        response.put("Due fees", 0.0);
		        response.put("status", true);
		        response.put("message", "Due fees cleared on book return.");
		    } catch (Exception e) {
		        // Handle any exceptions that occur
		        response.put("status", false);
		        response.put("message", "Error clearing due fees: " + e.getMessage());
		    }
		    
		    return response;
		}

	
	
	
	  private static final double DAILY_LATE_FEE = 100.0;
	  
	@Transactional
	@Override
	@Scheduled(cron = "0 0/30 * * * ?")
	public Map<String, Object> applyDailyLateFee() {
		 Map<String, Object> response = new HashMap<>();

	        try {
	            List<BookIssue> overdueIssues = bookIssueRepository.findByDueDateBefore(LocalDate.now());
	            Map<Long, Double> userFeeMap = new HashMap<>();
	            List<Map<String, Object>> feeDetails = new ArrayList<>();

	            for (BookIssue issue : overdueIssues) {
	                double newDueFees = issue.getDueFees() + DAILY_LATE_FEE;
	                issue.setDueFees(newDueFees);
	                bookIssueRepository.save(issue);

	                Long userId = issue.getUser().getId();
	                userFeeMap.put(userId, userFeeMap.getOrDefault(userId, 0.0) + DAILY_LATE_FEE);

	                // Collecting details of each overdue issue with updated fees
	                Map<String, Object> issueDetails = new HashMap<>();
	                issueDetails.put("bookId", issue.getBook().getId());
	                issueDetails.put("bookTitle", issue.getBook().getTitle());
	                issueDetails.put("userId", issue.getUser().getId());
	                issueDetails.put("userName", issue.getUser().getName());
	                issueDetails.put("dueDate", issue.getDueDate());
	                issueDetails.put("newDueFees", newDueFees);

	                feeDetails.add(issueDetails);
	            }

	            // Update the dueFees for each user based on accumulated fees
	            for (Map.Entry<Long, Double> entry : userFeeMap.entrySet()) {
	                User user = userRepository.findById(entry.getKey()).orElseThrow(() -> new IllegalArgumentException("User not found"));
	                user.setDueFees(user.getDueFees() + entry.getValue());
	                userRepository.save(user);
	            }

	            response.put("status", true);
	            response.put("message", "Daily late fees applied.");
	            response.put("feeDetails", feeDetails);
	        } catch (Exception e) {
	            System.err.println("Error applying daily late fees: " + e.getMessage());
	            response.put("status", false);
	            response.put("message", "Error applying daily late fees: " + e.getMessage());
	        }

	        return response;
	    }
			
	
	
	   
	   private static final double BLOCK_THRESHOLD = 5000.0;
	   
	   private static final int LOAN_PERIOD_DAYS = 0; // Assuming a 0-day loan period
	    @Transactional
	    @Override
	    public Map<String, Object> issueBookToUser(Long userId, Long bookId) {
	        Map<String, Object> response = new HashMap<>();
	        try {
	            User user = userRepository.findById(userId)
	                                       .orElseThrow(() -> new IllegalArgumentException("User not found"));
	            Book book = bookRepository.findById(bookId)
	                                       .orElseThrow(() -> new IllegalArgumentException("Book not found"));

	            double dueFees = user.getDueFees();

	            if (dueFees >= BLOCK_THRESHOLD) {
	                response.put("status", false);
	                response.put("message", "Cannot issue book: User has unpaid fees of " + dueFees + ". Please clear the dues to issue a new book.");
	                return response;
	            }

	            // Proceed with book issue logic
	            BookIssue newIssue = new BookIssue();
	            newIssue.setUser(user);
	            newIssue.setBook(book);
	            newIssue.setIssueDate(LocalDate.now());

	            // Calculate and set the due date
	            LocalDate dueDate = LocalDate.now().plusDays(LOAN_PERIOD_DAYS);
	            newIssue.setDueDate(dueDate);

	            bookIssueRepository.save(newIssue);

	            // Update the user's books set and save the user entity
	            user.getBooks().add(book);
	            userRepository.save(user);

	            response.put("status", true);
	            response.put("message", "Book issued successfully.");
	        } catch (Exception e) {
	            response.put("status", false);
	            response.put("message", "Error issuing book: " + e.getMessage());
	        }
	        return response;
	    }
	
	@Transactional
	@Override
	public Map<String, Object> archiveOverdueBooks() {
		 Map<String, Object> response = new HashMap<>();
		    List<Map<String, Object>> archivedBooksDetails = new ArrayList<>();
		    
		    try {
		        List<BookIssue> oldOverdueIssues = bookIssueRepository.findByDueDateBefore(LocalDate.now().minusYears(1));
		        for (BookIssue issue : oldOverdueIssues) {
		            // Logic to archive book issues (e.g., moving to archive table)
		            // Here we add a placeholder for the archiving logic, which should move the book issue to an archive table or mark it as archived.
		            
		            // Collect details of the archived book issue
		            Map<String, Object> bookDetails = new HashMap<>();
		            bookDetails.put("id", issue.getId());
		            bookDetails.put("bookTitle", issue.getBook().getTitle());
		            bookDetails.put("dueDate", issue.getDueDate());
		            bookDetails.put("User Name", issue.getUser().getName());
		            archivedBooksDetails.add(bookDetails);
		            
		           
		        }
		        
		        response.put("status", true);
		        response.put("message", "Old overdue books archived.");
		        response.put("archivedBooks", archivedBooksDetails);
		    } catch (Exception e) {
		        response.put("status", false);
		        response.put("message", "Error archiving overdue books: " + e.getMessage());
		    }
		    
		    return response;
		}

}
