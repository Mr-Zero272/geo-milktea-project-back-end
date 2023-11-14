package com.thuongmoon.geo.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// 1-5 stars
	private float stars;
	
	@Lob
	@Column(length = 350)
	private String content;
	
	private LocalDate publishedAt;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "comment")
	private List<Gallery> galleries = new ArrayList<>();
	
	@ManyToOne(fetch = FetchType.LAZY)
	private MilkTeaShop milkTeaShop;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	
	private int totalLikes;

}
