/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.exceptions;

import model.exceptions.DatabaseException;

/**
 *
 * Deze exception word opgegooit als de database niet klopt!
 */
public class WrongDatabaseException extends DatabaseException{
    
    public WrongDatabaseException(){
        super();
    }
    public WrongDatabaseException(String msg){
        super(msg);
    }
    
}
