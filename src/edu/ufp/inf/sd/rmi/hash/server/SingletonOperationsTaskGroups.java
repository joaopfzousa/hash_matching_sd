package edu.ufp.inf.sd.rmi.hash.server;

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
    public boolean CreateTaskGroup(TaskInput tk, DBMockup db) {
        try{
            TaskGroup tg = tk.getTasksGroup();
            ArrayList<TaskGroup> tasks = db.ListTaskGroups();

            int id;
            if(tasks.size() > 0)
                id = tasks.get(db.ListTaskGroups().size() - 1).getId() + 1;
            else
                id = 0;

            tg.setId(id);
            tg.setPause(false);
            tg.setSolved(false);
            tg.setSubsets(1000);
            tg.setLine(1);

            db.AddTaskGroup(tg);
        }catch (Exception e){
            System.out.println("[SingletonOperationsTaskGroups] - Create: " + e);
        }
        return true;
    }

    @Override
    public WorkerInput JoinTaskGroup(TaskInput tk, DBMockup db) {
        try{
            TaskGroup tg = db.ListTaskGroups().get(tk.getId());
            System.out.println(tg);

            boolean join = db.JoinWorkerinTaskGroup(tg, tk.getUsername());

            if(join)
            {
                WorkerInput wi = new WorkerInput(tg.getLine(), tg.getSubsets(), tg.getHash(), tg.getEncryption(), tg.getStrategy(), "./files/passwords.txt");

                tg.setLine(tg.getLine() + tg.getSubsets());

                return wi;
            }
        }catch (Exception e){
            System.out.println("[SingletonOperationsTaskGroups] - Join: " + e);
        }
        return null;
    }
}
