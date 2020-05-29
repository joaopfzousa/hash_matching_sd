package edu.ufp.inf.sd.rmi.hash.server;

import edu.ufp.inf.sd.rmi.hash.client.ObserverImpl;

import java.util.ArrayList;

public class SingletonOperationsTaskGroups implements SingletonOperationsI {

    private static SingletonOperationsTaskGroups singletonOperationsTaskGroups;

    public synchronized static SingletonOperationsTaskGroups createSingletonOperationsTaskGroups(){
        if (singletonOperationsTaskGroups==null){
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

        try{
            TaskGroup tg = taskgroup;
            ArrayList<TaskGroup> tasks = db.ListTaskGroups();

            int id;
            if(tasks.size() > 0)
                id = tasks.get(db.ListTaskGroups().size() - 1).getId() + 1;
            else
                id = 0;

            tg.setId(id);
            tg.setPause(false);
            tg.setSolved(false);

            int lines = db.getLines();
            float subsets =  (float) lines/10;
            double new_subets = Math.ceil(subsets);

            tg.setSubsets((int) new_subets);
            tg.setLine(0);
            tg.setHashSubjectRI(new HashSubjectImpl(id));
            tg.setObserver(new ObserverImpl(id, tg.getHashSubjectRI(), taskgroup.getOwner()));

            for(User u : db.getUsers())
            {
                if (u.getUname().compareTo(taskgroup.getOwner())==0)
                    tg.setPlafond(u.getCredits());
            }

            db.AddTaskGroup(tg);
        }catch (Exception e){
            System.out.println("[SingletonOperationsTaskGroups] - Create: " + e);
        }
        return true;
    }

    @Override
    public WorkerInput JoinTaskGroup(String user, int id, DBMockup db) {
        try{
            TaskGroup tg = db.ListTaskGroups().get(id);

            boolean join = db.JoinWorkerinTaskGroup(tg, user);

            if(join && tg.getLine() < db.getLines())
            {
                WorkerInput wi = new WorkerInput(tg.getLine(), tg.getSubsets(), tg.getHash(), tg.getEncryption(), tg.getStrategy(), "/Users/joaopfzousa/IdeaProjects/SD_Project/files/passwords.txt", tg.getHashSubjectRI());

                tg.setLine(tg.getLine() + tg.getSubsets() + 1);

                return wi;
            }
        }catch (Exception e){
            System.out.println("[SingletonOperationsTaskGroups] - Join: " + e);
        }
        return null;
    }

    @Override
    public boolean PauseTaskGroup(Integer idtask, DBMockup db) {
        try{
            TaskGroup tg = db.ListTaskGroups().get(idtask);

            boolean isPause = db.PauseTaskGroup(tg);

            if(isPause)
            {
                State s = new State("Pause", tg.getOwner(), tg.getId());
                tg.getHashSubjectRI().setState(s);
            }else{
                State s = new State("UnPause", tg.getOwner(), tg.getId());
                tg.getHashSubjectRI().setState(s);
            }

            return isPause;
        }catch (Exception e){
            System.out.println("[SingletonOperationsTaskGroups] - (Un)Pause: " + e);
        }
        return false;
    }

    @Override
    public boolean DeleteTaskGroup(Integer idtask, DBMockup db) {
        try{
            TaskGroup tg = db.ListTaskGroups().get(idtask);

            boolean delete = db.DeleteTaskGroup(tg);

            if(delete)
            {
                State s = new State("Delete", tg.getOwner(), tg.getId());
                tg.getHashSubjectRI().setState(s);
            }

            return delete;
        }catch (Exception e){
            System.out.println("[SingletonOperationsTaskGroups] - (Un)Pause: " + e);
        }
        return false;
    }
}
