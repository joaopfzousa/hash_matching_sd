package edu.ufp.inf.sd.rmi.hash.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class HashImpl extends UnicastRemoteObject implements HashRI {


    public HashImpl() throws RemoteException {
        super();
    }

    @Override
    public String methodName() throws RemoteException {
        System.out.println("Someone called me - methodName()!");
        return "Response from methodName!";
    }
}