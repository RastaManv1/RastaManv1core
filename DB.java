/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 *
 * @author Администратор
 */
public class DB
{

    private static DB instance = null;
    private int rowCount = 0;
    private ArrayList<HashMap> resList = new ArrayList();
    private ArrayList<String> columnList = new ArrayList();
    private Connection connection = null;
    private String connectionConf=null;
    private String connectionLogin=null;
    private String connectionPass=null;
    protected DB()
    {
        try
        {
           BufferedReader conf=new BufferedReader(new FileReader(new File("DB.conf")));
           String host=conf.readLine().split("=")[1];
           String port=conf.readLine().split("=")[1];
           String sid=conf.readLine().split("=")[1];
           connectionLogin=conf.readLine().split("=")[1];
           connectionPass =conf.readLine().split("=")[1];
           connectionConf=host+":"+port+":"+sid;
           conf.close();
        } catch (IOException e)
        {
            System.out.println("not found, creating standart conf - 127.0.0.1:1521:XE");
            try
            {
                FileWriter conf = new FileWriter("DB.conf");
                conf.write("Host=127.0.0.1\nport=1521\nsid=XE\nlogin=System\npassword=1");
                conf.close();
            } catch (IOException ex)
            {
                System.out.println(ex.toString());
            }
        }


    }

    public static DB getInstance()
    {
        if (instance == null)
        {
            instance = new DB();
        }
        return instance;
    }

    public boolean openConnection() throws SQLException
    {
        try
        {
            // Load the JDBC driver
            Locale.setDefault(Locale.ENGLISH);
            String driverName = "oracle.jdbc.driver.OracleDriver";
            Class.forName(driverName);

            // Create a connection to the database
//            String url = "jdbc:oracle:thin:@" + serverName + ":" + portNumber + ":" + sid;
            String url = "jdbc:oracle:thin:@" + connectionConf;
            connection = DriverManager.getConnection(url, connectionLogin, connectionPass);
        } catch (ClassNotFoundException e)
        {
            System.out.println(e.toString());
        } catch (SQLException e)
        {
            System.out.println(e.toString());
        }
        //connection.close();

        return true;
    }

    public boolean query(String q) throws SQLException
    {
        try
        {
            Statement stmt = connection.createStatement();
            ResultSet rset = stmt.executeQuery(q);
            ResultSetMetaData rsmt = rset.getMetaData();
            String cName;
            rowCount = 0;
            resList.clear();
            HashMap<String, String> result = new HashMap();
            while (rset.next())
            {
                rowCount++;
                for (int i = 1; i < rsmt.getColumnCount() + 1; i++)
                {
                    cName = rsmt.getColumnName(i);
                    result.put(cName, rset.getString(cName));
                }
                resList.add((HashMap) result.clone());

            }
            for (int i = 1; i < rsmt.getColumnCount() + 1; i++)
            {
                columnList.add(rsmt.getColumnName(i));
            }
            rset.close();

        } catch (SQLException e)
        {
            System.out.println("DB.Query "+e.toString());
        }

        return true;
    }

    public ArrayList<HashMap> getResultList()
    {
        return resList;
    }

    public ArrayList getColumns()
    {
        return columnList;
    }

    public int getRowcount()
    {
        return rowCount;
    }
}
