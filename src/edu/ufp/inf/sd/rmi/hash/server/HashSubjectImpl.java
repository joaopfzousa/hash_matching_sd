package edu.ufp.inf.sd.rmi.hash.server;

import edu.ufp.inf.sd.rmi.hash.client.ObserverRI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class HashSubjectImpl extends UnicastRemoteObject implements HashSubjectRI {

    private State subjectState;

    private ArrayList<ObserverRI> observers = new ArrayList<>();

    protected HashSubjectImpl(int idtaskgroup) throws RemoteException {
        super();
        this.subjectState = new State("","", idtaskgroup);
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
        System.out.println("STATE = " + state);
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
}
