package com.example.application.jira.model;

public enum TaskPriority {
    LOWEST("Lowest", "#6B778C", "🔽"),
    LOW("Low", "#0494A8", "⬇️"),
    MEDIUM("Medium", "#FFAA44", "➡️"),
    HIGH("High", "#E74440", "⬆️"),
    HIGHEST("Highest", "#CC0000", "🔺");
    
    private final String label;
    private final String color;
    private final String icon;
    
    TaskPriority(String label, String color, String icon) {
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

