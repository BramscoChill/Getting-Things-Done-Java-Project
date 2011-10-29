/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import javax.swing.JPasswordField;
import model.exceptions.ThingsException;
import model.exceptions.GoogleCaptachaAuthenticationError;
import java.awt.Component;
import com.google.gdata.util.ServiceException;
import java.io.IOException;
import javax.swing.JDialog;
import java.sql.SQLException;
import model.exceptions.WrongDatabaseException;
import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.GoogleCalendar;
import java.util.Map;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Dimension;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import javax.swing.JButton;
import model.Options;
import java.util.Random;
import javax.swing.JFrame;

import model.exceptions.DatabaseException;
import static view.MainConstants.*;
import static javaapplication5.daMain.*; 
        
/**
 *
 * @author Administrator
 */
public class OptionsFrame extends JFrame {
    
    private GoogleCalendar gcTransferer = new GoogleCalendar();
    private JLabel loadingLabel;
    
    public JButton gcSyncActions,gcCheckConnectionDB, previousButton;
    
    private JButton saveButton, cancelButton;
    private JLabel optionsTitle, googleUsernameLBL, googlePasswordLBL, startupScreenLBL, gcSyncOptionsLBL, errorMessageLBL;
    private JTextField googleUsername, googlePassword;
    private JComboBox startupScreen, gcSyncOptions;
    
    private JLabel dbUsernameLBL, dbPasswordLBL, dbServerNameLBL, dbDatabaseNameLBL;
    private JTextField dbUsernameTXT, dbPasswordTXT, dbServerNameTXT, dbDatabaseNameTXT;
    private JButton dbCheckBTN;
    
    private JCheckBox showPasswordsCHECK;
    
    //om ervoor te zorgen dat alle instellingen opgeslagen worden als het scherm gesloten wordt
    private Boolean doSave = true;
    
    public OptionsFrame(){
        super(OPTIONSMENUTITLE);
        //this.setTitle(OPTIONSMENUTITLE);
        setLayout(null);
        this.setResizable(true);
        setBounds(100,new Random().nextInt(200)+50,700,399);
        
        setVisible(true);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        SetLoadingLabel();
        
        AddComponents();
        
        AddListeners(); 
        
        //1ex update, laat alles dus zien
        UpdateScreenBounds();
        
        LoadOptions();
        
    }
    
    private void AddComponents(){
        setMinimumSize(new Dimension(600,600));
        //setMaximumSize(new Dimension(900,500));
        
        saveButton = new JButton("Opslaan");
        cancelButton = new JButton("Annuleren");
        gcCheckConnectionDB = new JButton("Test Verbinding en Kalender");
        gcSyncActions = new JButton("Synchroniseer Acties");
        gcSyncOptionsLBL = new JLabel("Google Calandar synchronisatie type:");
        errorMessageLBL = new JLabel();
        errorMessageLBL.setFont(FONTBUTTONS);
//        errorMessageLBL.setOpaque(true);
//        errorMessageLBL.setBackground(Color.YELLOW);
        errorMessageLBL.setForeground(Color.RED);
        gcSyncOptions = new JComboBox();
        gcSyncOptions.setBackground(Color.WHITE);
        previousButton = new JButton();
        googleUsername = new JTextField();
        googleUsername.setFont(OPTIONSMENUFONTTEXTFIELDS);
        googlePassword = new JPasswordField();
        googlePassword.setFont(OPTIONSMENUFONTTEXTFIELDS);
        ((JPasswordField)googlePassword).setEchoChar('*');
        startupScreen = new JComboBox();
        startupScreen.setBackground(Color.WHITE);
        
        for(int i = 0; i < OPTIONSSTARTUPSCREENVALUES.length; i++){
            startupScreen.addItem(OPTIONSSTARTUPSCREENVALUES[i]);
        }
        
        for(int i = 0; i < OPTIONSGCSYNCTYPRVALUES.length; i++){
            gcSyncOptions.addItem(OPTIONSGCSYNCTYPRVALUES[i]);
        }
        
        startupScreen.setSelectedIndex(0);
        gcSyncOptions.setSelectedIndex(0);
        
        optionsTitle = new JLabel("Opties",JLabel.CENTER);
        optionsTitle.setFont(FONTTITLE);
        //optionsTitle.setBackground(Color.GREEN);
        //optionsTitle.setOpaque(true);
        googleUsernameLBL = new JLabel("Google Gebruikersnaam (zonder @gmail.com)");
//        googleUsernameLBL.setBackground(Color.GREEN);
//        googleUsernameLBL.setOpaque(true);
        googlePasswordLBL = new JLabel("Google Wachtwoord");
        startupScreenLBL = new JLabel("Opstart scherm");
        
        dbServerNameLBL = new JLabel("Database Server Naam");
        dbServerNameLBL.setFont(FONTBUTTONS);
        dbServerNameTXT = new JTextField();
        dbServerNameTXT.setFont(FONTBUTTONS);
        
        dbUsernameLBL = new JLabel("Database Gebruikersnaam");
        dbUsernameLBL.setFont(FONTBUTTONS);
//        dbUsernameLBL.setOpaque(true);
//        dbUsernameLBL.setBackground(Color.GREEN);
        
        dbPasswordLBL = new JLabel("Database Wachtwoord");
        dbPasswordLBL.setFont(FONTBUTTONS);
        dbUsernameTXT = new JTextField();
        dbUsernameTXT.setFont(FONTBUTTONS);
        dbPasswordTXT = new JPasswordField();
        ((JPasswordField)dbPasswordTXT).setEchoChar('*');
        //((JPasswordField)dbPasswordTXT).setEchoChar((char)0);
        dbPasswordTXT.setFont(FONTBUTTONS);
        
        dbDatabaseNameLBL = new JLabel("Database Naam");
        dbDatabaseNameLBL.setFont(FONTBUTTONS);
        dbDatabaseNameTXT = new JTextField();
        dbDatabaseNameTXT.setFont(FONTBUTTONS);
        
        dbCheckBTN = new JButton("Check Database en Tabellen");
        
        showPasswordsCHECK = new JCheckBox("Laat wachtwoorden zien");
        showPasswordsCHECK.setFont(FONTBUTTONS);
        showPasswordsCHECK.setSelected(false);
        
        add(optionsTitle);
        add(googleUsernameLBL);
        add(googlePasswordLBL);
        add(startupScreenLBL);
        add(saveButton);
        add(cancelButton);
        add(gcCheckConnectionDB);
        add(gcSyncActions);
        add(previousButton);
        add(googleUsername);
        add(googlePassword);
        add(startupScreen);
        add(gcSyncOptionsLBL);
        add(gcSyncOptions);
        add(errorMessageLBL);
        add(dbServerNameLBL);
        add(dbServerNameTXT);
        add(dbUsernameLBL);
        add(dbUsernameTXT);
        add(dbPasswordLBL);
        add(dbPasswordTXT);
        add(dbCheckBTN);
        add(dbDatabaseNameLBL);
        add(dbDatabaseNameTXT);
        add(showPasswordsCHECK);
        previousButton.setIcon(PREVIOUSBUTTONIMAGEICON); // NOI18N
    }
    
    private void UpdateScreenBounds(){
                float margin = OPTIONSMENUMARGIN;
                
                double frameW = getBounds().getWidth();
                double frameH = getBounds().getHeight();
                
                double posX = margin;
                double posY = margin;
                
                double bW = (frameW / 2) - (margin * 2);
                double bH = (frameH / 2) - (margin * 2);
                
                int labelsHeight = 30;
                int labelsWidth = 300;
                
                int bHeight = 30;
                int bWidth = 100;
                
                
                int previousButXpos = (int) (frameW - margin - PREVIOUSBUTTONSIZE);
                previousButton.setBounds(previousButXpos - 5, (int)(margin/2), PREVIOUSBUTTONSIZE, PREVIOUSBUTTONSIZE);
                
                //googleUsername.setBounds(null);
                
                int optionsTitleW = 300;
                int optionsTitleH = 50;
                int optionsX = (int)(((frameW/2)) - (optionsTitleW/2));
                optionsTitle.setBounds(optionsX,(int)margin,optionsTitleW,optionsTitleH);
                
                int gULx = (int) margin;
                int textfieldsWith = (int)((frameW/2) - margin - margin);
                googleUsernameLBL.setBounds(gULx,(int) (optionsTitle.getLocation().y + margin + labelsHeight),labelsWidth, labelsHeight);
                googleUsername.setBounds(gULx,(int) (googleUsernameLBL.getLocation().y + labelsHeight),textfieldsWith, labelsHeight);
                googlePasswordLBL.setBounds(gULx,(int) (googleUsername.getLocation().y + margin + labelsHeight),labelsWidth, labelsHeight);
                googlePassword.setBounds(gULx,(int) (googlePasswordLBL.getLocation().y + labelsHeight),textfieldsWith, labelsHeight);
                
                
                
                startupScreenLBL.setBounds(gULx,(int) (googlePassword.getLocation().y + margin + labelsHeight),labelsWidth, labelsHeight);
                startupScreen.setBounds(gULx,(int) (startupScreenLBL.getLocation().y + labelsHeight),(int)(labelsWidth / 1.5), labelsHeight);
                
                showPasswordsCHECK.setBounds((int)(textfieldsWith + (margin * 2)),(int) (startupScreen.getLocation().y),textfieldsWith, labelsHeight);
                
                gcCheckConnectionDB.setBounds((int)(textfieldsWith + (margin * 2)),(int) (googlePassword.getLocation().y),250, labelsHeight);
                gcSyncActions.setBounds((int)(textfieldsWith + (margin * 2)),(int) (gcCheckConnectionDB.getLocation().y - labelsHeight - (margin / 2)),250, labelsHeight);
                gcSyncOptionsLBL.setBounds((int)(textfieldsWith + (margin * 2)),(int) (googleUsernameLBL.getLocation().y),250, labelsHeight);
                gcSyncOptions.setBounds((int)(textfieldsWith + (margin * 2)),(int) (googleUsernameLBL.getLocation().y + labelsHeight),250, labelsHeight);
                
                saveButton.setBounds((int)(frameW - bWidth - bWidth - margin - margin),(int) (frameH - (bHeight * 2.5)),bWidth, bHeight);
                cancelButton.setBounds((int)(frameW - bWidth - margin - 5),(int) (frameH - (bHeight * 2.5)),bWidth,bHeight);
                //System.out.println(" --- Resized " + "fW: " + frameW + "fH: " + frameH + ", optX: " + optionsX);  
                
                dbServerNameLBL.setBounds((int)(gULx),(int) (startupScreen.getLocation().getY() + margin + startupScreen.getSize().getHeight()),(int)(textfieldsWith),bHeight);
                dbServerNameTXT.setBounds((int)(gULx),(int) (dbServerNameLBL.getLocation().getY() + startupScreen.getSize().getHeight()),(int)(textfieldsWith),bHeight);
                
                dbDatabaseNameLBL.setBounds((int)(dbServerNameLBL.getBounds().getX() + dbServerNameLBL.getSize().getWidth() + margin),(int) (dbServerNameLBL.getLocation().getY()),(int)(textfieldsWith),bHeight);
                dbDatabaseNameTXT.setBounds((int)(dbDatabaseNameLBL.getBounds().getX()),(int) (dbDatabaseNameLBL.getLocation().getY() + dbDatabaseNameLBL.getSize().getHeight()),(int)(textfieldsWith),bHeight);
                        
                dbUsernameLBL.setBounds((int)(gULx),(int) (dbServerNameTXT.getLocation().getY() + margin + dbServerNameTXT.getSize().getHeight()),(int)(textfieldsWith),bHeight);
                dbUsernameTXT.setBounds((int)(gULx),(int) (dbUsernameLBL.getLocation().getY() + dbUsernameLBL.getSize().getHeight()),(int)(textfieldsWith),bHeight);
                
                dbPasswordLBL.setBounds((int)(dbUsernameLBL.getBounds().getX() + dbUsernameLBL.getSize().getWidth() + margin),(int) (dbUsernameLBL.getLocation().getY()),(int)(textfieldsWith),bHeight);
                dbPasswordTXT.setBounds((int)(dbPasswordLBL.getBounds().getX()),(int) (dbPasswordLBL.getLocation().getY() + dbPasswordLBL.getSize().getHeight()),(int)(textfieldsWith),bHeight);
                
                dbCheckBTN.setBounds((int)(gULx),(int) (dbPasswordTXT.getLocation().getY() + dbPasswordTXT.getSize().getHeight() + margin),(int)(frameW - (margin * 3)),bHeight);
                        
                errorMessageLBL.setBounds((int)(gULx),(int) (saveButton.getLocation().getY()),(int)(frameW - (frameW - (saveButton.getLocation().getX() - gULx - margin))),bHeight);
                
                
                //zit een bug in setMaximumSize, dit is de workarround
//                Robot robbie;
//                try
//                {
//                    robbie = new Robot() ;
//                }
//                catch (AWTException ex) // not supported on all platforms
//                {
//                    robbie = null ;
//                }
//                Point loc = this.getLocationOnScreen();
//                Point mouse = MouseInfo.getPointerInfo().getLocation();
//                int MAX_X = (int)this.getMaximumSize().getWidth();
//                int MAX_Y = (int)this.getMaximumSize().getHeight();
//                                    
//                if (robbie != null){
//                   robbie.mouseMove( (int)Math.min( mouse.getX(),loc.getX()+MAX_X ),
//                                 (int)Math.min( mouse.getY(),loc.getY()+MAX_Y ) ) ;
//                   setSize( Math.min(MAX_X, getWidth()), 
//                          Math.min(MAX_Y, getHeight()) );
//                }else{
//                   setSize( Math.min(MAX_X, getWidth()), 
//                          Math.min(MAX_Y, getHeight()) );
//                }
//                
//                revalidate();
    }
    
    private void AddListeners(){
        this.addComponentListener(new ComponentAdapter(){
            public void componentHidden(ComponentEvent e) {
                //System.out.println(e.getComponent().getClass().getName() + " --- Hidden");
            }

            public void componentMoved(ComponentEvent e) {
                //System.out.println(e.getComponent().getClass().getName() + " --- Moved");
            }

            public void componentResized(ComponentEvent e) {
                //System.out.println(e.getComponent().getClass().getName() + " --- Resized ");  
                UpdateScreenBounds();
                

                                 
            }

            public void componentShown(ComponentEvent e) {
                //System.out.println(e.getComponent().getClass().getName() + " --- Shown");

            }
        });
        
        
        dbCheckBTN.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                ( new Thread() {
                    public void run() {
                        DoLoading(true);
                        DoValidateDatabase();
                        DoLoading(false);
                    }
                }).start();
                
            }
        });
        
        saveButton.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                doSave = true;
                DoExit();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                doSave = false;
                DoExit();
            }
        });

        gcSyncActions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                DoSynchActions();
            }
        });
                
        //knop uit het opties menu om de verbinding met google calander te controleren
        gcCheckConnectionDB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                ( new Thread() {
                    public void run() {
                        DoLoading(true);
                        DoCheckConnectionGC();
                        DoLoading(false);
                    }
                }).start();

            }
        });
        
        showPasswordsCHECK.addItemListener(new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
            //System.out.println("Checked? " + showPasswordsCHECK.isSelected());
            if(showPasswordsCHECK.isSelected()){
                ((JPasswordField)dbPasswordTXT).setEchoChar((char)0);
                ((JPasswordField)googlePassword).setEchoChar((char)0);
            } else {
                ((JPasswordField)dbPasswordTXT).setEchoChar('*');
                ((JPasswordField)googlePassword).setEchoChar('*');
            }
        }
        });

        
    }
    
    //sluit dit scherm af en slaat van te voren alles op als dat moet
    public void DoExit(){
        if(doSave){
            SetOptionsValues();
            OPTIONS.SaveOptions(); //slaat de opties op voor het scherm gesloten word
        }
        setVisible(false);
        this.processWindowEvent( new WindowEvent(this, WindowEvent.WINDOW_CLOSING) );
        //dispose();
    }
    
    //knalt alle waardes uit het scherm in de opties
    private void SetOptionsValues(){
       OPTIONS.setGCUsername(googleUsername.getText());
       OPTIONS.setGCPassword(googlePassword.getText());
       OPTIONS.setDbServerName(dbServerNameTXT.getText());
       OPTIONS.setDbDatabaseName(dbDatabaseNameTXT.getText());
       OPTIONS.setDbUsername(dbUsernameTXT.getText());
       OPTIONS.setDbPassword(dbPasswordTXT.getText());
       
       //System.out.println(OPTIONSMENUSCREENVALUES.get((String)startupScreen.getSelectedItem()));
       OPTIONS.sePrefferedOpenedScreen(OPTIONSMENUSCREENVALUES.get((String)startupScreen.getSelectedItem()));
       OPTIONS.setGcSynxType((String)gcSyncOptions.getSelectedItem());
    }
    
    private void LoadOptions(){
        googleUsername.setText(OPTIONS.getGCUsername());
        googlePassword.setText(OPTIONS.getGCPassword());
        dbServerNameTXT.setText(OPTIONS.getDbServerName());
        dbDatabaseNameTXT.setText(OPTIONS.getDbDatabaseName());
        dbUsernameTXT.setText(OPTIONS.getDbUsername());
        dbPasswordTXT.setText(OPTIONS.getDbPassword());
        
        for (Map.Entry<String,MenuScreen> entry : OPTIONSMENUSCREENVALUES.entrySet()) {
            if(entry.getValue() == OPTIONS.getPrefferedOpenedScreen()){
                startupScreen.setSelectedItem(entry.getKey());
                //System.out.println("selected item: " + OPTIONS.getPrefferedOpenedScreen().name() + ", " + OPTIONSMENUSCREENVALUES.get(entry.getKey()));
                break;
            }
        }
        gcSyncOptions.setSelectedItem(OPTIONS.getGcSynxType());
    }
    
    private void DoValidateDatabase() {
        try {
            controller.GetModel().ValidateDatabase();
            SetErrorMessage("Database en rechten zijn in orde!", false);
            //MessageBox.DoOkErrorMessageBox((JFrame)(this.getContentPane()), "ERROR in Database!", "Database is niet valide of de rechten zijn niet in orde! Maak de database met het bijbehorend SQL bestand opnieuw aan!");
        } catch (WrongDatabaseException ex){
            //JFrame newFrame = (JFrame)this.getContentPane();
            SetErrorMessage("Database is niet valide of de rechten zijn niet in orde! Maak de database met het bijbehorend SQL bestand opnieuw aan!", true);
            //MessageBox.DoOkErrorMessageBox((JFrame)(this.getContentPane()), "ERROR in Database!", "Database is niet valide of de rechten zijn niet in orde! Maak de database met het bijbehorend SQL bestand opnieuw aan!");
            //System.out.println();
        } catch (DatabaseException ex) {
            SetErrorMessage("Database is niet valide of de rechten zijn niet in orde!", true);
            //MessageBox.DoOkErrorMessageBox(this, "ERROR in Database!", "De rechten zijn niet in orde!");
        } catch (SQLException ex) {
            SetErrorMessage("Kon geen verbinding met de database maken, check je verbinding / server / gebruikersnaam / wachtwoord!", true);
            //MessageBox.DoOkErrorMessageBox(this, "ERROR in Database!", "Kon geen verbinding met de database maken, check je verbinding / server / gebruikersnaam / wachtwoord!");
        }
    }
    
    private void DoCheckConnectionGC(){
        try{  
        if(gcTransferer.CheckCalendarExists()){
            SetErrorMessage("Connectie en kalender zijn orde!", false);
        } else {
            int n = DoYesNoQuestionMessage("Kalender niet gevonden!",
                "De kalender GTD is niet gevonden, "
                + "wilt u deze aanmaken?");
        if(n == 0){
            //System.out.println("" + n);
            if(gcTransferer.CreateCalendar()){
                SetErrorMessage("Connectie en kalender zijn orde!", false);
            } else {
                SetErrorMessage("Fout bij het aanmaken van de kalender", true);
            }
        } else {
            SetErrorMessage("Kalender niet aangemaakt!", true);
        }
        }
    } catch (IOException iOException) {
        iOException.printStackTrace();
        SetErrorMessage("FOUT: Controlleer uw verbinding!", true);
    } catch (ServiceException serviceException) {
        if(serviceException instanceof com.google.gdata.util.InvalidEntryException){
            System.out.println("URL klopt niet waarmee verbinding wordt gemaakt!");
            SetErrorMessage("FOUT: Interne applicatie error 1024!", true);
        } else if(serviceException instanceof com.google.gdata.client.GoogleService.InvalidCredentialsException){
            System.out.println("Gebruikersnaam en/of wachtwoord klopt niet!");
            SetErrorMessage("FOUT: Controlleer uw gebruikersnaam en/of wachtwoord!", true);
        } else {
            //verbindingsproblemen
            SetErrorMessage("FOUT: Controlleer uw verbinding!", true);
        }
        serviceException.printStackTrace();
    } catch (GoogleCaptachaAuthenticationError ex){
        SetErrorMessage(ex.getMessage(), true);
    } catch (Exception ex){
        ex.printStackTrace();
        SetErrorMessage("FOUT: Controlleer uw verbinding!", true);
    }
    }
    
    
    private void DoSynchActions(){
        
        //gcTransferer.GettAllActionEntrys();
        //gcTransferer.InsertActions(LoadAllActions()[0]);
    ( new Thread() {
 
        public void run() {
            DoLoading(true);
            gcTransferer.InsertActions(LoadAllActions(), (String)gcSyncOptions.getSelectedItem());
            SetErrorMessage("Alle acties zijn in google calandar gezet!", false);
            DoLoading(false);
        }
        }
        ).start();
        
    }
    public void SetErrorMessage(String txt, Boolean isError){
        errorMessageLBL.setText(txt);
        errorMessageLBL.setForeground((isError) ? Color.RED : new Color(1,153,1));
    }
    
    private int DoYesNoQuestionMessage(String title, String message){
        return MessageBox.DoYesNoQuestionMessage(this, title, message);
    }
    
    private void DoErrorCurrentScreen(String title, String message){
        MessageBox.DoOkErrorMessageBox(this, title, message);
    }
    
    //zet de loading tabel alvast neer, zodat andere shit later geladen kan worden
    private void SetLoadingLabel() {
        loadingLabel = MainConstants.SetLoadingTable((int)getBounds().getWidth(), (int)getBounds().getHeight());
        loadingLabel.setVisible(false);
        add(loadingLabel);
    }
    
    private void DoLoading(Boolean isLoading){
        if(isLoading){
            for(Component c : this.getContentPane().getComponents()){
                c.setVisible(false);
            }
            setEnabled(false);
            loadingLabel.setVisible(true);
        } else {
            for(Component c : this.getContentPane().getComponents()){
                c.setVisible(true);
            }
            setEnabled(true);
            loadingLabel.setVisible(false);
        }
    }
    
    private model.Action[] LoadAllActions(){

            DoLoading(true);
            //LoadContextsStatusesProjects();
            try {
                CloseConnectionAfterDatabaseAction = false;
                controller.GetModel().SetAllActions();
                CloseConnectionAfterDatabaseAction = true;
                    //controller.GetModel().GetAllActionsDoneAsArray();
                return controller.GetModel().GetAllActionsNotDoneAsArray();

                

                
            } catch (ThingsException ex) {
            ex.printStackTrace();
                DoErrorCurrentScreen("FOUT: laden Projecten, Contexten en Statussen!",
                "FOUT BIJ HET OPSLAAN VAN DE laden Projecten, Contexten en Statussen, verbinding is in orde,"
                + "\n Meuk kon niet opgehaald worden van de database!\nDit scherm zal nu sluiten!");
                DoExit();
                //this.processWindowEvent( new WindowEvent(this, WindowEvent.WINDOW_CLOSING) );
            } catch (DatabaseException ex) {
                ex.printStackTrace();
                DoErrorCurrentScreen("FOUT: laden Projecten, Contexten en Statussen!",
                "FOUT BIJ HET LADEN VAN DE laden Projecten, Contexten en Statussen, \ncontrolleer de verbinding!\nDit scherm zal nu sluiten!");
                DoExit();
            //this.processWindowEvent( new WindowEvent(this, WindowEvent.WINDOW_CLOSING) );
            }

            return null;
    }

}
