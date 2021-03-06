/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Component;
import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JButton;
import model.TableMeuk.UberTablePanel;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Dimension;
import model.Action;
import java.util.Random;
import javax.swing.JFrame;

import model.exceptions.DatabaseException;
import model.exceptions.ThingsException;

import static view.MainConstants.*;
import static javaapplication5.daMain.*;

/**
 *
 * @author Administrator
 */
public class ActionsFrame extends JFrame {
    public JButton previousButton = new JButton();;
    
    private JLabel loadingLabel;
    
    private Action[] actions;
    
    private UberTablePanel tablePanel;
    private JButton editActionBTN, newActionBTN, deleteActionBTN, markAsDoneBTN;
    private JLabel screenInfoLBL;
    
    private Action currentSelectedAction = null;
    
    private Boolean isHistory;
    
    public ActionsFrame(Boolean isHistory){
        super(ACTIONSMENUTITLE);
        setLayout(null);
        this.setIconImage(MAINICON.getImage());
        
        this.setResizable(true);
        setLocation(100,new Random().nextInt(200)+50);
        setMinimumSize(new Dimension(1000,600));
        
        this.isHistory = isHistory;
        
        SetLoadingLabel();
        
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        
        setVisible(true);
        
        LoadAll();
    }
    
    
    private void AddComponents(){
        
        
        tablePanel = new UberTablePanel(actions,300,300);
        
        if(isHistory){
            screenInfoLBL = new JLabel("Geschiedenis");
        } else {
            screenInfoLBL = new JLabel("Acties");
        }
        
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
        
        if(isHistory){
            markAsDoneBTN = new JButton("Niet Klaar");
        } else {
            markAsDoneBTN = new JButton("Klaar");
        }
        
        markAsDoneBTN.setFont(FONTBUTTONS);
        markAsDoneBTN.setEnabled(false);
        
        
        previousButton.setIcon(PREVIOUSBUTTONIMAGEICON);
        
        //this.setIconImage(MAINICON.getImage());

        
        add(tablePanel);
        add(newActionBTN);
        add(editActionBTN);
        add(deleteActionBTN);
        add(previousButton);
        add(screenInfoLBL);
        add(markAsDoneBTN);
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
        tablePanel.selectionChangedTableDoubleClick = new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                setCurrentSelectedAction((tablePanel.getSelectedObject() instanceof Action) ? 
                        (Action)tablePanel.getSelectedObject() : null);
                
                //als er een bestaande actie geselecteerd is, kan die veranderd worden
                editActionBTN.setEnabled(true);
                //als er een bestaande actie geselecteerd is, dan kan die gewist worden
                deleteActionBTN.setEnabled(true);
                markAsDoneBTN.setEnabled(true);
                DoEditAction();
                
            }
        };
        
        tablePanel.selectionChangedTableOneClick = new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                setCurrentSelectedAction((tablePanel.getSelectedObject() instanceof Action) ? 
                        (Action)tablePanel.getSelectedObject() : null);
                
                //als er een bestaande actie geselecteerd is, kan die veranderd worden
                editActionBTN.setEnabled(true);
                //als er een bestaande actie geselecteerd is, dan kan die gewist worden
                deleteActionBTN.setEnabled(true);
                markAsDoneBTN.setEnabled(true);
            }
        };
        
        
        newActionBTN.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                DoNewAction();
//                RefreshActionsList();
//                for(Action action : controller.GetModel().GetAllActionsAsArray()){
//                    System.out.println("Action: " + action.getDescription() + ", done: " + action.isDone());
//                }
                
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
        
        markAsDoneBTN.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                MarkSelectedActionAsDone();
            }
        });
        
        
    }
    
    private void UpdateScreenBounds(){
        int btnHeight = 50;
        int amountButtons = (isHistory) ? 3 : 4;
        
        int previousButXpos = (int) (this.getSize().getWidth() - ACTIONSSMENUMARGIN - PREVIOUSBUTTONSIZE);
        previousButton.setBounds(previousButXpos - 5, (int)(ACTIONSSMENUMARGIN/2), PREVIOUSBUTTONSIZE, PREVIOUSBUTTONSIZE);
                
        screenInfoLBL.setBounds((int)(this.getSize().getWidth()/2.5),(int)(ACTIONSSMENUMARGIN/2),
                (int)(300),50);
        
        tablePanel.UpdateSize(ACTIONSSMENUMARGIN,
                (int)((ACTIONSSMENUMARGIN/2) + screenInfoLBL.getLocation().getY() + screenInfoLBL.getSize().getHeight())
                ,(int)(this.getSize().getWidth() - (2.5 * ACTIONSSMENUMARGIN)), 
                (int)(this.getSize().getHeight() - 215));
        
        if(isHistory){
            editActionBTN.setBounds(ACTIONSSMENUMARGIN,(int)(tablePanel.getLocation().getY() + tablePanel.getSize().getHeight() + ACTIONSSMENUMARGIN),
                    (int)((this.getSize().getWidth() / amountButtons) - (1.5 * ACTIONSSMENUMARGIN)),btnHeight);
            deleteActionBTN.setBounds((int)(editActionBTN.getLocation().getX() + editActionBTN.getSize().getWidth() + ACTIONSSMENUMARGIN),
                    (int)(editActionBTN.getLocation().getY()),
                    (int)((this.getSize().getWidth() / amountButtons) - (1.5 * ACTIONSSMENUMARGIN)),btnHeight);
            markAsDoneBTN.setBounds((int)(deleteActionBTN.getLocation().getX() + deleteActionBTN.getSize().getWidth() + ACTIONSSMENUMARGIN),
                    (int)(deleteActionBTN.getLocation().getY()),
                    (int)((this.getSize().getWidth() / amountButtons) - (1.5 * ACTIONSSMENUMARGIN)),btnHeight);
        } else {
            newActionBTN.setBounds(ACTIONSSMENUMARGIN,(int)(tablePanel.getLocation().getY() + tablePanel.getSize().getHeight() + ACTIONSSMENUMARGIN),
                    (int)((this.getSize().getWidth() / amountButtons) - (1.5 * ACTIONSSMENUMARGIN)),btnHeight);
            editActionBTN.setBounds((int)(newActionBTN.getLocation().getX() + newActionBTN.getSize().getWidth() + ACTIONSSMENUMARGIN),
                    (int)(newActionBTN.getLocation().getY()),
                    (int)((this.getSize().getWidth() / amountButtons) - (1.5 * ACTIONSSMENUMARGIN)),btnHeight);
            deleteActionBTN.setBounds((int)(editActionBTN.getLocation().getX() + editActionBTN.getSize().getWidth() + ACTIONSSMENUMARGIN),
                    (int)(editActionBTN.getLocation().getY()),
                    (int)((this.getSize().getWidth() / amountButtons) - (1.5 * ACTIONSSMENUMARGIN)),btnHeight);
            markAsDoneBTN.setBounds((int)(deleteActionBTN.getLocation().getX() + deleteActionBTN.getSize().getWidth() + ACTIONSSMENUMARGIN),
                    (int)(deleteActionBTN.getLocation().getY()),
                    (int)((this.getSize().getWidth() / amountButtons) - (1.5 * ACTIONSSMENUMARGIN)),btnHeight);
        }

    }
    
    private Action getCurrentSelectedAction() {
        return currentSelectedAction;
    }

    private void setCurrentSelectedAction(Action currentSelectedAction) {
        this.currentSelectedAction = currentSelectedAction;
    }
    
    private void SetCurrentSelectedActionNull(){
        tablePanel.DeselectAll();
        editActionBTN.setEnabled(false);
        deleteActionBTN.setEnabled(false);
        markAsDoneBTN.setEnabled(false);
        setCurrentSelectedAction(null);
    }
    
    private void DoNewAction(){
        setCurrentSelectedAction(new Action());
        OpenActionNewScreen();
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
        if(getCurrentSelectedAction() != null && getCurrentSelectedAction().getID() != -1){
            getCurrentSelectedAction().setDone(true);
            try {
                controller.GetModel().DeleteAction(getCurrentSelectedAction());
                RefreshActionsList();
            } catch (ThingsException ex) {
            ex.printStackTrace();
            MessageBox.DoOkErrorMessageBox(this, "FOUT: verwijderen van actiemislukt!",
                    "FOUT BIJ HET VERWIJDEREN VAN DE ACTIE, \ncontrolleer de verbinding!");
            } catch (DatabaseException ex) {
                ex.printStackTrace();
                MessageBox.DoOkErrorMessageBox(this, "FOUT: verwijderen van actie mislukt!",
                        "FOUT BIJ HET VERWIJDEREN, verbinding is in orde, \nDe actie kon niet verwijderd worden uit de database!");
            }
        } else {
            //zou nooit mogen gebeuren, maar voor dn zekersheid #trolololll
            MessageBox.DoOkErrorMessageBox(this, "FOUT: wissen actie mislukt! - 001",
                    "FOUT BIJ HET WISSEN VAN DE ACTIE, kan geen lege actie wissen!");
        }
    }
    
    
    //markeerd de geselecteerde actie als gedaan
    private void MarkSelectedActionAsDone(){
        //checkt of er wel een actie geselecteerd is
        if(getCurrentSelectedAction() != null && getCurrentSelectedAction().getID() != -1){
            if(isHistory){
                getCurrentSelectedAction().setDone(false);
            } else {
                getCurrentSelectedAction().setDone(true);
            }
            try {
                controller.GetModel().UpdateAction(getCurrentSelectedAction());
                RefreshActionsList();
                SetCurrentSelectedActionNull();
            } catch (ThingsException ex) {
            ex.printStackTrace();
            MessageBox.DoOkErrorMessageBox(this, "FOUT: markeren van gedachte als klaar mislukt!",
                    "FOUT BIJ HET MARKEREN VAN DE ACTIE ALS KLAAR, \ncontrolleer de verbinding!");
            } catch (DatabaseException ex) {
                ex.printStackTrace();
                MessageBox.DoOkErrorMessageBox(this, "FOUT: opslaan gedachte mislukt!",
                        "FOUT BIJ HET MARKEREN VAN DE ACTIE ALS KLAAR, verbinding is in orde, \ngedachte kon niet geupdate worden in de database!");
            }
        } else {
            //zou nooit mogen gebeuren, maar voor dn zekersheid #trolololll
            MessageBox.DoOkErrorMessageBox(this, "FOUT: markeren als klaar mislukt! - 001",
                    "FOUT BIJ HET MARKEREN VAN DE ACTIE ALS KLAAR, kan geen lege actie markeren \n(of het is een nieuwe actie!)!");
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
                       SetCurrentSelectedActionNull();
                       RefreshActionsList();
                   }
                });
    }
    
    //laad de lijsten met de statussen, contexten en projecten opnieuw in
    private void RefreshActionsList(){
        ( new Thread() {
            public void run() {
                //try{
                    System.out.println("Refreshed da list ActionsFrame");
                    CloseConnectionAfterDatabaseAction = false;
                    //controller.GetModel().SetAllActions();
                    if(isHistory){
                        actions = controller.GetModel().GetAllActionsDoneAsArray();
                    } else {
                        actions = controller.GetModel().GetAllActionsNotDoneAsArray();
                    }
                    
//                    for(Action action : actions){
//                        System.out.println("Action: " + action.getDescription() + ", done: " + action.isDone());
//                    }
                    tablePanel.UpdateActions(actions);
                    CloseConnectionAfterDatabaseAction = true;
//                } catch (ThingsException ex) {
//                    ex.printStackTrace();
//                    DoErrorCurrentScreen("FOUT: updaten actielijst mislukt!",
//                            "FOUT BIJ HET UPDATEN VAN DE ACTIELIJST, \ncontrolleer de verbinding!");
//                    } catch (DatabaseException ex) {
//                        ex.printStackTrace();
//                        DoErrorCurrentScreen("FOUT: updaten actielijst mislukt!",
//                                "FOUT BIJ HET UPDATEN VAN DE ACTIELIJST, verbinding is in orde, \nActies kunnen niet opgehaald worden uit de database!");
//                    }
            }
        }).start();
    }
    
    private void DoErrorCurrentScreen(String title, String message){
        MessageBox.DoOkErrorMessageBox(this, title, message);
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
    
    private void DoExit(){
        this.processWindowEvent( new WindowEvent(this, WindowEvent.WINDOW_CLOSING) );
    }
    
    private void LoadContextsStatusesProjects(){
        ( new Thread() {
 
        public void run() {
            DoLoading(true);
            try {
                controller.GetModel().SetAllProjectsContextsStatuses();
                
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
            DoLoading(false);
        }
        }
        ).start();
    }
    
//    private void LoadActions(){
//        try{
//        controller.GetModel().SetAllActions();
//        actions = controller.GetModel().GetAllActionsAsArray();
//        } catch (ThingsException ex) {
//                ex.printStackTrace();
//                    MessageBox.DoOkErrorMessageBox(this, "FOUT: laden acties!",
//                            "FOUT BIJ HET OPSLAAN VAN DE ACTIES, verbinding is in orde,"
//                            + "\ngedachtes konden niet opgehaald worden van de database!");
//                    DoExit();
//                } catch (DatabaseException ex) {
//                    ex.printStackTrace();
//                MessageBox.DoOkErrorMessageBox(this, "FOUT: laden acties!",
//                        "FOUT BIJ HET LADEN VAN DE ACTIES, \ncontrolleer de verbinding!");
//                DoExit();
//                }
//    }
    
    private void LoadAll(){
        ( new Thread() {
 
        public void run() {
            DoLoading(true);
            //LoadContextsStatusesProjects();
            try {
                CloseConnectionAfterDatabaseAction = false;
                controller.GetModel().SetAllActions();
                //als het gaat om de history
                if(isHistory){
                    actions = controller.GetModel().GetAllActionsDoneAsArray();
                } else { //gaat niet om de history
                    actions = controller.GetModel().GetAllActionsNotDoneAsArray();
                }
                
                
                controller.GetModel().SetAllProjectsContextsStatuses();
                CloseConnectionAfterDatabaseAction = true;
                AddComponents();
                
                AddListeners();

                UpdateScreenBounds();
                
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
            DoLoading(false);
        }
        }
        ).start();
    }

    //zet de loading tabel alvast neer, zodat andere shit later geladen kan worden
    private void SetLoadingLabel() {
        loadingLabel = MainConstants.SetLoadingTable((int)getBounds().getWidth(), (int)getBounds().getHeight());
        add(loadingLabel);
    }

    
}
