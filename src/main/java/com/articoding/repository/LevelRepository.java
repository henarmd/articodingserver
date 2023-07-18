package com.articoding.repository;

import com.articoding.model.ClassRoom;
import com.articoding.model.Level;
import com.articoding.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LevelRepository extends JpaRepository<Level, Long> {
    <T> T findById(Long id, Class<T> type);
    <T> Page<T> findBy(Pageable pageable, Class<T> type);
    <T> Page<T> findByOwnerAndActiveTrue(User owner, Pageable pageable, Class<T> type);
    <T> Page<T> findByClassRoomsAndActiveTrue(ClassRoom classRoom, Pageable pageable,Class<T> type);
    <T> List<T> findByClassRoomsAndActiveTrue(ClassRoom classRoom, Class<T> type);
}
