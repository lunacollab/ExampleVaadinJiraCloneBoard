package com.example.application.jira.repository;

import com.example.application.jira.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t WHERE t.column.id = :columnId AND t.deleted = false ORDER BY t.position ASC")
    List<Task> findByColumnIdOrderByPosition(@Param("columnId") Long columnId);
    
    @Query("SELECT t FROM Task t WHERE t.project.id = :projectId AND t.deleted = false")
    List<Task> findByProjectId(@Param("projectId") Long projectId);
    
    @Query("SELECT MAX(t.position) FROM Task t WHERE t.column.id = :columnId")
    Integer findMaxPositionByColumnId(@Param("columnId") Long columnId);
}

