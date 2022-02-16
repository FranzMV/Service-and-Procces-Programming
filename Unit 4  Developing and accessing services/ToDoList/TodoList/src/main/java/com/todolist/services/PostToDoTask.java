package com.todolist.services;

import com.google.gson.Gson;
import com.todolist.model.TaskResponse;
import com.todolist.model.ToDoTask;
import com.todolist.utils.ServiceUtils;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import com.todolist.model.TaskResponse;

/**
 * <p>Class in charge of performing a POST operation on the server and create different {@link ToDoTask}.
 * Inherits from {@link Service}.</p>
 * @author Francisco David Manzanedo Valle.
 */
public class PostToDoTask extends Service<TaskResponse> {

    /**
     * The {@link ToDoTask} to be created.
     */
    private final ToDoTask toDoTask;

    /**
     *Creates a new instance.
     * @param toDoTask {@link ToDoTask} object to be created.
     */
    public PostToDoTask(ToDoTask toDoTask){
        this.toDoTask = toDoTask;
    }

    /**
     * Asynchronous {@link Task} to perform the POST operation of a {@link ToDoTask}.
     * @return A {@link TaskResponse} with the response of the operation.
     */
    @Override
    protected Task<TaskResponse> createTask() {
        return new Task<>() {
            @Override
            protected TaskResponse call() {
                Gson gson = new Gson();
                String json = ServiceUtils.getResponse(
                        ServiceUtils.SERVER + "/tasks", gson.toJson(toDoTask), "POST");
                return gson.fromJson(json, TaskResponse.class);
            }
        };
    }
}
