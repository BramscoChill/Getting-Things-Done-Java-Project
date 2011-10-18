/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

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
    
    private JLabel prNameLBL;
    private JTextField prNameTXT;
    
    private JLabel prNoteLBL;
    private JTextArea prNoteTXT;
    private JScrollPane prNoteSCROLL;
    
    private JButton prSaveBTN, prCancelBTN;
    
    public ProjectsFrame(){
        super(PROJECTSMENUTITLE);
        setLayout(null);
        this.setResizable(true);
        setBounds(100,new Random().nextInt(200)+50,700,399);
        setMinimumSize(new Dimension(400,350));
        setMaximumSize(new Dimension(9999,400));
        
        AddComponents();
        
        AddListeners(); 
        
        UpdateScreenBounds();
        
        setVisible(true);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    private void AddComponents() {
        
        prOverviewLBL = new JLabel("Notitie:");
        prOverviewLBL.setFont(FONTBUTTONS);
        prOverviewMODEL = new DefaultListModel();
        prOverviewLIST = new JList(prOverviewMODEL);
        prOverviewSCROLL = new JScrollPane(prOverviewLIST);
        
        prNameLBL = new JLabel("Notitie:");
        prNameLBL.setFont(FONTBUTTONS);
        
        prNoteLBL = new JLabel("Notitie:");
        prNoteLBL.setFont(FONTBUTTONS);
        prNoteTXT = new JTextArea();
        prNoteTXT.setFont(UBERTABLEINPUTFONT);
        prNoteTXT.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        prNoteTXT.setLineWrap(true);
        prNoteTXT.setWrapStyleWord(true);
        prNoteSCROLL = new JScrollPane(prNoteTXT);
        
        
    }

    private void AddListeners() {
        
    }

    private void UpdateScreenBounds() {
        
    }
}
