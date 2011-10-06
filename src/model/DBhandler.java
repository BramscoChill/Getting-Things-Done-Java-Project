/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import Model.Action;
import com.mysql.jdbc.PreparedStatement;
import Model.Thought;
import com.mysql.jdbc.ResultSetMetaData;
import java.sql.ResultSet;
import com.mysql.jdbc.Statement;
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
    private void MakeConnection(){
        //als er geen connectie is, dan maakt ie er eentje
        if(! CheckConnection()){
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
    }
    
    //Checkt of dat ie nog verbonden is en of er een query gesubmit kan worden
    private Boolean CheckConnection(){
        try {
            return (connection != null) && connection.isValid(100);
        } catch (SQLException ex) {
            //Logger.getLogger(DBhandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    //sluit de verbinding, indien nodig
    private void CloseConnection(){
        try {
            if(CheckConnection()){
                connection.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger(DBhandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private ResultSet ExecuteSelectQuery(String query){
        MakeConnection();
        try {
            Statement stmt = (Statement) connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE );
            ResultSet uprs = stmt.executeQuery(query);
            return uprs;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    private int GetRowCount(String tableName){
        int amount = 0;
        try {
            ResultSet resultSet = ExecuteSelectQuery("SELECT COUNT(*) FROM " + tableName + ";"); 
            
            resultSet.next();
            
            amount = resultSet.getInt(1);
            //System.out.println();
        
            resultSet.close();
        } catch (SQLException ex) {
            
            System.out.println("ERRORRRRR in: DBHandler - GetRowCount:");
            ex.printStackTrace();
            //Logger.getLogger(DBhandler.class.getName()).log(Level.SEVERE, null, ex);
        }
 
        return amount;
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="Gedachten Publieke Functies">
    private int GetColumnCount(ResultSet resultSet){
        ResultSetMetaData rsMetaData;
        try {
            rsMetaData = (ResultSetMetaData) resultSet.getMetaData();
            int amountColums = rsMetaData.getColumnCount();
            return amountColums;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }
    
    
    public Thought[] GetAllThoughts(){
        
        Thought thoughts[] = null;
        
        try {
            int amountRows = GetRowCount(TABLE_THOUGHT);
            ResultSet resultSet = ExecuteSelectQuery("SELECT * FROM " + TABLE_THOUGHT + " ORDER BY datum ASC;");
            if(amountRows > 0 && resultSet != null){
                
                thoughts = new Thought[amountRows]; //haalt het aantal rijen op
                
                int resultCounter = 0;
                while(resultSet.next()){
                    thoughts[resultCounter] = new Thought(resultSet.getInt(1), resultSet.getString(2), resultSet.getTimestamp(3));
                    thoughts[resultCounter].PrintThought();
                }
                
            }
            resultSet.close();
            CloseConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        
        CloseConnection();
        return thoughts;
    }
    
    public Boolean DeleteThought(Thought thought){
        try {
            MakeConnection(); //maakt database connectie indien nodig
            if(thought.GetID() > -1){
                PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement("DELETE FROM " + TABLE_THOUGHT + " WHERE notes = ? AND id = ? LIMIT 1;");
                preparedStatement.setString(1, thought.GetNote());
                preparedStatement.setInt(2, thought.GetID());
                
                //voert de query daadwerkelijk uit
                int affectedRows = preparedStatement.executeUpdate();
                System.out.println("Deleted: " + affectedRows);
                
                preparedStatement.close();
                CloseConnection();
                
                return (affectedRows == 1);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DBhandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    public Thought AddThought(String thought){
        try {
            MakeConnection(); //maakt database connectie indien nodig
            
            //gaat alles preparen, voorkomt sql injectie etc.
            PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement("INSERT INTO " + TABLE_THOUGHT + " (notes) VALUES (?);",Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, thought);
            
            //voert de query daadwerkelijk uit
            int affectedRows = preparedStatement.executeUpdate();
            
            // gaat de nieuw gegenereerde key ophalen
            int IDofInsertedThought = -1;
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                IDofInsertedThought = (int) generatedKeys.getLong(1);
            } else {
                preparedStatement.close();
                CloseConnection();
                return null;
            }
            
            //haalt de huidige ingevoegde op
            preparedStatement = (PreparedStatement) connection.prepareStatement("SELECT * FROM " + TABLE_THOUGHT + " WHERE id = ? AND notes = ?;");
            preparedStatement.setInt(1, IDofInsertedThought);
            preparedStatement.setString(2, thought);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            int resultSetCounter = 0;
            Thought daThought = null;
            
            while(resultSet.next()){
                if(resultSetCounter == 0) {
                    daThought = new Thought(resultSet.getInt(1), resultSet.getString(2), resultSet.getTimestamp(3));
                    daThought.PrintThought();
                    //System.out.println(thoughts[uprsCounter].GetNote());
                } else {
                    preparedStatement.close();
                    resultSet.close();
                    CloseConnection();
                    return null;
                }
            }
            
            preparedStatement.close();
            resultSet.close();
            CloseConnection();
            return daThought;
            
        } catch (SQLException ex) {
            Logger.getLogger(DBhandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    //</editor-fold>
    
    public Action[] GetAllActions(){
        
        Action actions[] = null;
        
        try {
            int amountRows = GetRowCount(TABLE_THOUGHT);
            ResultSet resultSet = ExecuteSelectQuery("SELECT * FROM " + TABLE_ACTION + " ORDER BY id DESC;");
            if(amountRows > 0 && resultSet != null){
                
                actions = new Action[amountRows]; //haalt het aantal rijen op
                
                int resultCounter = 0;
                while(resultSet.next()){
                    //actions[resultCounter] = new Thought(resultSet.getInt(1), resultSet.getString(2), resultSet.getTimestamp(3));
                    //actions[resultCounter].PrintThought();
                }
                
            }
            resultSet.close();
            CloseConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        
        CloseConnection();
        return actions;
    }
        
}
