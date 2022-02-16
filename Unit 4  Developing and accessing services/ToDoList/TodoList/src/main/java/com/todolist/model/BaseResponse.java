package com.todolist.model;

/**
 *<p>Represents the response sent by the server when a CRUD operation is performed on it</p>
 * @author Francisco David Manzanedo.
 */
public class BaseResponse {

    /**
     * If any operation is performed well.
     */
    private boolean ok;
    /**
     * In case of error, cause of it.
     */
    private String error;

    /**
     * Get If any operation is performed well.
     * @return Boolean true if the operation went well, false if not.
     */
    public boolean isOk(){ return ok; }

    /**
     * Get the error message if it occurs.
     * @return String with the error message.
     */
    public String getError(){ return error; }

}
