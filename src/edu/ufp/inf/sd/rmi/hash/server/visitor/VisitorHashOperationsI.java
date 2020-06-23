package edu.ufp.inf.sd.rmi.hash.server.visitor;

import edu.ufp.inf.sd.rmi.hash.server.DBMockup;
import edu.ufp.inf.sd.rmi.hash.server.HashSessionRI;

public interface VisitorHashOperationsI {
    public Object visitConcreteElementStateTasks(HashSessionRI element);
}

