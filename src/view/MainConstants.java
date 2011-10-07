/*
 * Algemene constanten die in heel de applicatie gebruikt worden
 */
package view;

import Model.Context;
import Model.Project;
import Model.Status;
import java.util.ArrayList;

/**
 *
 * @author Bram Klein
 */
public class MainConstants {
    public static final String MYSQL_SERVER = "databases.aii.avans.nl";
    public static final String MYSQL_USERNAME = "bklein";
    public static final String MYSQL_PASSWORD = "jF4TJid5";
    public static final String DB_NAME = "bklein_db2";
    
    public static final String TABLE_THOUGHT = "thought";
    public static final String TABLE_STATUS = "status";
    public static final String TABLE_CONTEXT = "context";
    public static final String TABLE_PROJECT = "project";
    public static final String TABLE_ACTION = "action";
    
    //als er meerdere acties in de database achter elkaar moeten worden uitgevoerd
    //dan moet ie niet steeds de verbinding sluiten
    //zet hem op false voor een reeks acties en daarna weer op true, dan houd ie de verbinding open
    public static Boolean CloseConnectionAfterDatabaseAction = true;
    
    
    //public static ArrayList<Status> statussen = new ArrayList<Status>();
    //public static ArrayList<Context> contexten = new ArrayList<Context>();
    //public static ArrayList<Project> projecten = new ArrayList<Project>();
    
    
}
