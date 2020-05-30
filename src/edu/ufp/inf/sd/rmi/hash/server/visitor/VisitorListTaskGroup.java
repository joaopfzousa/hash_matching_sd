package edu.ufp.inf.sd.rmi.hash.server.visitor;

import edu.ufp.inf.sd.rmi.hash.server.DBMockup;
import edu.ufp.inf.sd.rmi.hash.server.HashSessionImpl;
import edu.ufp.inf.sd.rmi.hash.server.HashSessionRI;
import edu.ufp.inf.sd.rmi.hash.server.SingletonOperationsTaskGroups;

import java.io.Serializable;

public class VisitorListTaskGroup implements VisitorHashOperationsI, Serializable {

    @Override
    public Object visitConcreteElementStateTasks(HashSessionRI element, DBMockup db) {
        SingletonOperationsTaskGroups sTaskGroup = ((HashSessionImpl)element).getStateTaskGroup();
        return sTaskGroup.ListTaskGroup(db);
    }

}
