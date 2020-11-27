package com.prs.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.prs.business.Vendor;
import com.prs.db.VendorRepo;

@CrossOrigin
@RestController
@RequestMapping("/api/vendors")
public class VendorController {

	@Autowired
	private VendorRepo vendorRepo;
	
	// --- list all vendors
	@GetMapping("")
	public List<Vendor> getAllVendors(){
		return vendorRepo.findAll();
	}

	// --- list vendor by id
	@GetMapping("/{id}")
	public Optional<Vendor> getVendor(@PathVariable int id){
		Optional<Vendor> v = vendorRepo.findById(id);
		if(v.isPresent()) {
			return v;
		}else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendor not found.");
		}
	}
	
	// --- add a vendor
	@PostMapping("")
	public Vendor addVendor(@RequestBody Vendor v) {
		return vendorRepo.save(v);
	}
	
	// --- update a vendor
	@PutMapping("/{id}")
	public Vendor updateVendor(@RequestBody Vendor v, @PathVariable int id) {
		if(id == v.getId()) {
			return vendorRepo.save(v);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to update. Vendor id does not match.");
		}
	}
	
	// --- delete a vendor
	@DeleteMapping("/{id}")
	public Optional<Vendor> deleteVendor(@PathVariable int id) {
		Optional<Vendor> v = vendorRepo.findById(id);
		if(v.isPresent()) {
			vendorRepo.deleteById(id);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to delete. Vendor id does not match.");			
		}
		return v;
	}
}
