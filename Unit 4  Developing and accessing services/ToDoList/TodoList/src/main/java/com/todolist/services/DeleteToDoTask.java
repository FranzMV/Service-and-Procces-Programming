package com.todolist.services;

import com.google.gson.Gson;
import com.todolist.model.TaskResponse;
import com.todolist.utils.ServiceUtils;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import com.todolist.model.ToDoTask;

/**
 * <p>Class in charge of performing a DELETE operation on the server and deleting a {@link ToDoTask}. Inherits from
 * {@link Service}.</p>
 * @author Francisco David Manzanedo Valle.
 */
public class DeleteToDoTask extends Service<TaskResponse> {

    /**
     * The id of the {@link ToDoTask} to be deleted.
     */
    private final String id;

    /**
     * Creates a new instance.
     * @param id A String corresponding with the id of the {@link ToDoTask}.
     */
    public DeleteToDoTask(String id){
        this.id = id;
    }

    /**
     * Asynchronous {@link Task} to perform the DELETE operation of a {@link ToDoTask}.
     * @return A {@link TaskResponse} with the response of the operation.
     */
    @Override
    protected Task<TaskResponse> createTask() {
        return new Task<TaskResponse>() {
            @Override
            protected TaskResponse call() throws Exception {
                Gson gson = new Gson();
                String json = ServiceUtils.getResponse(ServiceUtils.SERVER+"/tasks/"+id, null, "DELETE");
                return gson.fromJson(json, TaskResponse.class);
            }
        };
    }
}
