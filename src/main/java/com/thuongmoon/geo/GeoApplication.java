package com.thuongmoon.geo;
import com.thuongmoon.geo.helpers.*;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.thuongmoon.geo.models.MilkTeaShop;
import com.thuongmoon.geo.repositories.MGeoRespository;

@SpringBootApplication
public class GeoApplication{
	@Autowired
	private MGeoRespository respository;

	public static void main(String[] args) {
		SpringApplication.run(GeoApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//		MGeo mGeo = new MGeo();
//		mGeo.setName("Cong A Dai Hoc Can Tho");
//		Geometry point = new GeometryFactory().createPoint(new Coordinate(105.772097, 10.030249));
//		mGeo.setGeo(point);
//		//10.029072, 105.771051
//		MGeo mGeo2 = new MGeo();
//		mGeo2.setName("Cong B Dai Hoc Can Tho");
//		Geometry point2 = (Geometry) GeometryHelper.wktToGeometry("POINT (105.771051 10.029072)");
//		mGeo2.setGeo(point2);
//		
//		MGeo mGeo3 = new MGeo();
//		mGeo3.setName("Truong CNTT-TT");
//		Geometry polygon3 = (Geometry) GeometryHelper.wktToGeometry("POLYGON((105.76838246697184 10.030731560382705, 105.76898418779638 10.030307327514805, 105.76970340440221 10.0312749983689, 105.7691906955344 10.031639627216066, 105.76838246697184 10.030731560382705))");
//		mGeo3.setGeo(polygon3);
		
//		MGeo mGeo4 = new MGeo();
//		mGeo4.setName("Duong vao cong B");
//		Geometry lineString = (Geometry) GeometryHelper.wktToGeometry("LINESTRING (105.771156 10.028999, 105.769729 10.029886)");
//		mGeo4.setGeo(lineString);
//		
//		respository.save(mGeo);
//		respository.save(mGeo2);
//		respository.save(mGeo3);
//		respository.save(mGeo4);
//	}

}
