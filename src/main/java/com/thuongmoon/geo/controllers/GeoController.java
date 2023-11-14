package com.thuongmoon.geo.controllers;

import java.util.List;

import org.locationtech.jts.io.ParseException;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.thuongmoon.geo.dto.EleMapDto;
import com.thuongmoon.geo.dto.geoJsonRequest;
import com.thuongmoon.geo.services.GeoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/map")
@CrossOrigin(origins = "")
@RequiredArgsConstructor
public class GeoController {
	private final GeoService service;

	@GetMapping("/geojson")
	public ResponseEntity<String> getMap() throws JsonMappingException, JsonProcessingException {
//		//GeometryHelper.convertJtsGeometryToGeoJson(item.getGeom()).toString()

		String gString = service.getAllEleInMap().toString();

		return new ResponseEntity<String>(gString, HttpStatus.OK);
	}

	@GetMapping("/geojson/{id}")
	public ResponseEntity<String> getEleMapWithId(@PathVariable(name = "id", required = true) String id) throws JsonMappingException, JsonProcessingException {
//		//GeometryHelper.convertJtsGeometryToGeoJson(item.getGeom()).toString()
		String gString;
		if (id.equals("all")) {
			gString = service.getAllEleInMap().toString();
		} else {
			gString = service.getAllEleInMapById(Long.parseLong(id)).toString();			
		}
		return new ResponseEntity<String>(gString, HttpStatus.OK);
	}

	@GetMapping("/mapele")
	public ResponseEntity<List<EleMapDto>> getListEleMap() {
		return new ResponseEntity<List<EleMapDto>>(service.getAllEleMapDto(), HttpStatus.OK);
	}
	
	@GetMapping("/geojson/distance")                        //kinh do                   vi do
	public ResponseEntity<String> getEleMapAtCertainDistance(@Param("lng") double lng, @Param("lat") double lat, @Param("range") double range) {
		String gString = "";
		try {
			gString = service.getEleMapByDistance(lng, lat, range).toString();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<String>(gString, HttpStatus.OK);
	}
	
	@PostMapping("/geojson")
	public ResponseEntity<String> addElementToMap(@RequestBody List<geoJsonRequest> listGeoJsonRequestes) {
		String gString = "";
		try {
			service.addEleToDB(listGeoJsonRequestes);
			gString = "success";
			return new ResponseEntity<String>(gString, HttpStatus.OK);
		} catch (ParseException e) {
			gString = "error";
			return new ResponseEntity<String>(gString, HttpStatus.EXPECTATION_FAILED);
		}
	}
}
