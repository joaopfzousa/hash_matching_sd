package edu.ufp.inf.sd.rmi.hash.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HashLoginRI extends Remote {
    boolean register(String username, String password, int credits) throws  RemoteException;
    HashSessionRI login(String username, String password) throws RemoteException;
    HashSessionRI logout(String username) throws  RemoteException;
}