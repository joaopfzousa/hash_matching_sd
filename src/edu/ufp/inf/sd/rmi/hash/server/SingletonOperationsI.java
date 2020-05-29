package edu.ufp.inf.sd.rmi.hash.server;

import java.util.ArrayList;

public interface SingletonOperationsI {
    public ArrayList<TaskGroup> ListTaskGroup(DBMockup db);
    public boolean CreateTaskGroup(TaskGroup taskgroup, DBMockup db);
    public WorkerInput JoinTaskGroup(String user, int id, DBMockup db);
    public boolean PauseTaskGroup(Integer idtask, DBMockup db);
    public boolean DeleteTaskGroup(Integer idtask, DBMockup db);
}
