/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Администратор
 */
public class RMCore
{

    private static RMCore instance = null;

    private RMCore() //private constructor for singleton pattern
    {
    }
    //call private constructor if instance don't exists and return it

    public static RMCore getInstance()
    {
        if (instance == null)
        {
            instance = new RMCore();
            try
            {
                DB.getInstance().openConnection();
            }
            catch (SQLException ex)
            {
                return null;
            }
        }
        return instance;
    }

    //check for existing account with that log and pass
    public String checkAccount(String login, String paswd)
    {
        String q = null;
        q = "select * from Account where login='" + login + "' and password='" + paswd + "'";
        try
        {
            DB.getInstance().query(q);
            if (DB.getInstance().getRowcount() == 0)
            {
                return null;//if don't found any then returns null
            }
        } catch (SQLException e)
        {
            return null;
        }
        return DB.getInstance().getResultList().get(0).get("NAME").toString(); //if all OK returns name of user
    }

//returns priority of user of last calling checkAccount function
    public int getPriority()
    {
        try
        {
            return Integer.valueOf(DB.getInstance().getResultList().get(0).get("PRIORITY").toString());
        } catch (Exception e)
        {
            return -1;
        }
    }

//returns priority of user by his login
    public int getPriority(String login)
    {
        try
        {
            DB.getInstance().query("select priority from Account where login='" + login + "'");
            if (DB.getInstance().getRowcount() == 0)
            {
                return -1;
            }
        } catch (SQLException e)
        {
            return -1;
        }
        return Integer.valueOf(DB.getInstance().getResultList().get(0).get("PRIORITY").toString());
    }

//returns all resources in the system
    public ArrayList<HashMap> getResources()
    {
        try
        {
            if (!DB.getInstance().query("Select title from resources"))
            {
                return null;
            }
        } catch (SQLException ex)
        {
            return null;
        }
        return DB.getInstance().getResultList();
    }

    //returns all available roles in the system
    public ArrayList<HashMap> getRoles()
    {
        try
        {
            if (!DB.getInstance().query("Select title from role"))
            {
                return null;
            }
        } catch (SQLException ex)
        {
            return null;
        }
        return DB.getInstance().getResultList();
    }

    public void reReserve(String accID, String start, String end)
    {
//        SimpleDateFormat sd=new SimpleDateFormat("MM-dd:HH:mm");
//        System.out.println(sd.format(d));
        // DB.query("select login, start,end from Account a,Journal j where a.acc_id=j.acc_id and start<=todate("+start+") "+
        //         "or start<todate("+end+")");
    }
}
