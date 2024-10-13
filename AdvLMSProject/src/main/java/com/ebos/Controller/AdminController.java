package com.ebos.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ebos.Response.DeleteResponse;
import com.ebos.Response.SetListResponse;
import com.ebos.Service.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private UserService userService;

	@GetMapping("/getAllUsers")
	public ResponseEntity<?> findAll() {
		
		try {
			
			return new ResponseEntity<Map<String,Object>>(userService.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Map<String,Object>>(userService.findAll(), HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
	}
	
	@DeleteMapping("/deleteuser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
		try {
			
		return new ResponseEntity<Map<String, Object>>(userService.deleteUser(id), HttpStatus.OK);
		
		} catch (Exception e) {
			return new ResponseEntity<Map<String, Object>>(userService.deleteUser(id), HttpStatus.INTERNAL_SERVER_ERROR);
		
		}
		
	}

}
