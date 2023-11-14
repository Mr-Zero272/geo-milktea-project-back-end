package com.thuongmoon.geo.helpers;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.wololo.jts2geojson.GeoJSONReader;
import org.wololo.jts2geojson.GeoJSONWriter;

public class GeometryHelper {

	public static org.wololo.geojson.Geometry convertJtsGeometryToGeoJson(org.locationtech.jts.geom.Geometry geometry) {
		return new GeoJSONWriter().write(geometry);
	}

	public static org.locationtech.jts.geom.Geometry convertGeoJsonToJtsGeometry(String geoJson) {
		return new GeoJSONReader().read(geoJson);
	}

	public static Geometry wktToGeometry(String wellKnownText) throws ParseException {
		return new WKTReader().read(wellKnownText);
	}
}