package com.thuongmoon.geo.controllers;

import org.json.JSONObject;
import org.locationtech.jts.io.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import com.thuongmoon.geo.dto.RequestSaveGeoEle;
import com.thuongmoon.geo.services.MilkTeaShopService;

@RequestMapping("api/v1/milktea")
@CrossOrigin(origins = "http://localhost:3004")
@RestController
public class MilkTeaShopController {

	@Autowired
	private MilkTeaShopService milkTeaShopService;

	@GetMapping("/search")
	public ResponseEntity<String> getMilkTeaShops(@RequestParam(required = true, defaultValue = "") String q,
			@RequestParam(defaultValue = "less") String type) throws JsonMappingException, JsonProcessingException {
		JSONObject responseJsonObject = milkTeaShopService.searchEleInMap(q, type);
		return new ResponseEntity<>(responseJsonObject.toString(), HttpStatus.OK);
	}

	@PostMapping()
	public ResponseEntity<String> saveGeo(@RequestBody RequestSaveGeoEle requestSaveGeoEle) {
		System.out.println(requestSaveGeoEle.toString());
		try {
			milkTeaShopService.addItemToMapDB(requestSaveGeoEle);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<String>("error", HttpStatus.EXPECTATION_FAILED);
		}
		return new ResponseEntity<String>("ok", HttpStatus.OK);
//		return null;
	}

}
