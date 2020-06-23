package edu.ufp.inf.sd.rmi.hash.server.visitor;

import edu.ufp.inf.sd.rmi.hash.server.DBMockup;
import edu.ufp.inf.sd.rmi.hash.server.HashSessionImpl;
import edu.ufp.inf.sd.rmi.hash.server.HashSessionRI;
import edu.ufp.inf.sd.rmi.hash.server.SingletonOperationsTaskGroups;

import java.io.Serializable;

public class VisitorRequestCredits implements VisitorHashOperationsI, Serializable {
    private int idtask;

    private String user;

    private int credits;

    public VisitorRequestCredits(int idtask, String user, int credits) {
        this.idtask = idtask;
        this.user = user;
        this.credits = credits;
    }

    @Override
    public Object visitConcreteElementStateTasks(HashSessionRI element) {
        SingletonOperationsTaskGroups sTaskGroup = ((HashSessionImpl)element).getStateTaskGroup();
        return sTaskGroup.RequestCredits(idtask,user,credits);
    }
}
