/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

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
    private JButton saveThoughtBTN, clearSelectedThoughtBTN, deleteThoughtBTN;
    private JTextArea thoughtNoteTXT;
    private JLabel thoughtNoteLBL;
    
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
        
//        Thought[] thoughts = new Thought[5];
//        for(int i = 0; i < thoughts.length; i++){
//            thoughts[i] = new Thought(i+1, "" + i + "bamiii " + i+3*34334, java.sql.Timestamp.valueOf("2011-10-06 11:04:49"));
//        }
        
        tablePanel = new UberTablePanel(thoughts,300,300);
        saveThoughtBTN = new JButton("Opslaan");
        saveThoughtBTN.setFont(FONTBUTTONS);
        saveThoughtBTN.setEnabled(false);
        clearSelectedThoughtBTN = new JButton("Nieuwe Gedachte");
        clearSelectedThoughtBTN.setFont(FONTBUTTONS);
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
//        thoughtNoteLBL.setOpaque(true);
//        thoughtNoteLBL.setBackground(Color.GREEN);
        
        add(thoughtNoteLBL);
        add(saveThoughtBTN);
        add(deleteThoughtBTN);
        add(clearSelectedThoughtBTN);
        add(thoughtNoteTXT);
        add(tablePanel);
        
        
        
        AddListeners();

        UpdateScreenBounds();
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
                DoNewThought();
            }
        });
        
        //maak een nieuwe gedachte aan
        clearSelectedThoughtBTN.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                DoNewThought();
                saveThoughtBTN.setEnabled(true);
                
            }
        });
        
        deleteThoughtBTN.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                DoDeleteThought();
                saveThoughtBTN.setEnabled(true);
                //bij een nieuwe gedachte kan er niks gewist worden
                deleteThoughtBTN.setEnabled(false);
            }
        });
        
        //action listener om aan te geven dat de selectie van de tabel veranderd is
        //word aangeroepen vannuit het tablePanel
        tablePanel.selectionChangedTable = new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                currentSelectedThought = (tablePanel.getSelectedObject() instanceof Thought) ? 
                        (Thought)tablePanel.getSelectedObject() : null;
                SetThoughtNoteToCurrentThought();
                //als er een bestaande gedachte geselecteerd is, kan die opgeslagen worden
                saveThoughtBTN.setEnabled(true);
                //als er een bestaande gedachte geselecteerd is, dan kan die gewist worden
                deleteThoughtBTN.setEnabled(true);
            }
        };
    }
    
    private void UpdateScreenBounds(){
        int btnHeight = 50;
        tablePanel.UpdateSize(THOUGHTSMENUMARGIN,THOUGHTSMENUMARGIN,(int)(this.getSize().getWidth() - (2.5 * THOUGHTSMENUMARGIN)), 
                (int)(this.getSize().getHeight() - 400));
        thoughtNoteLBL.setBounds(THOUGHTSMENUMARGIN,(int)(tablePanel.getLocation().getY() + tablePanel.getSize().getHeight() + THOUGHTSMENUMARGIN),
                (int)(this.getSize().getWidth() - (2.5 * THOUGHTSMENUMARGIN)),30);
        thoughtNoteTXT.setBounds(THOUGHTSMENUMARGIN,(int)(thoughtNoteLBL.getLocation().getY() + thoughtNoteLBL.getSize().getHeight()),
                (int)(this.getSize().getWidth() - (2.5 * THOUGHTSMENUMARGIN)),100);
        saveThoughtBTN.setBounds(THOUGHTSMENUMARGIN,(int)(thoughtNoteTXT.getLocation().getY() + thoughtNoteTXT.getSize().getHeight() + THOUGHTSMENUMARGIN),
                (int)((this.getSize().getWidth() / 2) - (2.5 * THOUGHTSMENUMARGIN)),btnHeight);
        clearSelectedThoughtBTN.setBounds((int)(saveThoughtBTN.getLocation().getX() + saveThoughtBTN.getSize().getWidth() +
                THOUGHTSMENUMARGIN),
                (int)(thoughtNoteTXT.getLocation().getY() + thoughtNoteTXT.getSize().getHeight() + THOUGHTSMENUMARGIN),
                (int)((this.getSize().getWidth() - (saveThoughtBTN.getLocation().getX() + saveThoughtBTN.getSize().getWidth() +
                THOUGHTSMENUMARGIN)) - (1.5 * THOUGHTSMENUMARGIN)),btnHeight);
        deleteThoughtBTN.setBounds(THOUGHTSMENUMARGIN,(int)(clearSelectedThoughtBTN.getLocation().getY() + 
                clearSelectedThoughtBTN.getSize().getHeight() + (THOUGHTSMENUMARGIN / 2)),
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
        clearSelectedThoughtBTN.setEnabled(false);
        //bij een nieuwe gedachte kan er niks gewist worden
        deleteThoughtBTN.setEnabled(false);
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
                };
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
    
}