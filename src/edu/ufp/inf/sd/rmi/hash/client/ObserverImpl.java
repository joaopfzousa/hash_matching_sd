package edu.ufp.inf.sd.rmi.hash.client;


import edu.ufp.inf.sd.rmi.hash.server.HashSubjectRI;
import edu.ufp.inf.sd.rmi.hash.server.State;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ObserverImpl extends UnicastRemoteObject implements ObserverRI {

    private int idTaskGroup;

    private State lastObserverState;

    private String worker;

    private HashSubjectRI hashSubjectRI;

    public ObserverImpl(int idTaskGroup, HashSubjectRI hashSubjectRI, String worker) throws RemoteException{
        super();
        this.idTaskGroup = idTaskGroup;
        this.worker = worker;
        this.lastObserverState =  new State("",worker, idTaskGroup);
        this.hashSubjectRI = hashSubjectRI;
        this.hashSubjectRI.attach(this);
    }

    @Override
    public String toString() {
        return "ObserverImpl{" +
                "idTaskGroup=" + idTaskGroup +
                ", lastObserverState=" + lastObserverState +
                ", worker='" + worker + '\'' +
                '}';
    }

    @Override
    public int getIdTaskGroup() {
        return idTaskGroup;
    }

    public void setIdTaskGroup(int idTaskGroup) {
        this.idTaskGroup = idTaskGroup;
    }

    @Override
    public void update() throws RemoteException{
        this.lastObserverState = hashSubjectRI.getState();
    }

    public State getLastObserverState(){
        return this.lastObserverState;
    }
}
