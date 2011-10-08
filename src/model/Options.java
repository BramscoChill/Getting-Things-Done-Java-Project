/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Administrator
 */
public class Options {
    private String gmailUsername;
    private String gmailPassword;
    private MenuScreen lastOpenedScreen;
    
            
    public enum MenuScreen{
        MAIN, THOUGHTS, OPTIONS
    }
}
