/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
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
    private String gcSynxType = OPTIONSGCSYNCTYPRVALUES[0];

    public static String gcLink = "https://www.google.com/calendar/feeds/default/owncalendars/full";
    public String gcPersonalURL = "";
    
    private Properties properties = new Properties();
    private String optionsPath = CURRENTDIR + PATHSEPARATOR + "gtd.properties";        
    
    public void LoadOptions(){
        // Read properties file.
        if(OptionsFileExists()){
            try {
                properties.load(new FileInputStream(optionsPath));
                setGCUsername(properties.getProperty("gcUsername"));
                setGCPassword(properties.getProperty("gcPassword"));
                try{
                    setLastOpenedScreen(MenuScreen.valueOf(properties.getProperty("LOS")));
                } catch (Exception ex){
                    setLastOpenedScreen(MenuScreen.MAIN);
                }
                
                //properties.getProperty("gcSyncType")
                setGcSynxType((Arrays.asList(OPTIONSGCSYNCTYPRVALUES).contains(properties.getProperty("gcSyncType"))) ? 
                        properties.getProperty("gcSyncType") : OPTIONSGCSYNCTYPRVALUES[0]);
                
                
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
            properties.setProperty("gcUsername", getGCUsername());
            properties.setProperty("gcPassword", getGCPassword());
            properties.setProperty("LOS", getLastOpenedScreen().name());
            properties.setProperty("gcSyncType",getGcSynxType());
            properties.store(new FileOutputStream(optionsPath), null);
        } catch (IOException e) {
            System.out.println(optionsPath);
            e.printStackTrace();
        }
    }
    
    public void CreateDefaultOptionsFile(){
            properties = new Properties();
            SaveOptions();

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
    
    public String getGCPassword() {
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
    
    public String getGcSynxType() {
        return gcSynxType;
    }

    public void setGcSynxType(String gcSynxType) {
        this.gcSynxType = gcSynxType;
    }
}
