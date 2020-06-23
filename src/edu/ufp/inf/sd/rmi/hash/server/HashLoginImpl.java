package edu.ufp.inf.sd.rmi.hash.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;


public class HashLoginImpl extends UnicastRemoteObject implements HashLoginRI {

    private HashMap<String, HashSessionRI> sessions = new HashMap<>();
    //private DBMockup database;

    public HashLoginImpl(/*DBMockup db*/) throws RemoteException {
        super();
        //this.database = db;
    }
    @Override
    public boolean register(String username, String password, int credits) throws RemoteException {
        boolean register = DBMockup.register(username, password, credits);
        return register;
    }

    @Override
    public HashSessionRI login(String username, String password) throws RemoteException
    {
        if (this.sessions.containsKey(username))
        {
            return this.sessions.get(username);
        }

        if (DBMockup.exists(username, password))
        {
            HashSessionImpl lib = new HashSessionImpl(username);

            this.sessions.put(username, lib);
            return lib;
        }
        return null;
    }



    @Override
    public HashSessionRI logout(String username) throws RemoteException {
        this.sessions.remove(username);
        return null;
    }
}