/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.event.WindowEvent;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;
import java.awt.Dimension;
import java.util.Random;
import Model.TableMeuk.UberTablePanel;
import model.Thought;
import javax.swing.JFrame;

import model.exceptions.DatabaseException;
import model.exceptions.ThingsException;
import static view.MainConstants.*;
import static controller.Main.*;

/**
 *
 * @author Administrator
 */
public class ThoughtsFrame extends JFrame {
    private UberTablePanel tablePanel;
    private JButton saveThoughtBTN, newThoughtBTN, deleteThoughtBTN;
    private JTextArea thoughtNoteTXT;
    private JLabel thoughtNoteLBL;
    
    private JLabel loadingLabel;
    
    public JButton btnOpenActionFrame;
    
    private Thought currentSelectedThought = null;
    
    public ThoughtsFrame(Thought[] thoughts){
        super(THOUGHTSMENUTITLE);
        setLayout(null);
        this.setResizable(true);
        setLocation(100,new Random().nextInt(200)+50);
        setMinimumSize(new Dimension(700,600));
        
        setVisible(true);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        //******************************//
        //************TEST**************//
        setBounds(100,100,800,700);
        
        //SetLoadingTable();
        
        tablePanel = new UberTablePanel(thoughts,300,300);
        AddComponents();
        
        AddListeners();

        UpdateScreenBounds();
    }
    
    private void AddComponents(){
        saveThoughtBTN = new JButton("Opslaan");
        saveThoughtBTN.setFont(FONTBUTTONS);
        saveThoughtBTN.setEnabled(false);
        newThoughtBTN = new JButton("Nieuwe Gedachte");
        newThoughtBTN.setFont(FONTBUTTONS);
        deleteThoughtBTN = new JButton("Wis");
        deleteThoughtBTN.setFont(FONTBUTTONS);
        deleteThoughtBTN.setEnabled(false);
        thoughtNoteTXT = new JTextArea();
        thoughtNoteTXT.setFont(UBERTABLEINPUTFONT);
        thoughtNoteTXT.setBorder(BorderFactory.createLineBorder(Color.black));
        thoughtNoteTXT.setLineWrap(true);
        thoughtNoteTXT.setWrapStyleWord(true);

        thoughtNoteLBL = new JLabel("Gedachte:");
        thoughtNoteLBL.setFont(FONTBUTTONS);
        
        btnOpenActionFrame = new JButton("Open Acties Scherm");
        deleteThoughtBTN.setFont(FONTBUTTONS);
//        thoughtNoteLBL.setOpaque(true);
//        thoughtNoteLBL.setBackground(Color.GREEN);
        
        add(thoughtNoteLBL);
        add(saveThoughtBTN);
        add(deleteThoughtBTN);
        add(newThoughtBTN);
        add(thoughtNoteTXT);
        add(tablePanel);
        add(btnOpenActionFrame);
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
        
        saveThoughtBTN.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                //zet de huidige tekst in de notebox naar de huidige gedachte
                SetCurrentThoughtToThoughtNote();
                //slaat hem op
                SaveCurrentThought();
                //stelt een nieuwe gedachte in
                //DoNewThought();
            }
        });
        
        //maak een nieuwe gedachte aan
        newThoughtBTN.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                DoNewThought();
                
            }
        });
        
        deleteThoughtBTN.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                DoDeleteThought();
            }
        });
        
        //action listener om aan te geven dat de selectie van de tabel veranderd is
        //word aangeroepen vannuit het tablePanel
//        tablePanel.selectionChangedTableDoubleClick = new ActionListener() {
// 
//            public void actionPerformed(ActionEvent e)
//            {
//                currentSelectedThought = (tablePanel.getSelectedObject() instanceof Thought) ? 
//                        (Thought)tablePanel.getSelectedObject() : null;
//                SetThoughtNoteToCurrentThought();
//                //als er een bestaande gedachte geselecteerd is, kan die opgeslagen worden
//                saveThoughtBTN.setEnabled(true);
//                //als er een bestaande gedachte geselecteerd is, dan kan die gewist worden
//                deleteThoughtBTN.setEnabled(true);
//            }
//        };
        
        tablePanel.selectionChangedTableOneClick = new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                currentSelectedThought = (tablePanel.getSelectedObject() instanceof Thought) ? 
                        (Thought)tablePanel.getSelectedObject() : null;
                SetThoughtNoteToCurrentThought();
                SetButtonsNew();
            }
        };
    }
    
    private void UpdateScreenBounds(){
        int btnHeight = 50;
        tablePanel.UpdateSize(THOUGHTSMENUMARGIN,THOUGHTSMENUMARGIN,(int)(this.getSize().getWidth() - (2.5 * THOUGHTSMENUMARGIN)), 
                (int)(this.getSize().getHeight() - 440));
        thoughtNoteLBL.setBounds(THOUGHTSMENUMARGIN,(int)(tablePanel.getLocation().getY() + tablePanel.getSize().getHeight() + (THOUGHTSMENUMARGIN / 2)),
                (int)(this.getSize().getWidth() - (2.5 * THOUGHTSMENUMARGIN)),30);
        thoughtNoteTXT.setBounds(THOUGHTSMENUMARGIN,(int)(thoughtNoteLBL.getLocation().getY() + thoughtNoteLBL.getSize().getHeight()),
                (int)(this.getSize().getWidth() - (2.5 * THOUGHTSMENUMARGIN)),100);
        saveThoughtBTN.setBounds(THOUGHTSMENUMARGIN,(int)(thoughtNoteTXT.getLocation().getY() + thoughtNoteTXT.getSize().getHeight() + THOUGHTSMENUMARGIN),
                (int)((this.getSize().getWidth() / 2) - (2.5 * THOUGHTSMENUMARGIN)),btnHeight);
        newThoughtBTN.setBounds((int)(saveThoughtBTN.getLocation().getX() + saveThoughtBTN.getSize().getWidth() +
                THOUGHTSMENUMARGIN),
                (int)(thoughtNoteTXT.getLocation().getY() + thoughtNoteTXT.getSize().getHeight() + THOUGHTSMENUMARGIN),
                (int)((this.getSize().getWidth() - (saveThoughtBTN.getLocation().getX() + saveThoughtBTN.getSize().getWidth() +
                THOUGHTSMENUMARGIN)) - (1.5 * THOUGHTSMENUMARGIN)),btnHeight);
        deleteThoughtBTN.setBounds(THOUGHTSMENUMARGIN,(int)(newThoughtBTN.getLocation().getY() + 
                newThoughtBTN.getSize().getHeight() + (THOUGHTSMENUMARGIN / 2)),
                (int)((this.getSize().getWidth()) - (2.5 * THOUGHTSMENUMARGIN)),btnHeight);
        btnOpenActionFrame.setBounds(THOUGHTSMENUMARGIN,(int)(deleteThoughtBTN.getLocation().getY() + 
                deleteThoughtBTN.getSize().getHeight() + (THOUGHTSMENUMARGIN / 2)),
                (int)((this.getSize().getWidth()) - (2.5 * THOUGHTSMENUMARGIN)),btnHeight);
    }
    
    public void UpdateThoughts(Thought[] thoughts){
        tablePanel.UpdateThoughts(thoughts);
    }
    
    private void SetThoughtNoteToCurrentThought(){
        if(currentSelectedThought != null){
            thoughtNoteTXT.setText(currentSelectedThought.GetNote());
        } else {
            System.out.println("Faal in: SetThoughtNoteToCurrentThought");
            MessageBox.DoOkErrorMessageBox(this, "FOUT: bij het instellen gedachte - 34893!",
                    "FOUT bij het instellen van een gedachte, neem contact op met de makers!");
        }
    }
    
    //zet de tekst in de tekstbox naar de huidige note
    private void SetCurrentThoughtToThoughtNote(){
            currentSelectedThought.SetNote(thoughtNoteTXT.getText());
    }
    
    //slaat de huidige gedachte op
    private void SaveCurrentThought(){
        try {
            //slaat alles op naar het model en DB
            //System.out.println("ID gedachte: " + currentSelectedThought.GetID());
            controller.GetModel().UpdateThought(currentSelectedThought);
            tablePanel.DeselectAll();
            SetButtonsSavedDeleted();
        } catch (ThingsException ex) {
            ex.printStackTrace();
            MessageBox.DoOkErrorMessageBox(this, "FOUT: opslaan gedachte mislukt!",
                    "FOUT BIJ HET OPSLAAN VAN DE GEDACHTE, \ncontrolleer de verbinding!");
        } catch (DatabaseException ex) {
            ex.printStackTrace();
            MessageBox.DoOkErrorMessageBox(this, "FOUT: opslaan gedachte mislukt!",
                    "FOUT BIJ HET OPSLAAN VAN DE GEDACHTE, verbinding is in orde, \ngedachte kon niet opgeslagen worden in de database!");
        }
    }
    
    private void DoNewThought(){
        currentSelectedThought = new Thought();
        SetThoughtNoteToCurrentThought();
        tablePanel.DeselectAll();
        //als je een nieuwe gedachte maakt, dan kan je er niet nog een nieuwe maken
        //bij een nieuwe gedachte kan er niks gewist worden
        SetButtonsNew();
    }
    
    public Thought GetCurrentSelectedThought(){
        return currentSelectedThought;
    }
    
    public void SetCurrentSelectedThought(Thought t){
        currentSelectedThought = t;
    }
    
    public void DoDeleteThought(){
        //checkt of er wel een gedachte is om te wissen
        if(currentSelectedThought != null && currentSelectedThought.GetID() != -1){
            try {
                //slaat alles op naar het model en DB
                //System.out.println("ID gedachte: " + currentSelectedThought.GetID());
                if(!controller.GetModel().DeleteThought(currentSelectedThought)){
                    MessageBox.DoOkErrorMessageBox(this, "FOUT: wissen gedachte mislukt! - 001",
                        "FOUT BIJ HET WISSEN VAN DE GEDACHTE, verbinding is in orde,"
                        + "\ngedachte kon niet gewist worden in de database!");
                } else {
                    SetButtonsSavedDeleted();
                    tablePanel.DeselectAll();
                }
            } catch (ThingsException ex) {
                ex.printStackTrace();
                MessageBox.DoOkErrorMessageBox(this, "FOUT: wissen gedachte mislukt!",
                    "FOUT BIJ HET WISSEN VAN DE GEDACHTE, \ncontrolleer de verbinding!");
            } catch (DatabaseException ex) {
                ex.printStackTrace();
                MessageBox.DoOkErrorMessageBox(this, "FOUT: wissen gedachte mislukt!",
                    "FOUT BIJ HET WISSEN VAN DE GEDACHTE, verbinding is in orde,"
                    + "\ngedachte kon niet gewist worden in de database!");
            }
        } else { //zou nooit mogen gebeuren, maar voor dn zekersheid #trolololll
            MessageBox.DoOkErrorMessageBox(this, "FOUT: wissen gedachte mislukt! - 002",
                    "FOUT BIJ HET WISSEN VAN DE GEDACHTE, kan geen lege gedachte wissen!");
        }
    }
    
     private void DoExit(){
        this.processWindowEvent( new WindowEvent(this, WindowEvent.WINDOW_CLOSING) );
    }
     
    private void SetButtonsNew(){
        newThoughtBTN.setEnabled(false);
        deleteThoughtBTN.setEnabled(true);
        saveThoughtBTN.setEnabled(true);
    }
    
    private void SetButtonsSavedDeleted(){
        newThoughtBTN.setEnabled(true);
        deleteThoughtBTN.setEnabled(false);
        saveThoughtBTN.setEnabled(false);
    }
    
//    private void LoadThoughts(){
//        ( new Thread() {
// 
//        public void run() {
//            DoLoading(true);
//            try {
//                CloseConnectionAfterDatabaseAction = false;
//                controller.GetModel().SetAllThoughts();
//                controller.GetModel().GetAllThoughtsAsArray();
//                CloseConnectionAfterDatabaseAction = true;
//                
//            } catch (ThingsException ex) {
//                ex.printStackTrace();
//                    MessageBox.DoOkErrorMessageBox(mainMenuFrame, "FOUT: laden gedachtes!",
//                            "FOUT BIJ HET OPSLAAN VAN DE GEDACHTE, verbinding is in orde,"
//                            + "\ngedachtes konden niet opgehaald worden van de database!");
//                    DoExit();
//                } catch (DatabaseException ex) {
//                    ex.printStackTrace();
//                MessageBox.DoOkErrorMessageBox(mainMenuFrame, "FOUT: laden gedachtes!",
//                        "FOUT BIJ HET LADEN VAN DE GEDCHTES, \ncontrolleer de verbinding!");
//                DoExit();
//                }
//            DoLoading(false);
//        }
//        }
//        ).start();
//    }
    
   
    //zet de loading tabel alvast neer, zodat andere shit later geladen kan worden
//    private void SetLoadingTable() {
//        loadingLabel = MainConstants.SetLoadingTable((int)getBounds().getWidth(), (int)getBounds().getHeight());
//        add(loadingLabel);
//    }
//    
//    //zet de label neer dat ie aan het laden is en disable
//    private void DoLoading(Boolean isLoading){
//        if(isLoading){
//            for(Component c : this.getContentPane().getComponents()){
//                c.setVisible(false);
//            }
//            setEnabled(false);
//            loadingLabel.setVisible(true);
//        } else {
//            for(Component c : this.getContentPane().getComponents()){
//                c.setVisible(true);
//            }
//            setEnabled(true);
//            loadingLabel.setVisible(false);
//        }
//    }
//    
//    private void DoErrorCurrentScreen(String title, String message){
//        MessageBox.DoOkErrorMessageBox(this, title, message);
//    }
    
}
