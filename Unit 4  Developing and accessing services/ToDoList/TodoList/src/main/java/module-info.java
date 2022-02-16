module com.todolist.todolist {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens com.todolist to javafx.fxml;
    exports com.todolist;
}