package com.ebos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ebos.tables.User;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	 	Optional<User> findByEmail(String email);

	    Optional<User> findByUsernameOrEmail(String username, String email);

	    List<User> findAllByIdIn(List<Long> userIds);

	    Optional<User> findByUsername(String username);

	    Boolean existsByUsername(String username);

	    Boolean existsByEmail(String email);
	    
	    List<User> findByname(String name);
}
