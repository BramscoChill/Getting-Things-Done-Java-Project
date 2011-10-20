package Model.Database;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Calendar;
import java.util.ArrayList;
import model.*;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSetMetaData;
import java.sql.ResultSet;
import com.mysql.jdbc.Statement;
import java.sql.DriverManager;
import com.mysql.jdbc.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.exceptions.*;

import static view.MainConstants.*;
/**
 *
 * @author Bram Klein
 */
public class DBhandler {
    private Connection connection;
    
    //maakt de verbinding met de database
    private void MakeConnection() throws NoConnectionException{
        //als er geen connectie is, dan maakt ie er eentje
        if(! CheckConnection()){
            String daURL = "jdbc:mysql://" + MYSQL_SERVER + "/" + DB_NAME;
            try{
                System.out.println("Going to make connection with DB");
                connection = (Connection) DriverManager.getConnection(daURL, MYSQL_USERNAME, MYSQL_PASSWORD);
                System.out.println("DB Connection establisht: " + CheckConnection());
            }
            catch (Exception ex){
                {
                    System.out.println("CONNECTIE GEFAALT!!!");
                    ex.printStackTrace();
                    throw new NoConnectionException();
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
    private void CloseConnection() throws NoConnectionException {
        try {
            if(CloseConnectionAfterDatabaseAction && CheckConnection()){
                connection.close();
                System.out.println("DB Connection closed");
            }
        } catch (SQLException ex) {
            
            ex.printStackTrace();
            Logger.getLogger(DBhandler.class.getName()).log(Level.SEVERE, null, ex);
            throw new NoConnectionException();
        }
    }
    
    private ResultSet ExecuteSelectQuery(String query) throws DatabaseException{
        MakeConnection();
        try {
            Statement stmt = (Statement) connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE );
            ResultSet uprs = stmt.executeQuery(query);
            return uprs;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new QueryException(query);
        }
    }
    
    private int GetRowCount(String tableName) throws QueryException{
        int amount = 0;
        try{
            MakeConnection();
            
            ResultSet resultSet = ExecuteSelectQuery("SELECT COUNT(*) FROM " + tableName + ";"); 
            
            resultSet.next();
            
            amount = resultSet.getInt(1);
            //System.out.println();
        
            resultSet.close();
        } catch (Exception ex) {
            throw new QueryException("getting rowcount");
        }
 
        return amount;
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="Gedachten Publieke Functies">
    private int GetColumnCount(ResultSet resultSet) throws QueryException{
        ResultSetMetaData rsMetaData;
        try {
            MakeConnection();
            rsMetaData = (ResultSetMetaData) resultSet.getMetaData();
            int amountColums = rsMetaData.getColumnCount();
            return amountColums;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new QueryException("getting Colum count");
        }
    }
    
    
    public Thought[] GetAllThoughts() throws DatabaseException, ThingsException {
        
        Thought thoughts[] = null;
        
        try {
            int amountRows = GetRowCount(TABLE_THOUGHT);
            ResultSet resultSet = ExecuteSelectQuery("SELECT * FROM " + TABLE_THOUGHT + " ORDER BY datum ASC;");
            if(amountRows > 0 && resultSet != null){
                
                thoughts = new Thought[amountRows]; //haalt het aantal rijen op
                
                int resultCounter = 0;
                while(resultSet.next()){
                    thoughts[resultCounter] = new Thought(resultSet.getInt(1), resultSet.getString(2), resultSet.getTimestamp(3));
                    //thoughts[resultCounter].PrintThought();
                    resultCounter++;
                }
                
            }
            resultSet.close();
            CloseConnection();
            
            return thoughts;
        }  catch (SQLException ex) {
            ex.printStackTrace();
            throw new ThingsException("", new Thought(), ThingsException.ThingsWhat.GET);
        }
    }
    
    public Boolean DeleteThought(Thought thought) throws ThingsException, DatabaseException{
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
            throw new ThingsException("", new Thought(), ThingsException.ThingsWhat.DELETE);
            //Logger.getLogger(DBhandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    public Thought AddThought(Thought thought) throws ThingsException, DatabaseException{
        try {
            MakeConnection(); //maakt database connectie indien nodig
            
            if(thought.GetID() == -1){
                //gaat alles preparen, voorkomt sql injectie etc.
                PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement("INSERT INTO " + TABLE_THOUGHT + " (notes) VALUES (?);",Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, thought.GetNote());

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
                preparedStatement.setString(2, thought.GetNote());

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
            } else {
                PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement("UPDATE " + TABLE_THOUGHT + " SET notes = ? WHERE id = ?;",Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, thought.GetNote());
                preparedStatement.setInt(2, thought.GetID());

                //voert de query daadwerkelijk uit
                int affectedRows = preparedStatement.executeUpdate();
                
                return (affectedRows == 1) ? thought : null;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DBhandler.class.getName()).log(Level.SEVERE, null, ex);
            throw new ThingsException("", new Thought(), ThingsException.ThingsWhat.ADD);
        }
        
    }
    //</editor-fold>
    
    public Action[] GetAllActions() throws DatabaseException, ThingsException{
        
        Action actions[] = null;
        
        try {
            int amountRows = GetRowCount(TABLE_ACTION);
            //System.out.println("amountRows: " + TABLE_ACTION);
            //standaard query ophalen, sql injectie is hier toch niet mogenlijk
            ResultSet resultSet = ExecuteSelectQuery("SELECT * FROM " + TABLE_ACTION + " ORDER BY id DESC;");
            if(amountRows > 0 && resultSet != null){
                
                actions = new Action[amountRows]; //haalt het aantal rijen op
                
                int resultCounter = 0;
                while(resultSet.next()){
                    Boolean PreviousCloseConnectionAfterDatabaseAction = CloseConnectionAfterDatabaseAction; //hij moet de connectie niet gaan sluiten
                    CloseConnectionAfterDatabaseAction = false;
                    actions[resultCounter] = new Action();
                    actions[resultCounter].setID(resultSet.getInt(1));
                    actions[resultCounter].setDescription(resultSet.getString(2));
                    actions[resultCounter].setNote(resultSet.getString(3));
                    actions[resultCounter].setStatus((Status)GetNameItem(resultSet.getInt(4),new Status()));
                    actions[resultCounter].setContext((Context)GetNameItem(resultSet.getInt(5),new Context()));
                    actions[resultCounter].setProject((Project)GetNameItem(resultSet.getInt(6),new Project()));
                    CloseConnectionAfterDatabaseAction = PreviousCloseConnectionAfterDatabaseAction; //nou misschien wel
                    
                    try{
                        actions[resultCounter].setDatumTijd(resultSet.getTimestamp(7));
                    } catch (Exception ex){
                        actions[resultCounter].setDatumTijd(null);
                        //actions[resultCounter].setDatumTijd(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
                    }
                    
                    try{
                        actions[resultCounter].setStatusChanged(resultSet.getTimestamp(8));
                    } catch (Exception ex){
                        actions[resultCounter].setStatusChanged(null);
                        //actions[resultCounter].setStatusChanged(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
                    }
                    
                    actions[resultCounter].setDone(resultSet.getBoolean(9));
                    
                    
                    actions[resultCounter].PrintAll();
                    resultCounter++;
                }
                
            }
            resultSet.close();
            CloseConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new ThingsException("", new Action(), ThingsException.ThingsWhat.GET);
        }
        
        
        CloseConnection();
        return actions;
    }
    
    //krijgt alleen alle acties die nog niet klaar zijn
    public Action[] GetAllActionsNotDone() throws DatabaseException, ThingsException{
        Action[] daActions = GetAllActions();
        List<Action> daList = new ArrayList<Action>();
        
        for(int i = 0; i < daActions.length; i++){
            if(daActions[i] != null && !(daActions[i].isDone())){
                daList.add(daActions[i]);
            }
        }
        
        return (Action[]) daList.toArray(new Action[daList.size()]);
    }
    
    //krijgt alle acties die al klaar zijn
    public Action[] GetAllActionsDone() throws DatabaseException, ThingsException{
        Action[] daActions = GetAllActions();
        List<Action> daList = new ArrayList<Action>();
        
        for(int i = 0; i < daActions.length; i++){
            if(daActions[i] != null && (daActions[i].isDone())){
                daList.add(daActions[i]);
            }
        }
        
        return (Action[]) daList.toArray(new Action[daList.size()]);
    }
       
    /* met deze funtie voeg je een nieuwe actie toe, of update je een bestaande actie
     * Als het Action ID -1 is, dan is het een nieuwe actie, is het groter als -1
     * dan is het een bestaande actie en word deze geupdate aan de hand van de waardes die
     * in de betreffende action staan (parameter)
     */
    public Action AddAction(Action action) throws ThingsException, DatabaseException{
        try {
            MakeConnection(); //maakt database connectie indien nodig
            
            System.out.println("project ID:" + ((action.getProject() != null) ? "" + action.getProject().getID() : " is NULL"));
            
            //voegt changed ding toe
            action.setStatusChanged(new Timestamp(new Date().getTime()));
            
            PreparedStatement preparedStatement;
            if(action.getID() == -1){
            //gaat alles preparen, voorkomt sql injectie etc.
                preparedStatement = (PreparedStatement) connection.prepareStatement("INSERT INTO " + TABLE_ACTION + " " + 
                        "(description, notes, status_id, context_id, project_id, action_date, status_change, done) VALUES (?,?,?,?,?,?,?,?);"
                        ,Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, action.getDescription());
                preparedStatement.setString(2, action.getNote());
                if(action.getStatus() != null && action.getStatus().getID() != -1){preparedStatement.setInt(3, (action.getStatus().getID()));} else {preparedStatement.setNull(3, java.sql.Types.INTEGER);}
                if(action.getContext() != null && action.getContext().getID() != -1){preparedStatement.setInt(4, (action.getContext().getID()));} else {preparedStatement.setNull(4, java.sql.Types.INTEGER);}
                if(action.getProject() != null && action.getProject().getID() != -1){preparedStatement.setInt(5, (action.getProject().getID()));} else {preparedStatement.setNull(5, java.sql.Types.INTEGER);}
                preparedStatement.setTimestamp(6, action.getDatumTijd());
                preparedStatement.setTimestamp(7, action.getStatusChanged());
                preparedStatement.setBoolean(8, action.isDone());
            } else {
                preparedStatement = (PreparedStatement) connection.prepareStatement("UPDATE " + TABLE_ACTION + " SET " + 
                        "description = ?, notes = ?, status_id = ?, context_id = ?, project_id = ?, action_date = ?, status_change = ?, done = ? WHERE " + 
                        "id = ? LIMIT 1;"
                        ,Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, action.getDescription());
                preparedStatement.setString(2, action.getNote());
                
                if(action.getStatus() != null && action.getStatus().getID() != -1){preparedStatement.setInt(3, (action.getStatus().getID()));} else {preparedStatement.setNull(3, java.sql.Types.INTEGER);}
                if(action.getContext() != null && action.getContext().getID() != -1){preparedStatement.setInt(4, (action.getContext().getID()));} else {preparedStatement.setNull(4, java.sql.Types.INTEGER);}
                if(action.getProject() != null && action.getProject().getID() != -1){preparedStatement.setInt(5, (action.getProject().getID()));} else {preparedStatement.setNull(5, java.sql.Types.INTEGER);}
                
                preparedStatement.setTimestamp(6, action.getDatumTijd());
                preparedStatement.setTimestamp(7, action.getStatusChanged());
                preparedStatement.setBoolean(8, action.isDone());
                preparedStatement.setInt(9, action.getID());
                //System.out.println("so far so good 1");
            }
            
            System.out.println("Da query add action: " + preparedStatement);
            //voert de query daadwerkelijk uit
            int affectedRows = preparedStatement.executeUpdate();
            
            //System.out.println("AddAction affectedRows: " + affectedRows);
            
            //als het niet om een update gaat, maar om een nieuwe invoer!
            if(action.getID() == -1) {
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

                //System.out.println("AddAction IDofInsertedThought: " + IDofInsertedThought);
            
                //haalt de huidige ingevoegde op
                preparedStatement = (PreparedStatement) connection.prepareStatement("SELECT * FROM " + TABLE_ACTION + " WHERE id = ?;");
                preparedStatement.setInt(1, IDofInsertedThought);

                ResultSet resultSet = preparedStatement.executeQuery();

                int resultSetCounter = 0;
                Action daAction = null;

                while(resultSet.next()){
                    if(resultSetCounter == 0) {
                        Boolean PreviousCloseConnectionAfterDatabaseAction = CloseConnectionAfterDatabaseAction; //hij moet de connectie niet gaan sluiten
                        CloseConnectionAfterDatabaseAction = false;
                        daAction = new Action();
                        daAction.setID(resultSet.getInt(1));
                        daAction.setDescription(resultSet.getString(2));
                        daAction.setNote(resultSet.getString(3));
                        daAction.setStatus((Status)GetNameItem(resultSet.getInt(4),new Status()));
                        daAction.setContext((Context)GetNameItem(resultSet.getInt(5),new Context()));
                        daAction.setProject((Project)GetNameItem(resultSet.getInt(6),new Project()));
                        CloseConnectionAfterDatabaseAction = PreviousCloseConnectionAfterDatabaseAction;

                        try{
                            daAction.setDatumTijd(resultSet.getTimestamp(7));
                        } catch (Exception ex){
                            daAction.setDatumTijd(null);
                            //daAction.setDatumTijd(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
                        }

                        try{
                            daAction.setStatusChanged(resultSet.getTimestamp(8));
                        } catch (Exception ex){
                            daAction.setStatusChanged(null);
                            //daAction.setStatusChanged(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
                        }

                        daAction.setDone(resultSet.getBoolean(9));
                        //daAction.PrintAll();
                        //System.out.println(thoughts[uprsCounter].GetNote());
                    } else {
                        preparedStatement.close();
                        resultSet.close();
                        CloseConnection();
                        System.out.println("AddAction returned NULL!");
                        return null;
                    }
                    resultSetCounter++;
                }

                preparedStatement.close();
                resultSet.close();
                CloseConnection();
                return daAction;
            } else { //gaat wel om een update, dan kan ie alles gewoon terug pasen
                preparedStatement.close();
                CloseConnection();
                return action;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DBhandler.class.getName()).log(Level.SEVERE, null, ex);
            throw new ThingsException("", new Action(), ThingsException.ThingsWhat.ADD);
        }
    }
    
    public Boolean DeleteAction(Action action) throws ThingsException, DatabaseException{
        try {
            MakeConnection(); //maakt database connectie indien nodig
            if(action.getID() > -1){
                PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement("DELETE FROM " + TABLE_ACTION + " WHERE description = ? AND id = ? LIMIT 1;");
                preparedStatement.setString(1, action.getDescription());
                preparedStatement.setInt(2, action.getID());
                
                //voert de query daadwerkelijk uit
                int affectedRows = preparedStatement.executeUpdate();
                System.out.println("Deleted: " + affectedRows);
                
                preparedStatement.close();
                CloseConnection();
                
                return (affectedRows == 1); //eigenlijk onnodig
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DBhandler.class.getName()).log(Level.SEVERE, null, ex);
            throw new ThingsException("", new Action(), ThingsException.ThingsWhat.ADD);
        }
        
        return false;
    }
    
    /*haalt de meuk op van een nameitem
     * dit kan een: Status, Context of Project zijn
     * de laatste parameter houd in of ie de connectie ook moet sluiten
     * standaard doet ie dit wel (zie 2e functie van: GetNameItem)
     */
    public NameItem GetNameItem(int nameitemIndex, NameItem daItem) throws ThingsException, DatabaseException{
        try {
            NameItem result;
            String tableName = "";
            if(daItem instanceof Status){
                result = new Status();
                tableName = TABLE_STATUS;
                //System.out.println("Instance of status");
            } else if(daItem instanceof Context){
                result = new Context();
                tableName = TABLE_CONTEXT;
                //System.out.println("Instance of context");
            } else {
                result = new Project();
                tableName = TABLE_PROJECT;
                //System.out.println("Instance of project");
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
            
            CloseConnection();
            
            return result;
        } catch (SQLException ex) {
            ex.printStackTrace();
            CloseConnection();
            throw new ThingsException("", daItem, ThingsException.ThingsWhat.GET);
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="Status DB handlers">
    public Status[] GetAllStatuses() throws ThingsException, DatabaseException{
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
            
            CloseConnection();
            
            return results;
        } catch (SQLException ex) {
            ex.printStackTrace();
            CloseConnection();
            throw new ThingsException("", new Status(), ThingsException.ThingsWhat.GET);
        }
    }
    
    public Status AddStatus(Status status) throws ThingsException, DatabaseException{
        try {
            MakeConnection(); //maakt database connectie indien nodig
            
            if(status.getID() == -1){
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
            } else {
                PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement("UPDATE " + TABLE_STATUS + " SET name = ? WHERE id = ?;",Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, status.getName());
                preparedStatement.setInt(2, status.getID());

                //voert de query daadwerkelijk uit
                int affectedRows = preparedStatement.executeUpdate();
                
                return (affectedRows == 1) ? status : null;
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger(DBhandler.class.getName()).log(Level.SEVERE, null, ex);
            CloseConnection();
            throw new ThingsException("", new Status(), ThingsException.ThingsWhat.ADD);
        }
    }
    
    public Boolean DeleteStatus(Status status) throws ThingsException, DatabaseException{
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
            throw new ThingsException("", new Status(), ThingsException.ThingsWhat.DELETE);
        }
        
        return false;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Context DB Handlers">
    public Context[] GetAllContexts() throws ThingsException, DatabaseException{
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
            
            CloseConnection();
            
            return results;
        } catch (SQLException ex) {
            ex.printStackTrace();
            CloseConnection();
            throw new ThingsException("", new Context(), ThingsException.ThingsWhat.GET);
        }
    }
    
    public Context AddContext(Context context) throws ThingsException, DatabaseException{
        try {
            MakeConnection(); //maakt database connectie indien nodig
            
            if(context.getID() == -1){
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
            }  else {
                PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement("UPDATE " + TABLE_CONTEXT + " SET name = ? WHERE id = ?;",Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, context.getName());
                preparedStatement.setInt(2, context.getID());

                //voert de query daadwerkelijk uit
                int affectedRows = preparedStatement.executeUpdate();
                
                return (affectedRows == 1) ? context : null;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DBhandler.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            CloseConnection();
            throw new ThingsException("", new Context(), ThingsException.ThingsWhat.ADD);
        }
    }
    
    public Boolean DeleteContext(Context context) throws ThingsException, DatabaseException, MySQLIntegrityConstraintViolationException{
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
            ex.printStackTrace();
            if(ex.getErrorCode() == 1451){
                //System.out.println("O NOES, its a: MySQLIntegrityConstraintViolationException");
                throw new MySQLIntegrityConstraintViolationException();
            }
            Logger.getLogger(DBhandler.class.getName()).log(Level.SEVERE, null, ex);
            
            CloseConnection();
            throw new ThingsException("", new Context(), ThingsException.ThingsWhat.DELETE);
        }
        
        return false;
    }
    
     public Boolean DeleteContextAndRemoveDependencies(Context context) throws ThingsException, DatabaseException, MySQLIntegrityConstraintViolationException{
            MakeConnection(); //maakt database connectie indien nodig
                CloseConnectionAfterDatabaseAction = false;
                Action[] actions = GetAllActions();
                for(int i = 0; i < actions.length; i++){
//                    System.out.println("is action null: " + (actions[i].getContext() == null ) + ", using context: " + 
//                            context.getName() + ", actions contect: " + actions[i].getContext().getName());
                    if(actions[i].getContext() != null && 
                            actions[i].getContext().getName().equalsIgnoreCase(context.getName()) ){
                        actions[i].setContext(null);
                        AddAction(actions[i]);
                    }
                }
                
                
                CloseConnectionAfterDatabaseAction = true;
                CloseConnection();
                
                return DeleteContext(context);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Project DB Handlers">
    public Project[] GetAllProjects() throws ThingsException, DatabaseException{
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
            
            CloseConnection();
            
            return results;
        } catch (SQLException ex) {
            ex.printStackTrace();
            CloseConnection();
            throw new ThingsException("", new Project(), ThingsException.ThingsWhat.GET);
        }
    }
    
    public Project AddProject(Project project) throws ThingsException, DatabaseException{
        try {
            MakeConnection(); //maakt database connectie indien nodig
            
            //voeg nieuw project toe
            if(project.getID() == -1){
                //gaat alles preparen, voorkomt sql injectie etc.
                PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement("INSERT INTO " + TABLE_PROJECT + " (name,notes) VALUES (?,?);",Statement.RETURN_GENERATED_KEYS);
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
            }  else { //pas bestaand project aan
                PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement("UPDATE " + TABLE_PROJECT + " SET name = ?, notes = ? WHERE id = ?;",Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, project.getName());
                preparedStatement.setString(2, project.getNote());
                preparedStatement.setInt(3, project.getID());

                //voert de query daadwerkelijk uit
                int affectedRows = preparedStatement.executeUpdate();
                
                return (affectedRows == 1) ? project : null;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DBhandler.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            CloseConnection();
            throw new ThingsException("", new Project(), ThingsException.ThingsWhat.ADD);
        }
        
    }
    
    
    public Boolean DeleteProject(Project project) throws ThingsException, DatabaseException, MySQLIntegrityConstraintViolationException{
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
          
        } catch (MySQLIntegrityConstraintViolationException ex){
            throw new MySQLIntegrityConstraintViolationException();
        } catch (SQLException ex) {
            //System.out.println("SQLException error code: " + ex.getErrorCode());
            //als het project afhangkelijkheden heeft in acties!
            if(ex.getErrorCode() == 1451){
                //System.out.println("O NOES, its a: MySQLIntegrityConstraintViolationException");
                throw new MySQLIntegrityConstraintViolationException();
            }
            Logger.getLogger(DBhandler.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            CloseConnection();
            throw new ThingsException("", new Project(), ThingsException.ThingsWhat.DELETE);
        } 
        
        return false;
    }
    
    public Boolean DeleteProjectAndRemoveDependencies(Project project) throws ThingsException, DatabaseException, MySQLIntegrityConstraintViolationException{
            MakeConnection(); //maakt database connectie indien nodig
                CloseConnectionAfterDatabaseAction = false;
                Action[] actions = GetAllActions();
                for(int i = 0; i < actions.length; i++){
                    if(actions[i].getProject() != null && 
                            actions[i].getProject().getName().equalsIgnoreCase(project.getName()) && 
                            actions[i].getProject().getNote().equalsIgnoreCase(project.getNote())){
                        actions[i].setProject(null);
                        AddAction(actions[i]);
                    }
                }
                
                
                CloseConnectionAfterDatabaseAction = true;
                CloseConnection();
                
                return DeleteProject(project);
    }
    //</editor-fold>
    
    //@TODO ValidateDatabase afmaken
    //checkt of de database valide is
    public Boolean ValidateDatabase() throws ThingsException, DatabaseException{
        return true;
    }
    
    //@TODO ResetDatabase afmaken
    //reset de database naar zijn begin tabellen
    public Boolean ResetDatabase() throws ThingsException, DatabaseException{
        return true;
    }
    
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
