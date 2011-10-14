/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.exceptions;


/**
 * Is ervoor om te zien bij welke functie hij een fout geeft in de klasse: DBhandler
 * zo kan je achterhalen bij het ophalen, verwijderen of toevoegen van een status, actie, contect, project of nameitem
 * waat het misgaat
 */
public class ThingsException extends DatabaseException {
    
    public Object getWhat = null;
    public ThingsWhat getAddDelete;
    public enum ThingsWhat {
    GET,ADD,DELETE
}
    
    public ThingsException(){
        super();
    }
    public ThingsException(String msg, Object getWhat, ThingsWhat getAddDelete){
        super(msg);
        this.getWhat = getWhat;
        this.getAddDelete = getAddDelete;
    }
    
    public Object GetObject(){
        return getWhat;
    }
    
}

