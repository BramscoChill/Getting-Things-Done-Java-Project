/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Timestamp;



/**
 *
 * @author sjorsvanuden
 */
public class Action {

    private int ID = -1;
    private String description;
    private String note;
    private Status status = null;
    private Context context = null;
    private Project project = null;
    private Timestamp datumtijd;
    private Timestamp statusChanged;
    
    private boolean done = false;
    
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
    
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Timestamp getDatumTijd() {
        return datumtijd;
    }

    public void setDatumTijd(Timestamp datum) {
        this.datumtijd = datum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String discription) {
        this.description = discription;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
    public Timestamp getStatusChanged() {
        return statusChanged;
    }

    public void setStatusChanged(Timestamp statusChanged) {
        this.statusChanged = statusChanged;
    }
    
    public void PrintAll(){
        System.out.println("ID: " + ID + ", description: " + description + ", note: " + note + ", context: " + 
                context.getID() + " - " + context.getName() + ", status: " + status.getID() + " - " + status.getName() + ", project: "
                + project.getID() + " - " + project.getName() + " - " + project.getNote() + ", action date: " + datumtijd  + 
                ", status changed: " + statusChanged + ", done: " + done);
    }
           
    
}
