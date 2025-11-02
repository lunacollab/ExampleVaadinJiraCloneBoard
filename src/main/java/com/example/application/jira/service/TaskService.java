package com.example.application.jira.service;

import com.example.application.jira.model.*;
import com.example.application.jira.repository.BoardColumnRepository;
import com.example.application.jira.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    
    private final TaskRepository taskRepository;
    private final BoardColumnRepository boardColumnRepository;
    
    public TaskService(TaskRepository taskRepository, BoardColumnRepository boardColumnRepository) {
        this.taskRepository = taskRepository;
        this.boardColumnRepository = boardColumnRepository;
    }
    
    public List<Task> getTasksByProject(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }
    
    public List<Task> getTasksByColumn(Long columnId) {
        return taskRepository.findByColumnIdOrderByPosition(columnId);
    }
    
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }
    
    public Integer getMaxPositionByColumnId(Long columnId) {
        return taskRepository.findMaxPositionByColumnId(columnId);
    }
    
    @Transactional
    public Task createTask(String title, String description, TaskType type, TaskPriority priority, 
                          BoardColumn column, Project project, User creator, User assignee, 
                          Integer storyPoints, LocalDate dueDate) {
        
        Integer position = taskRepository.findMaxPositionByColumnId(column.getId());
        if (position == null) position = 0;
        else position++;
        
        Task task = Task.builder()
                .title(title)
                .description(description)
                .type(type)
                .priority(priority)
                .position(position)
                .column(column)
                .project(project)
                .creator(creator)
                .assignee(assignee)
                .reporter(creator)
                .storyPoints(storyPoints)
                .dueDate(dueDate)
                .build();
        
        return taskRepository.save(task);
    }
    
    @Transactional
    public Task updateTask(Long taskId, String title, String description, TaskType type, 
                          TaskPriority priority, User assignee, Integer storyPoints, LocalDate dueDate) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        
        task.setTitle(title);
        task.setDescription(description);
        task.setType(type);
        task.setPriority(priority);
        task.setAssignee(assignee);
        task.setStoryPoints(storyPoints);
        task.setDueDate(dueDate);
        
        return taskRepository.save(task);
    }
    
    @Transactional
    public void moveTask(Long taskId, Long targetColumnId, Integer newPosition) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        
        BoardColumn targetColumn = boardColumnRepository.findById(targetColumnId)
                .orElseThrow(() -> new RuntimeException("Column not found"));
        
        task.setColumn(targetColumn);
        task.setPosition(newPosition);
        taskRepository.save(task);
    }
    
    @Transactional
    public void softDeleteTask(Long taskId) {
        taskRepository.findById(taskId).ifPresent(Task::softDelete);
    }
}

