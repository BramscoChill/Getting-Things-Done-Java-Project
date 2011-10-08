/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.JFrame;
import model.Options;

/**
 *
 * @author Administrator
 */
public class GTDmainFrame implements Observer {

    //private ThoughtsFrame thoughtsFrame = new ThoughtsFrame();
    private MainMenuFrame mainMenuFrame = new MainMenuFrame();
    private OptionsFrame optionsMenuFrame = new OptionsFrame();
    private static Options opties = new Options();
    
    public GTDmainFrame(){
        //thoughtsFrame.setVisible(false);
        //mainMenu.setVisible(false);
        
        AddMainMenuListeners();
    }
    
    private void AddMainMenuListeners(){
        JButton[] buttons = mainMenuFrame.GetButtons();
        buttons[0].addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("PUSH DA BUTTON BUTTON 1");
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
        
        mainMenuFrame.GetOptionsMenuItem().addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("menu opties3");
            }
        });
        
        mainMenuFrame.GetExitMenuItem().addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("menu afsluiten");
            }
        });
        
        optionsMenuFrame.GetSaveButton().addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("save button");
            }
        });
        
    }
    
    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
