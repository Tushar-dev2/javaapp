package com.ebos.ServiceImplimentation;

import java.util.ArrayList;




import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ebos.Request.SignUpRequest;
import com.ebos.Response.ApiResponse;
import com.ebos.Response.DeleteResponse;
import com.ebos.Response.GetUserResponse;
import com.ebos.Response.SetListResponse;
import com.ebos.Response.UpdateResponse;
import com.ebos.Service.UserService;
import com.ebos.repository.BookRepository;
import com.ebos.repository.UserRepository;
import com.ebos.security.UserPrincipal;
import com.ebos.tables.Book;
import com.ebos.tables.User;

@Service
public class UserServiceImpls implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BookRepository bookRepository;
	  @Autowired
	  private PasswordEncoder passwordEncoder;

	@Override
	  public Map<String, Object> findAll() {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> dataList = new ArrayList<>();
        try {
        	UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            List<User> list = userRepository.findAll();

            list.forEach(users -> {
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("name", users.getName());
                userMap.put("username", users.getUsername());
                userMap.put("email", users.getEmail());
                userMap.put("password", users.getPassword());

                // Fetch book for the users
               Set<Book> books = users.getBooks();
                // Extract title and author from courses
                List<Map<String, Object>> bookList = new ArrayList<>();
                for (Book book : books) {
                    Map<String, Object> bookMap = new HashMap<>();
                    bookMap.put("BookTitle", book.getTitle());
                    bookMap.put("BookAuthor", book.getAuthor());
                    bookList.add(bookMap);
                }
                userMap.put("Books", bookList);

                dataList.add(userMap);
            });

            map.put("users", dataList);
            map.put("status", true);
			map.put("message", "Success");
        } catch (Exception e) {
        	map.put("status", false);
			map.put("message", "Failed");
        }
        return map;
    }

	
	@Override
	public Map<String, Object> save(SignUpRequest signUpRequest) {
	
		
		Map<String, Object> map = new HashMap<>();
		
		
		try {
			UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			User user2 = new User();
			
			user2.setName(signUpRequest.getName());
			user2.setUsername(signUpRequest.getUsername());
			user2.setEmail(signUpRequest.getEmail());
			user2.setPassword(signUpRequest.getPassword());
			
			User user3 = userRepository.save(user2);
			
			
			map.put("user", user3);
			map.put("Successs", true);
			map.put("Message", "User saved successfully");
		} catch (Exception e) {
            map.put("success", false);
            map.put("message", "Failed to save user: " + e.getMessage());
      
		}
		return map;
	}
	
	@Override
	public Map<String, Object> deleteUser(Long id) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	        // Get the currently authenticated user
	        UserPrincipal authenticatedUser = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

	        // Check if the authenticated user has admin privileges or is deleting their own account
	        if (authenticatedUser.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) || authenticatedUser.getId().equals(id)) {
	            // Find the user to delete
	            Optional<User> userOptional = userRepository.findById(id);
	            if (userOptional.isPresent()) {
	                User userToDelete = userOptional.get();
	                
	                // Remove the associations between the user and books
	                userToDelete.getBooks().clear();
	                
	                // Delete the user
	                userRepository.delete(userToDelete);
	                
	                response.put("message", "User deleted successfully");
	                response.put("status", "true");
	            } else {
	                response.put("message", "User not found");
	                response.put("status", "false");
	            }
	        } else {
	            response.put("message", "You are not authorized to delete this user");
	            response.put("status", "false");
	        }
	    } catch (Exception e) {
	        response.put("message", "Failed to delete user");
	        response.put("status", "false");
	    }
	    return response;
	}

	@Override
	public Map<String, Object> updateUser(SignUpRequest signUpRequest) {
		
		Map<String, Object> response = new HashMap<>();
		
	try {
		
		UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		  // Find the user in the database by ID
		Optional<User> optionalUser = userRepository.findById(user.getId());
		
		if (optionalUser.isPresent()) {
			
			User user2 = optionalUser.get();
			
			// Update user details with the provided SignUpRequest data
			
			user2.setEmail(signUpRequest.getEmail());
			user2.setName(signUpRequest.getName());
			user2.setUsername(signUpRequest.getUsername());
			 // Encode the password before setting it
            String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
            user2.setPassword(encodedPassword);
			
			// Save the updated user object

			userRepository.save(user2);
			
			response.put("message", "User updated successfully");
            response.put("success", true);
		} else {
			
			  // If user is not found, prepare response accordingly
            response.put("message", "User not found");
            response.put("success", false);
		}
		
	} catch (Exception e) {
		e.printStackTrace();
		 response.put("message", "Failed to update user");
	        response.put("success", false);
	        
	}
			return response;
		}
	


	

	@Override
	public Map<String, Object> findByname(String name) {
		
		Map<String, Object> map = new HashMap<>();
		List<Map<String, Object>> dataList = new ArrayList<>();
		
		try {
			
			UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			List<User> list = userRepository.findByname(name);
			
			list.forEach(users->{
				Map<String, Object> userMap = new HashMap<>();
				userMap.put("id", users.getId());
				userMap.put("username", users.getUsername());
				userMap.put("email", users.getEmail());
				userMap.put("password", user.getPassword());
				
				Set<Book> books = users.getBooks();
				
				List<Map<String, Object>> bookList = new ArrayList<>();
				
				for (Book book : books) {
					 Map<String, Object> bookMap = new HashMap<>();
					 bookMap.put("BookTitle", book.getTitle());
					 bookMap.put("BookAuthor", book.getAuthor());
					 bookList.add(bookMap);
				}
				userMap.put("Books", bookList);
				 dataList.add(userMap);
				
			});
			
			 map.put("users", dataList);
	         map.put("status", true);
	         map.put("message", "Success");
			
			
		} catch (Exception e) {
			map.put("status", false);
	         map.put("message", "Failed");
		}
		
		return map;
	}


	
	
}
	