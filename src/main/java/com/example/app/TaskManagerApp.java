package com.example.app;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

public class TaskManagerApp extends Application {
    static final String TASKS_FILE_PATH = "src/main/resources/tasks.json";
    static final String STYLES_FILE_PATH = "/css/style.css";
    static final int DEFAULT_WINDOW_WIDTH = 1600;
    static final int DEFAULT_WINDOW_HEIGHT = 800;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        ObservableList<Task> taskList = JsonHandler.readAllTasksFromJsonFile(TASKS_FILE_PATH);
        TableView<Task> taskListTableView = TableHandler.createTaskListTableView(taskList);

        Button createTaskButton = new Button("Create");
        Button deleteSelectedButton = new Button("Delete");
        Button completeSelectedButton = new Button("Done");
        Button returnSelectedButton = new Button("Reopen");

        HBox buttonsBox = GUIHandler.createButtonsBox(createTaskButton, deleteSelectedButton, completeSelectedButton, returnSelectedButton);

        createTaskButton.setOnAction(e -> showCreateTaskDialog(taskList));
        deleteSelectedButton.setOnAction(e -> deleteSelectedTask(taskList, taskListTableView));
        completeSelectedButton.setOnAction(e -> completeSelectedTask(taskList, taskListTableView));
        returnSelectedButton.setOnAction(e -> returnSelectedTask(taskList, taskListTableView));

        setupOpenTaskClickListener(taskListTableView, taskList);

        BorderPane layout = GUIHandler.createLayout(taskListTableView, buttonsBox);
        GUIHandler.showScene(primaryStage, layout, "Task Manager", true);
    }


    private void completeSelectedTask(ObservableList<Task> taskList, TableView<Task> taskTableView) {
        Task selectedTask = taskTableView.getSelectionModel().getSelectedItem();

        if (selectedTask != null && selectedTask.isActive()) {
            selectedTask.setActive(false);
            taskList.remove(selectedTask);
            taskList.add(selectedTask);
            taskTableView.refresh();

            updateTaskListFile(taskList);
        }
    }

    private void returnSelectedTask(ObservableList<Task> taskList, TableView<Task> taskTableView) {
        Task selectedTask = taskTableView.getSelectionModel().getSelectedItem();

        if (selectedTask != null && !selectedTask.isActive()) {
            selectedTask.setActive(true);
            taskList.remove(selectedTask);
            taskList.add(0, selectedTask);

            taskTableView.refresh();

            updateTaskListFile(taskList);
        }
    }

    private void deleteSelectedTask(ObservableList<Task> taskList, TableView<Task> taskTableView) {
        Task selectedTask = taskTableView.getSelectionModel().getSelectedItem();

        if (selectedTask != null) {
            taskList.remove(selectedTask);
        }

        updateTaskListFile(taskList);
    }

    private void showTaskDetails(Task task, ObservableList<Task> taskList) {
        Dialog<Void> dialog = GUIHandler.createDialog("Task Details", "Task Information:");
        GridPane grid = GUIHandler.createGrid(10, 15);


        TextArea descriptionTextArea = GUIHandler.createTextArea(false, true, task.getDescription());

        TextField commentTextField = new TextField();
        ListView<Task.Comment> commentsListView = new ListView<>();

        commentsListView.setCellFactory(param -> new ListCell<Task.Comment>() {
            @Override
            protected void updateItem(Task.Comment comment, boolean empty) {
                super.updateItem(comment, empty);
                if (empty || comment == null) {
                    setText(null);
                } else {
                    setText(comment.getCommentText() + "\n" + comment.getTimestamp());
                }
            }
        });

        ButtonType buttonTypeClose = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        Button addCommentButton = new Button("Add Comment");
        addCommentButton.setOnAction(e -> {
            String commentText = commentTextField.getText().trim();
            if (!commentText.isEmpty()) {
                task.addComment(commentText);
                commentsListView.getItems().setAll(task.getComments());
                try {
                    JsonHandler.writeTasksToJson(taskList, TASKS_FILE_PATH);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Error writing tasks to file.");
                    alert.showAndWait();
                }
                commentTextField.clear();
            }
        });

        List<Task.Comment> taskComments = task.getComments();
        if (taskComments != null) {
            commentsListView.getItems().addAll(taskComments);
        }


        grid.add(new Label("Name:"), 0, 0);
        grid.add(new Label(task.getName()), 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descriptionTextArea, 1, 1);
        grid.add(new Label("Start Date:"), 0, 2);
        grid.add(new Label(task.getStartDate().toString()), 1, 2);
        grid.add(new Label("End Date:"), 0, 3);
        grid.add(new Label(task.getEndDate().toString()), 1, 3);
        grid.add(new Label("Comments:"), 0, 4);
        grid.add(commentTextField, 0, 5, 2, 1);
        grid.add(addCommentButton, 0, 6, 2, 1);
        grid.add(commentsListView, 0, 7, 2, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeClose);

        dialog.showAndWait();
    }

    private void showCreateTaskDialog(ObservableList<Task> taskList) {
        Dialog<Task> dialog = GUIHandler.createDialog("Create Task", "Enter details for a new task:");
        GridPane grid = GUIHandler.createGrid(10, 10);

        Label nameLabel = new Label("Name:");
        Label descriptionLabel = new Label("Description:");
        Label startDateLabel = new Label("Start Date:");
        Label endDateLabel = new Label("End Date:");

        TextField nameField = new TextField();
        TextArea descriptionField = new TextArea();
        DatePicker startDatePicker = new DatePicker();
        DatePicker endDatePicker = new DatePicker();

        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(descriptionLabel, 0, 1);
        grid.add(descriptionField, 1, 1);
        grid.add(startDateLabel, 0, 2);
        grid.add(startDatePicker, 1, 2);
        grid.add(endDateLabel, 0, 3);
        grid.add(endDatePicker, 1, 3);

        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, ButtonType.CANCEL);

        Node okButton = dialog.getDialogPane().lookupButton(buttonTypeOk);
        okButton.setDisable(true);
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeOk) {
                try {
                    Task newTask = new Task(
                            UUID.randomUUID(),
                            nameField.getText(),
                            descriptionField.getText(),
                            Timestamp.valueOf(startDatePicker.getValue().atStartOfDay()),
                            Timestamp.valueOf(endDatePicker.getValue().atStartOfDay()),
                            true,
                            false
                    );

                    boolean startDateBeforeEndDate = true;
                    if (startDatePicker.getValue() != null && endDatePicker.getValue() != null) {
                        startDateBeforeEndDate = !startDatePicker.getValue().isAfter(endDatePicker.getValue());
                    }
                    if (!startDateBeforeEndDate) {
                        throw new IllegalArgumentException("Start date cannot be after end date.");
                    }

                    taskList.add(0, newTask);
                    return newTask;
                } catch (NullPointerException e) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Fill in all details.");
                    alert.showAndWait();
                } catch (IllegalArgumentException e) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Start date cannot be after end date.");
                    alert.showAndWait();
                }
            }
            return null;
        });

        Optional<Task> result = dialog.showAndWait();
        result.ifPresent(task ->  updateTaskListFile(taskList));
    }

    private void setupOpenTaskClickListener(TableView<Task> taskListTableView, ObservableList<Task> taskList) {
        taskListTableView.setRowFactory(tableView -> {
            TableRow<Task> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Task selectedTask = row.getItem();
                    showTaskDetails(selectedTask, taskList);
                }
            });
            return row;
        });
    }

    private void updateTaskListFile(ObservableList<Task> taskList) {
        try {
            JsonHandler.writeTasksToJson(taskList, TASKS_FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error writing tasks to file.");
            alert.showAndWait();
        }
    }

}

