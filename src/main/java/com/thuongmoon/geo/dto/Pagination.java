package com.thuongmoon.geo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pagination {
	private int totalPages;
	private int currentPage;
	private int size;
	private int totalElements;
}
