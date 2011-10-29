/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.exceptions;

/**
 *
 * @author Administrator
 */
public class GoogleCaptachaAuthenticationError extends Exception {
    public GoogleCaptachaAuthenticationError(){
        super();
        
    }
    public GoogleCaptachaAuthenticationError(String msg){
        super(msg);
    }
}
