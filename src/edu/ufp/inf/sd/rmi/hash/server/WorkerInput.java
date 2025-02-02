package edu.ufp.inf.sd.rmi.hash.server;

import java.io.Serializable;

public class WorkerInput implements Serializable {

    private int idTask;

    private int line;

    private int subset;

    private String hash;

    private Integer encryption;

    private int strategy;

    private String file;

    private HashSubjectRI hashSubjectRI;

    private String user;

    public WorkerInput(int idTask,int line, int subset, String hash, Integer encryption, int strategy, String file, HashSubjectRI hashSubjectRI,String user) {
        this.idTask = idTask;
        this.line = line;
        this.subset = subset;
        this.hash = hash;
        this.encryption = encryption;
        this.strategy = strategy;
        this.file = file;
        this.hashSubjectRI = hashSubjectRI;
        this.user = user;
    }

    public int getIdTask() {
        return idTask;
    }

    public void setIdTask(int idTask) {
        this.idTask = idTask;
    }

    public HashSubjectRI getHashSubjectRI() {
        return hashSubjectRI;
    }

    public void setHashSubjectRI(HashSubjectRI hashSubjectRI) {
        this.hashSubjectRI = hashSubjectRI;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getSubset() {
        return subset;
    }

    public void setSubset(int subset) {
        this.subset = subset;
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

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "WorkerInput{" +
                "line=" + line +
                ", subset=" + subset +
                ", hash='" + hash + '\'' +
                ", encryption=" + encryption +
                ", strategy=" + strategy +
                ", file='" + file + '\'' +
                '}';
    }
}
