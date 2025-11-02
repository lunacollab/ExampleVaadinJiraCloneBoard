package com.example.application.jira.repository;

import com.example.application.jira.model.BoardColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long> {
    @Query("SELECT c FROM BoardColumn c WHERE c.project.id = :projectId AND c.deleted = false ORDER BY c.position ASC")
    List<BoardColumn> findByProjectIdOrderByPosition(@Param("projectId") Long projectId);
}

