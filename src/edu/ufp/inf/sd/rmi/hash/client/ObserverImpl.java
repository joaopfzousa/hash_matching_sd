package edu.ufp.inf.sd.rmi.hash.client;


import edu.ufp.inf.sd.rmi.hash.server.State;
import edu.ufp.inf.sd.rmi.hash.server.VisitorHashOperationsI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ObserverImpl extends UnicastRemoteObject implements ObserverRI{
    private String id;
    private State lastObserverState;
    protected VisitorHashOperationsI visitorHashOperationsI;
    protected HashClient hashClient;

    public ObserverImpl(String id, HashClient hashClient, VisitorHashOperationsI visitorHashOperationsI) throws RemoteException{
        super();
        this.id=id;
        this.hashClient = hashClient;
        this.lastObserverState =  new State(id,"");
        this.visitorHashOperationsI = visitorHashOperationsI;
        this.visitorHashOperationsI.attach(this);
    }

    @Override
    public void update() throws RemoteException{
        this.lastObserverState = visitorHashOperationsI.getState();
        //this.chatFrame.updateTextArea();
    }

    protected State getLastObserverState(){
        return this.lastObserverState;
    }
}
