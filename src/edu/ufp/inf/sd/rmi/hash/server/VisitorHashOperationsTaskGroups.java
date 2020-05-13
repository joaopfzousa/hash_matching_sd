package edu.ufp.inf.sd.rmi.hash.server;

import edu.ufp.inf.sd.rmi.hash.client.ObserverRI;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class VisitorHashOperationsTaskGroups implements VisitorHashOperationsI, Serializable {

    private State subjectState;

    private ArrayList<ObserverRI> observers = new ArrayList<>();

    @Override
    public Object visitConcreteElementStateTasks(HashSessionRI element, TaskInput tk, DBMockup db) {

        SingletonOperationsTaskGroups sTaskGroup = ((HashSessionImpl)element).getStateTaskGroup();
        switch (tk.getOption())
        {
            case 1:
                return sTaskGroup.ListTaskGroup(db);
            case 2:
                return sTaskGroup.CreateTaskGroup(tk, db);
            case 3:
                // pause
            case 4:
                // delete
            case 5:
                this.subjectState = new State("","");
                return sTaskGroup.JoinTaskGroup(tk, db);
        }
        return null;
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
}
