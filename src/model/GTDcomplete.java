/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import Model.Database.DBhandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import model.exceptions.DatabaseException;
import model.exceptions.ThingsException;

/**
 *
 * @author Administrator
 */
public class GTDcomplete extends Observable {
    private ArrayList<Action> actions = new ArrayList<Action>();
    private ArrayList<Thought> thoughts = new ArrayList<Thought>();
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
        if(newThought != null)
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
        actions = new ArrayList<Action>(Arrays.asList(dbHandler.GetAllActionsNotDone()));
    }
    
    public Action[] GetAllActionssAsArray(){
        return (Action[]) actions.toArray(new Action[actions.size()]);
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
    public Boolean AddAction(Action action) throws ThingsException, DatabaseException{
        Action newAction = dbHandler.AddAction(action);
        if(newAction != null)
        {
            actions.add(newAction);
        }
        return (newAction != null);
    }
    
    public Boolean UpdateAction(Action action) throws ThingsException, DatabaseException{
        return AddAction(action);
    }
    
    public Boolean DeleteAction(Action action) throws ThingsException, DatabaseException{
        actions.remove(action);
        return dbHandler.DeleteAction(action);
    }
    //</editor-fold>
    
    public Boolean AddProject(Project project) throws ThingsException, DatabaseException{
        Project newProject = dbHandler.AddProject(project);
        return (newProject != null);
    }
    
    public Boolean UpdateProject(Project project) throws ThingsException, DatabaseException{
        return AddProject(project);
    }
    
    public Boolean DeleteProject(Project project) throws ThingsException, DatabaseException{
        return dbHandler.DeleteProject(project);
    }
    
    public Project[] GetProjects() throws ThingsException, DatabaseException{
        return dbHandler.GetAllProjects();
    }
    
    public Boolean AddStatus(Status status) throws ThingsException, DatabaseException{
        Status newStatus = dbHandler.AddStatus(status);
        return (newStatus != null);
    }
    
    public Boolean UpdateStatus(Status status) throws ThingsException, DatabaseException{
        return AddStatus(status);
    }
    
    public Boolean DeleteStatus(Status status) throws ThingsException, DatabaseException{
        return dbHandler.DeleteStatus(status);
    }
    
    public Status[] GetStatuses() throws ThingsException, DatabaseException{
        return dbHandler.GetAllStatuses();
    }
    
    public Boolean AddContext(Context context) throws ThingsException, DatabaseException{
        Context newContext = dbHandler.AddContext(context);
        return (newContext != null);
    }
    
    public Boolean UpdateContext(Context context) throws ThingsException, DatabaseException{
        return AddContext(context);
    }
    
    public Boolean DeleteContext(Context context) throws ThingsException, DatabaseException{
        return dbHandler.DeleteContext(context);
    }
    
    public Context[] GetContexts() throws ThingsException, DatabaseException{
        return dbHandler.GetAllContexts();
    }
    
}
