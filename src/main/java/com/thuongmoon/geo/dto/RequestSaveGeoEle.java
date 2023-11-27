package com.thuongmoon.geo.dto;

import java.util.List;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestSaveGeoEle {
	@Nullable
	private List<MilkTeaShopDto> listMilkTeaShops;
	@Nullable
	private List<RoadDto> listRoads;
	@Nullable
	private List<RoadDto> listLineStringsNeedEdit;
	@Nullable
	private List<MilkTeaShopDto> listPointsNeedEdit;
	@Nullable
	private List<Long> listPointDelete;
}
