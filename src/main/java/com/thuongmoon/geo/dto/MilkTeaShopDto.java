package com.thuongmoon.geo.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MilkTeaShopDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String description;
	private String address;
	private String openTime;
	private String closeTime;
	private String phoneNumber;
	private String positionWkt;
	private Long roadId;
	private Long realId;
	private boolean needEdit;
}
