/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import static view.MainConstants.*;

/**
 *
 * @author 
 */
public class Options {
    private String gmailUsername = "";
    private String gmailPassword = "";
    private MenuScreen lastOpenedScreen = MenuScreen.MAIN;
    
    private Properties properties = new Properties();
    private String optionsPath = CURRENTDIR + PATHSEPARATOR + "gtd.properties";        
    
    public enum MenuScreen{
        MAIN, THOUGHTS, OPTIONS, ACTIONS, HISTORY, PROJECTS
    }
    
    public void LoadOptions(){
        // Read properties file.
        if(OptionsFileExists()){
            try {
                properties.load(new FileInputStream(optionsPath));
                gmailUsername = properties.getProperty("gmailUsername");
                gmailPassword = properties.getProperty("gmailPassword");
                try{
                    lastOpenedScreen = MenuScreen.valueOf(properties.getProperty("LOS"));
                } catch (Exception ex){
                    lastOpenedScreen = MenuScreen.MAIN;
                }
                
                System.out.println("Sucessfully opnened options file! - " + optionsPath);
                //System.out.println("" + lastOpenedScreen.name());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            CreateDefaultOptionsFile();
        }
    }
    
    public void SaveOptions(){
        try {
            properties.setProperty("gmailUsername", gmailUsername);
            properties.setProperty("gmailPassword", gmailPassword);
            properties.setProperty("LOS", lastOpenedScreen.name());
            properties.store(new FileOutputStream(optionsPath), null);
        } catch (IOException e) {
            System.out.println(optionsPath);
            e.printStackTrace();
        }
    }
    
    public void CreateDefaultOptionsFile(){
        try {
            properties = new Properties();
            properties.setProperty("gmailUsername", gmailUsername);
            properties.setProperty("gmailPassword", gmailPassword);
            properties.setProperty("LOS", lastOpenedScreen.name());
            properties.store(new FileOutputStream(optionsPath), null);
        } catch (IOException e) {
            System.out.println(optionsPath);
            e.printStackTrace();
        }
    }
    
    private Boolean OptionsFileExists(){
        try {
            properties.load(new FileInputStream(optionsPath));
            properties.clear();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    public String getGmailPassword() {
        return gmailPassword;
    }

    public void setGmailPassword(String gmailPassword) {
        this.gmailPassword = gmailPassword;
    }

    public String getGmailUsername() {
        return gmailUsername;
    }

    public void setGmailUsername(String gmailUsername) {
        this.gmailUsername = gmailUsername;
    }

    public MenuScreen getLastOpenedScreen() {
        return lastOpenedScreen;
    }

    public void setLastOpenedScreen(MenuScreen lastOpenedScreen) {
        this.lastOpenedScreen = lastOpenedScreen;
    }
}
