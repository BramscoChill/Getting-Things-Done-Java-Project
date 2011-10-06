/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import Model.Project;
import Model.Context;
import Model.Status;
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
                //System.out.println("Deleted: " + affectedRows);
                
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
                resultSetCounter++;
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
            int amountRows = GetRowCount(TABLE_ACTION);
            System.out.println("amountRows: " + TABLE_ACTION);
            ResultSet resultSet = ExecuteSelectQuery("SELECT * FROM " + TABLE_ACTION + " ORDER BY id DESC;");
            if(amountRows > 0 && resultSet != null){
                
                actions = new Action[amountRows]; //haalt het aantal rijen op
                
                int resultCounter = 0;
                while(resultSet.next()){
                    actions[resultCounter] = new Action();
                    actions[resultCounter].setID(resultSet.getInt(1));
                    actions[resultCounter].setDiscription(resultSet.getString(2));
                    actions[resultCounter].setNote(resultSet.getString(3));
                    actions[resultCounter].setStatus((Status)GetNameItem(resultSet.getInt(4),new Status(),false));
                    actions[resultCounter].setContext((Context)GetNameItem(resultSet.getInt(5),new Context(),false));
                    actions[resultCounter].setProject((Project)GetNameItem(resultSet.getInt(6),new Project(),false));
                    
                    try{
                        actions[resultCounter].setDatumTijd(resultSet.getTimestamp(7));
                    } catch (Exception ex){
                        actions[resultCounter].setDatumTijd(new java.sql.Timestamp(20,0,0,0,0,0,0));
                    }
                    System.out.println(actions[resultCounter].getDatumTijd());
                    //actions[resultCounter].PrintAll();
                }
                
            }
            //resultSet.close();
            CloseConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        
        CloseConnection();
        return actions;
    }
       
    /*haalt de meuk op van een nameitem
     * dit kan een: Status, Context of Project zijn
     */
    public NameItem GetNameItem(int nameitemIndex, NameItem daItem, Boolean closeConnection){
        try {
            NameItem result;
            String tableName = "";
            if(daItem instanceof Status){
                result = new Status();
                tableName = TABLE_STATUS;
                System.out.println("Instance of status");
            } else if(daItem instanceof Context){
                result = new Context();
                tableName = TABLE_CONTEXT;
                System.out.println("Instance of context");
            } else {
                result = new Project();
                tableName = TABLE_PROJECT;
                System.out.println("Instance of project");
            }
            MakeConnection(); //maakt database connectie indien nodig
            
            //int amountRows = GetRowCount(TABLE_THOUGHT);
            ResultSet resultSet = ExecuteSelectQuery("SELECT * FROM " + tableName + " WHERE id = " + nameitemIndex + ";");
            
            
            if(resultSet != null){
                int resultCounter = 0; //om te checke of er wel iets wordt teruggegeven
                while(resultSet.next()){
                    if(resultCounter == 0){
                        result.setID(resultSet.getInt(1));
                        result.setName(resultSet.getString(2));
                        
                        if(daItem instanceof Project){
                            Project tmpProject = (Project) result;
                            tmpProject.setNote(resultSet.getString(3));
                            result = tmpProject;
                        }
                    } else {
                        
                        return null;
                    }
                    resultCounter++;
                }
            }
            resultSet.close();
            
            if(closeConnection){ CloseConnection(); }
            
            return result;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        
        if(closeConnection){ CloseConnection(); }
        return null;
    }
    
    public NameItem GetNameItem(int nameitemIndex, NameItem daItem){
        return GetNameItem(nameitemIndex,daItem,true);
    }
    
    //<editor-fold defaultstate="collapsed" desc="Status DB handlers">
    public Status[] GetAllStatuses(Boolean closeConnection){
        try {
            
            MakeConnection(); //maakt database connectie indien nodig
            
            Status results[] = new Status[GetRowCount(TABLE_STATUS)];
            //int amountRows = GetRowCount(TABLE_THOUGHT);
            ResultSet resultSet = ExecuteSelectQuery("SELECT * FROM " + TABLE_STATUS + ";");
            
            
            if(resultSet != null){
                int resultCounter = 0; //om te checke of er wel iets wordt teruggegeven
                while(resultSet.next()){
                    results[resultCounter] = new Status();
                    results[resultCounter].setID(resultSet.getInt(1));
                    results[resultCounter].setName(resultSet.getString(2));
                    resultCounter++;
                }
            }
            resultSet.close();
            
            if(closeConnection){ CloseConnection(); }
            
            return results;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        
        if(closeConnection){ CloseConnection(); }
        return null;
    }
    
    public Status[] GetAllStatuses(){
        return GetAllStatuses(true);
    }
    
    public Status AddStatus(Status status){
        try {
            MakeConnection(); //maakt database connectie indien nodig
            
            //gaat alles preparen, voorkomt sql injectie etc.
            PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement("INSERT INTO " + TABLE_STATUS + " (name) VALUES (?);",Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, status.getName());
            
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
            
            System.out.println("add context IDofInsertedThought: " + IDofInsertedThought);
            //haalt de huidige ingevoegde op
            preparedStatement = (PreparedStatement) connection.prepareStatement("SELECT * FROM " + TABLE_STATUS + " WHERE id = ?;");
            preparedStatement.setInt(1, IDofInsertedThought);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            int resultSetCounter = 0;
            Status daStatus = null;
            
            while(resultSet.next()){
                if(resultSetCounter == 0) {
                    daStatus = new Status(resultSet.getInt(1), resultSet.getString(2));
                    daStatus.PrintAll();
                    //System.out.println(thoughts[uprsCounter].GetNote());
                } else {
                    preparedStatement.close();
                    resultSet.close();
                    CloseConnection();
                    return null;
                }
                resultSetCounter++;
            }
            
            preparedStatement.close();
            resultSet.close();
            CloseConnection();
            return daStatus;
            
        } catch (SQLException ex) {
            Logger.getLogger(DBhandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public Boolean DeleteStatus(Status status){
        try {
            MakeConnection(); //maakt database connectie indien nodig
            if(status.getID() > -1){
                PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement("DELETE FROM " + TABLE_STATUS + " WHERE name = ? AND id = ? LIMIT 1;");
                preparedStatement.setString(1, status.getName());
                preparedStatement.setInt(2, status.getID());
                
                //voert de query daadwerkelijk uit
                int affectedRows = preparedStatement.executeUpdate();
                System.out.println("Deleted: " + affectedRows);
                
                preparedStatement.close();
                CloseConnection();
                
                return (affectedRows == 1); //eigenlijk onnodig
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DBhandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Context DB Handlers">
    public Context[] GetAllContexts(Boolean closeConnection){
        try {
            
            MakeConnection(); //maakt database connectie indien nodig
            
            Context results[] = new Context[GetRowCount(TABLE_CONTEXT)];
            //int amountRows = GetRowCount(TABLE_THOUGHT);
            ResultSet resultSet = ExecuteSelectQuery("SELECT * FROM " + TABLE_CONTEXT + ";");
            
            
            if(resultSet != null){
                int resultCounter = 0; //om te checke of er wel iets wordt teruggegeven
                while(resultSet.next()){
                    results[resultCounter] = new Context();
                    results[resultCounter].setID(resultSet.getInt(1));
                    results[resultCounter].setName(resultSet.getString(2));
                    resultCounter++;
                }
            }
            resultSet.close();
            
            if(closeConnection){ CloseConnection(); }
            
            return results;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        
        if(closeConnection){ CloseConnection(); }
        return null;
    }
    
    public Context[] GetAllContexts(){
        return GetAllContexts(true);
    }
    
    public Context AddContext(Context context){
        try {
            MakeConnection(); //maakt database connectie indien nodig
            
            //gaat alles preparen, voorkomt sql injectie etc.
            PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement("INSERT INTO " + TABLE_CONTEXT + " (name) VALUES (?);",Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, context.getName());
            
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
            
            System.out.println("add context IDofInsertedThought: " + IDofInsertedThought);
            //haalt de huidige ingevoegde op
            preparedStatement = (PreparedStatement) connection.prepareStatement("SELECT * FROM " + TABLE_CONTEXT + " WHERE id = ?;");
            preparedStatement.setInt(1, IDofInsertedThought);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            int resultSetCounter = 0;
            Context daContext = null;
            
            while(resultSet.next()){
                if(resultSetCounter == 0) {
                    daContext = new Context(resultSet.getInt(1), resultSet.getString(2));
                    daContext.PrintAll();
                    //System.out.println(thoughts[uprsCounter].GetNote());
                } else {
                    preparedStatement.close();
                    resultSet.close();
                    CloseConnection();
                    return null;
                }
                resultSetCounter++;
            }
            
            preparedStatement.close();
            resultSet.close();
            CloseConnection();
            return daContext;
            
        } catch (SQLException ex) {
            Logger.getLogger(DBhandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public Boolean DeleteContext(Context context){
        try {
            MakeConnection(); //maakt database connectie indien nodig
            if(context.getID() > -1){
                PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement("DELETE FROM " + TABLE_CONTEXT + " WHERE name = ? AND id = ? LIMIT 1;");
                preparedStatement.setString(1, context.getName());
                preparedStatement.setInt(2, context.getID());
                
                //voert de query daadwerkelijk uit
                int affectedRows = preparedStatement.executeUpdate();
                System.out.println("Deleted: " + affectedRows);
                
                preparedStatement.close();
                CloseConnection();
                
                return (affectedRows == 1); //eigenlijk onnodig
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DBhandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Project DB Handlers">
    public Project[] GetAllProjects(Boolean closeConnection){
        try {
            
            MakeConnection(); //maakt database connectie indien nodig
            
            Project results[] = new Project[GetRowCount(TABLE_PROJECT)];
            //int amountRows = GetRowCount(TABLE_THOUGHT);
            ResultSet resultSet = ExecuteSelectQuery("SELECT * FROM " + TABLE_PROJECT + ";");
            
            
            if(resultSet != null){
                int resultCounter = 0; //om te checke of er wel iets wordt teruggegeven
                while(resultSet.next()){
                    results[resultCounter] = new Project();
                    results[resultCounter].setID(resultSet.getInt(1));
                    results[resultCounter].setName(resultSet.getString(2));
                    results[resultCounter].setNote(resultSet.getString(3));
                    resultCounter++;
                }
            }
            resultSet.close();
            
            if(closeConnection){ CloseConnection(); }
            
            return results;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        
        if(closeConnection){ CloseConnection(); }
        return null;
    }
    
    public Project[] GetAllProjects(){
        return GetAllProjects(true);
    }
    
    public Project AddProject(Project project){
        try {
            MakeConnection(); //maakt database connectie indien nodig
            
            //gaat alles preparen, voorkomt sql injectie etc.
            PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement("INSERT INTO " + TABLE_PROJECT + " (name notes) VALUES (?,?);",Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, project.getName());
            preparedStatement.setString(2, project.getNote());
            
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
            preparedStatement = (PreparedStatement) connection.prepareStatement("SELECT * FROM " + TABLE_PROJECT + " WHERE id = ?;");
            preparedStatement.setInt(1, IDofInsertedThought);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            int resultSetCounter = 0;
            Project daProject = null;
            
            while(resultSet.next()){
                if(resultSetCounter == 0) {
                    daProject = new Project(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
                    daProject.PrintAll();
                    //System.out.println(thoughts[uprsCounter].GetNote());
                } else {
                    preparedStatement.close();
                    resultSet.close();
                    CloseConnection();
                    return null;
                }
                resultSetCounter++;
            }
            
            preparedStatement.close();
            resultSet.close();
            CloseConnection();
            return daProject;
            
        } catch (SQLException ex) {
            Logger.getLogger(DBhandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public Boolean DeleteProject(Project project){
        try {
            MakeConnection(); //maakt database connectie indien nodig
            if(project.getID() > -1){
                PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement("DELETE FROM " + TABLE_PROJECT + " WHERE name = ? AND id = ? LIMIT 1;");
                preparedStatement.setString(1, project.getName());
                preparedStatement.setInt(2, project.getID());
                
                //voert de query daadwerkelijk uit
                int affectedRows = preparedStatement.executeUpdate();
                System.out.println("Deleted: " + affectedRows);
                
                preparedStatement.close();
                CloseConnection();
                
                return (affectedRows == 1); //eigenlijk onnodig
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DBhandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    //</editor-fold>
    
    public void test(){
        NameItem daItem = new Status();
        
        NameItem result;
            if(daItem instanceof Status){
                result = new Status();
                System.out.println("status");
            } else if(daItem instanceof Context){
                result = new Context();
                System.out.println("Context");
            } else {
                result = new Project();
                System.out.println("project");
            }
    }
}
