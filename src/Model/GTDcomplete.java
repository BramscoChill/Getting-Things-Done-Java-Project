/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Model.Database.DBhandler;
import java.util.ArrayList;
import java.util.Observable;

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
        return (Thought[]) thoughts.toArray();
    }
    
    public ArrayList<Thought> GetAllThoughtsAsArrayList(){
        return thoughts;
    }
    
    public Thought GetThought(int index){
        if(index > -1 && index < thoughts.size()){
            return thoughts.get(index);
        }
        return null;
    }
    
    public Boolean AddThought(Thought thought){
        Thought newThought = dbHandler.AddThought(thought);
        if(newThought != null)
        {
            thoughts.add(newThought);
        }
        return (newThought != null);
    }
    
    public Boolean AddThought(String thought){
        return AddThought(new Thought(thought));
    }
    
    public Boolean UpdateThought(Thought thought){
        return AddThought(thought);
    }
    
    public Boolean DeleteThought(Thought thought){
        thoughts.remove(thought);
        return dbHandler.DeleteThought(thought);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Actions">
    public Action[] GetAllActionssAsArray(){
        return (Action[]) actions.toArray();
    }
    
    public ArrayList<Action> GetAllActionsAsArrayList(){
        return actions;
    }
    
    public Boolean AddAction(Action action){
        Action newAction = dbHandler.AddAction(action);
        if(newAction != null)
        {
            actions.add(newAction);
        }
        return (newAction != null);
    }
    
    public Boolean UpdateAction(Action action){
        return AddAction(action);
    }
    
    public Boolean DeleteAction(Action action){
        actions.remove(action);
        return dbHandler.DeleteAction(action);
    }
    //</editor-fold>
    
    public Boolean AddProject(Project project){
        Project newProject = dbHandler.AddProject(project);
        return (newProject != null);
    }
    
    public Boolean UpdateProject(Project project) {
        return AddProject(project);
    }
    
    public Boolean DeleteProject(Project project){
        return dbHandler.DeleteProject(project);
    }
    
    public Project[] GetProjects(){
        return dbHandler.GetAllProjects();
    }
    
    public Boolean AddStatus(Status status){
        Status newStatus = dbHandler.AddStatus(status);
        return (newStatus != null);
    }
    
    public Boolean UpdateStatus(Status status){
        return AddStatus(status);
    }
    
    public Boolean DeleteStatus(Status status){
        return dbHandler.DeleteStatus(status);
    }
    
    public Boolean AddContext(Context context){
        Context newContext = dbHandler.AddContext(context);
        return (newContext != null);
    }
    
    public Boolean UpdateContext(Context context){
        return AddContext(context);
    }
    
    public Boolean DeleteContext(Context context){
        return dbHandler.DeleteContext(context);
    }
    
}
