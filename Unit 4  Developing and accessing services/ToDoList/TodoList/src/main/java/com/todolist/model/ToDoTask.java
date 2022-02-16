package com.todolist.model;

import com.google.gson.annotations.SerializedName;

/**
 *<p>Represents a task and its attributes</p>
 * @author Francisco David Manzanedo Valle
 */
public class ToDoTask {

    /**
     *The id of the task.
     */
    @SerializedName("_id")
    private String id;
    /**
     *The description of the task.
     */
    private String description;
    /**
     *The type of the task.
     */
    private String type;
    /**
     *The priority of the task.
     */
    private int priority;
    /**
     *Whether the task is done or not
     */
    private boolean done;
    /**
     *The difficulty of the task
     */
    private int difficulty;

    /**
     * Creates a task with its attributes.
     * @param description The description of the task.
     * @param type The type of the task.
     * @param priority The priority of the task.
     * @param done Whether the task is done or not.
     * @param difficulty The difficulty of the task
     */
    public ToDoTask(String description, String type, int priority, boolean done, int difficulty) {
        this.description = description;
        this.type = type;
        this.priority = priority;
        this.done = done;
        this.difficulty = difficulty;
    }

    /**
     * Gets the task's id.
     * @return A String corresponding with the task id.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the task's id.
     * @param id The task id.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the task's description.
     * @return A String corresponding with the description language.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the task's description.
     * @param description A String corresponding with the task's description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *Gets the task's type.
     * @return A String representing the task's type.
     */
    public String getType() {
        return type;
    }

    /**
     *Sets the task's type.
     * @param type A String corresponding with the task's type.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *Gets the task's priority.
     * @return An Integer representing the task's priority.
     */
    public int getPriority() {
        return priority;
    }

    /**
     *Sets the task's priority.
     * @param priority An integer corresponding with the task's type.
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     *Gets if the task is done or not.
     * @return A boolean representing if the task is done or not.
     */
    public boolean isDone() {
        return done;
    }

    /**
     *Sets if the task is done or not.
     * @param done A boolean corresponding if the task is done or not.
     */
    public void setDone(boolean done) {
        this.done = done;
    }

    /**
     *Gets the task´s difficulty.
     * @return An integer representing the task´s difficulty.
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     *Sets the task´s difficulty.
     * @param difficulty An integer corresponding with the task difficulty.
     */
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    /**
     *Overload of toString Method.
     * @return String with the task´s description.
     */
    @Override
    public String toString(){
        return description ;
    }
}
