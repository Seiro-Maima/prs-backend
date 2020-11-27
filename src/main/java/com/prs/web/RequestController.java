package com.prs.web;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.prs.business.Request;
import com.prs.db.RequestRepo;
import com.prs.db.UserRepo;

@CrossOrigin
@RestController
@RequestMapping("/api/requests")
public class RequestController {
	
	// --- constants for request statuses
	static final String REQUEST_NEW		 = "New";
	static final String REQUEST_REVIEW 	 = "Review";
	static final String REQUEST_APPROVED = "Approved";
	static final String REQUEST_REJECTED = "Rejected";

	@Autowired
	private RequestRepo requestRepo;
	
	// --- list all requests
	@GetMapping("")
	public List<Request> getAllRequests(){
		return requestRepo.findAll();
	}

	// --- list requests by id
	@GetMapping("/{id}")
	public Optional<Request> getRequest(@PathVariable int id){
		Optional<Request> r = requestRepo.findById(id);
		if(r.isPresent()) {
			return r;
		}else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request id not found.");
		}
	}
	
	// --- list requests in "review" status that doesn't belong to logged in user
	@GetMapping("/reviews/{userid}")
	public List<Request> getAllRequests(@PathVariable int userid){
		return requestRepo.findByStatusAndUserIdNot(REQUEST_REVIEW, userid);
	}

	// --- set request to "review" status
	@PutMapping("/review")
	public Request setRequestReview(@RequestBody Request r) {
			// status will automatically be approved if total is less than or equal to $50
			if(r.getTotal() <= 50) {	
				r.setStatus(REQUEST_APPROVED);
			}else {
				r.setStatus(REQUEST_REVIEW);
			}
			return requestRepo.save(r);
	}
	
// commented out due to {id} being removed from path in Angular
//	// --- set request to "approved" status
//	@PutMapping("/approve/{id}")
//	public Request setRequestApprove(@RequestBody Request r, @PathVariable int id) {
//		if(id == r.getId()) {
//			r.setStatus(REQUEST_APPROVED);
//			return requestRepo.save(r);
//		} else {
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to approve. Request id does not match.");
//		}
//	}
	
	// --- set request to "approved" status
	@PutMapping("/approve")
	public Request setRequestApprove(@RequestBody Request r) {
		r.setStatus(REQUEST_APPROVED);
		return requestRepo.save(r);
	}

// commented out due to {id} being removed from path in Angular
//	// --- set request to "rejected" status
//	@PutMapping("/reject/{id}")
//	public Request setRequestReject(@RequestBody Request r, @PathVariable int id) {
//		if(id == r.getId()) {
//			r.setStatus(REQUEST_REJECTED);
//			return requestRepo.save(r);
//		} else {
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to reject. Request id does not match.");
//		}
//	}

	// --- set request to "rejected" status
	@PutMapping("/reject")
	public Request setRequestReject(@RequestBody Request r) {
		r.setStatus(REQUEST_REJECTED);
		return requestRepo.save(r);
	}
	
	// --- add a request
	@PostMapping("")
	public Request addRequest(@RequestBody Request r) {
		// when adding a request, save local date and time
		LocalDateTime submittedDate = LocalDateTime.now();
		r.setSubmittedDate(submittedDate);
		// when adding a request, the status should be "New"
		r.setStatus(REQUEST_NEW);
		return requestRepo.save(r);
	}
	
	// --- update a request
	@PutMapping("/{id}")
	public Request updateRequest(@RequestBody Request r, @PathVariable int id) {
		if(id == r.getId()) {
			return requestRepo.save(r);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to update. Request id does not match.");
		}
	}
	
	// --- delete a request
	@DeleteMapping("/{id}")
	public Optional<Request> deleteRequest(@PathVariable int id) {
		Optional<Request> r = requestRepo.findById(id);
		if(r.isPresent()) {
			requestRepo.deleteById(id);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to delete. Request id does not match.");			
		}
		return r;
	}
}
