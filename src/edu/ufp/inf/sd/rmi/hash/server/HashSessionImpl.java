package edu.ufp.inf.sd.rmi.hash.server;

import edu.ufp.inf.sd.rmi.hash.server.visitor.VisitorHashOperationsI;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.Key;
import java.util.Date;

public class HashSessionImpl extends UnicastRemoteObject implements HashSessionRI  {

    public static long ttlMillis = 864000L;

    private DBMockup database;

    public SingletonOperationsTaskGroups stateTaskGroup;

    public String jwt;

    public HashSessionImpl(DBMockup db, String username) throws RemoteException {
        super();
        this.database = db;
        this.stateTaskGroup = SingletonOperationsTaskGroups.createSingletonOperationsTaskGroups();
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + ttlMillis;
        Date exp = new Date(expMillis);
        this.jwt = Jwts.builder().setSubject(username).signWith(key).setExpiration(exp).compact();

        System.out.println(username + " JWT = " + this.jwt);
    }

    @Override
    public Object acceptVisitor(VisitorHashOperationsI visitor) throws RemoteException {
        return visitor.visitConcreteElementStateTasks(this, this.database);
    }

    public SingletonOperationsTaskGroups getStateTaskGroup() {
        return this.stateTaskGroup;
    }

}
