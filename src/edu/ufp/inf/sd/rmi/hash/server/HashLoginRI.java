package edu.ufp.inf.sd.rmi.hash.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HashLoginRI extends Remote {
    HashSessionRI login(String username, String password) throws RemoteException;
    HashSessionRI logout(String username) throws  RemoteException;
}