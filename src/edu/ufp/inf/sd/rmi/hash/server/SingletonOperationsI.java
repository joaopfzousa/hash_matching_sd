package edu.ufp.inf.sd.rmi.hash.server;

import java.util.ArrayList;

public interface SingletonOperationsI {
    public ArrayList<TaskGroup> ListTaskGroup(DBMockup db);
    public boolean CreateTaskGroup(TaskInput tk, DBMockup db);
    public WorkerInput JoinTaskGroup(TaskInput tk, DBMockup db);
    public boolean PauseTaskGroup(TaskInput tk, DBMockup db);
}
