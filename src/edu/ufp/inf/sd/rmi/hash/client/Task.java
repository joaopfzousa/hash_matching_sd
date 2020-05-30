package edu.ufp.inf.sd.rmi.hash.client;

import edu.ufp.inf.sd.rmi.hash.helpers.bcrypt.BCrypt;
import edu.ufp.inf.sd.rmi.util.lambdaworks.crypto.SCryptUtil;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static edu.ufp.inf.sd.rmi.hash.helpers.advanced.ReallyStrongSecuredPassword.generateStrongPasswordHash;
import static edu.ufp.inf.sd.rmi.hash.helpers.sha.SHAExample.get_SHA_512_SecurePassword;

class Task implements Runnable
{
    private String palavra;

    private Integer encrypt;

    private String hash;

    private ObserverImpl observer;

    private int line;

    public Task(String palavra, Integer encrypt, String hash, ObserverImpl observer, int line) {
        this.palavra = palavra;
        this.encrypt = encrypt;
        this.hash = hash;
        this.observer = observer;
        this.line = line;
    }

    public void run()
    {
        if(this.observer.getLastObserverState().getMsg().compareTo("Delete") == 0)
        {
            System.out.println("[ThreadPool] -> A taskGroup com o id " + this.observer.getLastObserverState().getIdTaskGroup() +", foi o " + this.observer.getLastObserverState().getWorker() + " que  mandou a Mensagem -> " + this.observer.getLastObserverState().getMsg() + ", parei na linha = " + this.line);
            return;
        }

        if(this.observer.getLastObserverState().getMsg().compareTo("Pause") == 0)
        {
            System.out.println("[ThreadPool] -> A taskGroup com o id " + this.observer.getLastObserverState().getIdTaskGroup() +", foi o " + this.observer.getLastObserverState().getWorker() + " que  mandou a Mensagem -> " + this.observer.getLastObserverState().getMsg() + ", parei na linha = " + this.line);

            String msg = this.observer.getLastObserverState().getMsg();
            int count = 0;
            while(msg.compareTo("Pause") == 0)
            {
                msg = this.observer.getLastObserverState().getMsg();
                count ++;
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(count == 100)
                    System.out.println("[ThreadPool] -> A mensagem enviada pelo " + this.observer.getLastObserverState().getWorker() + " ainda é -> " + this.observer.getLastObserverState().getMsg());
            }

            if(this.observer.getLastObserverState().getMsg().compareTo("Delete") == 0)
            {
                System.out.println("[ThreadPool] -> A taskGroup com o id " + this.observer.getLastObserverState().getIdTaskGroup() +", foi o " + this.observer.getLastObserverState().getWorker() + " que  mandou a Mensagem -> " + this.observer.getLastObserverState().getMsg() + ", parei na linha = " + this.line);
                return;
            }

            if(this.observer.getLastObserverState().getMsg().compareTo("UnPause") == 0)
            {
                System.out.println("[ThreadPool] -> A taskGroup com o id " + this.observer.getLastObserverState().getIdTaskGroup() + ", foi o " + this.observer.getLastObserverState().getWorker() + " que  mandou a Mensagem -> " + this.observer.getLastObserverState().getMsg() + ", vou continuar na linha = " + this.line);
            }
        }

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

            if (securePassword.compareTo(this.hash) == 0)
            {
                System.out.println("palavra = " + this.palavra);
                //System.out.println("Encontrei a hash na linha " + lnr.getLineNumber() + "!");
                //observer (descobri a password)
                //user recebe 10 créditos
                //para worker
            } else {
                System.out.println("palavra = " + this.palavra);
                //recebe 1 credito
            }

            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //System.out.println(securePassword);
    }
}