package com.thuongmoon.geo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.thuongmoon.geo.dto.EleMapDto;
import com.thuongmoon.geo.dto.MilkTeaShopDTO;
import com.thuongmoon.geo.dto.geoJsonRequest;
import com.thuongmoon.geo.helpers.GeometryHelper;
import com.thuongmoon.geo.models.MilkTeaShop;
import com.thuongmoon.geo.repositories.MilkTeaShopRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MilkTeaShopService {
	@Autowired
	private final MilkTeaShopRepository milkTeaShopRespository;

	public JSONObject getAllEleInMap() throws JsonMappingException, JsonProcessingException {
		JSONObject geo = new JSONObject();
		List<JSONObject> features = new ArrayList<JSONObject>();

		for (MilkTeaShop item : milkTeaShopRespository.findAll()) {
			String temp = GeometryHelper.convertJtsGeometryToGeoJson(item.getPosition()).toString();
			// System.out.println(temp + "asdfasdfasdfsdf");
			JSONObject geometryJson = new JSONObject(temp);

			JSONObject geoItem = new JSONObject();
			geoItem.put("type", "Feature");
			geoItem.put("properties", new JSONObject().put("Name", item.getName()));
			geoItem.put("geometry", geometryJson);
			features.add(geoItem);
		}

		geo.put("type", "FeatureCollection");
		geo.put("features", features);

		return geo;
	}

	public JSONObject getAllEleInMapById(Long id) throws JsonMappingException, JsonProcessingException {
		JSONObject geo = new JSONObject();
		List<JSONObject> features = new ArrayList<JSONObject>();

		MilkTeaShop mGeo = milkTeaShopRespository.findById(id).orElseThrow();

		String temp = GeometryHelper.convertJtsGeometryToGeoJson(mGeo.getPosition()).toString();
		JSONObject geometryJson = new JSONObject(temp);

		JSONObject geoItem = new JSONObject();
		geoItem.put("type", "Feature");
		geoItem.put("properties", new JSONObject().put("Name", mGeo.getName()));
		geoItem.put("geometry", geometryJson);
		features.add(geoItem);

		geo.put("type", "FeatureCollection");
		geo.put("features", features);

		return geo;
	}

	public List<EleMapDto> getAllEleMapDto() {
		List<MilkTeaShop> geos = milkTeaShopRespository.findAll();
		List<EleMapDto> eleMaps = new ArrayList<>();
		for (MilkTeaShop geo : geos) {
			eleMaps.add(new EleMapDto(geo.getId(), geo.getName()));
		}
		return eleMaps;
	}

	public JSONObject getEleMapByDistance(double lng, double lat, double range) throws ParseException {
		List<Object[]> tempList = milkTeaShopRespository.findEleMapByPointAndRange(lng, lat, range);
//		for (Object[] aObjects : tempList) {
//			System.out.println(aObjects[0] + ", " + aObjects[1] + ", " + aObjects[2] + ", " + aObjects[3]);
//		}
		JSONObject geo = new JSONObject();
		List<JSONObject> features = new ArrayList<JSONObject>();

		for (Object[] item : tempList) {
			String temp = GeometryHelper.convertJtsGeometryToGeoJson(GeometryHelper.wktToGeometry(item[1].toString())).toString();
			JSONObject geometryJson = new JSONObject(temp);

			JSONObject geoItem = new JSONObject();
			geoItem.put("type", "Feature");
			geoItem.put("properties", new JSONObject().put("Name", item[2].toString()).put("Distance", item[3].toString()));
			geoItem.put("geometry", geometryJson);
			features.add(geoItem);
		}

		geo.put("type", "FeatureCollection");
		geo.put("features", features);

		return geo;
	}
	
	@Transactional
	public void addEleToDB(List<geoJsonRequest> listEleGeoJsons) throws ParseException {
		for (geoJsonRequest item : listEleGeoJsons) {
			MilkTeaShop mGeo = new MilkTeaShop();
			mGeo.setName(item.getName());
			Geometry lineString;
			lineString = (Geometry) GeometryHelper.wktToGeometry(item.getWkt());
			mGeo.setPosition(lineString);
			milkTeaShopRespository.save(mGeo);
		}
	}
	
	public Page<MilkTeaShop> searchByNameOrAddress(Pageable pageable, String q) {
		return milkTeaShopRespository.findByNameOrAddress(pageable, q, q);
	}
	
	
    
	

	public JSONObject searchEleInMap(double lng, double lat, double range,String keyword)  {
	    JSONObject responseSearch = new JSONObject();

	    List<MilkTeaShop> milkTeaShops;
	    // Các thành phần JSON
	    JSONObject geo = new JSONObject();
	    List<JSONObject> features = new ArrayList<>();
	    if (keyword != null && !keyword.isEmpty()) {
	    	 milkTeaShops = milkTeaShopRespository.findMilkTeaShopByUserLocationAndRangeAndKeyword(lng, lat, range, keyword);
		}else {
			 milkTeaShops = milkTeaShopRespository.findMilkTeaShopByUserLocationAndRange(lng, lat, range);
		}
	    // Tạo chuỗi GeoJSON
	    for (MilkTeaShop item : milkTeaShops) {
	        String temp = GeometryHelper.convertJtsGeometryToGeoJson(item.getPosition()).toString();
	        JSONObject geometryJson = new JSONObject(temp);

	        JSONObject geoItem = new JSONObject();
	        geoItem.put("type", "Feature");
	        geoItem.put("properties", new JSONObject()
	                .put("name", item.getName())
	                .put("id", item.getId())
	                .put("openTime", item.getOpenTime())
	                .put("closeTime", item.getCloseTime())
	                .put("address", item.getAddress())
	                .put("description", item.getDescription())
	                .put("phoneNumber", item.getPhoneNumber())
	                .put("roadName", item.getRoad().getName())
	        );
	        geoItem.put("geometry", geometryJson);
	        features.add(geoItem);
	    }

	    geo.put("type", "FeatureCollection");
	    geo.put("features", features);

	    responseSearch.put("geoJson", geo);

	    return responseSearch;
	}

	    
	
	
	



}