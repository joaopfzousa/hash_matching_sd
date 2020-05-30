package edu.ufp.inf.sd.rmi.hash.server;

import java.util.ArrayList;

public interface SingletonOperationsI {
    ArrayList<TaskGroup> ListTaskGroup(DBMockup db);

    boolean CreateTaskGroup(TaskGroup taskgroup, DBMockup db);

    WorkerInput JoinTaskGroup(String user, int id, DBMockup db);

    boolean PauseTaskGroup(Integer idtask, DBMockup db);

    boolean DeleteTaskGroup(Integer idtask, DBMockup db);

    boolean RequestCredits(int idtask, String user, int credits, DBMockup db);
}
