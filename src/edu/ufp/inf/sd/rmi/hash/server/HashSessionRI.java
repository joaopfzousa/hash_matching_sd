package edu.ufp.inf.sd.rmi.hash.server;

import edu.ufp.inf.sd.rmi.hash.client.ObserverRI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HashSessionRI extends Remote {
    public Object acceptVisitor(VisitorHashOperationsI visitor, TaskInput tk) throws RemoteException;
    public void attach(ObserverRI obsRI) throws RemoteException;
    public void detach(ObserverRI obsRI) throws RemoteException;
    public State getState() throws RemoteException;
    public void setState(State state) throws RemoteException;
}