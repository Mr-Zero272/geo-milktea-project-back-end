package com.thuongmoon.geo.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Geometry;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Road implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private boolean isApproved;
	
	private String name;
	
	private Geometry position;
	
	
	@OneToMany(mappedBy = "road", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MilkTeaShop> milkTeaShops = new ArrayList<>();
	
	public int hashCode() {
		return 2023;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		return id != null && id.equals(((Road) obj).id);
	}
}
