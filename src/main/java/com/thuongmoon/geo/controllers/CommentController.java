package com.thuongmoon.geo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thuongmoon.geo.dto.ResponseMessage;
import com.thuongmoon.geo.models.Comment;
import com.thuongmoon.geo.models.MilkTeaShop;
import com.thuongmoon.geo.repositories.CommentRepository;
import com.thuongmoon.geo.repositories.MilkTeaShopRepository;

@RequestMapping("api/v1/comment")
@CrossOrigin(origins = "http://localhost:3004")
@RestController
public class CommentController {
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private MilkTeaShopRepository milkTeaShopRepository;
	
	@GetMapping
	public ResponseEntity<List<Comment>> getListCommentById(@RequestParam(required = true) Long id) {
		MilkTeaShop milkTeaShop = milkTeaShopRepository.findById(id).get();
		List<Comment> comments = commentRepository.findByListComment(milkTeaShop);
		return new ResponseEntity<>(comments, HttpStatus.OK);
	}
}
