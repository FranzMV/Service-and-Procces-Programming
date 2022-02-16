package com.todolist.model;

/**
 * <p>Represent the response sent by the server and inherits from {@link BaseResponse}.</p>
 * @author Francisco David Manzanedo Valle.
 */
public class TaskResponse extends BaseResponse{

    /**
     *{@link ToDoTask} object representing the response of the server.
     */
    private ToDoTask result;

    /**
     * Gets the {@link ToDoTask} as response of the server.
     * @return A {@link ToDoTask}.
     */
    public ToDoTask getResult() { return result; }
}
