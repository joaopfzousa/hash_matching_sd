package edu.ufp.inf.sd.rmi.hash.server;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface HashSessionRI extends Remote {
    public Object acceptVisitor(VisitorHashOperationsI visitor, TaskInput tk) throws RemoteException;
}