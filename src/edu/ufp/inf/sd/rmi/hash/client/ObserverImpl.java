package edu.ufp.inf.sd.rmi.hash.client;


import edu.ufp.inf.sd.rmi.hash.server.State;
import edu.ufp.inf.sd.rmi.hash.server.HashSessionRI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ObserverImpl extends UnicastRemoteObject implements ObserverRI{
    private String id;
    private State lastObserverState;
    protected HashSessionRI hashSessionRI;
    protected HashClient hashClient;

    public ObserverImpl(String id, HashClient hashClient, HashSessionRI hashSessionRI) throws RemoteException{
        super();
        this.id=id;
        this.hashClient = hashClient;
        this.lastObserverState =  new State(id,"");
        this.hashSessionRI = hashSessionRI;
        this.hashSessionRI.attach(this);
    }

    @Override
    public void update() throws RemoteException{
        this.lastObserverState = hashSessionRI.getState();
        //this.chatFrame.updateTextArea();
    }

    protected State getLastObserverState(){
        return this.lastObserverState;
    }
}
