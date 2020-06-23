package edu.ufp.inf.sd.rmi.hash.server;

import java.util.ArrayList;

/**
 * This class simulates a DBMockup for managing users.
 */
public class DBMockup {

    private static ArrayList<User> users = new ArrayList();

    private static ArrayList<TaskGroup> taskGroups = new ArrayList<>();

    //private static int lines;


    /**
     * This constructor creates and inits the database with some users.
     */
    /*
    public DBMockup(int lines) {

        users = new ArrayList<>();

        //Add one user
        users.add(new User("guest", "ufp",100));
        users.add(new User("hugo", "ufp",200));
        users.add(new User("joao", "ufp",10000));
        users.add(new User("manel", "ufp",300));

        taskGroups = new ArrayList<>();
        this.lines = lines;
    }*/

    /**
     * Registers a new user.
     *
     * @param u username
     * @param p passwd
     * @param credits n creditos
     */
    public static boolean register(String u, String p, int credits) {
        if (!exists(u, p)) {
            users.add(new User(u, p, credits));
            return true;
        }
        return false;
    }

    /**
     * Checks the credentials of an user.
     *
     * @param u username
     * @param p passwd
     * @return
     */
    public static boolean exists(String u, String p) {
        for (User usr : users) {
            if (usr.getUname().compareTo(u) == 0 && usr.getPword().compareTo(p) == 0) {
                return true;
            }
        }
        return false;
        //return ((u.equalsIgnoreCase("guest") && p.equalsIgnoreCase("ufp")) ? true : false);
    }

    public static ArrayList<TaskGroup> ListTaskGroups()
    {
        return  taskGroups;
    }

    public static boolean AddTaskGroup(TaskGroup tg)
    {
        taskGroups.add(tg);
        return true;
    }

    public static boolean DeleteTaskGroup(TaskGroup tg)
    {
        if (taskGroups.contains(tg))
        {
            taskGroups.remove(tg);
            return true;
        }
        return false;
    }

    public static boolean PauseTaskGroup(TaskGroup tg)
    {
        for(TaskGroup tk : taskGroups)
        {
            if(tk.getId() == tg.getId())
            {
                tk.setPause(!tk.isPause());
                return tk.isPause();
            }
        }
        return false;
    }

    public static boolean JoinWorkerinTaskGroup(TaskGroup tg, String u)
    {
        tg.getUsers().add(u);
        return true;
    }

    public static ArrayList<User> getUsers() {
        return users;
    }

    /*
    public static int getLines() {
        return lines;
    }*/
}