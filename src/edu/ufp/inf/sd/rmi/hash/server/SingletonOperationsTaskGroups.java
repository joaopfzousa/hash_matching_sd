package edu.ufp.inf.sd.rmi.hash.server;

import edu.ufp.inf.sd.rmi.hash.client.ObserverImpl;
import edu.ufp.inf.sd.rmi.util.States;

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
    public ArrayList<TaskGroup> ListTaskGroup(DBMockup db) {
        return db.ListTaskGroups();
    }

    @Override
    public boolean CreateTaskGroup(TaskGroup taskgroup, DBMockup db) {

        try {

            ArrayList<TaskGroup> tasks = db.ListTaskGroups();

            int id;
            if (tasks.size() > 0)
                id = tasks.get(db.ListTaskGroups().size() - 1).getId() + 1;
            else
                id = 0;

            taskgroup.setId(id);
            taskgroup.setPause(false);
            taskgroup.setSolved(false);

            int lines = db.getLines();
            float subsets = (float) lines / 10;
            double new_subets = Math.ceil(subsets);

            taskgroup.setSubsets((int) new_subets);
            taskgroup.setLine(0);
            taskgroup.setHashSubjectRI(new HashSubjectImpl(id));
            taskgroup.setObserver(new ObserverImpl(id, taskgroup.getHashSubjectRI(), taskgroup.getOwner()));
            for (int i = 0; i < db.getUsers().size(); i++) {
                if (db.getUsers().get(i).getUname().compareTo(taskgroup.getOwner()) == 0) {
                    if (!(db.getUsers().get(i).getCredits() < taskgroup.getPlafond()))
                    {
                        db.getUsers().get(i).setCredits(db.getUsers().get(i).getCredits() - taskgroup.getPlafond());
                        break;
                    }else
                        return false;
                }
            }
            db.AddTaskGroup(taskgroup);
        } catch (Exception e) {
            System.out.println("[SingletonOperationsTaskGroups] - Create: " + e);
        }
        return true;
    }

    @Override
    public WorkerInput JoinTaskGroup(String user, int id, DBMockup db) {
        try {
            TaskGroup tg = db.ListTaskGroups().get(id);

            boolean join = db.JoinWorkerinTaskGroup(tg, user);

            if (join && tg.getLine() < db.getLines()) {
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
    public boolean PauseTaskGroup(Integer idtask, DBMockup db) {
        try {
            TaskGroup tg = db.ListTaskGroups().get(idtask);

            boolean isPause = db.PauseTaskGroup(tg);

            if (isPause) {
                State s = new State(States.Pause, tg.getOwner(), tg.getId());
                tg.getHashSubjectRI().setState(s);
                db.ListTaskGroups().get(idtask).setPause(true);
            } else {
                State s = new State(States.UnPause, tg.getOwner(), tg.getId());
                tg.getHashSubjectRI().setState(s);
                db.ListTaskGroups().get(idtask).setPause(false);
            }

            return isPause;
        } catch (Exception e) {
            System.out.println("[SingletonOperationsTaskGroups] - (Un)Pause: " + e);
        }
        return false;
    }

    @Override
    public boolean DeleteTaskGroup(Integer idtask, DBMockup db) {
        try {
            TaskGroup tg = db.ListTaskGroups().get(idtask);

            boolean delete = db.DeleteTaskGroup(tg);

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
    public boolean RequestCredits(int idtask, String user, int credits, DBMockup db) {
        try {
            TaskGroup tg = db.ListTaskGroups().get(idtask);

            if(credits == 10)
            {
                State s = new State(States.Solved, tg.getOwner(), tg.getId());
                tg.getHashSubjectRI().setState(s);
                db.ListTaskGroups().get(idtask).setSolved(true);
            }

            if (tg.getPlafond() == 0 || tg.getPlafond() < credits)
            {
                State s = new State(States.NoCredit, tg.getOwner(), tg.getId());
                tg.getHashSubjectRI().setState(s);
                db.ListTaskGroups().get(idtask).setPause(true);
                return false;
            } else {
                for (User u : db.getUsers())
                {
                    if (u.getUname().compareTo(user) == 0)
                    {
                        u.setCredits(u.getCredits() + credits);
                        int plafond = tg.getPlafond();
                        tg.setPlafond(plafond - credits);
                        db.ListTaskGroups().get(idtask).setPlafond(tg.getPlafond());
                        break;
                    }
                }
                return true;
            }
        } catch (Exception e) {
            System.out.println("[SingletonOperationsTaskGroups] - RequestCredits: " + e);
        }
        return false;
    }

}
