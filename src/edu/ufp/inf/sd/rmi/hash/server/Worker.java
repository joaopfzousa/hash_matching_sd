package edu.ufp.inf.sd.rmi.hash.server;

import edu.ufp.inf.sd.rmi.hash.client.ObserverImpl;
import edu.ufp.inf.sd.rmi.hash.helpers.bcrypt.BCrypt;
import edu.ufp.inf.sd.rmi.util.lambdaworks.crypto.SCryptUtil;

import java.io.*;
import java.rmi.RemoteException;

import static edu.ufp.inf.sd.rmi.hash.helpers.advanced.ReallyStrongSecuredPassword.generateStrongPasswordHash;
import static edu.ufp.inf.sd.rmi.hash.helpers.sha.SHAExample.get_SHA_512_SecurePassword;

public class Worker extends Thread implements Serializable {

    private String username;

    private int idTaskGroup;

    private Integer option;

    private HashSessionRI session;

    private VisitorHashOperationsI v1;

    private HashSubjectRI hashSubjectRI;

    private ObserverImpl observer;

    public Worker(String username, int idTaskGroup, Integer option, HashSessionRI session, VisitorHashOperationsI v1) throws RemoteException {
        this.username = username;
        this.idTaskGroup = idTaskGroup;
        this.option = option;
        this.session = session;
        this.v1 = v1;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void run(){
        System.out.println("thread is running...");
        TaskInput tk = new TaskInput(this.idTaskGroup, this.option, this.username);

        try {
            WorkerInput wi = (WorkerInput) this.session.acceptVisitor(this.v1, tk);
            this.hashSubjectRI = wi.getHashSubjectRI();
            this.StartWorking(wi, this.getName());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        //System.out.println(wi);
    }

    public boolean StartWorking(WorkerInput wi, String idThread) throws RemoteException {

        this.observer = new ObserverImpl(this.idTaskGroup, this.hashSubjectRI, idThread);

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
                        while ((line = lnr.readLine()) != null && lnr.getLineNumber() < wi.getLine() + wi.getSubset())
                        {
                            securePassword = get_SHA_512_SecurePassword(line);

                            if(observer.getLastObserverState().getMsg().compareTo("Delete") == 0)
                            {
                                System.out.println("[" + idThread + "] -> A taskGroup com o id " + observer.getLastObserverState().getIdTaskGroup() +", foi o " + observer.getLastObserverState().getWorker() + " que  mandou a Mensagem -> " + observer.getLastObserverState().getMsg() + ", parei na linha = " + lnr.getLineNumber());
                                break;
                            }

                            if(observer.getLastObserverState().getMsg().compareTo("Pause") == 0)
                            {
                                System.out.println("[" +idThread + "] -> A taskGroup com o id " + observer.getLastObserverState().getIdTaskGroup() +", foi o " + observer.getLastObserverState().getWorker() + " que  mandou a Mensagem -> " + observer.getLastObserverState().getMsg() + ", parei na linha = " + lnr.getLineNumber());

                                String msg = observer.getLastObserverState().getMsg();
                                int count = 0;
                                while(msg.compareTo("Pause") == 0)
                                {
                                    msg = observer.getLastObserverState().getMsg();
                                    count ++;
                                    Thread.sleep(2000);
                                    if(count == 100){
                                        System.out.println("A mensagem enviada pelo " + observer.getLastObserverState().getWorker() + " ainda é -> " + observer.getLastObserverState().getMsg());
                                    }
                                    //System.out.println("msg = " + msg);
                                }

                                if(observer.getLastObserverState().getMsg().compareTo("Delete") == 0)
                                {
                                    System.out.println("[" + idThread + "] -> A taskGroup com o id " + observer.getLastObserverState().getIdTaskGroup() +", foi o " + observer.getLastObserverState().getWorker() + " que  mandou a Mensagem -> " + observer.getLastObserverState().getMsg() + ", parei na linha = " + lnr.getLineNumber());
                                    break;
                                }

                                if(observer.getLastObserverState().getMsg().compareTo("UnPause") == 0)
                                {
                                    System.out.println("[" + idThread + "] -> A taskGroup com o id " + observer.getLastObserverState().getIdTaskGroup() + ", foi o " + observer.getLastObserverState().getWorker() + " que  mandou a Mensagem -> " + observer.getLastObserverState().getMsg() + ", vou continuar na linha = " + lnr.getLineNumber());
                                }
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
