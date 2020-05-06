package edu.ufp.inf.sd.rmi.hash.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class HashSessionImpl extends UnicastRemoteObject implements HashSessionRI {


    private DBMockup database;

    public HashSessionImpl(DBMockup db) throws RemoteException {
        super();
        this.database = db;
    }

    @Override
    public Object acceptVisitor(VisitorHashOperationsI visitor, TaskInput tk) throws RemoteException {
        visitor.visitConcreteElementStateTasks(this, tk,this.database);
        return null;
    }
}