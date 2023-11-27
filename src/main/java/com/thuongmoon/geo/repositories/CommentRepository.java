package com.thuongmoon.geo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.thuongmoon.geo.models.Comment;
import com.thuongmoon.geo.models.MilkTeaShop;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	@Query("SELECT c FROM Comment c WHERE c.milkTeaShop = :milkTeaShop")
	List<Comment> findByListComment(MilkTeaShop milkTeaShop);
}
