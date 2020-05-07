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
            tg.setSubsets(100);

            db.AddTaskGroup(tg);
        }catch (Exception e){
            System.out.println("[visitConcreteElementStateTasks] - Create: " + e);
        }
        return true;
    }
}
