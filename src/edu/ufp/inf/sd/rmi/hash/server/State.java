package edu.ufp.inf.sd.rmi.hash.server;

import java.io.Serializable;

public class State  implements Serializable {

    private String msg;

    private String worker;

    private int idTaskGroup;

    public State(String msg, String worker, int idTaskGroup) {
        this.msg = msg;
        this.worker = worker;
        this.idTaskGroup = idTaskGroup;
    }

    @Override
    public String toString() {
        return "State{" +
                "msg='" + msg + '\'' +
                ", worker='" + worker + '\'' +
                ", idTaskGroup=" + idTaskGroup +
                '}';
    }

    public int getIdTaskGroup() {
        return idTaskGroup;
    }

    public void setIdTaskGroup(int idTaskGroup) {
        this.idTaskGroup = idTaskGroup;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }
}
