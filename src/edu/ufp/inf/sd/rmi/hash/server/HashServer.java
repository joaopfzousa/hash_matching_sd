package edu.ufp.inf.sd.rmi.hash.server;

import edu.ufp.inf.sd.rmi.util.rmisetup.SetupContextRMI;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HashServer {

    private SetupContextRMI contextRMI;

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
                HashLoginRI digLibLoginRI = new HashLoginImpl();
                DBMockup.register("hugo", "ufp",20000);
                DBMockup.register("joao", "ufp",10000);
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