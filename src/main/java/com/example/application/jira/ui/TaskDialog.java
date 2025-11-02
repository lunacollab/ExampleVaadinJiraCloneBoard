package com.example.application.jira.ui;

import com.example.application.jira.model.*;
import com.example.application.jira.repository.BoardColumnRepository;
import com.example.application.jira.service.TaskService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import java.time.LocalDate;
import java.util.List;

public class TaskDialog extends Dialog {
    
    private final TaskService taskService;
    private final BoardColumnRepository boardColumnRepository;
    private final Task task;
    private final Project project;
    
    private TextField titleField;
    private TextArea descriptionField;
    private ComboBox<TaskType> typeComboBox;
    private ComboBox<TaskPriority> priorityComboBox;
    private ComboBox<BoardColumn> columnComboBox;
    private ComboBox<User> assigneeComboBox;
    private IntegerField storyPointsField;
    private DatePicker dueDatePicker;
    
    public TaskDialog(Task task, Project project, TaskService taskService, 
                     BoardColumnRepository boardColumnRepository) {
        this.task = task;
        this.project = project;
        this.taskService = taskService;
        this.boardColumnRepository = boardColumnRepository;
        
        setWidth("600px");
        setMaxHeight("90vh");
        
        createContent();
        
        if (task != null) {
            loadTaskData();
        }
    }
    
    private void createContent() {
        H2 title = new H2(task == null ? "Create New Task" : "Edit Task");
        title.getStyle().set("margin", "0 0 1rem 0");
        
        FormLayout form = new FormLayout();
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2)
        );
        form.setWidthFull();
        
        titleField = new TextField("Title");
        titleField.setRequired(true);
        titleField.setWidthFull();
        
        descriptionField = new TextArea("Description");
        descriptionField.setMaxHeight("150px");
        descriptionField.setWidthFull();
        
        typeComboBox = new ComboBox<>("Type");
        typeComboBox.setItems(TaskType.values());
        typeComboBox.setItemLabelGenerator(TaskType::getLabel);
        typeComboBox.setRequired(true);
        
        priorityComboBox = new ComboBox<>("Priority");
        priorityComboBox.setItems(TaskPriority.values());
        priorityComboBox.setItemLabelGenerator(TaskPriority::getLabel);
        priorityComboBox.setRequired(true);
        
        List<BoardColumn> columns = boardColumnRepository.findByProjectIdOrderByPosition(project.getId());
        columnComboBox = new ComboBox<>("Column");
        columnComboBox.setItems(columns);
        columnComboBox.setItemLabelGenerator(BoardColumn::getName);
        columnComboBox.setRequired(true);
        columnComboBox.setValue(columns.isEmpty() ? null : columns.get(0));
        
        List<User> members = project.getMembers().stream()
                .filter(m -> m.getId() != null)
                .toList();
        assigneeComboBox = new ComboBox<>("Assignee");
        assigneeComboBox.setItems(members);
        assigneeComboBox.setItemLabelGenerator(User::getFullName);
        
        storyPointsField = new IntegerField("Story Points");
        storyPointsField.setMin(0);
        storyPointsField.setMax(100);
        
        dueDatePicker = new DatePicker("Due Date");
        dueDatePicker.setMin(LocalDate.now());
        
        form.add(titleField, 2);
        form.add(descriptionField, 2);
        form.add(typeComboBox, 1);
        form.add(priorityComboBox, 1);
        form.add(columnComboBox, 1);
        form.add(assigneeComboBox, 1);
        form.add(storyPointsField, 1);
        form.add(dueDatePicker, 1);
        
        Button saveBtn = new Button("Save", VaadinIcon.CHECK.create());
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveBtn.addClickListener(e -> saveTask());
        
        Button cancelBtn = new Button("Cancel");
        cancelBtn.addClickListener(e -> close());
        
        Button deleteBtn = new Button("Delete", VaadinIcon.TRASH.create());
        deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteBtn.setVisible(task != null);
        deleteBtn.addClickListener(e -> deleteTask());
        
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        if (task != null) {
            buttonsLayout.add(deleteBtn);
        }
        buttonsLayout.add(cancelBtn, saveBtn);
        buttonsLayout.setJustifyContentMode(HorizontalLayout.JustifyContentMode.END);
        buttonsLayout.setWidthFull();
        
        Div content = new Div(title, form, buttonsLayout);
        content.getStyle().set("padding", "1.5rem");
        content.setWidthFull();
        
        add(content);
    }
    
    private void loadTaskData() {
        titleField.setValue(task.getTitle());
        descriptionField.setValue(task.getDescription());
        typeComboBox.setValue(task.getType());
        priorityComboBox.setValue(task.getPriority());
        columnComboBox.setValue(task.getColumn());
        assigneeComboBox.setValue(task.getAssignee());
        if (task.getStoryPoints() != null) {
            storyPointsField.setValue(task.getStoryPoints());
        }
        dueDatePicker.setValue(task.getDueDate());
    }
    
    private void saveTask() {
        if (titleField.getValue() == null || titleField.getValue().isBlank()) {
            Notification.show("Title is required", 3000, Notification.Position.MIDDLE);
            return;
        }
        
        if (typeComboBox.getValue() == null || priorityComboBox.getValue() == null || 
            columnComboBox.getValue() == null) {
            Notification.show("Type, Priority and Column are required", 3000, Notification.Position.MIDDLE);
            return;
        }
        
        try {
            if (task == null) {
                // Create new task
                taskService.createTask(
                        titleField.getValue(),
                        descriptionField.getValue(),
                        typeComboBox.getValue(),
                        priorityComboBox.getValue(),
                        columnComboBox.getValue(),
                        project,
                        project.getLead(), // Creator
                        assigneeComboBox.getValue(),
                        storyPointsField.getValue(),
                        dueDatePicker.getValue()
                );
                Notification.show("Task created successfully", 3000, Notification.Position.MIDDLE);
            } else {
                // Update existing task
                taskService.updateTask(
                        task.getId(),
                        titleField.getValue(),
                        descriptionField.getValue(),
                        typeComboBox.getValue(),
                        priorityComboBox.getValue(),
                        assigneeComboBox.getValue(),
                        storyPointsField.getValue(),
                        dueDatePicker.getValue()
                );
                Notification.show("Task updated successfully", 3000, Notification.Position.MIDDLE);
            }
            close();
        } catch (Exception e) {
            Notification.show("Error saving task: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }
    
    private void deleteTask() {
        try {
            taskService.softDeleteTask(task.getId());
            Notification.show("Task deleted successfully", 3000, Notification.Position.MIDDLE);
            close();
        } catch (Exception e) {
            Notification.show("Error deleting task: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }
}

