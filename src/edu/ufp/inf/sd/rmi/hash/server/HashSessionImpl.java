package edu.ufp.inf.sd.rmi.hash.server;

import edu.ufp.inf.sd.rmi.hash.server.visitor.VisitorHashOperationsI;
import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.Key;
import java.util.Base64;
import java.util.Date;


public class HashSessionImpl extends UnicastRemoteObject implements HashSessionRI  {

    public static long ttlMillis = 864000L;

    private static String SECRET_KEY = "b2xhRXVRdWVyb0JhdGF0aW5oYXNDb21BcnJvemVQYW5hZG9zMTIzOTg3";

    private DBMockup database;

    public SingletonOperationsTaskGroups stateTaskGroup;

    public String jwt;

    public HashSessionImpl(DBMockup db, String username) throws RemoteException {
        super();
        this.database = db;
        this.stateTaskGroup = SingletonOperationsTaskGroups.createSingletonOperationsTaskGroups();
        this.jwt = createJWT(username, ttlMillis);
        //System.out.println(username + " JWT = " + this.jwt);
    }

    public static String createJWT(String subject, long ttlMillis) {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = Base64.getDecoder().decode(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(now)
                .setSubject(subject)
                .signWith(signatureAlgorithm, signingKey);

        //if it has been specified, let's add the expiration
        if (ttlMillis > 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    public static void decodeJWT(String jwt) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        //Claims claims =
                Jwts.parser()
                .setSigningKey(Base64.getDecoder().decode(SECRET_KEY))
                .parseClaimsJws(jwt).getBody();
        //return claims;
    }

    @Override
    public Object acceptVisitor(VisitorHashOperationsI visitor) throws RemoteException {
        //Claims jws;
        try {
            decodeJWT(this.jwt);

            return visitor.visitConcreteElementStateTasks(this, this.database);

            // we can safely trust the JWT
        }catch (ExpiredJwtException ex) {
            // we *cannot* use the JWT as intended by its creator
            System.out.println("Provided token is expired");
        }catch (JwtException ex) {
            // we *cannot* use the JWT as intended by its creator
            System.out.println("Exception = " + ex);
        }
        return null;
    }

    public SingletonOperationsTaskGroups getStateTaskGroup() {
        return this.stateTaskGroup;
    }

}
