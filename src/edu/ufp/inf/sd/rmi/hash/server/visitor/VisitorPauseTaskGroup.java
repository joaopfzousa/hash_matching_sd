package edu.ufp.inf.sd.rmi.hash.server.visitor;

import edu.ufp.inf.sd.rmi.hash.server.DBMockup;
import edu.ufp.inf.sd.rmi.hash.server.HashSessionImpl;
import edu.ufp.inf.sd.rmi.hash.server.HashSessionRI;
import edu.ufp.inf.sd.rmi.hash.server.SingletonOperationsTaskGroups;

import java.io.Serializable;

public class VisitorPauseTaskGroup implements VisitorHashOperationsI, Serializable {

    private Integer idtask;

    public VisitorPauseTaskGroup(Integer idtask) {
        this.idtask = idtask;
    }

    @Override
    public Object visitConcreteElementStateTasks(HashSessionRI element, DBMockup db) {
        SingletonOperationsTaskGroups sTaskGroup = ((HashSessionImpl)element).getStateTaskGroup();
        return sTaskGroup.PauseTaskGroup(idtask, db);
    }
}
