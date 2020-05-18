package edu.ufp.inf.sd.rmi.hash.server;

import edu.ufp.inf.sd.rmi.hash.client.HashClient;

import java.io.Serializable;
import java.rmi.RemoteException;

public class Worker extends Thread implements Serializable {

    private String username;

    private Integer idTaskInput;

    private Integer option;

    private HashSessionRI session;

    private VisitorHashOperationsI v1;


    public Worker(String username, Integer idTaskInput, Integer option, HashSessionRI session, VisitorHashOperationsI v1) {
        this.username = username;
        this.idTaskInput = idTaskInput;
        this.option = option;
        this.session = session;
        this.v1 = v1;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void run(){
        System.out.println("thread is running...");
        TaskInput tk = new TaskInput(this.idTaskInput, this.option, this.username);

        try {
            WorkerInput wi = (WorkerInput) this.session.acceptVisitor(this.v1, tk);
            HashClient.StartWorking(wi, this.getId());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        //System.out.println(wi);
    }
}
