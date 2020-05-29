package edu.ufp.inf.sd.rmi.hash.server;

import java.io.Serializable;

public class VisitorDeleteTaskGroup implements VisitorHashOperationsI, Serializable {

    private Integer idtask;

    public VisitorDeleteTaskGroup(Integer idtask) {
        this.idtask = idtask;
    }
    @Override
    public Object visitConcreteElementStateTasks(HashSessionRI element, DBMockup db) {
        SingletonOperationsTaskGroups sTaskGroup = ((HashSessionImpl)element).getStateTaskGroup();
        return sTaskGroup.DeleteTaskGroup(idtask, db);
    }
}
