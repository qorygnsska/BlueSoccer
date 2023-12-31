package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Review;
import com.example.demo.entity.User;

import java.util.List;


public interface ReviewRepository extends JpaRepository<Review, Integer>{

	List<Review> findByUserOrderByDate(User user);
	List<Review> findByOrderByDate();
	
	Page<Review> findByUserOrderByDate(User user, Pageable pageable);

}
