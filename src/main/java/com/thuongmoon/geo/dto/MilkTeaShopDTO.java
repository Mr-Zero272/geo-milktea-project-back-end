package com.thuongmoon.geo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MilkTeaShopDTO {
	private Long id;
    private String name;
    private String description;
    private String address;
    private String openTime;
    private String closeTime;
    private String phoneNumber;
    private String position;
    private boolean isApproved;
}
