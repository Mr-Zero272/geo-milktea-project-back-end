package com.thuongmoon.geo.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thuongmoon.geo.dto.Pagination;
import com.thuongmoon.geo.models.MilkTeaShop;
import com.thuongmoon.geo.services.MilkTeaShopService;

@RequestMapping("api/v1/milktea")
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class MilkTeaShopController {
	
	@Autowired
	private MilkTeaShopService milkTeaShopService;
	
	@GetMapping("/search")
	public ResponseEntity<Map<String, Object>> getMilkTeaShops(@RequestParam(required = true, defaultValue = "") String q, @RequestParam(defaultValue = "less") String type) {
		int size = type.equals("less") ? 5 : 10;
		Pageable pageable = PageRequest.of(0, size);
		Page<MilkTeaShop> page = milkTeaShopService.searchByNameOrAddress(pageable, q);
		Pagination pagination = new Pagination();
		pagination.setCurrentPage(page.getNumber());
		pagination.setSize(page.getSize());
		pagination.setTotalPages(page.getTotalPages());
		pagination.setTotalElements(page.getNumberOfElements());
		Map<String, Object> response = new HashMap<>();
		response.put("milkTeaShops", page.getContent());
		response.put("pagination", pagination);
		
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.OK);
	}
	
	
}
