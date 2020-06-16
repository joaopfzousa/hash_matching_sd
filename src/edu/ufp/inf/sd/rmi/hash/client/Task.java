package edu.ufp.inf.sd.rmi.hash.client;

import edu.ufp.inf.sd.rmi.hash.helpers.bcrypt.BCrypt;
import edu.ufp.inf.sd.rmi.hash.server.HashSessionRI;
import edu.ufp.inf.sd.rmi.hash.server.visitor.VisitorHashOperationsI;
import edu.ufp.inf.sd.rmi.hash.server.visitor.VisitorRequestCredits;
import edu.ufp.inf.sd.rmi.util.States;
import edu.ufp.inf.sd.rmi.util.lambdaworks.crypto.SCryptUtil;

import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static edu.ufp.inf.sd.rmi.hash.helpers.advanced.ReallyStrongSecuredPassword.generateStrongPasswordHash;
import static edu.ufp.inf.sd.rmi.hash.helpers.sha.SHAExample.get_SHA_512_SecurePassword;

class Task implements Runnable
{

    private int idTask;

    private String palavra;

    private Integer encrypt;

    private String hash;

    private ObserverImpl observer;

    private int line;

    private  String user;

    private HashSessionRI session;

    public Task(int idTask,String user, String palavra, Integer encrypt, String hash, ObserverImpl observer, int line, HashSessionRI session) {
        this.idTask = idTask;
        this.user = user;
        this.palavra = palavra;
        this.encrypt = encrypt;
        this.hash = hash;
        this.observer = observer;
        this.line = line;
        this.session = session;
    }

    public void run()
    {
        try {
            if(this.observer.checkStates())
            {
                try {
                    String securePassword = "";

                    switch (this.encrypt)
                    {
                        case (1):
                            try {
                                securePassword = get_SHA_512_SecurePassword(this.palavra);
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }
                            break;
                        case (2):
                            try {
                                securePassword = generateStrongPasswordHash(this.palavra);
                            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                                e.printStackTrace();
                            }
                            break;
                        case (3):
                            securePassword = BCrypt.hashpw(this.palavra, BCrypt.gensalt(12));
                            break;
                        case (4):
                            securePassword =  SCryptUtil.scrypt(this.palavra, 16, 16, 16);
                            break;
                    }

                    Thread.sleep(1000);
                    VisitorHashOperationsI v = null;

                    //System.out.println(securePassword);
                    if (hash.compareTo(securePassword) == 0)
                    {
                        System.out.println("Word found " + palavra);
                        v = new VisitorRequestCredits(idTask, user, 10);
                    } else {
                        v = new VisitorRequestCredits(idTask, user, 1);
                    }
                    session.acceptVisitor(v);
                    Thread.sleep(5000);
                } catch (InterruptedException | RemoteException e) {
                    e.printStackTrace();
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}