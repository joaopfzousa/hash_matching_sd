package edu.ufp.inf.sd.rmi.hash.client;


import edu.ufp.inf.sd.rmi.hash.server.HashSubjectRI;
import edu.ufp.inf.sd.rmi.hash.server.State;
import edu.ufp.inf.sd.rmi.util.States;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ObserverImpl extends UnicastRemoteObject implements ObserverRI {

    private int idTaskGroup;

    private State lastObserverState;

    private String worker;

    private HashSubjectRI hashSubjectRI;

    public ObserverImpl(int idTaskGroup, HashSubjectRI hashSubjectRI, String worker) throws RemoteException {
        super();
        this.idTaskGroup = idTaskGroup;
        this.worker = worker;
        this.lastObserverState = new State("", worker, idTaskGroup);
        this.hashSubjectRI = hashSubjectRI;
        this.hashSubjectRI.attach(this);
    }

    @Override
    public String toString() {
        return "ObserverImpl{" +
                "idTaskGroup=" + idTaskGroup +
                ", lastObserverState=" + lastObserverState +
                ", worker='" + worker + '\'' +
                '}';
    }

    @Override
    public int getIdTaskGroup() {
        return idTaskGroup;
    }

    public void setIdTaskGroup(int idTaskGroup) {
        this.idTaskGroup = idTaskGroup;
    }

    @Override
    public void checkStates() throws RemoteException {
        try {

            switch (this.getLastObserverState().getMsg()) {
                case (States.Pause):
                    System.out.println("[CLIENT] -> The taskGroup with id " + this.getLastObserverState().getIdTaskGroup() + ", user " + this.getLastObserverState().getWorker() + " sent-> " + States.Pause);
                    String msg = this.getLastObserverState().getMsg();
                    int count = 0;
                    while (msg.compareTo(States.Pause) == 0) {
                        msg = this.getLastObserverState().getMsg();
                        count++;

                        Thread.sleep(2000);

                        if (count == 100)
                            System.out.println("[CLIENT] -> The current state is still " + States.Pause);
                    }
                    break;
                case (States.UnPause):
                    System.out.println("[CLIENT] -> The taskGroup with id " + this.getLastObserverState().getIdTaskGroup() + ", user " + this.getLastObserverState().getWorker() + " sent-> " + States.UnPause);
                    break;
                case (States.Deleted):
                    System.out.println("[CLIENT] -> The taskGroup with id " + this.getLastObserverState().getIdTaskGroup() + ", user " + this.getLastObserverState().getWorker() + " sent-> " + States.Deleted);
                    break;
                case (States.NoCredit):
                    System.out.println("[CLIENT] -> The taskGroup with id " + this.getLastObserverState().getIdTaskGroup() + ", has no more credits");
                    break;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() throws RemoteException {
        this.lastObserverState = hashSubjectRI.getState();
    }

    public State getLastObserverState() {
        return this.lastObserverState;
    }
}
