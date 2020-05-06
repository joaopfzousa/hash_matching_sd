package edu.ufp.inf.sd.rmi.hash.server;

import java.util.ArrayList;

/**
 * This class simulates a DBMockup for managing users.
 *
 *
 */
public class DBMockup {

    private final ArrayList<User> users;// = new ArrayList();

    private ArrayList<TaskGroup> taskGroups;// = new ArrayList<>();

    /**
     * This constructor creates and inits the database with some users.
     */
    public DBMockup() {

        users = new ArrayList();

        //Add one user
        users.add(new User("guest", "ufp",100));
        users.add(new User("hugo", "ufp",200));
        users.add(new User("joao", "ufp",10000));
        users.add(new User("manel", "ufp",300));

        taskGroups = new ArrayList<>();

        taskGroups.add(new TaskGroup(1,users,"aklshdakshdjkashdkjashd","asdasdasd",1,100000,"batatas",1000,false,false));
    }

    /**
     * Registers a new user.
     *
     * @param u username
     * @param p passwd
     */
    public void register(String u, String p, int credits) {
        if (!exists(u, p)) {
            users.add(new User(u, p, credits));
        }
    }

    /**
     * Checks the credentials of an user.
     *
     * @param u username
     * @param p passwd
     * @return
     */
    public boolean exists(String u, String p) {
        for (User usr : this.users) {
            if (usr.getUname().compareTo(u) == 0 && usr.getPword().compareTo(p) == 0) {
                return true;
            }
        }
        return false;
        //return ((u.equalsIgnoreCase("guest") && p.equalsIgnoreCase("ufp")) ? true : false);
    }

    public ArrayList<TaskGroup> ListTaskGroups()
    {
        return  this.taskGroups;
    }

    public boolean AddTaskGroup(TaskGroup tg)
    {
        this.taskGroups.add(tg);
                return true;
    }

    public boolean DeleteTaskGroup(TaskGroup tg)
    {
        if (this.taskGroups.contains(tg))
        {
            this.taskGroups.remove(tg);
            return true;
        }
        return false;
    }

    public boolean UpdateTaskGroup(TaskGroup tg)
    {
        for(TaskGroup tk : taskGroups)
        {
            if(tk.getId() == tg.getId())
            {
                //tudo
                return true;
            }
        }
        return false;
    }
}