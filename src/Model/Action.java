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

    private int ID;
    private String discription;
    private String note;
    private Status status = null;
    private Context context = null;
    private Project project = null;
    private Timestamp datumtijd;
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

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
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
    
           
    
}
