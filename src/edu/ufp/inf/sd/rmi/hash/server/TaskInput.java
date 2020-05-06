package edu.ufp.inf.sd.rmi.hash.server;

import java.io.Serializable;

public class TaskInput implements Serializable {

    private int id;
    private int option;

    //List
    public TaskInput(int option) {
        this.option = option;
    }

    //Delete/Pause
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
}
