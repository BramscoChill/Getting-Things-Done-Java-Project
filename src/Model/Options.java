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
    
    private String dbServerName = "databases.aii.avans.nl";
    private String dbDatabaseName = "bklein_db2";
    private String dbUsername = "bklein";
    private String dbPassword = "jF4TJid5";
    
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
                
                setDbServerName(properties.getProperty("dbServerName"));
                setDbDatabaseName(properties.getProperty("dbDatabaseName"));
                setDbUsername(properties.getProperty("dbUsername"));
                setDbPassword(properties.getProperty("dbPassword"));
                
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
            } catch (Exception e){
                CreateDefaultOptionsFile();
            }
        } else {
            CreateDefaultOptionsFile();
        }
    }
    
    public void SaveOptions(){
        try {
            properties.setProperty("dbServerName", getDbServerName());
            properties.setProperty("dbDatabaseName", getDbDatabaseName());
            properties.setProperty("dbUsername", getDbUsername());
            properties.setProperty("dbPassword", getDbPassword());
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
    
    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
        MYSQL_PASSWORD = dbPassword;
    }

    public String getDbServerName() {
        return dbServerName;
    }

    public void setDbServerName(String dbServerName) {
        this.dbServerName = dbServerName;
        MYSQL_SERVER = dbServerName;
    }

    public String getDbUsername() {
        return dbUsername;
    }
    
    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
        MYSQL_USERNAME = dbUsername;
    }
    
    public String getDbDatabaseName() {
        return dbDatabaseName;
    }
    
    public void setDbDatabaseName(String dbDatabaseName) {
        this.dbDatabaseName = dbDatabaseName;
        DB_NAME = dbDatabaseName;
    }
    
    
}
