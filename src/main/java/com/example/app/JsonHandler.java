package com.example.app;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class JsonHandler {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ObservableList<Task> readAllTasksFromJsonFile(String fileName) throws IOException {
        final File tasksFilePath = new File(fileName);
        if (!tasksFilePath.exists()) {
            System.out.println("File does not exist: " + tasksFilePath.getAbsolutePath());
            try {
                if (tasksFilePath.createNewFile()) {
                    System.out.println("File created: " + tasksFilePath.getAbsolutePath());
                } else {
                    System.out.println("Failed to create file: " + tasksFilePath.getAbsolutePath());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (tasksFilePath.length() == 0) {
            System.out.println("File is empty: " + tasksFilePath.getAbsolutePath());
            return FXCollections.observableArrayList();
        }

        ArrayList<Task> taskListWrapper = objectMapper.readValue(tasksFilePath, new TypeReference<ArrayList<Task>>() {});
        ObservableList<Task> savedTasks = FXCollections.observableArrayList(taskListWrapper);
        return FXCollections.observableArrayList(savedTasks);
    }

    public static void writeTasksToJson(ObservableList<Task> tasks, String fileName) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), tasks);
    }
}
