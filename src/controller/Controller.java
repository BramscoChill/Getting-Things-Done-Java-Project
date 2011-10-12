/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import view.ThoughtsFrame;
import com.google.gdata.util.ServiceException;
import java.io.IOException;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import model.GTDcomplete;
import view.OptionsFrame;
import view.MainMenuFrame;
import model.GoogleCalendar;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.JFrame;
import model.Options;

import static view.MainConstants.*;

/**
 * Houd alles bij van alle schermen. Is een soort van totaaloverzicht omdat alle schermen los van elkaar moeten werken
 * @author 
 */
public class Controller implements Observer {

    //private ThoughtsFrame thoughtsFrame = new ThoughtsFrame();
    private GTDcomplete gtd = new GTDcomplete();
    
    private GoogleCalendar gcTransferer = new GoogleCalendar();
    
    private MainMenuFrame mainMenuFrame;
    private OptionsFrame optionsMenuFrame;
    private ThoughtsFrame thoughtsFrame;
    
    public Controller(){
        //thoughtsFrame.setVisible(false);
        //mainMenu.setVisible(false);
        mainMenuFrame = new MainMenuFrame();
        
        //voiegt listeners toe
        AddMainMenuListeners();
        
        //laad alle opties in
        OPTIONS.LoadOptions();
        
        GoogleCalendar calen = new GoogleCalendar();
    }
    
    //voegt de actionlisteners toe aan het mainframe en andere JFrames
    private void AddMainMenuListeners(){
        JButton[] buttons = mainMenuFrame.GetButtons();
        //thoughts scherm buttons
        buttons[0].addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("PUSH DA BUTTON BUTTON 1");
                System.out.println("save button - gUser: " + OPTIONS.getGCUsername());
                thoughtsFrame = new ThoughtsFrame(gtd.GetAllThoughtsAsArray());
            }
        });
        
        buttons[1].addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("PUSH DA BUTTON BUTTON 2");
            }
        });
        
        buttons[2].addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("PUSH DA BUTTON BUTTON 2");
            }
        });
        
        buttons[3].addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("PUSH DA BUTTON BUTTON 3");
            }
        });
        
        //haalt het optionsmenuitem op uit de het mainMenu en koppelt hier weer een actionlistener aan
        mainMenuFrame.GetOptionsMenuItem().addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("menu opties3");
                //laat het options scherm en schakelt het hoofdscherm uit
                optionsMenuFrame = new OptionsFrame();
                mainMenuFrame.setEnabled(false);
                System.out.println("fap fap fap 1");
                
                //de listener voor het optionsMenu (Ander JFrame) om ervoor te zorgen
                //dat als het options scherm sluit, je het main scherm weer kan bewerken
                optionsMenuFrame.addWindowListener(new WindowAdapter(){
                   public void windowOpened( WindowEvent e ){
                        //field1.requestFocus();
                       System.out.println("fap fap fap 3");
                     }
                   public void windowClosing( WindowEvent e ){
                       mainMenuFrame.setEnabled(true);
                       optionsMenuFrame.dispose();
                       optionsMenuFrame = null;
                       mainMenuFrame.toFront();
                       
                   }
                });
                optionsMenuFrame.gcSyncActions.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e)
                    {
                        Object[] options = {"Ja","Nee"};
                        int n = JOptionPane.showOptionDialog(optionsMenuFrame,
                            "Weet u zeker dat u de acties met uw google kalender wilt synchroniseren?",
                            "Kalender Synchronisatie",
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,options,options[0]);
                        
                            if(n == 0){
                                System.out.println("Sync acties google calandar");
                            }
                    }
                });
                
                optionsMenuFrame.gcCheckConnectionDB.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e)
                    {
                                try{  
                                    if(gcTransferer.CheckCalendarExists()){
                                        optionsMenuFrame.SetErrorMessage("Connectie en kalender zijn orde!", false);
                                    } else {
                                        Object[] options = {"Ja","Nee"};
                                        int n = JOptionPane.showOptionDialog(optionsMenuFrame,
                                            "De kalender GTD is niet gevonden, "
                                            + "wilt u deze aanmaken?",
                                            "Kalender niet gevonden!",
                                            JOptionPane.YES_NO_CANCEL_OPTION,
                                            JOptionPane.QUESTION_MESSAGE,
                                            null,
                                            options,
                                            options[0]);
                                if(n == 0){
                                    //System.out.println("" + n);
                                    if(gcTransferer.CreateCalendar()){
                                        optionsMenuFrame.SetErrorMessage("Connectie en kalender zijn orde!", false);
                                    } else {
                                        optionsMenuFrame.SetErrorMessage("Fout bij het aanmaken van de kalender", true);
                                    }
                                } else {
                                    optionsMenuFrame.SetErrorMessage("Kalender niet aangemaakt!", true);
                                }
                                    }
                                } catch (IOException iOException) {
                                    iOException.printStackTrace();
                                    optionsMenuFrame.SetErrorMessage("FOUT: Controlleer uw verbinding!", true);
                                } catch (ServiceException serviceException) {
                                    if(serviceException instanceof com.google.gdata.util.InvalidEntryException){
                                        System.out.println("URL klopt niet waarmee verbinding wordt gemaakt!");
                                        optionsMenuFrame.SetErrorMessage("FOUT: Interne applicatie error 1024!", true);
                                    } else if(serviceException instanceof com.google.gdata.client.GoogleService.InvalidCredentialsException){
                                        System.out.println("Gebruikersnaam en/of wachtwoord klopt niet!");
                                        optionsMenuFrame.SetErrorMessage("FOUT: Controlleer uw gebruikersnaam en/of wachtwoord!", true);
                                    } else {
                                        //verbindingsproblemen
                                        optionsMenuFrame.SetErrorMessage("FOUT: Controlleer uw verbinding!", true);
                                    }
                                    serviceException.printStackTrace();
                                } catch (Exception ex){
                                    ex.printStackTrace();
                                    optionsMenuFrame.SetErrorMessage("FOUT: Controlleer uw verbinding!", true);
                                }

                    }
                });
                
                    }
                });

        
        //de exit knop uit het hoofdscherm
        mainMenuFrame.GetExitMenuItem().addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });
        

        
    }
    
    private void OpenWindow(MenuScreen windowType){
        //@TODO OpenWindow afmaken
        switch(windowType){
            case ACTIONS:
                break;
            case OPTIONS:
                break;
            case LASTOPENED:
                break;
            case THOUGHTS:
                break;
            case PROJECTS:
                break;
            case HISTORY:
                break;
            default: //MAIN
                break;
        }
    }
    
    private void CloseWindow(MenuScreen windowType){
        //@TODO CloseWindow afmaken
        switch(windowType){
            case ACTIONS:
                break;
            case OPTIONS:
                break;
            case LASTOPENED:
                break;
            case THOUGHTS:
                break;
            case PROJECTS:
                break;
            case HISTORY:
                break;
            default: //MAIN
                break;
        }
        
        //check all windows closed, exit app
    }
    
    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}

    
