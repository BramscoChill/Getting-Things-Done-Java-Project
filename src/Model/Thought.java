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
public class Thought {
    int ID = -1;
    private String note = "";
    private Timestamp timestamp;
    
    public Thought(int ID, String note, Timestamp timestamp){
        this(note,timestamp);
        SetID(ID);
    }
    
    public Thought(String note, Timestamp timestamp){
        this(note);
        SetTimestamp(timestamp);
    }
    
    public Thought(String note){
        SetNote(note);
    }
    
    public Thought(){
        this("");
    }
    
    public void SetNote(String note){
        this.note = note;
    }

    public String GetNote(){
        return note;
    }
    
    public void SetTimestamp(Timestamp timestamp){
        this.timestamp = timestamp;
    }
    
    public Timestamp GetTimestamp(){
        return timestamp;
    }
    
    public void SetID(int id){
        this.ID = id;
    }
    
    public int GetID(){
        return ID;
    }
    
    public void PrintThought(){
        System.out.println("ID: " + GetID() + ", thought: " + GetNote() + ", timestamp: " + GetTimestamp());
    }
}
