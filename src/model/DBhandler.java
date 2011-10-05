/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.DriverManager;
import com.mysql.jdbc.Connection;
import static view.MainConstants.*;
/**
 *
 * @author Bram Klein
 */
public class DBhandler {
    private Connection connection;
    
    public void MakeConnection(){
        String daURL = "jdbc:mysql://" + MYSQL_SERVER + "/" + DB_NAME;
        try{
            connection = (Connection) DriverManager.getConnection(daURL, MYSQL_USERNAME, MYSQL_PASSWORD);
        }
        catch (Exception ex){
            {
                System.out.println("CONNECTIE GEFAALT!!!");
                ex.printStackTrace();
            }
        }
    }
}
