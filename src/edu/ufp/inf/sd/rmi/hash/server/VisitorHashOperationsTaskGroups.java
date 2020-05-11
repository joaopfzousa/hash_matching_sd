package edu.ufp.inf.sd.rmi.hash.server;

import java.io.Serializable;

public class VisitorHashOperationsTaskGroups implements VisitorHashOperationsI, Serializable {

    @Override
    public Object visitConcreteElementStateTasks(HashSessionRI element, TaskInput tk, DBMockup db) {

        SingletonOperationsTaskGroups sTaskGroup = ((HashSessionImpl)element).getStateTaskGroup();
        switch (tk.getOption())
        {
            case 1:
                return sTaskGroup.ListTaskGroup(db);
            case 2:
                return sTaskGroup.CreateTaskGroup(tk, db);
            case 3:
                // pause
            case 4:
                // delete
            case 5:
                return sTaskGroup.JoinTaskGroup(tk, db);
        }
        return null;
    }
}
