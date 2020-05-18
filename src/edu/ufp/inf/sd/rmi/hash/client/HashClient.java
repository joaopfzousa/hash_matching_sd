package edu.ufp.inf.sd.rmi.hash.client;

import edu.ufp.inf.sd.rmi.hash.helpers.bcrypt.BCrypt;
import edu.ufp.inf.sd.rmi.hash.server.*;
import edu.ufp.inf.sd.rmi.util.lambdaworks.crypto.SCryptUtil;
import edu.ufp.inf.sd.rmi.util.rmisetup.SetupContextRMI;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static edu.ufp.inf.sd.rmi.hash.helpers.advanced.ReallyStrongSecuredPassword.generateStrongPasswordHash;
import static edu.ufp.inf.sd.rmi.hash.helpers.sha.SHAExample.get_SHA_512_SecurePassword;

public class HashClient extends Thread {

    private SetupContextRMI contextRMI;
    private HashLoginRI hashLoginRI;
    private ObserverImpl observer;

    public HashClient(String[] args) {
        try {
            String registryIP = args[0];
            String registryPort = args[1];
            String serviceName = args[2];
            contextRMI = new SetupContextRMI(this.getClass(), registryIP, registryPort, new String[]{serviceName});
        } catch (RemoteException e) {
            Logger.getLogger(HashClient.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static void main(String[] args) {
        if (args != null && args.length < 2) {
            System.exit(-1);
        } else {
            HashClient clt = new HashClient(args);
            clt.lookupService();
            clt.playService();
        }
    }

    private Remote lookupService() {
        try {
            Registry registry = contextRMI.getRegistry();
            if (registry != null) {
                String serviceUrl = contextRMI.getServicesUrl(0);
                hashLoginRI = (HashLoginRI) registry.lookup(serviceUrl);
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "registry not bound (check IPs). :(");
            }
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return hashLoginRI;
    }

    private void playService() {
        try {
            Scanner in = new Scanner(System.in);
            int option = 0;

            System.out.println("\n\n-------------------------------------------");
            System.out.println("-------- Hash Matching Colaborativo -------");
            System.out.println("-------------------------------------------");

            do {
                System.out.println("\n-----> Please Register/Login <-----\n");
                System.out.println("[1] -> Register");
                System.out.println("[2] -> Login");
                System.out.println("[0] -> Exit\n");

                System.out.print("Option: ");
                String opt = in.nextLine();
                option = tryParseInt(opt, 0);
                switch (option) {
                    case 1:
                        System.out.println("\n-----> Please Register new User <-----\n");
                        System.out.print("Insert Username: ");
                        String username = in.nextLine();
                        System.out.print("Insert Password: ");
                        String pass = in.nextLine();
                        System.out.print("Insert Credits: ");
                        Integer credits = tryParseInt(in.nextLine(), 100);
                        boolean isregisted = hashLoginRI.register(username, pass, credits);

                        if (isregisted == true) {
                            System.out.println("User successfully registered!\n");
                        } else {
                            System.out.println("User not registed!\n");
                        }
                        break;
                    case 2:
                        System.out.println("\n-------> Please Login <---------\n");
                        System.out.print("\nPlease insert your username: ");
                        String user = in.nextLine();
                        System.out.print("Please insert your password: ");
                        String password = in.nextLine();
                        HashSessionRI session = login(user, password);
                        if (session != null) {
                            System.out.println("\n\t\tWelcome " + user);
                            VisitorHashOperationsI v1 = new VisitorHashOperationsTaskGroups();
                            do {
                                System.out.println("-------------------------------------------");
                                System.out.println("------------------- Menu ------------------");
                                System.out.println("-------------------------------------------");
                                System.out.println("[1] -> List task groups");
                                System.out.println("[2] -> Create task group");
                                System.out.println("[3] -> (Un)Pause task group");
                                System.out.println("[4] -> Delete task group");
                                System.out.println("[5] -> Join task group");
                                System.out.println("[-1] -> Exit\n");

                                System.out.print("Option: ");
                                String op = in.nextLine();
                                option = tryParseInt(op, 0);
                                TaskInput tk = new TaskInput(option);
                                switch (option) {
                                    case 1:
                                        ArrayList<TaskGroup> tasks = (ArrayList<TaskGroup>) session.acceptVisitor(v1, tk);

                                        for (TaskGroup tg : tasks) {
                                            System.out.println(tg);
                                        }
                                        break;
                                    case 2:
                                        System.out.println("-----> Please insert TaskGroup <-----");
                                        System.out.print("Insert Hash: ");
                                        String hash = in.nextLine();

                                        System.out.println("[1] -> SHA-512");
                                        System.out.println("[2] -> PBKDF2");
                                        System.out.println("[3] -> BCrypt");
                                        System.out.println("[4] -> SCrypt");

                                        System.out.print("Insert Encryption: ");
                                        Integer encryption = tryParseInt(in.nextLine(), 0);

                                        while (encryption < 1 || encryption > 4) {
                                            System.out.println("This id not exists");
                                            System.out.print("Insert Encryption: ");
                                            encryption = tryParseInt(in.nextLine(), 0);
                                        }

                                        System.out.print("Insert Plafond: ");
                                        Integer plafond = tryParseInt(in.nextLine(), 10);

                                        TaskGroup taskGroup = new TaskGroup(hash, encryption, plafond, user);

                                        tk = new TaskInput(option, taskGroup);

                                        boolean create = (boolean) session.acceptVisitor(v1, tk);

                                        System.out.println();
                                        if (create == true) {
                                            System.out.println("Created TaskGroup sucessfully!");
                                        } else {
                                            System.out.println("TakGroup not created!");
                                        }
                                        break;
                                    case 3:
                                        tk.setOption(1);
                                        ArrayList<TaskGroup> join_tasks_pause = (ArrayList<TaskGroup>) session.acceptVisitor(v1, tk);

                                        for (TaskGroup tg : join_tasks_pause) {
                                            System.out.println("Id da TaskGroup = " + tg.getId());
                                        }

                                        System.out.print("Insert id: ");
                                        Integer idtask = tryParseInt(in.nextLine(), 0);

                                        boolean idexisttask = false;
                                        for (TaskGroup tg : join_tasks_pause) {
                                            if (tg.getId() == idtask) {
                                                idexisttask = true;
                                                break;
                                            }
                                        }

                                        while (idexisttask == false) {
                                            System.out.println("This id not exists");
                                            System.out.print("Insert id: ");
                                            idtask = tryParseInt(in.nextLine(), 0);
                                        }

                                        tk = new TaskInput(idtask, option);

                                        boolean pause = (boolean) session.acceptVisitor(v1, tk);

                                        break;
                                    case 4:
                                        break;
                                    case 5:
                                        System.out.print("Quantos workers quer inserir: ");
                                        Integer nWorkers = tryParseInt(in.nextLine(), 1);

                                        tk.setOption(1);
                                        ArrayList<TaskGroup> join_tasks = (ArrayList<TaskGroup>) session.acceptVisitor(v1, tk);

                                        for (TaskGroup tg : join_tasks) {
                                            System.out.println("Id da TaskGroup = " + tg.getId());
                                        }

                                        System.out.print("Insert id: ");
                                        Integer id = tryParseInt(in.nextLine(), 0);

                                        boolean idexist = false;
                                        for (TaskGroup tg : join_tasks) {
                                            if (tg.getId() == id) {
                                                idexist = true;
                                                break;
                                            }
                                        }

                                        while (idexist == false) {
                                            System.out.println("This id not exists");
                                            System.out.print("Insert id: ");
                                            id = tryParseInt(in.nextLine(), 0);
                                        }

                                        for(int i = 0; i < nWorkers; i++)
                                        {
                                            Worker w = new Worker(user, id, option, session, v1);
                                            w.start();
                                        }

                                        break;
                                    case -1:
                                        hashLoginRI.logout(user);
                                        break;
                                    default:
                                        System.out.println("Wrong option");
                                }
                            } while (option != -1);
                        } else {
                            System.out.println("[Session] - No Session, Error credentials\n");
                        }
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Wrong option");
                }
            } while (option != 0);
        } catch (RemoteException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    private HashSessionRI login(String username, String password) throws RemoteException {
        return hashLoginRI.login(username, password);
    }

    public int tryParseInt(String value, int defaultVal) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    public static boolean StartWorking(WorkerInput wi, long id) {
        System.out.println("thread nº = " + id);
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
                            securePassword = get_SHA_512_SecurePassword(line);

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