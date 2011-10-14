/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.exceptions;

/**
 * exeption als het misgaat bij het uitvoeren van een query
 */
public class QueryException extends DatabaseException {
    
    public QueryException(){
        super();
    }
    public QueryException(String msg){
        super("Query: '" + msg + "' FAILED!");
    }
}
