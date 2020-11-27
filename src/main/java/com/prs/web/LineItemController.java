 package com.prs.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.prs.business.LineItem;
import com.prs.business.Request;
import com.prs.db.LineItemRepo;
import com.prs.db.RequestRepo;

@CrossOrigin
@RestController
@RequestMapping("/api/lines")
public class LineItemController {

	@Autowired
	private LineItemRepo lineItemRepo;
	@Autowired
	private RequestRepo requestRepo;
	
	// --- list all lineItems
	@GetMapping("")
	public List<LineItem> getAllLineItems(){
		return lineItemRepo.findAll();
	}

	// --- list all lineItem for specified request
	@GetMapping("/byrequest/{id}")
	public List<LineItem> getAllLinesByRequest(@PathVariable int id){
		return lineItemRepo.findByRequestId(id);
	}
	
	// --- list line item by id
	@GetMapping("/{id}")
	public Optional<LineItem> getLineItem(@PathVariable int id){
		Optional<LineItem> l = lineItemRepo.findById(id);
		if(l.isPresent()) {
			return l;
		}else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Line item not found.");
		}
	}
	
	// --- add a line item by "request id"
	// --- @PostMapping("/{reqid}")
	// --- 	public LineItem addLineItem(@RequestBody LineItem l, @PathVariable int reqid) {

	@PostMapping("")
	public LineItem addLineItem(@RequestBody LineItem l) {
			lineItemRepo.save(l);
			// recalculate the request total reflecting the line item change
			recalculateRequestTotal(l.getRequest());
			// return the updated line item
			return l;
	}
		
	//--- update a line item
	@PutMapping("/{id}")
	public LineItem updateLineItem(@RequestBody LineItem l, @PathVariable int id) {
		if(id == l.getId()) {
			// save the updated line item
			lineItemRepo.save(l);
			// recalculate the request total reflecting the line item change
			recalculateRequestTotal(l.getRequest());
			return l;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to update. Line item id does not match.");
		}
	}
	
	// --- delete a line item
	@DeleteMapping("/{id}")
	public Optional<LineItem> deleteLineItem(@PathVariable int id) {
		Optional<LineItem> l = lineItemRepo.findById(id);
		if(l.isPresent()) {
			// save the deleted line item
			lineItemRepo.deleteById(id);
			// recalculate the request total reflecting the line item change
			recalculateRequestTotal(l.get().getRequest());
			return l;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to delete. Line item id does not match.");			
		}
	}

	// --- recalculate request total (custom method)
	private void recalculateRequestTotal (Request r) {
		// get list of all line items
		List <LineItem> lineList = lineItemRepo.findByRequestId(r.getId());
		// initialize total to 0, so it's always recalculated properly
		double total = 0.0;
		// loop through list of line items and add to total
		for(LineItem li: lineList) {
			// single line item total : product price * quantity
			// line item -> product -> price			
			double lineItemTotal = li.getProduct().getPrice() *  li.getQuantity();
			// add line items to total
			total = total + lineItemTotal;
		}
		// save total in request instance
		r.setTotal(total);
		requestRepo.save(r);
	}

}
