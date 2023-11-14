package com.thuongmoon.geo.services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.thuongmoon.geo.dto.EleMapDto;
import com.thuongmoon.geo.dto.geoJsonRequest;
import com.thuongmoon.geo.helpers.GeometryHelper;
import com.thuongmoon.geo.models.MilkTeaShop;
import com.thuongmoon.geo.repositories.MGeoRespository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeoService {
	@Autowired
	private final MGeoRespository respository;

	public JSONObject getAllEleInMap() throws JsonMappingException, JsonProcessingException {
		JSONObject geo = new JSONObject();
		List<JSONObject> features = new ArrayList<JSONObject>();

		for (MilkTeaShop item : respository.findAll()) {
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

		MilkTeaShop mGeo = respository.findById(id).orElseThrow();

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
		List<MilkTeaShop> geos = respository.findAll();
		List<EleMapDto> eleMaps = new ArrayList<>();
		for (MilkTeaShop geo : geos) {
			eleMaps.add(new EleMapDto(geo.getId(), geo.getName()));
		}
		return eleMaps;
	}

	public JSONObject getEleMapByDistance(double lng, double lat, double range) throws ParseException {
		List<Object[]> tempList = respository.findEleMapByPointAndRange(lng, lat, range);
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
	public void addEleToDB(List<geoJsonRequest> listEleGeoJsons) throws ParseException {
		for (geoJsonRequest item : listEleGeoJsons) {
			MilkTeaShop mGeo = new MilkTeaShop();
			mGeo.setName(item.getName());
			Geometry lineString;
			lineString = (Geometry) GeometryHelper.wktToGeometry(item.getWkt());
			mGeo.setPosition(lineString);
			respository.save(mGeo);
		}
	}
}