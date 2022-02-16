package com.todolist.services;

import com.google.gson.Gson;
import com.todolist.model.TaskListResponse;
import com.todolist.model.ToDoTask;
import com.todolist.utils.ServiceUtils;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import com.todolist.model.TaskResponse;
/**
 * <p>Class in charge of performing a GET operations on the server a gets the different filtered out {@link ToDoTask}.
 * Inherits from {@link Service}.</p>
 * @author Francisco David Manzanedo Valle.
 */
public class GetToDoTasks extends Service<TaskListResponse> {

    /**
     * Represents the filter to be applied.
     */
    private String filter;

    /**
     * Creates an instance to be able to perform the different GET operations on the server.
     * @param filterType Filter {@link ToDoTask} by type.
     * @param filterPriority Filter {@link ToDoTask} by priority.
     * @param filterDifficulty Filter {@link ToDoTask} by difficulty.
     * @param filterDone Filter {@link ToDoTask} if they are done or not.
     */
    public GetToDoTasks(String filterType, String filterPriority, String filterDifficulty, String filterDone){
        filter ="";
        if(filterType != null && !filterType.equals("Show all")) {
            filter = "/type/" + filterType;
            System.out.println("/tasks"+filter);
        }
        else if(filterPriority != null && !filterPriority.equals("Show all")) {
            filter = "/priority/" + filterPriority;
            System.out.println("/tasks"+filter);
        }
        else if(filterDone != null && !filterDone.equals("Show all")){
            filter = "/done/" + (filterDone.equals("Done") ? "true": "false");
            System.out.println("/tasks"+filter);
        }

        else if(filterDifficulty != null && !filterDifficulty.equals("Show all")) {
            filter = "/difficulty/" + filterDifficulty;
            System.out.println("/tasks"+filter);
        }
    }

    /**
     * Asynchronous {@link Task} to perform the GET operations of a {@link ToDoTask}.
     * @return A {@link TaskResponse} with the response of the operation.
     */
    @Override
    protected Task<TaskListResponse> createTask() {
        return new Task<TaskListResponse>() {
            @Override
            protected TaskListResponse call() throws Exception {
                String json = ServiceUtils.getResponse(ServiceUtils.SERVER +"/tasks"+filter, null, "GET");
                Gson gson = new Gson();
                return gson.fromJson(json, TaskListResponse.class);
            }
        };
    }
}

