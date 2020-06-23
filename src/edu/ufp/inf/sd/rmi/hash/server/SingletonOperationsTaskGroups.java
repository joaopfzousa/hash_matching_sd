package edu.ufp.inf.sd.rmi.hash.server;

import edu.ufp.inf.sd.rmi.hash.client.ObserverImpl;
import edu.ufp.inf.sd.rmi.util.States;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class SingletonOperationsTaskGroups implements SingletonOperationsI {

    private static SingletonOperationsTaskGroups singletonOperationsTaskGroups;

    public synchronized static SingletonOperationsTaskGroups createSingletonOperationsTaskGroups() {
        if (singletonOperationsTaskGroups == null) {
            singletonOperationsTaskGroups = new SingletonOperationsTaskGroups();
        }
        return singletonOperationsTaskGroups;
    }

    @Override
    public ArrayList<TaskGroup> ListTaskGroup(/*DBMockup db*/) {
        return DBMockup.ListTaskGroups();
    }

    @Override
    public boolean CreateTaskGroup(TaskGroup taskgroup) {

        try {

            int lines = getLines();

            ArrayList<TaskGroup> tasks = DBMockup.ListTaskGroups();

            int id;
            if (tasks.size() > 0)
                id = tasks.get(DBMockup.ListTaskGroups().size() - 1).getId() + 1;
            else
                id = 0;

            taskgroup.setId(id);
            taskgroup.setPause(false);
            taskgroup.setSolved(false);

            float subsets = (float) lines / 10;
            double new_subets = Math.ceil(subsets);

            taskgroup.setSubsets((int) new_subets);
            taskgroup.setLine(0);
            taskgroup.setHashSubjectRI(new HashSubjectImpl(id));
            taskgroup.setObserver(new ObserverImpl(id, taskgroup.getHashSubjectRI(), taskgroup.getOwner()));
            for (int i = 0; i < DBMockup.getUsers().size(); i++) {
                if (DBMockup.getUsers().get(i).getUname().compareTo(taskgroup.getOwner()) == 0) {
                    if (!(DBMockup.getUsers().get(i).getCredits() < taskgroup.getPlafond()))
                    {
                        DBMockup.getUsers().get(i).setCredits(DBMockup.getUsers().get(i).getCredits() - taskgroup.getPlafond());
                        break;
                    }else
                        return false;
                }
            }
            DBMockup.AddTaskGroup(taskgroup);
        } catch (Exception e) {
            System.out.println("[SingletonOperationsTaskGroups] - Create: " + e);
        }
        return true;
    }

    @Override
    public WorkerInput JoinTaskGroup(String user, int id) {
        try {
            TaskGroup tg = DBMockup.ListTaskGroups().get(id);

            boolean join = DBMockup.JoinWorkerinTaskGroup(tg, user);



            if (join && tg.getLine() < getLines()) {
                WorkerInput wi = new WorkerInput(tg.getId(), tg.getLine(), tg.getSubsets(), tg.getHash(), tg.getEncryption(), tg.getStrategy(), "/Users/joaopfzousa/IdeaProjects/SD_Project/files/passwords.txt", tg.getHashSubjectRI(), user);

                tg.setLine(tg.getLine() + tg.getSubsets() + 1);

                return wi;
            }
        } catch (Exception e) {
            System.out.println("[SingletonOperationsTaskGroups] - Join: " + e);
        }
        return null;
    }

    @Override
    public boolean PauseTaskGroup(Integer idtask/*, DBMockup db*/) {
        try {
            TaskGroup tg = DBMockup.ListTaskGroups().get(idtask);

            boolean isPause = DBMockup.PauseTaskGroup(tg);

            if (isPause) {
                State s = new State(States.Pause, tg.getOwner(), tg.getId());
                tg.getHashSubjectRI().setState(s);
                DBMockup.ListTaskGroups().get(idtask).setPause(true);
            } else {
                State s = new State(States.UnPause, tg.getOwner(), tg.getId());
                tg.getHashSubjectRI().setState(s);
                DBMockup.ListTaskGroups().get(idtask).setPause(false);
            }

            return isPause;
        } catch (Exception e) {
            System.out.println("[SingletonOperationsTaskGroups] - (Un)Pause: " + e);
        }
        return false;
    }

    @Override
    public boolean DeleteTaskGroup(Integer idtask/*, DBMockup db*/) {
        try {
            TaskGroup tg = DBMockup.ListTaskGroups().get(idtask);

            boolean delete = DBMockup.DeleteTaskGroup(tg);

            if (delete) {
                State s = new State(States.Deleted, tg.getOwner(), tg.getId());
                tg.getHashSubjectRI().setState(s);
            }

            return delete;
        } catch (Exception e) {
            System.out.println("[SingletonOperationsTaskGroups] - (Un)Pause: " + e);
        }
        return false;
    }

    @Override
    public boolean RequestCredits(int idtask, String user, int credits/*, DBMockup db*/) {
        try {
            TaskGroup tg = DBMockup.ListTaskGroups().get(idtask);

            if(tg != null)
            {
                if (tg.getPlafond() == 0 || tg.getPlafond() < credits)
                {
                    State s = new State(States.NoCredit, tg.getOwner(), tg.getId());
                    tg.getHashSubjectRI().setState(s);
                    DBMockup.ListTaskGroups().get(idtask).setPause(true);
                    return false;
                } else {

                    if(credits == 10)
                    {
                        State s = new State(States.Solved, tg.getOwner(), tg.getId());
                        tg.getHashSubjectRI().setState(s);
                        DBMockup.ListTaskGroups().get(idtask).setSolved(true);
                    }

                    for (User u : DBMockup.getUsers())
                    {
                        if (u.getUname().compareTo(user) == 0)
                        {
                            u.setCredits(u.getCredits() + credits);
                            int plafond = tg.getPlafond();
                            tg.setPlafond(plafond - credits);
                            DBMockup.ListTaskGroups().get(idtask).setPlafond(tg.getPlafond());
                            break;
                        }
                    }
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("[SingletonOperationsTaskGroups] - RequestCredits: " + e);
        }
        return false;
    }

    public int getLines()
    {
        BufferedReader reader = null;
        int lines = 0;
        try {
            reader = new BufferedReader(new FileReader("/Users/joaopfzousa/IdeaProjects/SD_Project/files/passwords.txt"));
            while (reader.readLine() != null) lines++;
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lines;
    }

}
