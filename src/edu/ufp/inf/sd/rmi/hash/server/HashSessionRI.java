package edu.ufp.inf.sd.rmi.hash.server;

import edu.ufp.inf.sd.rmi.hash.server.visitor.VisitorHashOperationsI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HashSessionRI extends Remote {
    public Object acceptVisitor(VisitorHashOperationsI visitor) throws RemoteException;
}