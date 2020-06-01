package edu.ufp.inf.sd.rmi.hash.server;

import edu.ufp.inf.sd.rmi.util.rmisetup.SetupContextRMI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HashServer {

    private SetupContextRMI contextRMI;

    private HashSubjectRI myRI;

    public static void main(String[] args) {
        if (args != null && args.length < 3) {
            System.exit(-1);
        } else {
            assert args != null;
            HashServer srv = new HashServer(args);
            srv.rebindService();
        }
    }

    public HashServer(String[] args) {
        try {
            String registryIP   = args[0];
            String registryPort = args[1];
            String serviceName  = args[2];
            contextRMI = new SetupContextRMI(this.getClass(), registryIP, registryPort, new String[]{serviceName});
        } catch (RemoteException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    private void rebindService() {
        try {
            Registry registry = contextRMI.getRegistry();
            if (registry != null) {

                BufferedReader reader = null;
                int lines = 0;
                try {
                    reader = new BufferedReader(new FileReader("/Users/joaopfzousa/IdeaProjects/SD_Project/files/passwords.txt"));
                    while (reader.readLine() != null) lines++;
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                DBMockup db = new DBMockup(lines);
                HashLoginRI digLibLoginRI = new HashLoginImpl(db);
                String serviceUrl = contextRMI.getServicesUrl(0);
                registry.rebind(serviceUrl, digLibLoginRI);
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "registry not bound (check IPs). :(");
            }
        } catch (RemoteException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }
}