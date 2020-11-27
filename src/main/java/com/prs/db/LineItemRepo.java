package com.prs.db;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.prs.business.LineItem;

public interface LineItemRepo extends JpaRepository<LineItem, Integer> {
	// list of line items under specific request id
	List<LineItem> findByRequestId(int id);
}
