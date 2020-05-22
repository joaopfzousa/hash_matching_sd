package edu.ufp.inf.sd.rmi.hash.server;

import edu.ufp.inf.sd.rmi.hash.client.ObserverRI;

import java.io.Serializable;
import java.util.ArrayList;

public class TaskGroup implements Serializable {

    private int id;

    private ArrayList<String> users;

    private String hash;

    private Integer encryption;

    private int strategy;

    private int plafond;

    private String owner;

    private boolean pause;
    //nº de linhas que vai partir
    private int subsets;

    private int line;

    private boolean solved;

    private HashSubjectRI hashSubjectRI;

    private ObserverRI observer;


    public TaskGroup(String hash, Integer encryption, String owner) {
        this.users = new ArrayList<>();
        this.hash = hash;
        this.encryption = encryption;
        this.owner = owner;
    }

    public HashSubjectRI getHashSubjectRI() {
        return hashSubjectRI;
    }

    public void setHashSubjectRI(HashSubjectRI hashSubjectRI) {
        this.hashSubjectRI = hashSubjectRI;
    }

    public ObserverRI getObserver() {
        return observer;
    }

    public void setObserver(ObserverRI observer) {
        this.observer = observer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
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

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
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
