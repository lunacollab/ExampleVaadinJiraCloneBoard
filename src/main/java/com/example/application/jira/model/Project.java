package com.example.application.jira.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "projects")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Project extends BaseEntity {
    
    @NotBlank
    @Size(min = 3, max = 100)
    @Column(name = "name", nullable = false)
    @EqualsAndHashCode.Include
    private String name;
    
    @Size(max = 20)
    @Column(name = "`key`", unique = true, nullable = false, length = 20)
    private String key;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "avatar_url")
    private String avatarUrl;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id")
    private User lead;
    
    @Builder.Default
    @ManyToMany
    @JoinTable(
        name = "project_members",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members = new HashSet<>();
    
    @Builder.Default
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    @OrderBy("position ASC")
    private List<BoardColumn> columns = new ArrayList<>();
    
    @Builder.Default
    @OneToMany(mappedBy = "project")
    private Set<Task> tasks = new HashSet<>();
}

