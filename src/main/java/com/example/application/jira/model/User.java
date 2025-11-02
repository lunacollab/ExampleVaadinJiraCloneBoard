package com.example.application.jira.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class User extends BaseEntity {
    
    @NotBlank
    @Size(min = 3, max = 50)
    @Column(name = "username", unique = true, nullable = false)
    @EqualsAndHashCode.Include
    private String username;
    
    @NotBlank
    @Size(max = 100)
    @Column(name = "full_name", nullable = false)
    private String fullName;
    
    @Email
    @NotBlank
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    
    @NotBlank
    @Size(min = 60)
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;
    
    @Column(name = "avatar_url")
    private String avatarUrl;
    
    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
    
    @Builder.Default
    @ManyToMany(mappedBy = "members")
    private Set<Project> projects = new HashSet<>();
    
    @Builder.Default
    @OneToMany(mappedBy = "assignee")
    private Set<Task> assignedTasks = new HashSet<>();
    
    @Builder.Default
    @OneToMany(mappedBy = "reporter")
    private Set<Task> reportedTasks = new HashSet<>();
    
    @Builder.Default
    @OneToMany(mappedBy = "creator")
    private Set<Task> createdTasks = new HashSet<>();
    
    public boolean hasRole(String roleName) {
        return roles.stream().anyMatch(role -> role.getName().equals(roleName));
    }
    
    public boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }
    
    public boolean isProjectManager() {
        return hasRole("ROLE_PROJECT_MANAGER");
    }
    
    public boolean isDeveloper() {
        return hasRole("ROLE_DEVELOPER");
    }
}

