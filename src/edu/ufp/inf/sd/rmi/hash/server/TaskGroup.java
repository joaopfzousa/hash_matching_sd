package edu.ufp.inf.sd.rmi.hash.server;

import java.io.Serializable;
import java.util.ArrayList;

public class TaskGroup implements Serializable {

    private int id;

    private ArrayList<User> users;

    private String hash;

    private Integer encryption;

    private int strategy;

    private int plafond;

    private String owner;

    private boolean pause;
    //nº de linhas que vai partir
    private int subsets;

    private boolean solved;

    public TaskGroup(String hash, Integer encryption, int plafond, String owner) {
        this.users = new ArrayList<>();
        this.hash = hash;
        this.encryption = encryption;
        this.plafond = plafond;
        this.owner = owner;
    }

    public TaskGroup(int id, ArrayList<User> users, String hash, Integer encryption, int strategy, int plafond, String owner, boolean pause, int subsets, boolean solved) {
        this.id = id;
        this.users = users;
        this.hash = hash;
        this.encryption = encryption;
        this.strategy = strategy;
        this.plafond = plafond;
        this.owner = owner;
        this.pause = pause;
        this.subsets = subsets;
        this.solved = solved;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Integer getEncryption() {
        return encryption;
    }

    public void setEncryption(Integer encryption) {
        this.encryption = encryption;
    }

    public int getStrategy() {
        return strategy;
    }

    public void setStrategy(int strategy) {
        this.strategy = strategy;
    }

    public int getPlafond() {
        return plafond;
    }

    public void setPlafond(int plafond) {
        this.plafond = plafond;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getSubsets() {
        return subsets;
    }

    public void setSubsets(int subsets) {
        this.subsets = subsets;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    @Override
    public String toString() {
        return "TaskGroup{" +
                "id=" + id +
                ", hash='" + hash + '\'' +
                ", encryption='" + encryption + '\'' +
                ", strategy=" + strategy +
                ", plafond=" + plafond +
                ", owner='" + owner + '\'' +
                ", pause=" + pause +
                ", solved=" + solved +
                '}';
    }
}
