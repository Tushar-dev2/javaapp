package com.ebos.Controller;

import java.util.Map;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ebos.Request.SignUpRequest;
import com.ebos.Response.ApiResponse;
import com.ebos.Response.DeleteResponse;
import com.ebos.Response.GetUserResponse;
import com.ebos.Response.SetListResponse;
import com.ebos.Response.UpdateResponse;
import com.ebos.Service.UserService;
import com.ebos.tables.User;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;


	@GetMapping("/getAllUser")
	public ResponseEntity<?> getUserData() {
		
		try {
			return new ResponseEntity<Map<String,Object>>(userService.findAll(), HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<Map<String,Object>>(userService.findAll(), HttpStatus.INTERNAL_SERVER_ERROR);
			
	}
	}


	@PostMapping("/addUser")
	public ResponseEntity<?> save(@RequestBody SignUpRequest signUpRequest){
		try {
			return new ResponseEntity<Map<String, Object>>(userService.save(signUpRequest), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Map<String, Object>>(userService.save(signUpRequest), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/updateUserInfo")
	public ResponseEntity<?> updateUserInfo(@RequestBody SignUpRequest signUpRequest) {
		
		try {
			return new ResponseEntity<Map<String, Object>>(userService.updateUser(signUpRequest), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Map<String, Object>>(userService.updateUser(signUpRequest), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@GetMapping("/findUserBy/{name}")
	public ResponseEntity<?> findByName(@PathVariable("name") String name){
		
		try {
			return new ResponseEntity<Map<String, Object>>(userService.findByname(name), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Map<String, Object>>(userService.findByname(name), HttpStatus.INTERNAL_SERVER_ERROR);
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


