/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author sjorsvanuden
 */
public class Project extends NameItem {
    
    private String note;
    
    public Project(int id, String name, String note){
        super(id, name);
        setNote(note);
    }
    
    public Project(int id, String name){
        super(id, name);
    }
    
    public Project(int id){
        super(id);
    }
    
    public Project(){
        super();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    
    @Override
    public void PrintAll(){
        System.out.println("ID: " + getID() + ", name: " + getName() + ", note: " + getNote());
    }
    
}
