/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import model.Project;
import model.exceptions.DatabaseException;
import model.exceptions.ThingsException;
import java.awt.Component;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;
import javax.swing.BorderFactory;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Dimension;
import java.util.Random;
import javax.swing.JFrame;

import static controller.Main.*;
import static view.MainConstants.*;

/**
 *
 * @author Administrator
 */
public class ProjectsFrame extends JFrame {
    
    public JButton previousButton;
    
    private JLabel prOverviewLBL;
    private JList prOverviewLIST;
    private DefaultListModel prOverviewMODEL;
    private JScrollPane prOverviewSCROLL;
    private JButton prOverviewAddBTN;
    
    private JLabel prNameLBL;
    private JTextField prNameTXT;
    
    private JLabel prNoteLBL;
    private JTextArea prNoteTXT;
    private JScrollPane prNoteSCROLL;
    
    private JButton prSaveBTN, prCancelBTN;
    private JLabel loadingLabel;
    
    private int currentSelectedProjectINT;
    
    private Boolean somethingChanged = false;
    public Boolean alreadyExited = false;
    
    public ProjectsFrame(){
        super(PROJECTSMENUTITLE);
        setLayout(null);
        this.setResizable(true);
        setBounds(100,new Random().nextInt(200)+50,700,399);
        setMinimumSize(new Dimension(400,350));
        setMaximumSize(new Dimension(9999,400));
        
        SetLoadingLabel();
        
        AddComponents();
        
        AddListeners(); 
        
        UpdateScreenBounds();
        
        setVisible(true);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        LoadContextsStatusesProjects();
    }

    private void AddComponents() {
        prOverviewLBL = new JLabel("Projecten Lijst");
//        prOverviewLBL.setOpaque(true);
//        prOverviewLBL.setBackground(Color.GREEN);
        prOverviewLBL.setFont(FONTBUTTONS);
        prOverviewMODEL = new DefaultListModel();
        prOverviewLIST = new JList(prOverviewMODEL);
        prOverviewLIST.setFont(FONTBUTTONS);
        prOverviewSCROLL = new JScrollPane(prOverviewLIST);
        prOverviewAddBTN = new JButton("Toevoegen");
        
        prNameLBL = new JLabel("Naam:");
        prNameLBL.setFont(FONTBUTTONS);
//        prNameLBL.setOpaque(true);
//        prNameLBL.setBackground(Color.GREEN);
        prNameTXT = new JTextField();
        prNameTXT.setFont(FONTBUTTONS);
        prNameTXT.setEnabled(false);
        
        prNoteLBL = new JLabel("Notitie:");
        prNoteLBL.setFont(FONTBUTTONS);
        prNoteTXT = new JTextArea();
        prNoteTXT.setFont(UBERTABLEINPUTFONT);
        prNoteTXT.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        prNoteTXT.setLineWrap(true);
        prNoteTXT.setWrapStyleWord(true);
        prNoteTXT.setBackground(Color.WHITE);
        prNoteTXT.setEnabled(false);
        prNoteSCROLL = new JScrollPane(prNoteTXT);
        
        prSaveBTN = new JButton("Opslaan");
        prCancelBTN = new JButton("Annuleren");
        
        previousButton = new JButton();
        previousButton.setIcon(PREVIOUSBUTTONIMAGEICON); // NOI18N
        
        
        add(prOverviewSCROLL);
        add(prOverviewLBL);
        add(prOverviewAddBTN);
        add(prNameTXT);
        add(prNameLBL);
        add(prNoteLBL);
        add(prNoteSCROLL);
        add(prSaveBTN);
        add(prCancelBTN);
        add(previousButton);
    }

    private void AddListeners() {
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
        
        prSaveBTN.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                SetSomethingNotChanged();
                DoSaveAll();
                DoExit();
            }
        });
        
        prCancelBTN.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                DoExit();
            }
        });
        previousButton.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                DoExit();
            }
        });
        prOverviewAddBTN.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                DoAddNewProject();
            }
        });
        
        prNameTXT.getDocument().addDocumentListener(new DocumentListener() { 
            public void changedUpdate(DocumentEvent e) { 
                //als de input niet hetzelfde is als het object, dan is er iets veranderd
                if(!prNameTXT.getText().equals(GetName())){
                    SetSomethingChanged();
                }
                SetName(prNameTXT.getText());
            } 
            public void removeUpdate(DocumentEvent e) { 
                if(!prNameTXT.getText().equals(GetName())){
                    SetSomethingChanged();
                }
                SetName(prNameTXT.getText());
            } 
            public void insertUpdate(DocumentEvent e) { 
                if(!prNameTXT.getText().equals(GetName())){
                    SetSomethingChanged();
                }
                SetName(prNameTXT.getText());
            } 
        }); 
        
        prNoteTXT.getDocument().addDocumentListener(new DocumentListener() { 
            public void changedUpdate(DocumentEvent e) { 
                //als de input niet hetzelfde is als het object, dan is er iets veranderd
                if(!prNoteTXT.getText().equals(GetNote())){
                    SetSomethingChanged();
                }
                SetNote(prNoteTXT.getText());
            } 
            public void removeUpdate(DocumentEvent e) { 
                if(!prNoteTXT.getText().equals(GetNote())){
                    SetSomethingChanged();
                }
                SetNote(prNoteTXT.getText());
            } 
            public void insertUpdate(DocumentEvent e) { 
                if(!prNoteTXT.getText().equals(GetNote())){
                    SetSomethingChanged();
                }
                SetNote(prNoteTXT.getText());
            } 
        }); 
        
        //stelt het geselecteerde project in
        prOverviewLIST.addListSelectionListener(new ListSelectionListener ()
        {
            public void valueChanged(ListSelectionEvent evt) {
                //System.out.println("Selection changed actionProjectLIST: " + actionProjectLIST.getSelectedValue().toString());
                if(prOverviewLIST.getSelectedIndex() != -1){
                    currentSelectedProjectINT = prOverviewLIST.getSelectedIndex();
                    SetProjectToSelectedIndex();
                    prNameTXT.setEnabled(true);
                    prNoteTXT.setEnabled(true);
                } else {
                        //prOverviewLIST.setBackground(Color.RED);
                        prNameTXT.setEnabled(false);
                        prNoteTXT.setEnabled(false);
                }
            }
        });
    }

    private void UpdateScreenBounds() {
        int lblHeight = 30;
        int btnWidth = 200;
        int btnHeight = 40;
        int spaceBetweenObjectAndButton = 10;
        int algSizeFromRight = (int)(1.5 * PROJECTSMENUMARGIN);
        
        double sW = getBounds().getWidth();
        double sH = getBounds().getHeight();
                
        int previousButXpos = (int)(sW - algSizeFromRight - PREVIOUSBUTTONSIZE);
        
        prCancelBTN.setBounds((int)(sW - btnWidth - algSizeFromRight), (int)(sH - (2.3 * btnHeight)),
        btnWidth, btnHeight);
        prSaveBTN.setBounds((int)(prCancelBTN.getLocation().getX() - btnWidth - (PROJECTSMENUMARGIN / 2)), (int)(prCancelBTN.getLocation().getY()),
        btnWidth, btnHeight);
        
        previousButton.setBounds(previousButXpos, (int)(PROJECTSMENUMARGIN/2), PREVIOUSBUTTONSIZE, PREVIOUSBUTTONSIZE);
                
        prOverviewLBL.setBounds(PROJECTSMENUMARGIN, PROJECTSMENUMARGIN,
                (int)((sW/2)-((2 * PROJECTSMENUMARGIN)+(sW - previousButton.getLocation().getX())) ),lblHeight);
        
        
        prOverviewSCROLL.setLocation(PROJECTSMENUMARGIN,
                (int)(prOverviewLBL.getLocation().getY() + prOverviewLBL.getSize().getHeight()));
        int prOverviewSCROLLH = (int)(prCancelBTN.getLocation().getY() - (prOverviewSCROLL.getLocation().getY() + (PROJECTSMENUMARGIN / 2)));
                //(sH - (prOverviewSCROLL.getLocation().getY() + prOverviewLBL.getSize().getHeight() + PROJECTSMENUMARGIN));
        
        prOverviewSCROLL.setSize((int)((sW/2)-((2 * PROJECTSMENUMARGIN)+(sW - previousButton.getLocation().getX())) )
                ,prOverviewSCROLLH - btnHeight - spaceBetweenObjectAndButton);
        
        prOverviewAddBTN.setBounds((int)(prOverviewSCROLL.getLocation().getX()), 
                (int)(prOverviewSCROLL.getLocation().getY() + prOverviewSCROLL.getSize().getHeight() + spaceBetweenObjectAndButton),
                (int)(prOverviewSCROLL.getSize().getWidth()),
                (int)(btnHeight));
        
        prNameLBL.setLocation((int)(prOverviewSCROLL.getLocation().getX() + prOverviewSCROLL.getSize().getWidth() + (PROJECTSMENUMARGIN / 2)),
             (int)(previousButton.getLocation().getY() + previousButton.getSize().getHeight() + PROJECTSMENUMARGIN));
        prNameLBL.setSize((int)(sW - prNameLBL.getLocation().getX() - algSizeFromRight),
                (int)(lblHeight));
        prNameTXT.setBounds((int)(prNameLBL.getLocation().getX()),(int)(prNameLBL.getLocation().getY() + prNameLBL.getSize().getHeight()),
                (int)(prNameLBL.getSize().getWidth()),(int)(prNameLBL.getSize().getHeight()));
        
        prNoteLBL.setBounds((int)(prNameTXT.getLocation().getX()),(int)(prNameTXT.getLocation().getY() + prNameTXT.getSize().getHeight() + PROJECTSMENUMARGIN),
                (int)(prNameTXT.getSize().getWidth()),(int)(prNameTXT.getSize().getHeight()));
        prNoteSCROLL.setLocation((int)(prNoteLBL.getLocation().getX()),(int)(prNoteLBL.getLocation().getY() + prNoteLBL.getSize().getHeight()));
        prNoteSCROLL.setSize((int)(prNoteLBL.getSize().getWidth()), (int)(prCancelBTN.getLocation().getY() - (prNoteSCROLL.getLocation().getY() + (PROJECTSMENUMARGIN / 2))) );
    }
    
    //zorgt ervoor dat er iets veranderd is
    private void SetSomethingChanged(){
        somethingChanged = true;
    }
    
    private void SetSomethingNotChanged(){
        somethingChanged = false;
    }
    
    private void DoExit(){
        DoExitReal();
    }
    
    public void DoExitReal(){
        //als je afsluit en er is iets gewijzigd, dan moet er een melding in beeld komen
        if(somethingChanged){
            int yesClicked = MessageBox.DoYesNoCancelQuestionMessage(this, "Afsluiten?", "Er zijn wijzegingen aangebracht, wil je deze opslaan?");
            if(yesClicked == 0){
                DoSaveAll();
                setVisible(false);
                alreadyExited = true;
                this.processWindowEvent( new WindowEvent(this, WindowEvent.WINDOW_CLOSING) );
            } else if(yesClicked == 1){
                setVisible(false);
                alreadyExited = true;
                this.processWindowEvent( new WindowEvent(this, WindowEvent.WINDOW_CLOSING) );
            }
        } else {
            setVisible(false);
            alreadyExited = true;
            this.processWindowEvent( new WindowEvent(this, WindowEvent.WINDOW_CLOSING) );
        }
        alreadyExited = false;
    }
    
    //zet de label neer dat ie aan het laden is en disable
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
    
        //zet de loading tabel alvast neer, zodat andere shit later geladen kan worden
    private void SetLoadingLabel() {
        loadingLabel = MainConstants.SetLoadingTable((int)getBounds().getWidth(), (int)getBounds().getHeight());
        add(loadingLabel);
    }
    
    private void DoErrorCurrentScreen(String title, String message){
        MessageBox.DoOkErrorMessageBox(this, title, message);
    }
    
    private void LoadContextsStatusesProjects(){
        ( new Thread() {
 
        public void run() {
            DoLoading(true);
            try {
                CloseConnectionAfterDatabaseAction = false;
                controller.GetModel().SetAllProjectsContextsStatuses();
                SetCurrentProjectsToListBox();
                CloseConnectionAfterDatabaseAction = true;
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
    
    private void SetCurrentProjectsToListBox(){
        prOverviewMODEL.clear();
        Project[] projects = controller.GetModel().GetProjectsInternal();
        for(Project pr : projects){
            prOverviewMODEL.addElement(pr.getName());
        }
        
    }
    
    private String GetName(){
        return controller.GetModel().GetProjectInternal(currentSelectedProjectINT).getName();
    }
    
    private void SetName(String str){
        controller.GetModel().GetProjectInternal(currentSelectedProjectINT).setName(str);
        prOverviewMODEL.set(currentSelectedProjectINT, str);
        
        //System.out.println("setname: " + str);
    }
    
    private String GetNote(){
        return controller.GetModel().GetProjectInternal(currentSelectedProjectINT).getNote();
    }
    
    private void SetNote(String str){
        controller.GetModel().GetProjectInternal(currentSelectedProjectINT).setNote(str);
    }
    
    private void DoAddNewProject(){
        Project proj = new Project();
        int newNameINT = prOverviewMODEL.getSize();
        System.out.println("amount projects - DoAddNewProject: " + newNameINT);
        proj.setName("project " + (newNameINT + 1));
        proj = controller.GetModel().AddProjectInternal(proj);
        if (proj != null){
            //prOverviewLIST.clearSelection();
            SetCurrentProjectsToListBox();
            prOverviewLIST.setSelectedIndex(newNameINT);
            prNameTXT.selectAll();
            prNameTXT.requestFocus();
            //SetProjectToSelectedIndex();
        } else {
            System.out.println("DoAddNewProject is null!!!");
        }
    }
    
    private void DoSaveAll(){
        ( new Thread() {
 
        public void run() {
            DoLoading(true);
            try {
                CloseConnectionAfterDatabaseAction = false;
                if(controller.GetModel().UpdatInternalProjectsToDB()){
                    CloseConnectionAfterDatabaseAction = true;
                    //DoExit();
                } else {
                    CloseConnectionAfterDatabaseAction = true;
                    DoErrorCurrentScreen("FOUT: opslaan Projecten mislukt - 4833!",
                    "FOUT BIJ HET OPSLAAN VAN DE PROJECTEN, verbinding is in orde,"
                    + "\nIntern faalt er iets!!!");
                }
                
                
                
            } catch (ThingsException ex) {
            ex.printStackTrace();
                DoErrorCurrentScreen("FOUT: opslaan Projecten!",
                "FOUT BIJ HET OPSLAAN VAN DE PROJECTEN, verbinding is in orde,"
                + "\n Meuk kon niet opgeslagen worden in de database!");
                //DoExit();
                //this.processWindowEvent( new WindowEvent(this, WindowEvent.WINDOW_CLOSING) );
            } catch (DatabaseException ex) {
                ex.printStackTrace();
                DoErrorCurrentScreen("FOUT: opslaan Projecten!",
                "FOUT BIJ HET OPSLAAN VAN DE PROJECTEN, \ncontrolleer de verbinding!");
            //this.processWindowEvent( new WindowEvent(this, WindowEvent.WINDOW_CLOSING) );
            }
            DoLoading(false);
        }
        }
        ).start();
    
    }
    
    private void SetProjectToSelectedIndex(){
        prNameTXT.setText(GetName());
        prNoteTXT.setText(GetNote());
    }
}
