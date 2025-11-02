package com.example.application.jira.service;

import com.example.application.jira.model.BoardColumn;
import com.example.application.jira.model.Project;
import com.example.application.jira.model.User;
import com.example.application.jira.repository.BoardColumnRepository;
import com.example.application.jira.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    
    private final ProjectRepository projectRepository;
    private final BoardColumnRepository boardColumnRepository;
    
    public ProjectService(ProjectRepository projectRepository, BoardColumnRepository boardColumnRepository) {
        this.projectRepository = projectRepository;
        this.boardColumnRepository = boardColumnRepository;
    }
    
    public List<Project> getAllActiveProjects() {
        return projectRepository.findAllActive();
    }
    
    public List<Project> getProjectsByUser(Long userId) {
        return projectRepository.findByMemberId(userId);
    }
    
    public Optional<Project> findByKey(String key) {
        return projectRepository.findByKey(key);
    }
    
    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);
    }
    
    @Transactional
    public Project createProject(String name, String key, String description, String avatarUrl, User lead) {
        Project project = Project.builder()
                .name(name)
                .key(key)
                .description(description)
                .avatarUrl(avatarUrl)
                .lead(lead)
                .build();
        
        project = projectRepository.save(project);
        
        // Tạo các columns mặc định
        createDefaultColumns(project);
        
        return project;
    }
    
    @Transactional
    public void addMember(Project project, User member) {
        if (!project.getMembers().contains(member)) {
            project.getMembers().add(member);
            projectRepository.save(project);
        }
    }
    
    @Transactional
    public void softDeleteProject(Long projectId) {
        projectRepository.findById(projectId).ifPresent(Project::softDelete);
    }
    
    private void createDefaultColumns(Project project) {
        String[] columnNames = {"To Do", "In Progress", "In Review", "Done"};
        String[] colors = {"#6B778C", "#0052CC", "#FFAB00", "#00875A"};
        
        for (int i = 0; i < columnNames.length; i++) {
            BoardColumn column = BoardColumn.builder()
                    .name(columnNames[i])
                    .position(i)
                    .color(colors[i])
                    .project(project)
                    .build();
            boardColumnRepository.save(column);
        }
    }
}

