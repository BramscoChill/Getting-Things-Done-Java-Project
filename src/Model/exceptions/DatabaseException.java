/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.exceptions;

/**
 *
 * @author Administrator
 */
public class DatabaseException extends Exception {
    String exception;
    
    public DatabaseException(){
        super();
        exception = "Unknown";
    }
    public DatabaseException(String msg){
        super(msg);
        this.exception = msg;
    }
    public String getException(){
        return this.exception;
    }

}
