module com.example.desktoptaskmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires java.sql;
    requires static lombok;
    requires java.desktop;


    opens com.example.app to javafx.fxml, com.fasterxml.jackson.databind;
    exports com.example.app;
}