package edu.ufp.inf.sd.rmi.hash.client;

import edu.ufp.inf.sd.rmi.hash.server.HashLoginRI;
import edu.ufp.inf.sd.rmi.hash.server.HashSessionImpl;
import edu.ufp.inf.sd.rmi.hash.server.HashSessionRI;
import edu.ufp.inf.sd.rmi.util.rmisetup.SetupContextRMI;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HashClient {


    public Button loginButton;
    public TextField passText;
    public TextField userText;

    private SetupContextRMI contextRMI;
    private HashLoginRI hashLoginRI;


    public static void main(String[] args) {
        if (args != null && args.length < 2) {
            System.exit(-1);
        } else {
            HashClient clt = new HashClient(args);
            clt.lookupService();
            clt.playService();
        }
    }

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
            HashSessionRI session = login(userText.getText() ,passText.getText());

        } catch (RemoteException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    private HashSessionRI login (String username, String password) throws RemoteException {
        return hashLoginRI.login(username, password);
    }
}
