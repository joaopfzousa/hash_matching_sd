package edu.ufp.inf.sd.rmi.hash.server;

import edu.ufp.inf.sd.rmi.hash.client.ObserverRI;

import java.rmi.RemoteException;

public interface VisitorHashOperationsI {
    public Object visitConcreteElementStateTasks(HashSessionRI element,TaskInput tk, DBMockup db);
}

