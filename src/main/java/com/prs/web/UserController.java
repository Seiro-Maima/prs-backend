package com.prs.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.prs.business.User;
import com.prs.db.UserRepo;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private UserRepo userRepo;
	
	// --- login with user id and password
	@GetMapping("/{username}/{password}")
	public User getUserLogin(@PathVariable String username, @PathVariable String password) {
		User u = userRepo.findByUserNameAndPassword(username, password);
		// check if combo of user name and password exists
		if(u != null) {
			return u;
		}else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User and password combo not found. You trying to hack us?");
		}
	}
	
	// --- list all users
	@GetMapping("")
	public List<User> getAllUsers(){
		return userRepo.findAll();
	}

	// --- list user by id
	@GetMapping("/{id}")
	public Optional<User> getUser(@PathVariable int id){
		Optional<User> u = userRepo.findById(id);
		if(u.isPresent()) {
			return u;
		}else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
		}
	}
	
	// --- add a user
	@PostMapping("")
	public User addUser(@RequestBody User u) {
		return userRepo.save(u);
	}
	
	// --- update a user
	@PutMapping("/{id}")
	public User updateUser(@RequestBody User u, @PathVariable int id) {
		if(id == u.getId()) {
			return userRepo.save(u);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to update. User id does not match.");
		}
	}
	
	// --- delete a user
	@DeleteMapping("/{id}")
	public Optional<User> deleteUser(@PathVariable int id) {
		Optional<User> u = userRepo.findById(id);
		if(u.isPresent()) {
			userRepo.deleteById(id);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to delete. User id does not match.");			
		}
		return u;
	}

}
