package edu.ufp.inf.sd.rmi.hash.server;

import edu.ufp.inf.sd.rmi.hash.client.ObserverRI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class HashSessionImpl extends UnicastRemoteObject implements HashSessionRI {

    private DBMockup database;

    public SingletonOperationsTaskGroups stateTaskGroup;

    private State subjectState;

    private ArrayList<ObserverRI> observers = new ArrayList<>();

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


    @Override
    public void attach(ObserverRI obsRI) {
        if(!this.observers.contains(obsRI)) this.observers.add(obsRI);
    }

    @Override
    public void detach(ObserverRI obsRI) {
        this.observers.remove(obsRI);
    }

    @Override
    public State getState() {
        return this.subjectState;
    }

    @Override
    public void setState(State state) {
        this.subjectState = state;
        this.notifyAllObservers();
    }

    public void notifyAllObservers() {
        for(ObserverRI obs : observers){
            try{
                obs.update();
            } catch (RemoteException ex){
                System.out.println(ex.toString());
            }
        }
    }

    //                this.subjectState = new State("","");
}
