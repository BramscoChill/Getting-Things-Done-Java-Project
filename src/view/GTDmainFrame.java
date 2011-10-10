/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import Model.GoogleCalendar;
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
public class GTDmainFrame implements Observer {

    //private ThoughtsFrame thoughtsFrame = new ThoughtsFrame();
    private MainMenuFrame mainMenuFrame;
    private OptionsFrame optionsMenuFrame;
    
    public GTDmainFrame(){
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
        buttons[0].addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("PUSH DA BUTTON BUTTON 1");
                System.out.println("save button - gUser: " + OPTIONS.getGClUsername());
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
                       mainMenuFrame.toFront();
                       optionsMenuFrame.dispose();
                       optionsMenuFrame = null;
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
