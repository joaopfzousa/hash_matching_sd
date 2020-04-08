package edu.ufp.inf.sd.rmi.hash.server;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface HashRI extends Remote {
    public String methodName() throws RemoteException;
}