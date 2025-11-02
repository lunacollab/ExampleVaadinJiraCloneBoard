package com.example.application.jira.repository;

import com.example.application.jira.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByKey(String key);
    
    @Query("SELECT p FROM Project p WHERE p.deleted = false")
    List<Project> findAllActive();
    
    @Query("SELECT p FROM Project p JOIN p.members m WHERE m.id = :userId AND p.deleted = false")
    List<Project> findByMemberId(@Param("userId") Long userId);
}

