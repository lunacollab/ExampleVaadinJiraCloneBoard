package com.example.application.jira.ui;

import com.example.application.jira.model.Task;
import com.example.application.jira.model.User;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;

import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

public class TaskCard extends Div {
    
    public TaskCard(Task task, Consumer<Task> onClick) {
        getElement().setAttribute("draggable", "true");
        getElement().setAttribute("data-task-id", String.valueOf(task.getId()));
        addClassNames("kanban-task");
        
        getStyle()
                .set("background", "white")
                .set("border", "1px solid #e5e7eb")
                .set("border-radius", "8px")
                .set("padding", "0.75rem")
                .set("cursor", "grab")
                .set("margin-bottom", "0.5rem")
                .set("transition", "all 0.2s");
        
        addClickListener(e -> onClick.accept(task));
        
        // Drag events
        getElement().executeJs(
            "this.addEventListener('dragstart', function(e) {" +
            "  this.style.opacity = '0.5';" +
            "  e.dataTransfer.setData('text/plain', 'task-' + arguments[0]);" +
            "});" +
            "this.addEventListener('dragend', function(e) {" +
            "  this.style.opacity = '1';" +
            "});",
            task.getId()
        );
        
        getElement().addEventListener("mouseenter", e -> 
                getStyle().set("box-shadow", "0 4px 6px rgba(0,0,0,0.1)"));
        getElement().addEventListener("mouseleave", e -> 
                getStyle().remove("box-shadow"));
        
        createContent(task);
    }
    
    private void createContent(Task task) {
        // Task type and priority
        Div badgesDiv = new Div();
        badgesDiv.getStyle().set("display", "flex").set("gap", "0.25rem").set("margin-bottom", "0.5rem");
        
        Span typeBadge = createBadge(task.getType().getIcon(), task.getType().getColor());
        Span priorityBadge = createBadge(task.getPriority().getIcon(), task.getPriority().getColor());
        
        badgesDiv.add(typeBadge, priorityBadge);
        
        // Title
        Span title = new Span(task.getTitle());
        title.getStyle()
                .set("font-weight", "600")
                .set("display", "block")
                .set("margin-bottom", "0.5rem")
                .set("color", "#172b4d");
        
        // Description (truncated)
        String description = task.getDescription();
        if (description != null && description.length() > 100) {
            description = description.substring(0, 100) + "...";
        }
        if (description != null && !description.isEmpty()) {
            Span desc = new Span(description);
            desc.getStyle().set("color", "#6B778C").set("font-size", "0.875rem");
            add(desc);
        }
        
        // Footer
        Div footer = new Div();
        footer.getStyle()
                .set("display", "flex")
                .set("justify-content", "space-between")
                .set("align-items", "center")
                .set("margin-top", "0.75rem");
        
        // Story points
        if (task.getStoryPoints() != null && task.getStoryPoints() > 0) {
            Span storyPoints = new Span("SP: " + task.getStoryPoints());
            storyPoints.getStyle().set("color", "#6B778C").set("font-size", "0.875rem");
            footer.add(storyPoints);
        }
        
        // Due date
        if (task.getDueDate() != null) {
            Span dueDate = new Span(task.getDueDate().format(DateTimeFormatter.ofPattern("MMM dd")));
            dueDate.getStyle().set("color", "#6B778C").set("font-size", "0.875rem");
            footer.add(dueDate);
        }
        
        // Assignee avatar
        if (task.getAssignee() != null) {
            User assignee = task.getAssignee();
            Image avatar = new Image(
                    assignee.getAvatarUrl() != null ? assignee.getAvatarUrl() : 
                    "https://ui-avatars.com/api/?name=" + assignee.getFullName() + "&background=random",
                    assignee.getFullName()
            );
            avatar.setWidth("24px");
            avatar.setHeight("24px");
            avatar.getStyle().set("border-radius", "50%").set("border", "2px solid white");
            footer.add(avatar);
        }
        
        add(badgesDiv, title, description != null ? new Span(description) : new Span(), footer);
    }
    
    private Span createBadge(String text, String color) {
        Span badge = new Span(text);
        badge.getStyle()
                .set("background", color)
                .set("color", "white")
                .set("padding", "0.125rem 0.375rem")
                .set("border-radius", "4px")
                .set("font-size", "0.75rem");
        return badge;
    }
}

