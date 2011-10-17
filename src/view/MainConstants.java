/*
 * Algemene constanten die in heel de applicatie gebruikt worden
 */
package view;

import model.Context;
import model.Project;
import model.Status;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import model.Options;

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
    
    public static final String MAINTITLE = "GTDne";
    public static final String MAINVERSION = "1.001";
    public static final String CURRENTDIR = System.getProperty("user.dir");
    public static final String PATHSEPARATOR = System.getProperty("file.separator");

    
    public static final String MAINMENUTITLE = MAINTITLE + " Main Menu - V" + MAINVERSION;
    public static final int MAINMENUBUTTONSMARGIN = 30;
    
    public static enum MenuScreen{MAIN, LASTOPENED, THOUGHTS, PROJECTS, ACTIONS, HISTORY, OPTIONS}
    public static final String[] OPTIONSSTARTUPSCREENVALUES = {"Hoofdscherm","Laatst Geopend","Gedachten", "Projecten","Acties","History"};
    public static final String[] OPTIONSGCSYNCTYPRVALUES = {"Alles","Alleen acties in het heden"};
    public static final HashMap<String,MenuScreen> OPTIONSMENUSCREENVALUES = new HashMap<String, MenuScreen>() {

        {
            put(OPTIONSSTARTUPSCREENVALUES[0], MenuScreen.MAIN);
            put(OPTIONSSTARTUPSCREENVALUES[1], MenuScreen.LASTOPENED);
            put(OPTIONSSTARTUPSCREENVALUES[2], MenuScreen.THOUGHTS);
            put(OPTIONSSTARTUPSCREENVALUES[3], MenuScreen.PROJECTS);
            put(OPTIONSSTARTUPSCREENVALUES[4], MenuScreen.ACTIONS);
            put(OPTIONSSTARTUPSCREENVALUES[5], MenuScreen.HISTORY);
        }

        ;
    };
    
    public static final ImageIcon PREVIOUSBUTTONIMAGEICON = new ImageIcon(MainConstants.class .getResource("/resources/buttonicons/Actions-go-previous-icon2.png"));
    public static final ImageIcon PLUSBUTTONIMAGEICON = new ImageIcon(MainConstants.class .getResource("/resources/buttonicons/Plus-icon-128.png"));
    public static final ImageIcon MINUSBUTTONIMAGEICON = new ImageIcon(MainConstants.class .getResource("/resources/buttonicons/Minus-icon-128.png"));
    public static final int PREVIOUSBUTTONSIZE = 55;
    
    public static final String OPTIONSMENUTITLE = MAINTITLE + " Options - V" + MAINVERSION;
    public static final int OPTIONSMENUMARGIN = 20;
    public static final Font OPTIONSMENUFONTTEXTFIELDS = new Font("Arial", Font.BOLD, 18);
    public static Options OPTIONS = new Options();
    
    public static final String ACTIONSMENUTITLE = MAINTITLE + " Acties - " + "V" + MAINVERSION;
    public static final int ACTIONSSMENUMARGIN = 30;
    public static final String ACTIONMENUTITLE = MAINTITLE + " Actie: ";
    
    public static final String PROJECTSMENUTITLE = MAINTITLE + " Projecten - " + "V" + MAINVERSION;
    
    public static final String HISTORYMENUTITLE = MAINTITLE + " Geschiedenis - " + "V" + MAINVERSION;
    
    public static final String THOUGHTSMENUTITLE = MAINTITLE + " Gedachten - " + "V" + MAINVERSION;
    public static final int THOUGHTSMENUMARGIN = 30;
    //als er meerdere acties in de database achter elkaar moeten worden uitgevoerd
    //dan moet ie niet steeds de verbinding sluiten
    //zet hem op false voor een reeks acties en daarna weer op true, dan houd ie de verbinding open
    public static Boolean CloseConnectionAfterDatabaseAction = true;
    
    public static final Font FONT = new Font("Arial", Font.PLAIN, 14);
    public static final Font FONTBUTTONS = new Font("Arial", Font.BOLD, 20);
    public static final Font FONTTITLE = new Font("Arial", Font.BOLD, 40);
    public static final Font UBERTABLEFONT = new Font("Arial", Font.PLAIN, 16);
    public static final Font UBERTABLEINPUTFONT = new Font("Arial", Font.BOLD, 18);
    public static final int UBERTABLEROWHEIGHT = 30;
    public static final Color BACKGROUND = new Color(200, 220, 230);
    public static final Boolean PRINTEMPTYTABLEROWS = false; //of de lege rijen in de UberTables ook gekleurt moeten worden
    public static final Color TABLEROWCOLOR = Color.GRAY; // new Color(200, 220, 230);
    public static final int HEIGHTOFCOMBOBOXESTABLE = 30;
    public static final int HEIGHTOFTABLEHEADER = 30;
    
    
    
    //public static ArrayList<Status> statussen = new ArrayList<Status>();
    //public static ArrayList<Context> contexten = new ArrayList<Context>();
    //public static ArrayList<Project> projecten = new ArrayList<Project>();
    
    public static ImageIcon ResizeImageToButton(ImageIcon imgIcon, int imageW, int imageH){
        int margin = 5;
        Image img = imgIcon.getImage();
        return new ImageIcon(img.getScaledInstance(imageW - margin, imageH - margin,  java.awt.Image.SCALE_SMOOTH));   
    }
    
    public static String MakeStringTimestampOfTimestamp(Timestamp stamp){
        String dd = (("" + stamp.getDate()).length() == 1) ? "0" + stamp.getDate() : "" + stamp.getDate();
        String mm = (("" + stamp.getMonth()).length() == 1) ? "0" + stamp.getMonth() : "" + stamp.getMonth();
        String yyyy = (("" + stamp.getYear()).length() == 1) ? "0" + stamp.getYear() : "" + stamp.getYear();
        
        String hh = (("" + stamp.getHours()).length() == 1) ? "0" + stamp.getHours() : "" + stamp.getHours();
        String min = (("" + stamp.getMinutes()).length() == 1) ? "0" + stamp.getMinutes() : "" + stamp.getMinutes();
        
        return dd + "-" + mm + "-" + yyyy + " " + hh + ":" + min;
    }
}
