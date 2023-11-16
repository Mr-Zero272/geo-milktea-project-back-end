package com.thuongmoon.geo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thuongmoon.geo.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
