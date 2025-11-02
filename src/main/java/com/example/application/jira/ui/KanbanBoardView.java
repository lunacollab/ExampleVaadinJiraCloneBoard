package com.example.application.jira.ui;

import com.example.application.jira.model.BoardColumn;
import com.example.application.jira.model.Project;
import com.example.application.jira.model.Task;
import com.example.application.jira.repository.BoardColumnRepository;
import com.example.application.jira.repository.ProjectRepository;
import com.example.application.jira.service.TaskService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Kanban Board | Jira Clone")
@Route(value = "kanban", layout = MainLayout.class)
@AnonymousAllowed
public class KanbanBoardView extends VerticalLayout {

    private final ProjectRepository projectRepository;
    private final BoardColumnRepository boardColumnRepository;
    private final TaskService taskService;
    
    private ComboBox<Project> projectComboBox;
    private Div boardContainer;
    private List<KanbanColumn> columnComponents = new ArrayList<>();
    
    public KanbanBoardView(ProjectRepository projectRepository, 
                          BoardColumnRepository boardColumnRepository,
                          TaskService taskService) {
        this.projectRepository = projectRepository;
        this.boardColumnRepository = boardColumnRepository;
        this.taskService = taskService;
        
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        
        createHeader();
        createBoard();
        
        loadDefaultProject();
    }
    
    private void createHeader() {
        H1 title = new H1("Kanban Board");
        title.getStyle().set("margin", "0").set("font-size", "1.5rem").set("font-weight", "600");
        
        // Project selector
        projectComboBox = new ComboBox<>("Project");
        projectComboBox.setItems(projectRepository.findAll());
        projectComboBox.setItemLabelGenerator(Project::getName);
        projectComboBox.setWidth("300px");
        projectComboBox.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                loadProject(e.getValue().getId());
            }
        });
        
        Button addTaskBtn = new Button("Add Task", VaadinIcon.PLUS.create());
        addTaskBtn.addClickListener(e -> openTaskDialog(null));
        
        Button refreshBtn = new Button("Refresh", VaadinIcon.REFRESH.create());
        refreshBtn.addClickListener(e -> refreshBoard());
        
        HorizontalLayout header = new HorizontalLayout(title, projectComboBox, addTaskBtn, refreshBtn);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setWidthFull();
        header.setPadding(true);
        header.getStyle().set("border-bottom", "1px solid #e5e7eb");
        
        add(header);
    }
    
    private void createBoard() {
        boardContainer = new Div();
        boardContainer.setWidthFull();
        boardContainer.getStyle().set("display", "flex")
                .set("gap", "1rem")
                .set("padding", "1rem")
                .set("overflow-x", "auto")
                .set("background", "#f3f4f6")
                .set("flex-grow", "1");
        
        add(boardContainer);
        setFlexGrow(1, boardContainer);
    }
    
    private void loadDefaultProject() {
        List<Project> projects = projectRepository.findAll();
        if (!projects.isEmpty()) {
            projectComboBox.setValue(projects.get(0));
            loadProject(projects.get(0).getId());
        }
    }
    
    private void loadProject(Long projectId) {
        columnComponents.clear();
        boardContainer.removeAll();
        
        List<BoardColumn> columns = boardColumnRepository.findByProjectIdOrderByPosition(projectId);
        for (BoardColumn column : columns) {
            KanbanColumn columnComponent = new KanbanColumn(
                    column, 
                    taskService,
                    this::openTaskDialog,
                    this::refreshBoard
            );
            columnComponents.add(columnComponent);
            boardContainer.add(columnComponent);
        }
        
        // Setup global drop handler
        setupDragAndDrop();
    }
    
    private void setupDragAndDrop() {
        getElement().executeJs(
            "var self = this;" +
            "document.addEventListener('drop', function(e) {" +
            "  var taskId = e.dataTransfer.getData('text/plain');" +
            "  if (taskId && taskId.startsWith('task-')) {" +
            "    taskId = taskId.replace('task-', '');" +
            "    var targetColumn = e.target.closest('[data-column-id]');" +
            "    if (targetColumn) {" +
            "      var columnId = targetColumn.getAttribute('data-column-id');" +
            "      self.$server.handleTaskDrop(parseInt(taskId), parseInt(columnId));" +
            "    }" +
            "  }" +
            "});"
        );
    }
    
    @ClientCallable
    private void handleTaskDrop(Long taskId, Long targetColumnId) {
        // Move task to new column
        Integer maxPosition = taskService.getMaxPositionByColumnId(targetColumnId);
        int newPosition = (maxPosition != null) ? maxPosition + 1 : 0;
        taskService.moveTask(taskId, targetColumnId, newPosition);
        refreshBoard();
    }
    
    private void refreshBoard() {
        Project currentProject = projectComboBox.getValue();
        if (currentProject != null) {
            loadProject(currentProject.getId());
        }
    }
    
    private void openTaskDialog(Task task) {
        Project currentProject = projectComboBox.getValue();
        if (currentProject == null) {
            return;
        }
        
        TaskDialog dialog = new TaskDialog(task, currentProject, taskService, boardColumnRepository);
        dialog.addOpenedChangeListener(e -> {
            if (!e.isOpened()) {
                refreshBoard();
            }
        });
        dialog.open();
    }
}

