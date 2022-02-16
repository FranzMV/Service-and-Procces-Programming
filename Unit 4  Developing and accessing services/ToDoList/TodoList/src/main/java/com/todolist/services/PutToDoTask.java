package com.todolist.services;

import com.google.gson.Gson;
import com.todolist.model.TaskResponse;
import com.todolist.model.ToDoTask;
import com.todolist.utils.ServiceUtils;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * <p>Class in charge of performing a PUT operation on the server and update a {@link ToDoTask}.
 * Inherits from {@link Service}.</p>
 * @author Francisco David Manzanedo Valle.
 */
public class PutToDoTask extends Service<TaskResponse> {

    /**
     * The {@link ToDoTask} to be updated.
     */
    private final ToDoTask toDoTask;

    /**
     *Creates a new instance.
     * @param toDoTask {@link ToDoTask} object to be updated.
     */
    public PutToDoTask(ToDoTask toDoTask){
        this.toDoTask = toDoTask;
    }


    /**
     * Asynchronous {@link Task} to perform the PUT operation of a {@link ToDoTask}.
     * @return A {@link TaskResponse} with the response of the operation.
     */
    @Override
    protected Task<TaskResponse> createTask() {
        return new Task<>() {
            @Override
            protected TaskResponse call() {
                Gson gson = new Gson();
                String response = ServiceUtils.getResponse(ServiceUtils.SERVER + "/tasks/" + toDoTask.getId(),
                        gson.toJson(toDoTask), "PUT");
                System.out.println(response);
                return gson.fromJson(response, TaskResponse.class);
            }
        };
    }
}
