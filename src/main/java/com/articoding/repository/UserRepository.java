package com.articoding.repository;

import com.articoding.model.ClassRoom;
import com.articoding.model.in.IUser;
import com.articoding.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.articoding.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	User findByUsername(String username);

	Page<IUser> findByRolesIn(Pageable pageable, List<Role> roles);

	<T> Page<T> findByClassRoomsIn(Pageable pageable, List<ClassRoom> classRooms,Class<T> type);
	<T> Page<T> findBy(Pageable pageable, Class<T> type);
}