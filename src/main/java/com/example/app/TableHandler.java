package com.example.app;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Timestamp;

public class TableHandler {

    public static TableView<Task> createTaskListTableView(ObservableList<Task> taskList) {
        TableView<Task> tableView = new TableView<>();

        TableColumn<Task, String> nameColumn = createStringColumn();
        TableColumn<Task, Timestamp> startDateColumn = createDateColumn("Start Date", "startDate");
        TableColumn<Task, Timestamp> endDateColumn = createDateColumn("End Date", "endDate");
        TableColumn<Task, String> statusColumn = createStatusColumn();

        tableView.getColumns().addAll(nameColumn, startDateColumn, endDateColumn, statusColumn);

        tableView.setItems(taskList);

        return tableView;
    }

    private static TableColumn<Task, String> createStringColumn() {
        TableColumn<Task, String> column = new TableColumn<>("Name");
        column.setCellValueFactory(new PropertyValueFactory<>("name"));
        column.setMinWidth(700);
        return column;
    }

    private static TableColumn<Task, Timestamp> createDateColumn(String name, String property) {
        TableColumn<Task, Timestamp> column = new TableColumn<>(name);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setMinWidth(300);
        setCenterAlignment(column);
        return column;
    }

    private static TableColumn<Task, String> createStatusColumn() {
        TableColumn<Task, String> column = new TableColumn<>("Status");
        column.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().isActive() ? "In Progress" : "Done"
        ));
        column.setMinWidth(200);
        setCenterAlignment(column);
        return column;
    }

    private static <T> void setCenterAlignment(TableColumn<Task, T> column) {
        column.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                    setAlignment(Pos.CENTER);
                }
            }
        });
    }
}
