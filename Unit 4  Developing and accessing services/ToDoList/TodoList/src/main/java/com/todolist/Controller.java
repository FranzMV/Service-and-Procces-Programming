package com.todolist;
import com.todolist.model.ToDoTask;
import com.todolist.services.DeleteToDoTask;
import com.todolist.services.GetToDoTasks;
import com.todolist.services.PostToDoTask;
import com.todolist.services.PutToDoTask;
import com.todolist.utils.MessageUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;


/**
 *<p> Main controller of the program.</p>
 * @author  Francisco David Manzanedo Valle.
 */
public class Controller implements Initializable {

    @FXML private TextField txtDescription;
    @FXML private ComboBox<String> comboShowType;
    @FXML private ComboBox<String> comboShowPriority;
    @FXML private ComboBox<String>comboShowDone;
    @FXML private ComboBox<String> comboShowDifficulty;
    @FXML private ListView<ToDoTask> listViewTasks;
    @FXML private ComboBox<String> comboSelectType;
    @FXML private ComboBox<String> comboSelectPriority;
    @FXML private ComboBox<String> comboSelectDone;
    @FXML private ComboBox<String> comboSelectDifficulty;

    private ObservableList<ToDoTask> toDoTaskObservableList;

    /** SERVICES */
    private GetToDoTasks getToDoTasks;
    private PostToDoTask postToDoTask;
    private PutToDoTask putToDoTask;
    private DeleteToDoTask deleteToDoTask;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadFilters();
        loadToDoTaskList();

        listViewTasks.getSelectionModel().selectedItemProperty().addListener(
                (ov, oldValue, newValue)->{
                    if(newValue != null)
                        updateFields(newValue);
                }
        );
    }

    /**
     *Button event to create a {@link ToDoTask}
     */
    @FXML
    private void addNewTask() {
        String description = txtDescription.getText();
        String type = comboSelectType.getValue();
        String done = comboSelectDone.getValue();
        String difficulty = comboSelectDifficulty.getValue();
        String priority = comboSelectPriority.getValue();

        if(!description.isEmpty() && !type.isEmpty() && !done.isEmpty() && !difficulty.isEmpty() && !priority.isEmpty() ) {
            ToDoTask newTask = new ToDoTask(description, type, Integer.parseInt(priority), done.equals("Done"),
                    Integer.parseInt(difficulty));
            postToDoTask = new PostToDoTask(newTask);
            postToDoTask.start();

            postToDoTask.setOnSucceeded(e -> {
                if (postToDoTask.getValue().isOk()) {
                    clearAllFields();
                    loadToDoTaskList();
                } else {
                    MessageUtils.showError("Error adding task", getToDoTasks.getValue().getError());
                }
            });
            postToDoTask.setOnFailed(e -> MessageUtils.showError("Error", "Error connecting to the server"));

        }else MessageUtils.showError("Error", "all the fields are mandatory");
    }


    /**
     *Button event to update a {@link ToDoTask}
     */
    @FXML
    private void updateTask() {
        if(listViewTasks.getSelectionModel().getSelectedItem() != null) {
            String description = txtDescription.getText();
            String type = comboSelectType.getValue();
            String done = comboSelectDone.getValue();
            String difficulty = comboSelectDifficulty.getValue();
            String priority = comboSelectPriority.getValue();
            ToDoTask updateTask = listViewTasks.getSelectionModel().getSelectedItem();
            //updateTask.setId(listViewTasks.getSelectionModel().getSelectedItem().getId());
            updateTask.setDescription(description);
            updateTask.setType(type);
            updateTask.setPriority(Integer.parseInt(priority));
            updateTask.setDifficulty(Integer.parseInt(difficulty));
            updateTask.setDone(done.equals("Done"));
            putToDoTask = new PutToDoTask(updateTask);
            putToDoTask.start();

            putToDoTask.setOnSucceeded(e->{
                if(putToDoTask.getValue().isOk()){
                    clearAllFields();
                    loadToDoTaskList();
                }else
                    MessageUtils.showError("Error updating task", putToDoTask.getValue().getError());
            });

            putToDoTask.setOnFailed(e->  MessageUtils.showError("Error", "Error connecting to the server"));

        }else MessageUtils.showMessage("Information", "You must select a task to update");

    }


    /**
     *Button event to delete a {@link ToDoTask}
     */
    @FXML
    public void deleteTask() {
        ToDoTask deleteTask = listViewTasks.getSelectionModel().getSelectedItem();
        if(deleteTask != null) {
            boolean confirmDelete = MessageUtils.showConfirmation("Delete task ", "Delete the task '" +
                    deleteTask.getDescription() + "' with type: " + deleteTask.getType());
            if(confirmDelete){
                deleteToDoTask = new DeleteToDoTask(deleteTask.getId());
                deleteToDoTask.start();

                deleteToDoTask.setOnSucceeded(e -> {
                    if (deleteToDoTask.getValue().isOk()) {
                        clearAllFields();
                        loadToDoTaskList();
                    } else
                        MessageUtils.showError("Error deleting task", getToDoTasks.getValue().getError());
                });

                deleteToDoTask.setOnFailed(e -> MessageUtils.showError("Error", "Error connecting to the server"));
            }
        }else MessageUtils.showMessage("Information", "You must select a task to delete");
    }


    /**
     *Filter the tasks to be displayed by type
     */
    @FXML
    private void filterType() {
        listViewTasks.getItems().clear();
        clearTaskFields();
        String filterType = comboShowType.getSelectionModel().getSelectedItem();
        GetToDoTasks getToDoTasks = new GetToDoTasks(filterType, null, null , null);
        getToDoTasks.start();

        getToDoTasks.setOnSucceeded(e-> {
            if(getToDoTasks.getValue().isOk()) {
                toDoTaskObservableList = FXCollections.observableList(getToDoTasks.getValue().getToDoTasks());
                listViewTasks.setItems(toDoTaskObservableList);
            }else{
                listViewTasks.getItems().clear();
                MessageUtils.showMessage("Information", "No tasks found for the selected filter");
            }
        });
    }


    /**
     *Filter the tasks to be displayed by priority.
     */
    @FXML
    private void filterPriority() {
        listViewTasks.getItems().clear();
        clearTaskFields();
        String filterPriority = comboShowPriority.getSelectionModel().getSelectedItem();
        GetToDoTasks getToDoTasks = new GetToDoTasks(null, filterPriority,null , null);
        getToDoTasks.start();

        getToDoTasks.setOnSucceeded(e-> {
            if(getToDoTasks.getValue().isOk()) {
                toDoTaskObservableList = FXCollections.observableList(getToDoTasks.getValue().getToDoTasks());
                listViewTasks.setItems(toDoTaskObservableList);
            }else {
                listViewTasks.getItems().clear();
                MessageUtils.showMessage("Information", "No tasks found for the selected filter");
            }
        });
    }

    /**
     *Filter the tasks to be displayed if they are done or not.
     */
    @FXML
    private void filterDone() {
        listViewTasks.getItems().clear();
        clearTaskFields();
        String filterDone = comboShowDone.getSelectionModel().getSelectedItem();
        GetToDoTasks getToDoTasks = new GetToDoTasks(null,  null,null , filterDone);
        getToDoTasks.start();

        getToDoTasks.setOnSucceeded(e-> {
            if(getToDoTasks.getValue().isOk()) {
                toDoTaskObservableList = FXCollections.observableList(getToDoTasks.getValue().getToDoTasks());
                listViewTasks.setItems(toDoTaskObservableList);
            }else{
                listViewTasks.getItems().clear();
                MessageUtils.showMessage("Information", "No tasks found for the selected filter");
            }
        });
    }

    /**
     *Filter the tasks to be displayed by difficulty.
     */
    @FXML
    private void filterDifficulty() {
        listViewTasks.getItems().clear();
        clearTaskFields();
        String filterDifficulty = comboShowDifficulty.getSelectionModel().getSelectedItem();
        GetToDoTasks getToDoTasks = new GetToDoTasks(null,  null,filterDifficulty , null);
        getToDoTasks.start();

        getToDoTasks.setOnSucceeded(e-> {
            if(getToDoTasks.getValue().isOk()) {
                toDoTaskObservableList = FXCollections.observableList(getToDoTasks.getValue().getToDoTasks());
                listViewTasks.setItems(toDoTaskObservableList);
            }else{
                listViewTasks.getItems().clear();
                MessageUtils.showMessage("Information", "No tasks found for the selected filter");
            }
        });
    }

    /**
     *Load all tasks to display them in a listView.
     */
    private void loadToDoTaskList(){
        String typeFilter = comboShowType.getSelectionModel().getSelectedItem();
        String doneFilter = comboShowDone.getSelectionModel().getSelectedItem();
        String priorityFilter = comboShowPriority.getSelectionModel().getSelectedItem();
        String difficultyFilter = comboShowDifficulty.getSelectionModel().getSelectedItem();
        getToDoTasks = new GetToDoTasks(typeFilter, doneFilter, priorityFilter , difficultyFilter);
        getToDoTasks.start();

        getToDoTasks.setOnSucceeded(e-> {
            if(getToDoTasks.getValue().isOk()) {
                toDoTaskObservableList = FXCollections.observableList(getToDoTasks.getValue().getToDoTasks());
                listViewTasks.setItems(toDoTaskObservableList);
            }else{
                listViewTasks.getItems().clear();
                MessageUtils.showMessage("Information", "There is not tasks stored!");
            }
        });
    }

    /**
     * Set the values in its corresponding fields when the user select an item in the listView.
     * @param task {@link ToDoTask} object selected by the user.
     */
    private void updateFields(ToDoTask task){
        txtDescription.setText(task.getDescription());
        comboSelectType.getSelectionModel().select(task.getType());
        comboSelectDifficulty.getSelectionModel().select(task.getDifficulty());
        comboSelectDone.getSelectionModel().select(task.isDone()? "Done": "Not done");
        comboSelectPriority.getSelectionModel().select(task.getPriority()-1);
    }

    /**
     *Load all the values in its corresponding Filter comboBox's and set "show all" value as default.
     */
    private void loadFilters(){
        comboShowType.getItems().addAll("Show all","home", "work", "family", "sport", "undefined");
        comboShowDone.getItems().addAll("Show all", "Done", "Not done");
        comboShowPriority.getItems().addAll("Show all","1","2","3","4", "5");
        comboShowDifficulty.getItems().addAll("Show all", "0","1","2","3", "4","5","6","7","8", "9", "10");
        comboSelectType.getItems().addAll("home", "work", "family", "sport", "undefined");
        comboSelectDone.getItems().addAll("Done", "Not done");
        comboSelectPriority.getItems().addAll("1","2","3","4","5");
        comboSelectDifficulty.getItems().addAll("0","1","2","3","4","5","6","7","8","9","10");
        comboShowType.getSelectionModel().selectFirst();
        comboShowDone.getSelectionModel().selectFirst();
        comboShowPriority.getSelectionModel().selectFirst();
        comboShowDifficulty.getSelectionModel().selectFirst();
    }

    /**
     * Clear the fields of the view
     */
    private void clearAllFields(){
        txtDescription.clear();
        comboSelectType.getSelectionModel().clearSelection();
        comboSelectPriority.getSelectionModel().clearSelection();
        comboSelectDone.getSelectionModel().clearSelection();
        comboSelectDifficulty.getSelectionModel().clearSelection();
        comboShowType.getSelectionModel().selectFirst();
        comboShowDone.getSelectionModel().selectFirst();
        comboShowPriority.getSelectionModel().selectFirst();
        comboShowDifficulty.getSelectionModel().selectFirst();
    }

    private void clearTaskFields(){
        txtDescription.clear();
        comboSelectType.getSelectionModel().clearSelection();
        comboSelectPriority.getSelectionModel().clearSelection();
        comboSelectDone.getSelectionModel().clearSelection();
        comboSelectDifficulty.getSelectionModel().clearSelection();
    }
}