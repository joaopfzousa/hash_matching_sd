package edu.ufp.inf.sd.rmi.hash.server;

import java.io.Serializable;

public class VisitorListTaskGroup implements VisitorHashOperationsI, Serializable {

    @Override
    public Object visitConcreteElementStateTasks(HashSessionRI element, DBMockup db) {
        SingletonOperationsTaskGroups sTaskGroup = ((HashSessionImpl)element).getStateTaskGroup();
        return sTaskGroup.ListTaskGroup(db);
    }

}
