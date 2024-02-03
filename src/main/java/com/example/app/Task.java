package com.example.app;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Task {
    private UUID UUID;
    private String name;
    private String description;
    private Timestamp startDate;
    private Timestamp endDate;
    private ArrayList<Comment> comments;
    private boolean active;
    private boolean selected;

    public Task(UUID UUID, String name, String description, Timestamp startDate, Timestamp endDate, boolean active, boolean selected) {
        this.UUID = UUID;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.comments = new ArrayList<>();
        this.active = active;
        this.selected = selected;
    }

    public void addComment(String commentText) {
        comments.add(new Comment(commentText));
    }



    @Getter
    @Setter
    @NoArgsConstructor
    public static class Comment {
        private String commentText;
        private Timestamp timestamp;

        public Comment(String commentText) {
            this.commentText = commentText;
            this.timestamp = new Timestamp(System.currentTimeMillis());
        }
    }
}
