package com.thuongmoon.geo.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String username;
	private String password;
	private String avatar;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
	private List<MilkTeaShop> milkTeaShops = new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
	private List<Comment> comments = new ArrayList<>();
}
