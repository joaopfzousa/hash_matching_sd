package edu.ufp.inf.sd.rmi.hash.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class HashSessionImpl extends UnicastRemoteObject implements HashSessionRI {

    private DBMockup database;

    public SingletonOperationsTaskGroups stateTaskGroup;

    public HashSessionImpl(DBMockup db) throws RemoteException {
        super();
        this.database = db;
        this.stateTaskGroup = SingletonOperationsTaskGroups.createSingletonOperationsTaskGroups();
    }

    @Override
    public Object acceptVisitor(VisitorHashOperationsI visitor, TaskInput tk) throws RemoteException {
        return visitor.visitConcreteElementStateTasks(this, tk, this.database);
    }

    public SingletonOperationsTaskGroups getStateTaskGroup() {
        return this.stateTaskGroup;
    }

}
