package edu.ufp.inf.sd.rmi.hash.server;

import java.io.Serializable;

public class TaskInput implements Serializable {

    private int id;

    private int option;

    private TaskGroup tasksGroup;

    private String username;

    //List
    public TaskInput(int option) {
        this.option = option;
    }

    //Create
    public TaskInput(int option, TaskGroup tasksGroup) {
        this.option = option;
        this.tasksGroup = tasksGroup;
    }

    //Delete/Pause
    public TaskInput(int id, int option) {
        this.id = id;
        this.option = option;
    }

    //Join
    public TaskInput(int id, int option, String username) {
        this.id = id;
        this.option = option;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public int getOption() {
        return option;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOption(int option) {
        this.option = option;
    }

    public TaskGroup getTasksGroup() {
        return tasksGroup;
    }

    public void setTasksGroup(TaskGroup tasksGroup) {
        this.tasksGroup = tasksGroup;
    }
}
