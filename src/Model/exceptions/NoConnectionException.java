/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.exceptions;

/**
 * exception als er iets mis gaat met de verbinding maken of sluiten
 */
public class NoConnectionException extends DatabaseException {
    
    public NoConnectionException(){
        super();
    }
    public NoConnectionException(String msg){
        super(msg);
    }
}
