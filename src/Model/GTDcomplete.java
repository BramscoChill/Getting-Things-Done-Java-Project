/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import model.Database.DBhandler;
import model.exceptions.WrongDatabaseException;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import model.exceptions.DatabaseException;
import model.exceptions.ThingsException;
import view.MainConstants;

/**
 *
 * @author Administrator
 */
public class GTDcomplete extends Observable {
    private ArrayList<Action> actions = new ArrayList<Action>();
    private ArrayList<Thought> thoughts = new ArrayList<Thought>();
    
    private ArrayList<Context> contexts = new ArrayList<Context>();
    private ArrayList<Project> projects = new ArrayList<Project>();
    private ArrayList<Status> statuses = new ArrayList<Status>();
    
    private DBhandler dbHandler = new DBhandler();
    
    //<editor-fold defaultstate="collapsed" desc="Gedachten">
    public Thought[] GetAllThoughtsAsArray(){
        return (Thought[]) thoughts.toArray(new Thought[thoughts.size()]);
    }
    
    public void SetAllThoughts() throws DatabaseException, ThingsException{
        thoughts = new ArrayList<Thought>(Arrays.asList(dbHandler.GetAllThoughts()));
    }
    
    public ArrayList<Thought> GetAllThoughtsAsArrayList(){
        return thoughts;
    }
    
    public Thought GetThought(int thoughtID){
//        if(index > -1 && index < thoughts.size()){
//            return thoughts.get(index);
//        }
        for(int i = 0; i < thoughts.size(); i++ ){
            if(thoughts.get(i) != null && thoughts.get(i).GetID() == thoughtID){
                return thoughts.get(i);
            }
        }
        return null;
    }
    
    public Boolean AddThought(Thought thought) throws ThingsException, DatabaseException{
        Thought newThought = dbHandler.AddThought(thought);
        if(newThought != null && thought.GetID() == -1)
        {
            thoughts.add(newThought);
        }
        //observer call
        setChanged();
        notifyObservers(newThought);
        return (newThought != null);
    }
    
    public Boolean AddThought(String thought) throws ThingsException, DatabaseException{
        return AddThought(new Thought(thought));
    }
    
    public Boolean UpdateThought(Thought thought) throws ThingsException, DatabaseException{
        return AddThought(thought);
    }
    
    public Boolean DeleteThought(Thought thought) throws ThingsException, DatabaseException{
        thoughts.remove(thought);
        setChanged();
        notifyObservers(thought);
        return dbHandler.DeleteThought(thought);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Actions">
    
    public void SetAllActions() throws DatabaseException, ThingsException{
        actions = new ArrayList<Action>(Arrays.asList(dbHandler.GetAllActions()));
    }
    
    
    //filtert alle acties eruit die al wel gedaan zijn
    public void RefreshAllActionsNotDone(){
        //System.out.println("RefreshAllActionsNotDone len: " + actions.size());
        ArrayList<Action> tmpActions = new ArrayList<Action>();
        for(int i = 0; i < actions.size(); i++){
            if(actions.get(i).isDone()){
                tmpActions.add(actions.get(i));
                System.out.println("Removed action!");
            }
        }
        actions.removeAll(tmpActions);
        //System.out.println("RefreshAllActionsNotDone len: " + actions.size());
    }
    
    public Action[] GetAllActionsAsArray(){
        return (Action[]) actions.toArray(new Action[actions.size()]);
    }
    
    public Action[] GetAllActionsNotDoneAsArray(){
        ArrayList<Action> daActions = actions;
        ArrayList<Action> tmpActions = new ArrayList<Action>();
        for(int i = 0; i < daActions.size(); i++){
            if(!daActions.get(i).isDone()){
                tmpActions.add(daActions.get(i));
                //System.out.println("Removed action!");
            }
        }
        //daActions.removeAll(tmpActions);
        return (Action[]) tmpActions.toArray(new Action[tmpActions.size()]);
    }
    
    public Action[] GetAllActionsDoneAsArray(){
        ArrayList<Action> daActions = actions;
        ArrayList<Action> tmpActions = new ArrayList<Action>();
        for(int i = 0; i < daActions.size(); i++){
            if(daActions.get(i).isDone()){
                tmpActions.add(daActions.get(i));
                //System.out.println("Removed action!");
            }
        }
        //daActions.removeAll(tmpActions);
        return (Action[]) tmpActions.toArray(new Action[tmpActions.size()]);
    }
    
    public ArrayList<Action> GetAllActionsAsArrayList(){
        return actions;
    }
    
    public Action GetAction(int actionID){
//        if(index > -1 && index < thoughts.size()){
//            return thoughts.get(index);
//        }
        for(int i = 0; i < actions.size(); i++ ){
            if(actions.get(i) != null && actions.get(i).getID() == actionID){
                return actions.get(i);
            }
        }
        return null;
    }
    public Action AddAction(Action action) throws ThingsException, DatabaseException{
        Action newAction = dbHandler.AddAction(action);
        System.out.println("GTD complete - AddAction - added new action");
        if(newAction != null)
        {
            actions.add(newAction);
        }
        return newAction;
    }
    
    public Action UpdateAction(Action action) throws ThingsException, DatabaseException{
        Action newAction = dbHandler.AddAction(action);
        Boolean foundAction = false;
        
        //System.out.println("UpdateAction action list count: " + actions.size());
        
        if(newAction != null){
            //vervangt de bestaande actie in de lijst met acties, dan hoeft niet alles opnieuw opgehaald te worden
            for(int i = 0; i < actions.size(); i++){
                if(actions.get(i).getID() == newAction.getID()){
                    actions.set(i, newAction);
                    foundAction = true;
                    //filtert alle acties eruit dit niet gedaan zijn
                    
                    break;
                }
            }
        }
        //System.out.println("UpdateAction action list count: " + actions.size());
        
        if(foundAction){
            //RefreshAllActionsNotDone();
            return newAction;
        }
        //als ie niet geupdate kan worden, dan geeft ie null terug
        return  null;
    }
    
    public Boolean DeleteAction(Action action) throws ThingsException, DatabaseException{
        actions.remove(action);
        return dbHandler.DeleteAction(action);
    }
    //</editor-fold>
    
    public Project AddProject(Project project) throws ThingsException, DatabaseException{
        Project newProject = dbHandler.AddProject(project);
        if(newProject != null){
            projects.add(newProject);
        }
        return newProject;
    }
    
    //aaleen om een nieuw project intern toe te voegen
    public Project AddProjectInternal(Project project){
        if(project != null && project.getID() == -1){
            projects.add(project);
            return project;
        }
        return null;
    }
    
    public Project UpdateProject(Project project) throws ThingsException, DatabaseException{
        return AddProject(project);
    }
    
    public Boolean DeleteProject(Project project) throws ThingsException, DatabaseException, MySQLIntegrityConstraintViolationException{
        if(project.getID() != -1){
            Boolean gelukt = dbHandler.DeleteProject(project);
            if(! gelukt){
                return null;
            }
        }
        projects.remove(project);
        for(int i = 0; i < actions.size(); i++){
            if(actions.get(i).getProject() == project){
               Action newAction = actions.get(i);
               newAction.setProject(null);
                actions.set(i, newAction);
            }
        }
        return true;
    }
    
    public Boolean DeleteProjectAndRemoveDependencies(Project project) throws ThingsException, DatabaseException, MySQLIntegrityConstraintViolationException{
        if(project.getID() != -1){
            Boolean gelukt = dbHandler.DeleteProjectAndRemoveDependencies(project);
            if(! gelukt){
                return null;
            }
        }
        projects.remove(project);
        for(int i = 0; i < actions.size(); i++){
            if(actions.get(i).getProject() == project){
               Action newAction = actions.get(i);
               newAction.setProject(null);
                actions.set(i, newAction);
            }
        }
        return true;
    }
    
    
    
    public Project[] GetProjectsFromDB() throws ThingsException, DatabaseException{
        return dbHandler.GetAllProjects();
    }
    
    public Project[] GetProjectsInternal() {
        return (Project[]) projects.toArray(new Project[projects.size()]);
    }
    
    //haalt een project op aan de hand van de index in de lijst, dus NIET het ID
    public Project GetProjectInternal(int index){
        System.out.println("projects size: " + projects.size());
        if(index > -1 && index < projects.size()){
            return projects.get(index);
        }
        return null;
    }
    
    public Boolean UpdatInternalProjectsToDB() throws ThingsException, DatabaseException{
        MainConstants.CloseConnectionAfterDatabaseAction = false;
        for(int i = 0; i < projects.size(); i++){
            Project newProject = dbHandler.AddProject(projects.get(i));
            if(newProject == null){
                return false;
            }
            projects.set(i, newProject);
        }
        MainConstants.CloseConnectionAfterDatabaseAction = true;
        return true;
    }
    
    public Status AddStatus(Status status) throws ThingsException, DatabaseException{
        Status newStatus = dbHandler.AddStatus(status);
        if(newStatus != null){
            statuses.add(newStatus);
        }
        return newStatus;
    }
    
    public Status UpdateStatus(Status status) throws ThingsException, DatabaseException{
        return AddStatus(status);
    }
    
    public Boolean DeleteStatus(Status status) throws ThingsException, DatabaseException{
        return dbHandler.DeleteStatus(status);
    }
    
    public Status[] GetStatusesFromDB() throws ThingsException, DatabaseException{
        return dbHandler.GetAllStatuses();
    }
    
    public Status[] GetStatusesInternal() throws ThingsException, DatabaseException{
        return (Status[]) statuses.toArray(new Status[statuses.size()]);
    }
    
    public Context AddContext(Context context) throws ThingsException, DatabaseException{
        Context newContext = dbHandler.AddContext(context);
        if(newContext != null){
            contexts.add(newContext);
        }
        return(newContext);
    }
    
    public Context UpdateContext(Context context) throws ThingsException, DatabaseException{
        return AddContext(context);
    }
    
    public Boolean DeleteContext(Context context) throws ThingsException, DatabaseException, MySQLIntegrityConstraintViolationException{
        if(context.getID() != -1){
            Boolean gelukt = dbHandler.DeleteContext(context);
            if(! gelukt){
                return null;
            }
        }
        contexts.remove(context);
        for(int i = 0; i < actions.size(); i++){
            if(actions.get(i).getContext() == context){
               Action newAction = actions.get(i);
               newAction.setContext(null);
                actions.set(i, newAction);
            }
        }
        return true; 
    }
    
    public Boolean DeleteContextAndRemoveDependencies(Context context) throws ThingsException, DatabaseException, MySQLIntegrityConstraintViolationException{
        if(context.getID() != -1){
            Boolean gelukt = dbHandler.DeleteContextAndRemoveDependencies(context);
            if(! gelukt){
                return null;
            }
        }
        contexts.remove(context);
        for(int i = 0; i < actions.size(); i++){
            if(actions.get(i).getContext() == context){
               Action newAction = actions.get(i);
               newAction.setContext(null);
                actions.set(i, newAction);
            }
        }
        return true;
    }
    
    public Boolean ValidateDatabase() throws DatabaseException, WrongDatabaseException, SQLException{
        return dbHandler.ValidateDatabase();
    }
    
    public Context[] GetContextsFromDB() throws ThingsException, DatabaseException{
        return dbHandler.GetAllContexts();
    }
    
    public Context[] GetContextsInternal() throws ThingsException, DatabaseException{
        return (Context[]) contexts.toArray(new Context[contexts.size()]);
    }
    
    public void SetAllProjectsContextsStatuses() throws ThingsException, DatabaseException{
        contexts = new ArrayList<Context>(Arrays.asList(GetContextsFromDB()));
        projects = new ArrayList<Project>(Arrays.asList(GetProjectsFromDB()));
        statuses = new ArrayList<Status>(Arrays.asList(GetStatusesFromDB()));
    }
    
}
