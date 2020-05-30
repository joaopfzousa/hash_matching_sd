package edu.ufp.inf.sd.rmi.hash.server.visitor;

import edu.ufp.inf.sd.rmi.hash.server.DBMockup;
import edu.ufp.inf.sd.rmi.hash.server.HashSessionImpl;
import edu.ufp.inf.sd.rmi.hash.server.HashSessionRI;
import edu.ufp.inf.sd.rmi.hash.server.SingletonOperationsTaskGroups;

import java.io.Serializable;

public class VisitorJoinTaskGroup implements VisitorHashOperationsI, Serializable {

    private String user;

    private int id;

    public VisitorJoinTaskGroup(String user, int id) {
        this.user = user;
        this.id = id;
    }

    @Override
    public Object visitConcreteElementStateTasks(HashSessionRI element, DBMockup db) {
        SingletonOperationsTaskGroups sTaskGroup = ((HashSessionImpl)element).getStateTaskGroup();
        return sTaskGroup.JoinTaskGroup(user, id, db);
    }
}
