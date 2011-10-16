/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JButton;
import Model.TableMeuk.UberTablePanel;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Dimension;
import model.Action;
import java.util.Random;
import javax.swing.JFrame;

import static view.MainConstants.*;

/**
 *
 * @author Administrator
 */
public class ActionsFrame extends JFrame {
    public JButton previousButton;
    
    private Action[] actions;
    
    private UberTablePanel tablePanel;
    private JButton editActionBTN, newActionBTN, deleteActionBTN;
    private JLabel screenInfoLBL;
    
    private Action currentSelectedAction = null;
    
    
    public ActionsFrame(Action[] actions){
        super(ACTIONSMENUTITLE);
        
        this.actions = actions;
        setLayout(null);
        this.setResizable(true);
        setLocation(100,new Random().nextInt(200)+50);
        setMinimumSize(new Dimension(1000,600));
        
        AddComponents();
        
        AddListeners();
        
        setVisible(true);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        UpdateScreenBounds();
    }
    
    
    private void AddComponents(){
        tablePanel = new UberTablePanel(actions,300,300);
        
        screenInfoLBL = new JLabel("Acties");
        screenInfoLBL.setFont(FONTTITLE);
        
        editActionBTN = new JButton("Aanpassen");
        editActionBTN.setFont(FONTBUTTONS);
        editActionBTN.setEnabled(false);
        
        newActionBTN = new JButton("Nieuw");
        newActionBTN.setFont(FONTBUTTONS);
        //newActionBTN.setEnabled(false);
        
        deleteActionBTN = new JButton("Wissen");
        deleteActionBTN.setFont(FONTBUTTONS);
        deleteActionBTN.setEnabled(false);
        
        previousButton = new JButton();
        previousButton.setIcon(PREVIOUSBUTTONIMAGEICON);
        
        add(tablePanel);
        add(newActionBTN);
        add(editActionBTN);
        add(deleteActionBTN);
        add(previousButton);
        add(screenInfoLBL);
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
        
                //action listener om aan te geven dat de selectie van de tabel veranderd is
        //word aangeroepen vannuit het tablePanel
        tablePanel.selectionChangedTable = new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                setCurrentSelectedAction((tablePanel.getSelectedObject() instanceof Action) ? 
                        (Action)tablePanel.getSelectedObject() : null);
                
                //als er een bestaande actie geselecteerd is, kan die veranderd worden
                editActionBTN.setEnabled(true);
                //als er een bestaande actie geselecteerd is, dan kan die gewist worden
                deleteActionBTN.setEnabled(true);
            }
        };
        
        newActionBTN.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                DoNewAction();
            }
        });
        
        editActionBTN.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                DoEditAction();
            }
        });
        
        deleteActionBTN.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                DoDeleteAction();
            }
        });
    }
    
    private void UpdateScreenBounds(){
        int btnHeight = 50;
        
        int previousButXpos = (int) (this.getSize().getWidth() - ACTIONSSMENUMARGIN - PREVIOUSBUTTONSIZE);
        previousButton.setBounds(previousButXpos - 5, (int)(ACTIONSSMENUMARGIN/2), PREVIOUSBUTTONSIZE, PREVIOUSBUTTONSIZE);
                
        screenInfoLBL.setBounds((int)(this.getSize().getWidth()/2.5),(int)(ACTIONSSMENUMARGIN/2),
                (int)(300),50);
        
        tablePanel.UpdateSize(ACTIONSSMENUMARGIN,
                (int)((ACTIONSSMENUMARGIN/2) + screenInfoLBL.getLocation().getY() + screenInfoLBL.getSize().getHeight())
                ,(int)(this.getSize().getWidth() - (2.5 * ACTIONSSMENUMARGIN)), 
                (int)(this.getSize().getHeight() - 215));
        newActionBTN.setBounds(ACTIONSSMENUMARGIN,(int)(tablePanel.getLocation().getY() + tablePanel.getSize().getHeight() + ACTIONSSMENUMARGIN),
                (int)((this.getSize().getWidth()/3) - (1.5 * ACTIONSSMENUMARGIN)),btnHeight);
        editActionBTN.setBounds((int)(newActionBTN.getLocation().getX() + newActionBTN.getSize().getWidth() + ACTIONSSMENUMARGIN),
                (int)(newActionBTN.getLocation().getY()),
                (int)((this.getSize().getWidth()/3) - (1.5 * ACTIONSSMENUMARGIN)),btnHeight);
        deleteActionBTN.setBounds((int)(editActionBTN.getLocation().getX() + editActionBTN.getSize().getWidth() + ACTIONSSMENUMARGIN),
                (int)(editActionBTN.getLocation().getY()),
                (int)((this.getSize().getWidth()/3) - (1.5 * ACTIONSSMENUMARGIN)),btnHeight);
    }
    
    private Action getCurrentSelectedAction() {
        return currentSelectedAction;
    }

    private void setCurrentSelectedAction(Action currentSelectedAction) {
        this.currentSelectedAction = currentSelectedAction;
    }
    
    private void DoNewAction(){
        setCurrentSelectedAction(new Action());
        OpenActionNewScreen();
        //op t einde is er niks meer geselecteerd
        tablePanel.DeselectAll();
        editActionBTN.setEnabled(false);
        deleteActionBTN.setEnabled(false);
        setCurrentSelectedAction(null);
    }
    
    private void DoEditAction(){
        //checkt of er wel een actie geselecteerd is
        if(getCurrentSelectedAction() != null){
            OpenActionNewScreen();
        } else {
            //zou nooit mogen gebeuren, maar voor dn zekersheid #trolololll
            MessageBox.DoOkErrorMessageBox(this, "FOUT: aanpassen actie mislukt! - 001",
                    "FOUT BIJ HET AANPASSEN VAN DE ACTIE, kan geen lege actie aanpassen!");
        }
    }
    
    private void DoDeleteAction(){
        //checkt of er wel een actie geselecteerd is
        if(getCurrentSelectedAction() != null){
            
        } else {
            //zou nooit mogen gebeuren, maar voor dn zekersheid #trolololll
            MessageBox.DoOkErrorMessageBox(this, "FOUT: wissen actie mislukt! - 001",
                    "FOUT BIJ HET WISSEN VAN DE ACTIE, kan geen lege actie wissen!");
        }
    }
    
    private void OpenActionNewScreen(){
        final OneActionFrame newAction = new OneActionFrame(getCurrentSelectedAction());
        setEnabled(false);
        newAction.addWindowListener(new WindowAdapter(){
                   public void windowOpened( WindowEvent e ){
                        //field1.requestFocus();
                     }
                   public void windowClosing( WindowEvent e ){
                       setEnabled(true);
                       newAction.dispose();
                       toFront();
                       
                   }
                });
    }
}
