package com.example.app;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class GUIHandler {

    public static BorderPane createLayout(TableView<Task> taskListTableView, HBox buttonsBox) {
        BorderPane layout = new BorderPane();
        BorderPane.setMargin(taskListTableView, new Insets(20, 0, 0, 0));
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setTop(buttonsBox);
        layout.setCenter(taskListTableView);

        return layout;
    }

    public static void showScene(Stage primaryStage, BorderPane layout, String title, boolean maximized) {
        Scene scene = new Scene(layout, TaskManagerApp.DEFAULT_WINDOW_WIDTH, TaskManagerApp.DEFAULT_WINDOW_HEIGHT);
        scene.getStylesheets().add(String.valueOf(GUIHandler.class.getResource(TaskManagerApp.STYLES_FILE_PATH)));
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(maximized);
        primaryStage.show();
    }

    public static HBox createButtonsBox(Button... buttons) {
        HBox buttonsBox = new HBox(10);
        buttonsBox.setAlignment(Pos.TOP_LEFT);
        buttonsBox.getChildren().addAll(buttons);

        return buttonsBox;
    }

    public static <T> Dialog<T> createDialog(String title, String headerText) {
        Dialog<T> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);

        return dialog;
    }

    public static GridPane createGrid(int hgap, int vgap) {
        GridPane grid = new GridPane();
        grid.setHgap(hgap);
        grid.setVgap(vgap);

        return grid;
    }

    public static TextArea createTextArea(boolean editable, boolean hasWrapText, String text) {
        TextArea ta = new TextArea(text);
        ta.setEditable(editable);
        ta.setWrapText(hasWrapText);

        return ta;
    }


}
