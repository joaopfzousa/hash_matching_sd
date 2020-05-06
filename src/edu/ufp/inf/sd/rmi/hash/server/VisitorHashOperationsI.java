package edu.ufp.inf.sd.rmi.hash.server;

public interface VisitorHashOperationsI {
    public Object visitConcreteElementStateTasks(HashSessionRI element,TaskInput tk, DBMockup db);
}

