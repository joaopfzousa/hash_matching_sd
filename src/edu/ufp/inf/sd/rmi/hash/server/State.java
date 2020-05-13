package edu.ufp.inf.sd.rmi.hash.server;

import java.io.Serializable;

public class State  implements Serializable {

    private String msg;

    private String id;

    public State(String msg, String id) {
        this.msg = msg;
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
