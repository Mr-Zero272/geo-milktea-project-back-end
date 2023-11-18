package com.thuongmoon.geo.dto;

import com.thuongmoon.geo.models.MilkTeaShop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SumaryReview {
	private int totalComment;
	private float avgRating;
	private MilkTeaShop milkTeaShopInfo;
}
