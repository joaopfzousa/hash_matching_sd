package edu.ufp.inf.sd.rmi.hash.client;

import edu.ufp.inf.sd.rmi.hash.server.*;
import edu.ufp.inf.sd.rmi.util.rmisetup.SetupContextRMI;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HashClient {

    private SetupContextRMI contextRMI;
    private HashLoginRI hashLoginRI;


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
                    System.out.println("[3] -> Pause task group");
                    System.out.println("[4] -> Delete task group");
                    System.out.println("[5] -> Join task group");
                    System.out.println("[-1] -> Exit");

                    String op = in.nextLine();
                    option = tryParseInt(op,0);
                    switch (option)
                    {
                        case 1:
                            TaskInput tk = new TaskInput(option);
                            var x = session.acceptVisitor(v1,tk);

                            break;
                        case 2:
                            System.out.println("Please insert ");
                            break;
                        case 3:
                            break;
                        case 4:
                            break;
                        case 5:
                            break;
                        case -1:
                            hashLoginRI.logout(user);
                            break;
                        default:
                            System.out.println("Wrong option");
                    }
                }while (option != -1);
            } else {
                System.out.println("[Session] - No Session, Error credentials\n");
            }
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
}