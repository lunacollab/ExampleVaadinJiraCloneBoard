package com.example.application.jira.repository;

import com.example.application.jira.model.TaskComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskCommentRepository extends JpaRepository<TaskComment, Long> {
    @Query("SELECT c FROM TaskComment c WHERE c.task.id = :taskId AND c.deleted = false ORDER BY c.createdAt ASC")
    List<TaskComment> findByTaskIdOrderByCreatedAt(@Param("taskId") Long taskId);
}

