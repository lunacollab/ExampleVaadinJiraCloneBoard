package com.example.application.jira.ui;

import com.example.application.jira.model.BoardColumn;
import com.example.application.jira.model.Task;
import com.example.application.jira.service.TaskService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.function.Consumer;

public class KanbanColumn extends VerticalLayout {
    
    private final BoardColumn column;
    private final TaskService taskService;
    private final Consumer<Task> onTaskClick;
    private Div tasksContainer;
    
    public KanbanColumn(BoardColumn column,
                       TaskService taskService,
                       Consumer<Task> onTaskClick,
                       Runnable onRefresh) {
        this.column = column;
        this.taskService = taskService;
        this.onTaskClick = onTaskClick;
        
        setWidth("300px");
        setMinWidth("300px");
        setMaxWidth("300px");
        setSpacing(false);
        setPadding(false);
        getStyle().set("background", "white").set("border-radius", "8px").set("height", "fit-content");
        
        createHeader();
        createTasksContainer();
        loadTasks();
        
        // Global drop handler will be set up in JavaScript later
    }
    
    private void createHeader() {
        Div headerDot = new Div();
        headerDot.setWidth("12px");
        headerDot.setHeight("12px");
        headerDot.getStyle()
                .set("border-radius", "50%")
                .set("background", column.getColor() != null ? column.getColor() : "#6B778C");
        
        H3 headerTitle = new H3(column.getName());
        headerTitle.getStyle().set("margin", "0").set("font-size", "1rem").set("font-weight", "600");
        
        Span taskCount = new Span(String.valueOf(
                taskService.getTasksByColumn(column.getId()).size()));
        taskCount.getStyle().set("color", "#6B778C");
        
        Div headerContent = new Div(headerDot, headerTitle, taskCount);
        headerContent.getStyle()
                .set("display", "flex")
                .set("align-items", "center")
                .set("gap", "0.5rem");
        
        Button addBtn = new Button(VaadinIcon.PLUS.create());
        addBtn.setWidth("32px");
        addBtn.setHeight("32px");
        addBtn.addClickListener(e -> onTaskClick.accept(null));
        
        Div header = new Div(headerContent, addBtn);
        header.getStyle()
                .set("display", "flex")
                .set("justify-content", "space-between")
                .set("align-items", "center")
                .set("padding", "1rem")
                .set("border-bottom", "1px solid #e5e7eb");
        
        add(header);
    }
    
    private void createTasksContainer() {
        tasksContainer = new Div();
        tasksContainer.setWidthFull();
        tasksContainer.getStyle()
                .set("min-height", "200px")
                .set("padding", "0.5rem")
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("gap", "0.5rem");
        
        // Add column ID as attribute
        getElement().setAttribute("data-column-id", String.valueOf(column.getId()));
        addClassNames("kanban-column");
        
        // Drop zone events - using JavaScript
        getElement().executeJs(
            "this.addEventListener('dragover', function(e) {" +
            "  e.preventDefault();" +
            "  e.stopPropagation();" +
            "  this.classList.add('drag-over');" +
            "});" +
            "this.addEventListener('dragleave', function(e) {" +
            "  this.classList.remove('drag-over');" +
            "});" +
            "this.addEventListener('drop', function(e) {" +
            "  e.preventDefault();" +
            "  e.stopPropagation();" +
            "  this.classList.remove('drag-over');" +
            "});"
        );
        
        add(tasksContainer);
    }
    
    private void loadTasks() {
        tasksContainer.removeAll();
        java.util.List<Task> tasks = taskService.getTasksByColumn(column.getId());
        for (Task task : tasks) {
            tasksContainer.add(new TaskCard(task, onTaskClick));
        }
    }
    
    public void refresh() {
        loadTasks();
        // Update task count in header
        ((Span) ((Div) ((Div) getComponentAt(0)).getComponentAt(0)).getComponentAt(2))
                .setText(String.valueOf(taskService.getTasksByColumn(column.getId()).size()));
    }
    
    public BoardColumn getColumn() {
        return column;
    }
}

