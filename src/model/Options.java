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
    private String gcUsername = "";
    private String gcPassword = "";
    private MenuScreen lastOpenedScreen = MenuScreen.MAIN;
    public static String gcLink = "https://www.google.com/calendar/feeds/default/owncalendars/full";
    public String gcPersonalURL = "";
    
    private Properties properties = new Properties();
    private String optionsPath = CURRENTDIR + PATHSEPARATOR + "gtd.properties";        
    
    public void LoadOptions(){
        // Read properties file.
        if(OptionsFileExists()){
            try {
                properties.load(new FileInputStream(optionsPath));
                gcUsername = properties.getProperty("gcUsername");
                gcPassword = properties.getProperty("gcPassword");
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
            properties.setProperty("gcUsername", gcUsername);
            properties.setProperty("gcPassword", gcPassword);
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
            properties.setProperty("gcUsername", gcUsername);
            properties.setProperty("gcPassword", gcPassword);
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
    
    public String getGClPassword() {
        return gcPassword;
    }

    public void setGCPassword(String gmailPassword) {
        this.gcPassword = gmailPassword;
    }

    public String getGCUsername() {
        return gcUsername;
    }

    public void setGCUsername(String gmailUsername) {
        this.gcUsername = gmailUsername;
        //System.out.println("gcUuser: " + gmailUsername);
    }

    public MenuScreen getLastOpenedScreen() {
        return lastOpenedScreen;
    }

    public void setLastOpenedScreen(MenuScreen lastOpenedScreen) {
        this.lastOpenedScreen = lastOpenedScreen;
    }
    
    public String getGcPersonalURL() {
        return gcPersonalURL;
    }

    public void setGcPersonalURL(String gcPersonalURL) {
        this.gcPersonalURL = gcPersonalURL;
    }
}
