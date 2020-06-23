package edu.ufp.inf.sd.rmi.hash.server.visitor;

import edu.ufp.inf.sd.rmi.hash.server.*;

import java.io.Serializable;

public class VisitorCreateTaskGroup implements VisitorHashOperationsI, Serializable {

    private TaskGroup taskGroup;

    public VisitorCreateTaskGroup(TaskGroup taskGroup) {
        this.taskGroup = taskGroup;
    }

    @Override
    public Object visitConcreteElementStateTasks(HashSessionRI element) {
        SingletonOperationsTaskGroups sTaskGroup = ((HashSessionImpl)element).getStateTaskGroup();
        return sTaskGroup.CreateTaskGroup(this.taskGroup);
    }
}
