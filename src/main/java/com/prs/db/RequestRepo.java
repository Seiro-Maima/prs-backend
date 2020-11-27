package com.prs.db;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.prs.business.Request;

public interface RequestRepo extends JpaRepository<Request, Integer> {

	// find all request with status "review" and do not belong to logged in user
	List<Request> findByStatusAndUserIdNot(String status, int userid);
	
}
