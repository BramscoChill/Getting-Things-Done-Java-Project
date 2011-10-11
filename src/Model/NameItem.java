/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Administrator
 */
public class NameItem {
    
    private int ID = -1;
    private String name;

    public NameItem(int id, String name){
        this(id);
        setName(name);
    }
    
    public NameItem(int id){
        setID(id);
    }
    public NameItem(){
        
    }
    
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void PrintAll(){
        System.out.println("ID: " + getID() + ", name: " + getName());
    }
    
}
