package com.example.application.jira.model;

public enum TaskType {
    STORY("User Story", "#0052CC", "📘"),
    TASK("Task", "#0B875B", "📋"),
    BUG("Bug", "#E5493A", "🐛"),
    EPIC("Epic", "#7F5DD5", "📖");
    
    private final String label;
    private final String color;
    private final String icon;
    
    TaskType(String label, String color, String icon) {
        this.label = label;
        this.color = color;
        this.icon = icon;
    }
    
    public String getLabel() {
        return label;
    }
    
    public String getColor() {
        return color;
    }
    
    public String getIcon() {
        return icon;
    }
}

