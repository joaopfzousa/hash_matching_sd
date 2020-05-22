package edu.ufp.inf.sd.rmi.hash.server;

import edu.ufp.inf.sd.rmi.hash.client.HashClient;
import edu.ufp.inf.sd.rmi.hash.client.ObserverImpl;
import edu.ufp.inf.sd.rmi.hash.helpers.bcrypt.BCrypt;
import edu.ufp.inf.sd.rmi.util.lambdaworks.crypto.SCryptUtil;

import java.io.*;
import java.rmi.RemoteException;

import static edu.ufp.inf.sd.rmi.hash.helpers.advanced.ReallyStrongSecuredPassword.generateStrongPasswordHash;
import static edu.ufp.inf.sd.rmi.hash.helpers.sha.SHAExample.get_SHA_512_SecurePassword;

public class Worker extends Thread implements Serializable {

    private String username;

    private Integer idTaskInput;

    private Integer option;

    private HashSessionRI session;

    private VisitorHashOperationsI v1;

    private HashSubjectRI hashSubjectRI;

    private ObserverImpl observer;

    public Worker(String username, Integer idTaskInput, Integer option, HashSessionRI session, VisitorHashOperationsI v1) throws RemoteException {
        this.username = username;
        this.idTaskInput = idTaskInput;
        this.option = option;
        this.session = session;
        this.v1 = v1;
        hashSubjectRI = new HashSubjectImpl(idTaskInput);

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void run(){
        System.out.println("thread is running...");
        TaskInput tk = new TaskInput(this.idTaskInput, this.option, this.username);

        try {
            WorkerInput wi = (WorkerInput) this.session.acceptVisitor(this.v1, tk);
            this.StartWorking(wi, this.getName());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        //System.out.println(wi);
    }

    public boolean StartWorking(WorkerInput wi, String idThread) throws RemoteException {
        System.out.println("thread nº = " + idThread);

        observer = new ObserverImpl(this.idTaskInput, this.hashSubjectRI, idThread);
        try {
            File f = new File(wi.getFile());
            String f1 = f.getAbsolutePath();
            if (!f.isFile() || !f.canRead())
                throw new IOException("can't read " + wi.getFile());

            BufferedReader br = new BufferedReader(new FileReader(f));
            try (LineNumberReader lnr = new LineNumberReader(br)) {
                String line;
                while (lnr.readLine() != null && lnr.getLineNumber() < wi.getLine()) {
                }

                String securePassword = "";
                switch (wi.getEncryption()) {
                    case (1):
                        while ((line = lnr.readLine()) != null && lnr.getLineNumber() < wi.getLine() + wi.getSubset()) {

                            //System.out.println(observer);
                            //String msg="[" + observer.getLastObserverState().getWorker() + "] " + observer.getLastObserverState().getMsg();
                            System.out.println("MSG = " + observer.getLastObserverState().getMsg());
                            if(observer.getLastObserverState().getMsg().compareTo("Pause") == 0)
                            {
                                System.out.println("Parei lindo, conseguiste na linha = " + lnr.getLineNumber());
                                break;
                            }
                            securePassword = get_SHA_512_SecurePassword(line);
                            //System.out.println(securePassword);

                            if(lnr.getLineNumber() == 100 || lnr.getLineNumber() == 10010)
                            {
                                System.out.println(" hash = " + securePassword);
                            }

                            if (securePassword.compareTo(wi.getHash()) == 0) {
                                System.out.println("Encontrei a hash na linha " + lnr.getLineNumber() + "!");
                                //observer (descobri a password)
                                //user recebe 10 créditos
                                //para worker
                            } else {
                                //recebe 1 credito
                            }
                            //System.out.println(securePassword);
                        }
                        break;
                    case (2):
                        while ((line = lnr.readLine()) != null && lnr.getLineNumber() < wi.getLine() + wi.getSubset()) {
                            securePassword = generateStrongPasswordHash(line);

                            if (securePassword.compareTo(wi.getHash()) == 0) {
                                System.out.println("Encontrei a hash na linha " + lnr.getLineNumber() + "!");
                                //observer (descobri a password)
                                //user recebe 10 créditos
                                //para worker
                            } else {
                                //recebe 1 credito
                            }
                            //System.out.println(securePassword);
                        }

                        break;
                    case (3):
                        while ((line = lnr.readLine()) != null && lnr.getLineNumber() < wi.getLine() + wi.getSubset()) {
                            securePassword = BCrypt.hashpw(line, BCrypt.gensalt(12));

                            if (securePassword.compareTo(wi.getHash()) == 0) {
                                System.out.println("Encontrei a hash na linha " + lnr.getLineNumber() + "!");
                                //observer (descobri a password)
                                //user recebe 10 créditos
                                //para worker
                            } else {
                                //recebe 1 credito
                            }
                            //System.out.println(securePassword);
                        }
                        break;
                    case (4):
                        while ((line = lnr.readLine()) != null && lnr.getLineNumber() < wi.getLine() + wi.getSubset()) {
                            securePassword =  SCryptUtil.scrypt(line, 16, 16, 16);
                            if (securePassword.compareTo(wi.getHash()) == 0) {
                                System.out.println("Encontrei a hash na linha " + lnr.getLineNumber() + "!");
                                //observer (descobri a password)
                                //user recebe 10 créditos
                                //para worker
                            } else {
                                //recebe 1 credito
                            }
                            //System.out.println(securePassword);
                        }

                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
