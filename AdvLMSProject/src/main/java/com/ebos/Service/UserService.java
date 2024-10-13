package com.ebos.Service;
import java.util.Map;

import com.ebos.Request.SignUpRequest;
import com.ebos.Response.ApiResponse;
import com.ebos.Response.DeleteResponse;
import com.ebos.Response.GetUserResponse;
import com.ebos.Response.SetListResponse;
import com.ebos.Response.UpdateResponse;

public interface UserService {
	 	Map<String,Object> findAll();
	 	
	    Map<String, Object> save(SignUpRequest signUpRequest);
	 
	    Map<String, Object> deleteUser(Long id);
	    
	    Map<String, Object> updateUser(SignUpRequest signUpRequest);
	    
        Map<String, Object> findByname(String name);

}
