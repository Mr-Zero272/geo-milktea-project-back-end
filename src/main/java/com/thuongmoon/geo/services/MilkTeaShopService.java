package com.thuongmoon.geo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.thuongmoon.geo.dto.MilkTeaShopDto;
import com.thuongmoon.geo.dto.RequestSaveGeoEle;
import com.thuongmoon.geo.dto.RoadDto;
import com.thuongmoon.geo.helpers.GeometryHelper;
import com.thuongmoon.geo.models.MilkTeaShop;
import com.thuongmoon.geo.models.Road;
import com.thuongmoon.geo.models.User;
import com.thuongmoon.geo.repositories.MilkTeaShopRepository;
import com.thuongmoon.geo.repositories.RoadRepository;
import com.thuongmoon.geo.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MilkTeaShopService {
	@Autowired
	private final MilkTeaShopRepository milkTeaShopRespository;
	
	@Autowired
	private final RoadRepository roadRepository;
	
	@Autowired
	private final UserRepository userRepository;
	
	public JSONObject searchEleInMap(String q, String type) throws JsonMappingException, JsonProcessingException {
		int size = type.equals("less") ? 5 : 10;
		Pageable pageable = PageRequest.of(0, size + 95);
		
		JSONObject responseSearch = new JSONObject();
		
		// cac json thanh phan
		JSONObject geo = new JSONObject();
		List<JSONObject> features = new ArrayList<JSONObject>();
		JSONObject paginationJsonInfo = new JSONObject();

		Page<MilkTeaShop> milkTeaShops = milkTeaShopRespository.findByNameOrAddressOrRoadName(pageable, q, q, q);
		
		// pagination info
		paginationJsonInfo.put("currentPage", milkTeaShops.getNumber());
		paginationJsonInfo.put("size", milkTeaShops.getSize());
		paginationJsonInfo.put("totalPages", milkTeaShops.getTotalPages());
		paginationJsonInfo.put("totalElement", milkTeaShops.getNumberOfElements());
		
		// tao chuoi geojson point
		for (MilkTeaShop item : milkTeaShops.getContent()) {
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
					.put("roadId", item.getRoad().getId())
					);
			geoItem.put("geometry", geometryJson);
			features.add(geoItem);
		}
		
		// tao chuoi geojson lineString road
		for (Road item : roadRepository.findAll()) {
			String temp = GeometryHelper.convertJtsGeometryToGeoJson(item.getPosition()).toString();
			JSONObject geometryJson = new JSONObject(temp);

			JSONObject geoItem = new JSONObject();
			geoItem.put("type", "Feature");
			geoItem.put("properties", new JSONObject()
					.put("name", item.getName())
					.put("id", item.getId())
					);
			geoItem.put("geometry", geometryJson);
			features.add(geoItem);
		}
		
		geo.put("type", "FeatureCollection");
		geo.put("features", features);
		
		// nếu type = less khỏi trả pagination ngược lại thì có
		if (type.equals("less")) {
			responseSearch.put("geoJson", geo);			
		} else {
			responseSearch.put("geoJson", geo);
			responseSearch.put("pagination", paginationJsonInfo);
		}
		
		return responseSearch;
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

	public JSONObject getEleMapByDistance(double lng, double lat, double range) throws ParseException {
		List<Object[]> tempList = milkTeaShopRespository.findEleMapByPointAndRange(lng, lat, range);
		for (Object[] aObjects : tempList) {
			System.out.println(aObjects[0] + ", " + aObjects[1] + ", " + aObjects[2] + ", " + aObjects[3]);
		}
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
	public void addEleToDB(List<MilkTeaShopDto> listEleGeoJsons) throws ParseException {
		for (MilkTeaShopDto item : listEleGeoJsons) {
			MilkTeaShop mGeo = new MilkTeaShop();
			mGeo.setName(item.getName());
			Geometry lineString;
			lineString = (Geometry) GeometryHelper.wktToGeometry(item.getPositionWkt());
			mGeo.setPosition(lineString);
			milkTeaShopRespository.save(mGeo);
		}
	}
	
	@Transactional
	public void addItemToMapDB(RequestSaveGeoEle request) throws ParseException {
		if (!request.getListMilkTeaShops().isEmpty()) {
			for (MilkTeaShopDto itemMilkTeaShopDto : request.getListMilkTeaShops()) {
				MilkTeaShop mGeo = new MilkTeaShop();
				mGeo.setName(itemMilkTeaShopDto.getName());
				mGeo.setDescription(itemMilkTeaShopDto.getDescription());
				mGeo.setAddress(itemMilkTeaShopDto.getAddress());
				mGeo.setOpenTime(itemMilkTeaShopDto.getOpenTime());
				mGeo.setCloseTime(itemMilkTeaShopDto.getCloseTime());
				mGeo.setPhoneNumber(itemMilkTeaShopDto.getPhoneNumber());
				mGeo.setApproved(false);
				Geometry pointGeometry = (Geometry) GeometryHelper.wktToGeometry(itemMilkTeaShopDto.getPositionWkt());
				mGeo.setPosition(pointGeometry);
				Optional<Road> roadOp = roadRepository.findById(itemMilkTeaShopDto.getRoadId());
				Road road;
				if (roadOp.isPresent()) {
					road = roadOp.get();
				} else {
					road = roadRepository.findById(1L).get();
				}
				mGeo.setRoad(road);
				User user = userRepository.findById(1L).get();
				mGeo.setUser(user);
				milkTeaShopRespository.save(mGeo);
			}
		}
		
		if (!request.getListRoads().isEmpty()) {
			for(RoadDto item : request.getListRoads()) {
				Road road = new Road();
				road.setName(item.getName());
				Geometry lineString = (Geometry) GeometryHelper.wktToGeometry(item.getPositionWkt());
				road.setPosition(lineString);
				road.setApproved(false);
				roadRepository.save(road);
			}
		}
		
		if(!request.getListPointsNeedEdit().isEmpty()) {
			for (MilkTeaShopDto item: request.getListPointsNeedEdit()) {
				MilkTeaShop milkTeaShop = milkTeaShopRespository.findById(item.getRealId()).get();
				milkTeaShop.setName(item.getName());
				milkTeaShop.setDescription(item.getDescription());
				milkTeaShop.setAddress(item.getAddress());
				milkTeaShop.setOpenTime(item.getOpenTime());
				milkTeaShop.setCloseTime(item.getCloseTime());
				milkTeaShop.setPhoneNumber(item.getPhoneNumber());
				Geometry pointGeometry = (Geometry) GeometryHelper.wktToGeometry(item.getPositionWkt());
				milkTeaShop.setPosition(pointGeometry);
				Optional<Road> roadOp = roadRepository.findById(item.getRoadId());
				Road road;
				if (roadOp.isPresent()) {
					road = roadOp.get();
				} else {
					road = roadRepository.findById(1L).get();
				}
				milkTeaShop.setRoad(road);
				User user = userRepository.findById(1L).get();
				milkTeaShop.setUser(user);
				milkTeaShopRespository.save(milkTeaShop);
			}
		}
		
		if(!request.getListLineStringsNeedEdit().isEmpty()) {
			for (RoadDto item: request.getListLineStringsNeedEdit()) {
				Road road = roadRepository.findById(item.getRealId()).get();
				road.setName(item.getName());
				Geometry lineString = (Geometry) GeometryHelper.wktToGeometry(item.getPositionWkt());
				road.setPosition(lineString);
				roadRepository.save(road);
			}
		}
		
		if(!request.getListPointDelete().isEmpty()) {
			for (Long item: request.getListPointDelete()) {
				milkTeaShopRespository.deleteById(item);
			}
		}
	}

}