/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.DefaultListModel;
import java.util.Date;
import java.awt.FlowLayout;
import java.awt.Container;
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.sql.Timestamp;
import java.awt.event.WindowEvent;
import model.Status;
import model.Context;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Project;
import javax.swing.SwingConstants;
import javax.swing.ListCellRenderer;
import javax.swing.DefaultListCellRenderer;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Random;
import javax.swing.JTextArea;
import javax.swing.JFrame;
import model.Action;

import model.exceptions.DatabaseException;
import model.exceptions.ThingsException;
import static view.MainConstants.*;
import static controller.Main.*;

/**
 *
 * @author Administrator
 */
public class OneActionFrame extends JFrame {
    
    private Action daAction;
    
    
    //COMPONENTEN
    private JLabel loadingLabel;
    private JLabel actionDescriptionLBL,actionNoteLBL;
    private JTextArea actionDescriptionTXT,actionNoteTXT;
    private JScrollPane actionDescriptionSCROLL,actionNoteSCROLL;
    
    private JLabel actionProjectLBL, actionContextLBL;
    private JList actionProjectLIST, actionContextLIST;
    private DefaultListModel actionProjectMODEL, actionContextMODEL;
    private JScrollPane actionProjectSCROLL, actionContextSCROLL;
    private JButton actionAddProjectBTN, actionRemoveProjectBTN, actionAddContextBTN, actionRemoveContextBTN;
    
    private JLabel actionStatusLBL;
    private JComboBox actionStatusCOMBO;
    
    private JLabel actionDatumLBL, actionTijdLBL;
    private JTextField actionDatumTXT, actionTijdTXT;
    private JButton actionDatumBTN;
    
    private JButton actionSaveBTN, actionCancelBTN;
    
    //VARIABLES
    //met plaatjes istie trager! -> true = trager, false = sneller
    private Boolean useImagesForButtons = false;
    private Project[] projects;
    private Context[] contexts;
    private Status[] statuses;
    
    
    public OneActionFrame(Action actie){
        super(ACTIONMENUTITLE + ((actie.getID() > -1) ? actie.getDescription().trim() : "* nieuwe actie *"));
        daAction = actie;
        
        setLayout(null);
        this.setResizable(true);
        setLocation(100,new Random().nextInt(200)+50);
        setMinimumSize(new Dimension(400,500));
        
        setSize(600,600);
        
        AddComponents();
        
        AddListeners();
        
        setVisible(true);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        UpdateScreenBounds();
        
        GetProjectsContextsStatuses();
        
        SetFormToCurrentAction();
        
    }
    
    public OneActionFrame(){
        this(new Action());
    }
    
    private void AddComponents(){
        
        actionDescriptionLBL = new JLabel("Beschrijving:");
        actionDescriptionLBL.setFont(FONTBUTTONS);
        actionDescriptionTXT = new JTextArea();
        actionDescriptionTXT.setFont(UBERTABLEINPUTFONT);
        actionDescriptionTXT.setBorder(BorderFactory.createLineBorder(Color.black));
        actionDescriptionTXT.setLineWrap(true);
        actionDescriptionTXT.setWrapStyleWord(true);
        actionDescriptionSCROLL = new JScrollPane(actionDescriptionTXT);
        
        actionNoteLBL = new JLabel("Notitie:");
        actionNoteLBL.setFont(FONTBUTTONS);
        actionNoteTXT = new JTextArea();
        actionNoteTXT.setFont(UBERTABLEINPUTFONT);
        actionNoteTXT.setBorder(BorderFactory.createLineBorder(Color.black));
        actionNoteTXT.setLineWrap(true);
        actionNoteTXT.setWrapStyleWord(true);
        actionNoteSCROLL = new JScrollPane(actionNoteTXT);
        
        actionProjectLBL = new JLabel("Gekoppeld Project:");
        actionProjectLBL.setFont(FONTBUTTONS);
        actionProjectMODEL = new DefaultListModel();
        actionProjectLIST = new JList(actionProjectMODEL);
        actionProjectSCROLL = new JScrollPane(actionProjectLIST);
        actionAddProjectBTN = new JButton();
        actionRemoveProjectBTN = new JButton();
        actionAddProjectBTN.setEnabled(false);
        actionRemoveProjectBTN.setEnabled(false);
        if(! useImagesForButtons){
            actionAddProjectBTN.setText("+");
            actionRemoveProjectBTN.setText("-");
            actionRemoveProjectBTN.setFont(FONTBUTTONS);
        }
        
        actionContextLBL = new JLabel("Gekoppeld Context:");
        actionContextLBL.setFont(FONTBUTTONS);
        actionContextMODEL = new DefaultListModel();
        actionContextLIST = new JList(actionContextMODEL);
        actionContextSCROLL = new JScrollPane(actionContextLIST);
        actionAddContextBTN = new JButton();
        actionRemoveContextBTN = new JButton();
        if(! useImagesForButtons){
            actionAddContextBTN.setText("+");
            actionRemoveContextBTN.setText("-");
            actionRemoveContextBTN.setFont(FONTBUTTONS);
        }
        
        actionDatumLBL = new JLabel("Datum:");
        actionDatumLBL.setFont(FONTBUTTONS);
//        actionDatumLBL.setOpaque(true);
//        actionDatumLBL.setBackground(Color.GREEN);
        actionTijdLBL = new JLabel("Tijd:");
        actionTijdLBL.setFont(FONTBUTTONS);
//        actionTijdLBL.setOpaque(true);
//        actionTijdLBL.setBackground(Color.GREEN);
        actionDatumTXT = new JTextField();
        actionDatumTXT.setFont(UBERTABLEINPUTFONT);
        actionDatumTXT.setHorizontalAlignment(JTextField.CENTER);
        actionTijdTXT = new JTextField();
        actionTijdTXT.setFont(UBERTABLEINPUTFONT);
        actionTijdTXT.setHorizontalAlignment(JTextField.CENTER);
        actionDatumBTN = new JButton("...");
        actionDatumBTN.setFont(FONTBUTTONS);
        
        actionStatusLBL = new JLabel("Status:");
        actionStatusLBL.setFont(FONTBUTTONS);
        actionStatusCOMBO = new JComboBox();
        actionStatusCOMBO.setFont(FONTBUTTONS);
        actionStatusCOMBO.setBackground(Color.WHITE);
        ListCellRenderer renderer = new DefaultListCellRenderer();
        ( (JLabel) renderer ).setHorizontalAlignment( SwingConstants.CENTER );
        actionStatusCOMBO.setRenderer(renderer);
        //actionStatusCOMBO.addItem("aaaa");
        
        actionSaveBTN = new JButton("Opslaan");
        actionCancelBTN = new JButton("Annuleren");
        
        
        loadingLabel = new JLabel("Loading....");
        loadingLabel.setFont(FONTTITLE);
        loadingLabel.setSize(200,50);
        loadingLabel.setOpaque(true);
        loadingLabel.setBackground(Color.BLACK);
        loadingLabel.setForeground(Color.WHITE);
        
        loadingLabel.setLocation((int)((this.getBounds().getWidth()/2) - loadingLabel.getSize().getWidth()),
                (int)((this.getBounds().getHeight() / 2) - loadingLabel.getSize().getHeight()));
        
        add(loadingLabel);
        add(actionDescriptionLBL);
        add(actionDescriptionSCROLL);
        add(actionNoteLBL);
        add(actionNoteSCROLL);
        add(actionProjectLBL);
        add(actionProjectSCROLL);
        add(actionAddProjectBTN);
        add(actionRemoveProjectBTN);
        add(actionContextLBL);
        add(actionContextSCROLL);
        add(actionAddContextBTN);
        add(actionRemoveContextBTN);
        add(actionDatumLBL);
        add(actionDatumTXT);
        add(actionTijdLBL);
        add(actionTijdTXT);
        add(actionDatumBTN);
        add(actionStatusLBL);
        add(actionStatusCOMBO);
        add(actionSaveBTN);
        add(actionCancelBTN);
        
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
        
        //listener voor de description texbox dat het meteen in het model wordt geknald
        actionDescriptionTXT.getDocument().addDocumentListener(new DocumentListener() { 
            public void changedUpdate(DocumentEvent e) { 
                SetDescription(actionDescriptionTXT.getText());
            } 
            public void removeUpdate(DocumentEvent e) { 
                SetDescription(actionDescriptionTXT.getText());
            } 
            public void insertUpdate(DocumentEvent e) { 
                SetDescription(actionDescriptionTXT.getText());
            } 
        }); 
        
        //same als bovenste listener
        actionNoteTXT.getDocument().addDocumentListener(new DocumentListener() { 
            public void changedUpdate(DocumentEvent e) { 
                SetDescription(actionDescriptionTXT.getText());
            } 
            public void removeUpdate(DocumentEvent e) { 
                SetDescription(actionDescriptionTXT.getText());
            } 
            public void insertUpdate(DocumentEvent e) { 
                SetDescription(actionDescriptionTXT.getText());
            } 
        }); 
        
        //actionDatumTXT
        
        //als de focus weg gaat, dan moet ie de tijd instellen
        actionTijdTXT.addFocusListener(new FocusListener() {
          public void focusGained(FocusEvent e) {
              
          }

          public void focusLost(FocusEvent e) {
                if(GetTime() != null){
                    if(SetTime(actionTijdTXT.getText())){
                        actionTijdTXT.setBackground(Color.WHITE);
                        actionStatusCOMBO.requestFocus();
                    } else {
                        actionTijdTXT.setBackground(Color.RED);
                    }
                } else {
                    DoErrorCurrentScreen("Geen datum ingesteld!","Er is nog geen datum ingesteld, doet dit eerst voor er een tijd ingesteld word!");
                    actionTijdTXT.setText("");
                    actionDatumTXT.requestFocus();
                }
          }
        });
        
        
        actionTijdTXT.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                actionStatusCOMBO.requestFocus();
            }
        });
        
        actionDatumTXT.addFocusListener(new FocusListener() {
          public void focusGained(FocusEvent e) {
              
          }

          public void focusLost(FocusEvent e) {
                if(SetDate(actionDatumTXT.getText())){
                    actionDatumTXT.setBackground(Color.WHITE);
                } else {
                    actionDatumTXT.setBackground(Color.RED);
                }
          }
        });
        
        actionStatusCOMBO.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                //System.out.println("Combo status OneActionFrame changed!: " + actionStatusCOMBO.getSelectedItem().toString());
                if(SetStatus(actionStatusCOMBO.getSelectedItem().toString())){
                    actionStatusCOMBO.setBackground(Color.WHITE);
                } else {
                    actionStatusCOMBO.setBackground(Color.RED);
                }
            }
        });
        
        actionProjectLIST.addListSelectionListener(new ListSelectionListener ()
        {
            public void valueChanged(ListSelectionEvent evt) {
                //System.out.println("Selection changed actionProjectLIST: " + actionProjectLIST.getSelectedValue().toString());
                if(SetProject(actionProjectLIST.getSelectedValue().toString())){
                    actionProjectLIST.setBackground(Color.WHITE);
                } else {
                    actionProjectLIST.setBackground(Color.RED);
                }
            }
        });
        
        actionContextLIST.addListSelectionListener(new ListSelectionListener ()
        {
            public void valueChanged(ListSelectionEvent evt) {
                //System.out.println("Selection changed actionProjectLIST: " + actionProjectLIST.getSelectedValue().toString());
                if(actionContextLIST.getSelectedValue() != null){
                    if(SetContext(actionContextLIST.getSelectedValue().toString()) == true){
                        actionContextLIST.setBackground(Color.WHITE);
                    } else {
                        actionContextLIST.setBackground(Color.RED);
                    }
                } else {
                        actionContextLIST.setBackground(Color.RED);
                }
            }
        });
        
        actionAddContextBTN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                DoAddContext();
            }
        });
        
        actionRemoveContextBTN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                if(actionContextLIST.getSelectedIndex() != -1){
                    int clickEd = DoYesNoCurrentScreen("Verwijderen Context","Weet je zeker dat je deze context wilt weghalen?");
                    if(clickEd == 0){
                        DoRemove();
                    }
                }
            }
        });
        
        actionDatumBTN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                DoNewCalendarFrame();
            }
        });
                
        actionDatumTXT.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                actionTijdTXT.requestFocus();
            }
        });
        
        actionSaveBTN.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Description: " + GetDescription());
                System.out.println("Timestamp: " + GetDate() + " " +  GetTime());
            }
        });
        
        actionCancelBTN.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                //System.out.println("Description: " + GetDescription());
                DoExit();
            }
        });
            
    }

    private void UpdateScreenBounds() {
        
        int sW = (int)this.getSize().getWidth();
        int sH = (int)this.getSize().getHeight();
        
        
        int btnHeight = 50;
        int btnWidth = 200;
        
        int lblHeight = 30;
        int firstLayerHeight = (int) (sH / 6);
        
        int addRemoveButtonsWidth = 45;
        
        //we bouwen alles van onder naar boven op!
        actionCancelBTN.setBounds((int)(sW - btnWidth - THOUGHTSMENUMARGIN), (int)(sH - (2 * btnHeight)),
        btnWidth, btnHeight);
        actionSaveBTN.setBounds((int)(actionCancelBTN.getLocation().getX() - btnWidth - (THOUGHTSMENUMARGIN / 2)), (int)(sH - (2 * btnHeight)),
        btnWidth, btnHeight);
        
        actionStatusCOMBO.setBounds(THOUGHTSMENUMARGIN,
                (int)(actionSaveBTN.getLocation().getY() - actionSaveBTN.getSize().getHeight()),
                (int)(sW - (2 * THOUGHTSMENUMARGIN)),lblHeight);
        
        actionStatusLBL.setBounds(THOUGHTSMENUMARGIN,
                (int)(actionStatusCOMBO.getLocation().getY() - actionStatusCOMBO.getSize().getHeight()),
                (int)(sW - (2 * THOUGHTSMENUMARGIN)),lblHeight);

        actionDatumTXT.setBounds(THOUGHTSMENUMARGIN,(int)(actionStatusLBL.getLocation().getY() - actionStatusLBL.getSize().getHeight() - (THOUGHTSMENUMARGIN / 3)),
                (int)((sW / 2) - (3 * THOUGHTSMENUMARGIN)), 
                (int)(lblHeight));
        actionDatumBTN.setBounds((int)(actionDatumTXT.getLocation().getX() + actionDatumTXT.getSize().getWidth() + (THOUGHTSMENUMARGIN / 3)),
                (int)(actionDatumTXT.getLocation().getY()),
                (int)(35), 
                (int)(lblHeight));
        actionDatumLBL.setBounds(THOUGHTSMENUMARGIN,(int)(actionDatumTXT.getLocation().getY() - actionDatumTXT.getSize().getHeight() - (THOUGHTSMENUMARGIN / 3)),
                (int)((sW / 2) - (2 * THOUGHTSMENUMARGIN)), lblHeight);

        
        actionTijdTXT.setLocation((int)(actionTijdLBL.getLocation().getX()),
                (int)(actionDatumTXT.getLocation().getY() ));
        actionTijdTXT.setSize((int)(sW - actionTijdLBL.getLocation().getX() - (1.7 * THOUGHTSMENUMARGIN)), 
                (int)(lblHeight));        
        
        actionTijdLBL.setLocation((int)(actionDatumLBL.getLocation().getX() + actionDatumLBL.getSize().getWidth() + THOUGHTSMENUMARGIN),
                (int)(actionDatumLBL.getLocation().getY() ));
        actionTijdLBL.setSize((int)(sW - actionTijdLBL.getLocation().getX() - (1.7 * THOUGHTSMENUMARGIN)), 
                (int)(lblHeight));

        
        int overigeBovenRuimte = (int)(sH - (sH - (actionTijdLBL.getLocation().getY())) - (2 * lblHeight) - THOUGHTSMENUMARGIN);
        
        actionDescriptionLBL.setBounds(THOUGHTSMENUMARGIN,(int)(THOUGHTSMENUMARGIN/3),(int)(200), 
                (int)(lblHeight));
        actionDescriptionSCROLL.setBounds(THOUGHTSMENUMARGIN,(int)(actionDescriptionLBL.getLocation().getY() + 
                actionDescriptionLBL.getSize().getHeight() ),(int)((sW / 2) - (3 * THOUGHTSMENUMARGIN)), 
                (int)(overigeBovenRuimte / 2));
        actionNoteLBL.setBounds((int)(actionDescriptionSCROLL.getLocation().getX() + actionDescriptionSCROLL.getSize().getWidth() + THOUGHTSMENUMARGIN) ,
                (int)(actionDescriptionLBL.getLocation().getY() ), (int)(200), (int)(lblHeight));
        actionNoteSCROLL.setLocation((int)(actionNoteLBL.getLocation().getX()),
                (int)(actionNoteLBL.getLocation().getY() + actionNoteLBL.getSize().getHeight() ));
        actionNoteSCROLL.setSize((int)(sW - actionNoteSCROLL.getLocation().getX() - (1.5 * THOUGHTSMENUMARGIN)),
                (int)actionDescriptionSCROLL.getSize().getHeight());
        
        //System.out.println("overigeBovenRuimte: " + overigeBovenRuimte + ", sH: " + sH);
        
        actionProjectLBL.setBounds(THOUGHTSMENUMARGIN,(int)(actionDescriptionSCROLL.getLocation().getY() + 
                actionDescriptionSCROLL.getSize().getHeight() + (THOUGHTSMENUMARGIN / 3) ),(int)((sW / 2) - (3 * THOUGHTSMENUMARGIN)), 
                (int)(lblHeight));
        int ruimteTussenDescDatum = (int)(actionDatumLBL.getLocation().getY() - (actionProjectLBL.getLocation().getY() + actionProjectLBL.getSize().getHeight()) - (THOUGHTSMENUMARGIN / 3));
        actionProjectSCROLL.setBounds(THOUGHTSMENUMARGIN,(int)(actionProjectLBL.getLocation().getY() + actionProjectLBL.getSize().getHeight()),
                (int)((sW / 2) - (2.1 * THOUGHTSMENUMARGIN) - (1.8 * addRemoveButtonsWidth)), 
                (int)(ruimteTussenDescDatum));
        actionAddProjectBTN.setBounds((int)(actionProjectSCROLL.getLocation().getX() + actionProjectSCROLL.getSize().getWidth() + (0.2 * addRemoveButtonsWidth)),
                (int)(actionProjectSCROLL.getLocation().getY() ),
                (int)(addRemoveButtonsWidth), 
                (int)((actionProjectSCROLL.getSize().getHeight() / 2) - (0.1 * addRemoveButtonsWidth)));
        actionRemoveProjectBTN.setBounds((int)(actionProjectSCROLL.getLocation().getX() + actionProjectSCROLL.getSize().getWidth() + (0.2 * addRemoveButtonsWidth)),
                (int)(actionAddProjectBTN.getLocation().getY() + actionAddProjectBTN.getSize().getHeight() + (0.2 * addRemoveButtonsWidth) ),
                (int)(addRemoveButtonsWidth), 
                (int)((actionProjectSCROLL.getSize().getHeight() / 2) - (0.1 * addRemoveButtonsWidth)));

        //met plaatjes istie trager!
        if(useImagesForButtons){
            actionAddProjectBTN.setIcon(ResizeImageToButton(PLUSBUTTONIMAGEICON,(int) actionAddProjectBTN.getSize().getWidth(), (int) actionAddProjectBTN.getSize().getHeight()));
            actionRemoveProjectBTN.setIcon(ResizeImageToButton(MINUSBUTTONIMAGEICON,(int) actionRemoveProjectBTN.getSize().getWidth(), (int) actionRemoveProjectBTN.getSize().getHeight()));
        }
        
        
        actionContextLBL.setBounds((int)(actionDescriptionSCROLL.getLocation().getX() + actionDescriptionSCROLL.getSize().getWidth() + THOUGHTSMENUMARGIN),
                (int)(actionProjectLBL.getLocation().getY() ),(int)(sW - actionContextLBL.getLocation().getX() - (0.8 * THOUGHTSMENUMARGIN)), 
                (int)(lblHeight));
        actionContextSCROLL.setLocation((int)(actionContextLBL.getLocation().getX()),
                (int)(actionContextLBL.getLocation().getY() + actionContextLBL.getSize().getHeight() ));
        actionContextSCROLL.setSize((int)(sW - actionContextLBL.getLocation().getX() - (0.8 * THOUGHTSMENUMARGIN) - (1.8 * addRemoveButtonsWidth)),
                ruimteTussenDescDatum);
        actionAddContextBTN.setBounds((int)(actionContextSCROLL.getLocation().getX() + actionContextSCROLL.getSize().getWidth() + (0.2 * addRemoveButtonsWidth)),
                (int)(actionContextSCROLL.getLocation().getY() ),
                (int)(addRemoveButtonsWidth), 
                (int)((actionContextSCROLL.getSize().getHeight() / 2) - (0.1 * addRemoveButtonsWidth)));
        actionRemoveContextBTN.setBounds((int)(actionContextSCROLL.getLocation().getX() + actionContextSCROLL.getSize().getWidth() + (0.2 * addRemoveButtonsWidth)),
                (int)(actionAddContextBTN.getLocation().getY() + actionAddContextBTN.getSize().getHeight() + (0.2 * addRemoveButtonsWidth) ),
                (int)(addRemoveButtonsWidth), 
                (int)((actionContextSCROLL.getSize().getHeight() / 2) - (0.1 * addRemoveButtonsWidth)));
        //met plaatjes istie trager!
        if(useImagesForButtons){
            actionAddContextBTN.setIcon(ResizeImageToButton(PLUSBUTTONIMAGEICON,(int) actionAddContextBTN.getSize().getWidth(), (int) actionAddContextBTN.getSize().getHeight()));
            actionRemoveContextBTN.setIcon(ResizeImageToButton(MINUSBUTTONIMAGEICON,(int) actionRemoveContextBTN.getSize().getWidth(), (int) actionRemoveContextBTN.getSize().getHeight()));
        }
    }
    
    private void GetProjectsContextsStatuses(){
        ( new Thread() {
 
        public void run() {
            DoLoading(true);
            try {
                projects = controller.GetModel().GetProjects();
                statuses = controller.GetModel().GetStatuses();
                contexts = controller.GetModel().GetContexts();
                
                actionProjectMODEL.clear();
                actionContextMODEL.clear();
                actionStatusCOMBO.removeAllItems();
                
                for(Status status : statuses){
                    actionStatusCOMBO.addItem(status.getName());
                }
                
                for(Context context : contexts){
                    actionContextMODEL.addElement(context.getName()); 
                }
                
                for(Project project : projects){
                    actionProjectMODEL.addElement(project.getName()); 
                }
                
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
    
    private void SetFormToCurrentAction(){
        actionDescriptionTXT.setText(GetDescription());
    }
    private String GetDescription(){
        return daAction.getDescription();
    }
    
    private void SetDescription(String str){
        daAction.setDescription(str);
    }
    
    private String GetNote(){
        return daAction.getNote();
    }
    
    private void SetNote(String str){
        daAction.setNote(str);
    }
    
    private String GetProject(){
        return daAction.getProject().getName();
    }
    
    private Boolean SetProject(String str){
       
        for(Project pr : projects){
             System.out.println("SetProject str: " + str + ", pr: " + pr.getName());
            if(pr.getName().trim().toLowerCase().equals(str.trim().toLowerCase())){
                daAction.setProject(pr);
                return true;
            }
        }
        return false;
    }
    
    private String GetContext(){
        return daAction.getContext().getName();
    }
    
    private Boolean SetContext(String str){
        for(Context daContext : contexts){
            if(daContext.getName().trim().equals(str.trim())){
                daAction.setContext(daContext);
                return true;
            }
        }
        return false;
    }
    
    private String GetStatus(){
        return daAction.getStatus().getName();
    }
    
    private Boolean SetStatus(String str){
        for(Status st : statuses){
            if(st.getName().trim().equals(str.trim())){
                daAction.setStatus(st);
                return true;
            }
        }
        return false;
    }
    
    private String GetTime(){
        Timestamp datumTijd = daAction.getDatumTijd();
        return (datumTijd == null) ? null : ("" + datumTijd.getHours() + ":" + datumTijd.getMinutes());
    }
    
    private Boolean SetTime(String str){
        Timestamp datumTijd = daAction.getDatumTijd();
        String[] time = str.split(":");
        System.out.println("str: " + str + ". timestamp: " + datumTijd + ", arr  amount: " + time.length);
        if(time.length == 2){
            try{
                int hh = Integer.parseInt(time[0]);
                int mm = Integer.parseInt(time[1]);
                if(hh > -1 && hh < 25 && mm > -1 && mm < 61){
                    datumTijd.setHours(hh);
                    datumTijd.setMinutes(mm);

                    System.out.println("Datum: " + ("" + datumTijd.getDate() + "-" + datumTijd.getMonth() + "-" + datumTijd.getYear() + " " + datumTijd.getHours() + ":" + datumTijd.getMinutes()));
                    
                    daAction.setDatumTijd(datumTijd);
                    return true;
                }
            } catch (Exception ex){
                
            }
        }
        return false;
    }
    
    private String GetDate(){
        Timestamp datumTijd = daAction.getDatumTijd();
        return (datumTijd == null) ? null : ("" + datumTijd.getDay() + "-" + datumTijd.getMonth() + "-" + datumTijd.getYear());
    }
    
    private Boolean SetDate(String str){
        Timestamp datumTijd = daAction.getDatumTijd();
        
        Date dateNow = new Date();
        
        datumTijd = (datumTijd == null) ? new Timestamp(0) : datumTijd;
        
        String[] date = str.split("-");
        if(date.length == 3){
            try{
                int dd = Integer.parseInt(date[0]);
                int mm = Integer.parseInt(date[1]);
                int yyyy = Integer.parseInt(date[2]);
                
                
                
                if(dd > -1 && dd < 25 && mm > -1 && mm < 13 && yyyy > 1600 && yyyy < 3000){
                    System.out.println("Datum ints: " + ("" + dd + "-" + mm + "-" + yyyy));
                    datumTijd.setDate(dd);
                    datumTijd.setMonth(mm);
                    datumTijd.setYear(yyyy);
                    
                    daAction.setDatumTijd(datumTijd);
                    //System.out.println("Datum: " + ("" + datumTijd.getDate() + "-" + datumTijd.getMonth() + "-" + datumTijd.getYear()));
                    
                    return true;
                }
            } catch (Exception ex){
                
            }
        }
        return false;
    }
    
    private void DoAddContext() {
        String str = MessageBox.DoEnterTextInputDialog(this, "Context Toevoegen", "Typ de naam in van de context:");
        Boolean alreadyExists = false;
        for(Context cont : contexts){
            if(cont.getName().toLowerCase().trim().equals(str.trim().toLowerCase())){
                alreadyExists = true;
            }
        }
        if(! alreadyExists){
            try {
                Context daContext = controller.GetModel().AddContext(new Context(-1,str));
                if(daContext != null){
                    actionContextMODEL.addElement(daContext.getName());
                } else {
                    DoErrorCurrentScreen("FOUT: laden context!",
                        "FOUT BIJ HET LADEN VAN DE CONTEXT, verbinding is in orde,"
                        + "\n NULL gereturnt van de database, neem contact op met de maker!");
                }
            } catch (ThingsException ex) {
            ex.printStackTrace();
                DoErrorCurrentScreen("FOUT: laden context!",
                        "FOUT BIJ HET LADEN VAN DE CONTEXT, verbinding is in orde,"
                        + "\n Meuk kon niet opgehaald worden van de database!\nDit scherm zal nu sluiten!");
                DoExit();
                //this.processWindowEvent( new WindowEvent(this, WindowEvent.WINDOW_CLOSING) );
            } catch (DatabaseException ex) {
                ex.printStackTrace();
            DoErrorCurrentScreen("FOUT: laden context!",
                    "FOUT BIJ HET LADEN VAN DE CONTEXT, \ncontrolleer de verbinding!\nDit scherm zal nu sluiten!");
            DoExit();
            //this.processWindowEvent( new WindowEvent(this, WindowEvent.WINDOW_CLOSING) );
            }
        }
    }
    
    private void DoRemove() {
        String str = actionContextLIST.getSelectedValue().toString();
        Context contextToRemove = null;
        for(Context cont : contexts){
            if(cont.getName().toLowerCase().trim().equals(str.trim().toLowerCase())){
                contextToRemove = cont;
                break;
            }
        }
        if(contextToRemove != null){
            try {
                Boolean removeSucceded = controller.GetModel().DeleteContext(contextToRemove);
                if(removeSucceded){
                    actionContextMODEL.removeElement(contextToRemove.getName());
                } else {
                    DoErrorCurrentScreen("FOUT: laden context!",
                        "FOUT BIJ HET LADEN VAN DE CONTEXT, verbinding is in orde,"
                        + "\n NULL gereturnt van de database, neem contact op met de maker!");
                }
            } catch (ThingsException ex) {
            ex.printStackTrace();
                DoErrorCurrentScreen("FOUT: laden context!",
                        "FOUT BIJ HET LADEN VAN DE CONTEXT, verbinding is in orde,"
                        + "\n Meuk kon niet opgehaald worden van de database!\nDit scherm zal nu sluiten!");
                DoExit();
                //this.processWindowEvent( new WindowEvent(this, WindowEvent.WINDOW_CLOSING) );
            } catch (DatabaseException ex) {
                ex.printStackTrace();
            DoErrorCurrentScreen("FOUT: laden context!",
                    "FOUT BIJ HET LADEN VAN DE CONTEXT, \ncontrolleer de verbinding!\nDit scherm zal nu sluiten!");
            DoExit();
            //this.processWindowEvent( new WindowEvent(this, WindowEvent.WINDOW_CLOSING) );
            }
        }
    }
    
    private void DoNewCalendarFrame(){
        final CalendarFrame calFrame = new CalendarFrame();
        final JFrame f = new JFrame("Kalender");
        f.setResizable(false);
        Container c = f.getContentPane();
        c.setLayout(new FlowLayout());

        calFrame.okBTN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                actionDatumTXT.setText("" + calFrame.GetDay() + "-" + calFrame.GetMonth() + "-" + calFrame.GetYear());
                if(SetDate(actionDatumTXT.getText())){
                    actionDatumTXT.setBackground(Color.WHITE);
                } else {
                    actionDatumTXT.setBackground(Color.RED);
                }
                f.dispose();
            }
        });
        // for this test driver, hardcode 1995/02/10.
        //c.add(new CalendarFrame(1995, 2 - 1, 10));
        c.add(calFrame);

        f.pack();
        f.setVisible(true);
    }
    
    private void DoErrorCurrentScreen(String title, String message){
        MessageBox.DoOkErrorMessageBox(this, title, message);
    }
    
    private int DoYesNoCurrentScreen(String title, String message){
        return MessageBox.DoYesNoQuestionMessage(this, title, message);
    }

    private void DoExit(){
        this.processWindowEvent( new WindowEvent(this, WindowEvent.WINDOW_CLOSING) );
    }
    
    private void DoLoading(Boolean isLoading){
        if(isLoading){
            setEnabled(false);
            loadingLabel.setVisible(true);
        } else {
            setEnabled(true);
            loadingLabel.setVisible(false);
        }
    }
}
