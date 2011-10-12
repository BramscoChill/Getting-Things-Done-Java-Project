/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;
import java.awt.Dimension;
import java.util.Random;
import Model.TableMeuk.UberTablePanel;
import model.Thought;
import javax.swing.JFrame;

import static view.MainConstants.*;

/**
 *
 * @author Administrator
 */
public class ThoughtsFrame extends JFrame {
    private UberTablePanel tablePanel;
    private JButton saveThoughtBTN, clearSelectedThoughtBTN;
    private JTextArea thoughtNoteTXT;
    
    public ThoughtsFrame(Thought[] thoughts){
        super(THOUGHTSMENUTITLE);
        setLayout(null);
        this.setResizable(true);
        setBounds(100,new Random().nextInt(200)+50,700,399);
        setMinimumSize(new Dimension(400,350));
        setMaximumSize(new Dimension(9999,400));
        
        setVisible(true);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        //******************************//
        //************TEST**************//
        setBounds(100,100,500,500);
        
//        Thought[] thoughts = new Thought[5];
//        for(int i = 0; i < thoughts.length; i++){
//            thoughts[i] = new Thought(i+1, "" + i + "bamiii " + i+3*34334, java.sql.Timestamp.valueOf("2011-10-06 11:04:49"));
//        }
        
        tablePanel = new UberTablePanel(thoughts,300,300);
        saveThoughtBTN = new JButton("Opslaan");
        saveThoughtBTN.setFont(FONTBUTTONS);
        clearSelectedThoughtBTN = new JButton("Deselecteer geselecteerde gedachte");
        clearSelectedThoughtBTN.setFont(FONTBUTTONS);
        thoughtNoteTXT = new JTextArea();
        thoughtNoteTXT.setFont(FONT);
        add(saveThoughtBTN);
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
    }
    
    private void UpdateScreenBounds(){
        tablePanel.UpdateSize(THOUGHTSMENUMARGIN,THOUGHTSMENUMARGIN,(int)(this.getSize().getWidth() - (2.5 * THOUGHTSMENUMARGIN)), 
                (int)(this.getSize().getHeight() - 400));
        thoughtNoteTXT.setBounds(THOUGHTSMENUMARGIN,(int)(tablePanel.getLocation().getY() + tablePanel.getSize().getHeight() + THOUGHTSMENUMARGIN),
                (int)(this.getSize().getWidth() - (2.5 * THOUGHTSMENUMARGIN)),400);
    }
    
    public void UpdateThoughts(Thought[] thoughts){
        tablePanel.UpdateThoughts(thoughts);
    }
    
}
