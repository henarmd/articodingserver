package com.articoding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.articoding.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	
	User findByUsername(String username);
	
}