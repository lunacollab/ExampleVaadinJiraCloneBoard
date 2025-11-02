package com.example.application.jira.config;

import com.example.application.jira.model.*;
import com.example.application.jira.repository.*;
import com.example.application.jira.service.ProjectService;
import com.example.application.jira.service.TaskService;
import com.example.application.jira.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;

@Configuration
public class DataSeeder {
    
    @Bean
    CommandLineRunner initDatabase(
            RoleRepository roleRepository,
            UserService userService,
            ProjectService projectService,
            BoardColumnRepository boardColumnRepository,
            TaskService taskService,
            PasswordEncoder passwordEncoder,
            UserRepository userRepository) {
        return args -> {
            // Tạo roles
            createRoles(roleRepository);
            
            // Tạo users
            User admin = createUsers(userService, userRepository);
            
            // Tạo projects
            Project project1 = createProjects(projectService, admin, userRepository);
            
            // Tạo tasks
            createTasks(project1, admin, taskService, boardColumnRepository, userRepository);
        };
    }
    
    private void createRoles(RoleRepository roleRepository) {
        if (roleRepository.count() == 0) {
            Role adminRole = Role.builder().name("ROLE_ADMIN").description("Administrator").build();
            Role pmRole = Role.builder().name("ROLE_PROJECT_MANAGER").description("Project Manager").build();
            Role devRole = Role.builder().name("ROLE_DEVELOPER").description("Developer").build();
            Role userRole = Role.builder().name("ROLE_USER").description("User").build();
            
            roleRepository.saveAll(Arrays.asList(adminRole, pmRole, devRole, userRole));
        }
    }
    
    private User createUsers(UserService userService, UserRepository userRepository) {
        if (userService.getAllActiveUsers().isEmpty()) {
            User admin = userService.createUser(
                    "admin",
                    "Administrator",
                    "admin@jira.com",
                    "admin123",
                    "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&h=150&fit=crop",
                    Set.of("ROLE_ADMIN")
            );
            
            userService.createUser(
                    "jdoe",
                    "John Doe",
                    "jdoe@jira.com",
                    "password123",
                    "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=150&h=150&fit=crop",
                    Set.of("ROLE_PROJECT_MANAGER", "ROLE_DEVELOPER")
            );
            
            userService.createUser(
                    "jsmith",
                    "Jane Smith",
                    "jsmith@jira.com",
                    "password123",
                    "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150&h=150&fit=crop",
                    Set.of("ROLE_DEVELOPER")
            );
            
            userService.createUser(
                    "mjohnson",
                    "Mike Johnson",
                    "mjohnson@jira.com",
                    "password123",
                    "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150&h=150&fit=crop",
                    Set.of("ROLE_DEVELOPER")
            );
            
            return admin;
        }
        return userService.findByUsername("admin").orElseThrow();
    }
    
    private Project createProjects(ProjectService projectService, User admin, UserRepository userRepository) {
        if (projectService.getAllActiveProjects().isEmpty()) {
            Project project1 = projectService.createProject(
                    "E-Commerce Platform",
                    "ECOM",
                    "Modern e-commerce platform with advanced features",
                    "https://images.unsplash.com/photo-1556742502-ec7c0e9f34b1?w=400&h=200&fit=crop",
                    admin
            );
            
            userRepository.findByUsername("jdoe").ifPresent(u -> projectService.addMember(project1, u));
            userRepository.findByUsername("jsmith").ifPresent(u -> projectService.addMember(project1, u));
            userRepository.findByUsername("mjohnson").ifPresent(u -> projectService.addMember(project1, u));
            
            projectService.createProject(
                    "Mobile App Redesign",
                    "MOBILE",
                    "Redesign and improve mobile application user experience",
                    "https://images.unsplash.com/photo-1512941937669-90a1b58e7e9c?w=400&h=200&fit=crop",
                    admin
            );
            
            return project1;
        }
        return projectService.getAllActiveProjects().get(0);
    }
    
    private void createTasks(Project project, User admin, TaskService taskService, BoardColumnRepository boardColumnRepository, UserRepository userRepository) {
        if (taskService.getTasksByProject(project.getId()).isEmpty()) {
            java.util.List<BoardColumn> columns = boardColumnRepository.findByProjectIdOrderByPosition(project.getId());
            BoardColumn todo = columns.get(0);
            BoardColumn inProgress = columns.get(1);
            BoardColumn inReview = columns.get(2);
            BoardColumn done = columns.get(3);
            
            // Get users
            User jdoe = userRepository.findByUsername("jdoe").orElse(admin);
            User jsmith = userRepository.findByUsername("jsmith").orElse(admin);
            User mjohnson = userRepository.findByUsername("mjohnson").orElse(admin);
            
            // Create tasks
            taskService.createTask(
                    "Implement User Authentication",
                    "Create login, registration, and password reset functionality with JWT tokens",
                    TaskType.STORY,
                    TaskPriority.HIGH,
                    todo,
                    project,
                    admin,
                    jdoe,
                    5,
                    LocalDate.now().plusDays(7)
            );
            
            taskService.createTask(
                    "Design Product Catalog UI",
                    "Create responsive product listing and detail pages with filters",
                    TaskType.STORY,
                    TaskPriority.HIGH,
                    todo,
                    project,
                    admin,
                    jsmith,
                    8,
                    LocalDate.now().plusDays(10)
            );
            
            taskService.createTask(
                    "Fix Payment Gateway Bug",
                    "Users unable to complete payment on Safari browser",
                    TaskType.BUG,
                    TaskPriority.HIGHEST,
                    inProgress,
                    project,
                    admin,
                    mjohnson,
                    3,
                    LocalDate.now().plusDays(2)
            );
            
            taskService.createTask(
                    "Add Product Reviews Feature",
                    "Allow users to review and rate products with moderation",
                    TaskType.STORY,
                    TaskPriority.MEDIUM,
                    inProgress,
                    project,
                    admin,
                    jdoe,
                    5,
                    LocalDate.now().plusDays(14)
            );
            
            taskService.createTask(
                    "Optimize Database Queries",
                    "Review and optimize slow queries in product search",
                    TaskType.TASK,
                    TaskPriority.LOW,
                    inReview,
                    project,
                    admin,
                    jsmith,
                    3,
                    LocalDate.now().plusDays(5)
            );
            
            taskService.createTask(
                    "Setup CI/CD Pipeline",
                    "Configure GitHub Actions for automated testing and deployment",
                    TaskType.TASK,
                    TaskPriority.MEDIUM,
                    done,
                    project,
                    admin,
                    mjohnson,
                    5,
                    LocalDate.now().minusDays(3)
            );
            
            taskService.createTask(
                    "Document API Endpoints",
                    "Create comprehensive API documentation with examples",
                    TaskType.TASK,
                    TaskPriority.LOW,
                    done,
                    project,
                    admin,
                    jdoe,
                    3,
                    LocalDate.now().minusDays(7)
            );
        }
    }
}

