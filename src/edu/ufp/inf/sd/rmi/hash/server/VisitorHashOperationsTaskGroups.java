package edu.ufp.inf.sd.rmi.hash.server;

import java.io.Serializable;

public class VisitorHashOperationsTaskGroups implements VisitorHashOperationsI, Serializable {

    @Override
    public Object visitConcreteElementStateTasks(HashSessionRI element, TaskInput tk, DBMockup db) {
        switch (tk.getOption())
        {
            case 1:
               var x =  db.ListTaskGroups();
                for (TaskGroup tg : x)
                {
                    System.out.println(tg);
                }
                return db.ListTaskGroups();
        }
        return null;
    }
}
