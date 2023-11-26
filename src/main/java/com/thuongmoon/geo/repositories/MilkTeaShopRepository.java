package com.thuongmoon.geo.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thuongmoon.geo.models.MilkTeaShop;

public interface MilkTeaShopRepository extends JpaRepository<MilkTeaShop, Long> {
	@Query(value = "SELECT *, ROUND(ST_Distance(ST_SRID(geo, 4326), ST_SRID(Point(:lng, :lat), 4326), 'metre'), 2) "
			+ "as dis_m FROM geodb.mgeo HAVING dis_m < :range", nativeQuery = true)
	List<Object[]> findEleMapByPointAndRange(@Param("lng") double lng, @Param("lat") double lat, @Param("range") double range);
	
	@Query("SELECT mts FROM MilkTeaShop mts WHERE mts.name LIKE %:name% OR mts.address LIKE %:address%")
	Page<MilkTeaShop> findByNameOrAddress(Pageable pageable, String name, String address);
	
	@Query("SELECT mts FROM MilkTeaShop mts WHERE mts.name LIKE %:name% OR mts.address LIKE %:address% OR mts.road.name LIKE %:roadName%")
	Page<MilkTeaShop> findByNameOrAddressOrRoadName(Pageable pageable, String name, String address, String roadName);
}
