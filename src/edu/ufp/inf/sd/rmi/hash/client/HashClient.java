package edu.ufp.inf.sd.rmi.hash.client;

import edu.ufp.inf.sd.rmi.hash.server.*;
import edu.ufp.inf.sd.rmi.hash.server.visitor.*;
import edu.ufp.inf.sd.rmi.util.States;
import edu.ufp.inf.sd.rmi.util.rmisetup.SetupContextRMI;
import edu.ufp.inf.sd.rmi.util.threading.ThreadPool;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class HashClient extends Thread {

    HashSessionRI session = null;

    private SetupContextRMI contextRMI;

    private HashLoginRI hashLoginRI;

    private HashSubjectRI hashSubjectRI;

    private ObserverImpl observer;

    private ThreadPool tPool = new ThreadPool(5);


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
                System.out.println("[-1] -> Exit\n");

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

                        if (isregisted) {
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

                        session = login(user, password);
                        if (session != null) {
                            System.out.println("\n\t\tWelcome " + user);
                            VisitorHashOperationsI v = null;
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
                                switch (option) {
                                    case 1:
                                        v = new VisitorListTaskGroup();
                                        ArrayList<TaskGroup> tasks = (ArrayList<TaskGroup>) session.acceptVisitor(v);

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

                                        System.out.print("Insert the ammount of credits you wish to use: ");
                                        int plafond = tryParseInt(in.nextLine(), 0);

                                        TaskGroup taskGroup = new TaskGroup(hash, encryption, user, plafond);

                                        System.out.println(taskGroup);

                                        v = new VisitorCreateTaskGroup(taskGroup);

                                        boolean create = (boolean) session.acceptVisitor(v);

                                        System.out.println();
                                        if (create) {
                                            System.out.println("Created TaskGroup sucessfully!");
                                        } else {
                                            System.out.println("TakGroup not created! Please check inserted data or your credits");
                                        }
                                        break;
                                    case 3:
                                        v = new VisitorListTaskGroup();
                                        ArrayList<TaskGroup> join_tasks_pause = (ArrayList<TaskGroup>) session.acceptVisitor(v);

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

                                        while (!idexisttask) {
                                            System.out.println("This id not exists");
                                            System.out.print("Insert id: ");
                                            idtask = tryParseInt(in.nextLine(), 0);

                                            for (TaskGroup tg : join_tasks_pause) {

                                                if (tg.getId() == idtask) {
                                                    idexisttask = true;
                                                    break;
                                                }
                                            }
                                        }

                                        v = new VisitorPauseTaskGroup(idtask);
                                        boolean pause = (boolean) session.acceptVisitor(v);

                                        System.out.println();
                                        if (pause) {
                                            System.out.println("Pause TaskGroup sucessfully!");
                                        } else {
                                            System.out.println("UnPause TaskGroup sucessfully!!");
                                        }
                                        break;
                                    case 4:
                                        v = new VisitorListTaskGroup();
                                        ArrayList<TaskGroup> join_tasks_delete = (ArrayList<TaskGroup>) session.acceptVisitor(v);

                                        for (TaskGroup tg : join_tasks_delete) {
                                            System.out.println("Id da TaskGroup = " + tg.getId());
                                        }

                                        System.out.print("Insert id: ");
                                        Integer idtask_delete = tryParseInt(in.nextLine(), 0);

                                        boolean idexisttask_delete = false;
                                        for (TaskGroup tg : join_tasks_delete) {

                                            if (tg.getId() == idtask_delete) {
                                                idexisttask_delete = true;
                                                break;
                                            }
                                        }

                                        while (!idexisttask_delete) {
                                            System.out.println("This id not exists");
                                            System.out.print("Insert id: ");
                                            idtask_delete = tryParseInt(in.nextLine(), 0);

                                            for (TaskGroup tg : join_tasks_delete) {

                                                if (tg.getId() == idtask_delete) {
                                                    idexisttask_delete = true;
                                                    break;
                                                }
                                            }
                                        }
                                        v = new VisitorDeleteTaskGroup(idtask_delete);
                                        boolean delete = (boolean) session.acceptVisitor(v);

                                        System.out.println();
                                        if (delete) {
                                            System.out.println("Deleted TaskGroup sucessfully!");
                                        }
                                        break;
                                    case 5:
                                        v = new VisitorListTaskGroup();
                                        ArrayList<TaskGroup> join_tasks = (ArrayList<TaskGroup>) session.acceptVisitor(v);

                                        for (TaskGroup tg : join_tasks) {
                                            System.out.println("Id da TaskGroup = " + tg.getId());
                                        }
                                        System.out.print("Insert id: ");
                                        int id = tryParseInt(in.nextLine(), 0);

                                        boolean idexist = false;
                                        for (TaskGroup tg : join_tasks) {
                                            if (tg.getId() == id) {
                                                idexist = true;
                                                break;
                                            }
                                        }
                                        while (!idexist) {
                                            System.out.println("This id does not exists");
                                            System.out.print("Insert id: ");
                                            id = tryParseInt(in.nextLine(), 0);

                                            for (TaskGroup tg : join_tasks) {
                                                if (tg.getId() == id) {
                                                    idexist = true;
                                                    break;
                                                }
                                            }
                                        }

                                        if (join_tasks.get(id).getOwner().compareTo(user) == 0) {
                                            System.out.println("Unable to join your own Task group");
                                        } else {
                                            v = new VisitorJoinTaskGroup(user, id);
                                            try {
                                                WorkerInput wi = (WorkerInput) session.acceptVisitor(v);
                                                System.out.println(wi);

                                                if (wi != null) {
                                                    hashSubjectRI = wi.getHashSubjectRI();
                                                    this.observer = new ObserverImpl(id, this.hashSubjectRI, user);
                                                    this.observer = new ObserverImpl(id, this.hashSubjectRI, user);
                                                    this.StartWorking(wi);
                                                } else {
                                                    System.out.println("Não tenho linhas para ler!!!");
                                                }
                                            } catch (RemoteException e) {
                                                e.printStackTrace();
                                            }
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
                    case -1:
                        tPool = null;
                        break;
                    default:
                        System.out.println("Wrong option");
                }
            } while (option != -1);
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

    public void StartWorking(WorkerInput wi) throws RemoteException {
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
                while ((line = lnr.readLine()) != null && lnr.getLineNumber() < wi.getLine() + wi.getSubset()) {
                    this.observer.checkStates();

                    if(this.observer.getLastObserverState().getMsg().compareTo(States.Deleted) == 0)
                    {
                      return;
                    }else if(this.observer.getLastObserverState().getMsg().compareTo(States.Solved) == 0)
                    {
                        hashSubjectRI.detach(this.observer);
                    }else if(this.observer.getLastObserverState().getMsg().compareTo(States.NoCredit) == 0)
                    {
                        return;
                    }
                    Runnable r = new Task(wi.getIdTask(), wi.getUser(), line, wi.getEncryption(), wi.getHash(), this.observer, lnr.getLineNumber(), session);
                    tPool.execute(r);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}