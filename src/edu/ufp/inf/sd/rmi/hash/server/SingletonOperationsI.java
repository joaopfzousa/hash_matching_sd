package edu.ufp.inf.sd.rmi.hash.server;

import java.util.ArrayList;

public interface SingletonOperationsI {
    ArrayList<TaskGroup> ListTaskGroup();

    boolean CreateTaskGroup(TaskGroup taskgroup);

    WorkerInput JoinTaskGroup(String user, int id);

    boolean PauseTaskGroup(Integer idtask);

    boolean DeleteTaskGroup(Integer idtask);

    boolean RequestCredits(int idtask, String user, int credits);
}
