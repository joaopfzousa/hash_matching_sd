package edu.ufp.inf.sd.rmi.hash.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;


public class HashLoginImpl extends UnicastRemoteObject implements HashLoginRI {

    private HashMap<String, HashSessionRI> sessions = new HashMap<>();
    private DBMockup database;

    public HashLoginImpl(DBMockup db) throws RemoteException {
        super();
        this.database = db;
    }

    @Override
    public HashSessionRI login(String username, String password) throws RemoteException {
        if (this.sessions.containsKey(username)) {
            return this.sessions.get(username);
        }

        if (this.database.exists(username, password)) {
            HashSessionImpl lib = new HashSessionImpl(this.database);
            this.sessions.put(username, lib);

            return lib;
        }

        return null;
    }
}