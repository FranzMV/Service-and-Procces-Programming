package com.todolist.model;

import java.util.List;

/**
 * <p>Represents a {@link List} of {@link ToDoTask} and inherits from {@link BaseResponse}. </p>
 * @author Francisco David Manzanedo Valle.
 */
public class TaskListResponse extends BaseResponse {

    /**
     * A {@link List} of {@link ToDoTask}.
     */
    private List<ToDoTask> result;

    /**
     * Gets the {@link List} of {@link ToDoTask}.
     * @return A {@link List} of {@link ToDoTask}.
     */
    public List<ToDoTask> getToDoTasks() { return result ;}
}
