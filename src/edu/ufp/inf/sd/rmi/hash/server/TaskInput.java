package edu.ufp.inf.sd.rmi.hash.server;

import java.io.Serializable;

public class TaskInput implements Serializable {

    private int id;

    private int option;

    private TaskGroup tasksGroup;

    //List
    public TaskInput(int option) {
        this.option = option;
    }

    //Create
    public TaskInput(int option, TaskGroup tasksGroup) {
        this.option = option;
        this.tasksGroup = tasksGroup;
    }

    //Delete/Pause/Join
    public TaskInput(int id, int option) {
        this.id = id;
        this.option = option;
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
