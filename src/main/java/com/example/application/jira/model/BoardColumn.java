package com.example.application.jira.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "board_columns")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class BoardColumn extends BaseEntity {
    
    @NotBlank
    @Column(name = "name", nullable = false)
    @EqualsAndHashCode.Include
    private String name;
    
    @Column(name = "position", nullable = false)
    private Integer position;
    
    @Column(name = "color")
    private String color;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
    
    @Builder.Default
    @OneToMany(mappedBy = "column", cascade = CascadeType.ALL)
    @OrderBy("position ASC")
    private List<Task> tasks = new ArrayList<>();
}

