/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.DriverManager;
import com.mysql.jdbc.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static view.MainConstants.*;
/**
 *
 * @author Bram Klein
 */
public class DBhandler {
    private Connection connection;
    
    //maakt de verbinding met de database
    public void MakeConnection(){
        String daURL = "jdbc:mysql://" + MYSQL_SERVER + "/" + DB_NAME;
        try{
            System.out.println("Going to make  connection");
            connection = (Connection) DriverManager.getConnection(daURL, MYSQL_USERNAME, MYSQL_PASSWORD);
            System.out.println("Connection establisht: " + CheckConnection());
        }
        catch (Exception ex){
            {
                System.out.println("CONNECTIE GEFAALT!!!");
                ex.printStackTrace();
            }
        }
    }
    
    //Checkt of dat ie nog verbonden is en of er een query gesubmit kan worden
    public Boolean CheckConnection(){
        try {
            return (connection != null) && connection.isValid(100);
        } catch (SQLException ex) {
            //Logger.getLogger(DBhandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    //sluit de verbinding, indien nodig
    public void CloseConnection(){
        try {
            if(CheckConnection()){
                connection.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger(DBhandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
