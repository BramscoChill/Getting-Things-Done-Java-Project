/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

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
        
        prOverviewLBL = new JLabel("Projecten Lijst");
        prOverviewLBL.setOpaque(true);
        prOverviewLBL.setBackground(Color.GREEN);
        prOverviewLBL.setFont(FONTBUTTONS);
        prOverviewMODEL = new DefaultListModel();
        prOverviewLIST = new JList(prOverviewMODEL);
        prOverviewLIST.setFont(FONTBUTTONS);
        prOverviewSCROLL = new JScrollPane(prOverviewLIST);
        
        prNameLBL = new JLabel("Naam:");
        prNameLBL.setFont(FONTBUTTONS);
        prNameLBL.setOpaque(true);
        prNameLBL.setBackground(Color.GREEN);
        prNameTXT = new JTextField();
        prNameTXT.setFont(FONTBUTTONS);
        
        prNoteLBL = new JLabel("Notitie:");
        prNoteLBL.setFont(FONTBUTTONS);
        prNoteTXT = new JTextArea();
        prNoteTXT.setFont(UBERTABLEINPUTFONT);
        prNoteTXT.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        prNoteTXT.setLineWrap(true);
        prNoteTXT.setWrapStyleWord(true);
        prNoteTXT.setEnabled(true);
        prNoteTXT.setEditable(true);
        prNoteTXT.setBackground(Color.WHITE);
        prNoteSCROLL = new JScrollPane(prNoteTXT);
        
        prSaveBTN = new JButton("Opslaan");
        prCancelBTN = new JButton("Annuleren");
        
        previousButton = new JButton();
        previousButton.setIcon(PREVIOUSBUTTONIMAGEICON); // NOI18N
        
        add(prOverviewSCROLL);
        add(prOverviewLBL);
        add(prNameTXT);
        add(prNameLBL);
        add(prNoteTXT);
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
    }

    private void UpdateScreenBounds() {
        int lblHeight = 30;
        int btnWidth = 100;
        int btnHeight = 40;
        double sW = getBounds().getWidth();
        double sH = getBounds().getHeight();
                
        int previousButXpos = (int)(sW - PROJECTSMENUMARGIN - PREVIOUSBUTTONSIZE);
        
        prCancelBTN.setBounds((int)(sW - btnWidth - PROJECTSMENUMARGIN), (int)(sH - (2.3 * btnHeight)),
        btnWidth, btnHeight);
        prSaveBTN.setBounds((int)(prCancelBTN.getLocation().getX() - btnWidth - (PROJECTSMENUMARGIN / 2)), (int)(prCancelBTN.getLocation().getY()),
        btnWidth, btnHeight);
        
        previousButton.setBounds(previousButXpos - 5, (int)(PROJECTSMENUMARGIN/2), PREVIOUSBUTTONSIZE, PREVIOUSBUTTONSIZE);
                
        prOverviewLBL.setBounds(PROJECTSMENUMARGIN, PROJECTSMENUMARGIN,
                (int)((sW/2)-((2 * PROJECTSMENUMARGIN)+(sW - previousButton.getLocation().getX())) ),lblHeight);
        
        
        prOverviewSCROLL.setLocation(PROJECTSMENUMARGIN,
                (int)(prOverviewLBL.getLocation().getY() + prOverviewLBL.getSize().getHeight()));
        int prOverviewSCROLLH = (int)(prCancelBTN.getLocation().getY() - (prOverviewSCROLL.getLocation().getY() + (PROJECTSMENUMARGIN / 2)));
                //(sH - (prOverviewSCROLL.getLocation().getY() + prOverviewLBL.getSize().getHeight() + PROJECTSMENUMARGIN));
        
        prOverviewSCROLL.setSize((int)((sW/2)-((2 * PROJECTSMENUMARGIN)+(sW - previousButton.getLocation().getX())) )
                ,prOverviewSCROLLH);
        
        prNameLBL.setLocation((int)(prOverviewSCROLL.getLocation().getX() + prOverviewSCROLL.getSize().getWidth() + (PROJECTSMENUMARGIN / 2)),
             (int)(previousButton.getLocation().getY() + previousButton.getSize().getHeight() + PROJECTSMENUMARGIN));
        prNameLBL.setSize((int)(sW - prNameLBL.getLocation().getX() - (1.5 * PROJECTSMENUMARGIN)),
                (int)(lblHeight));
        prNameTXT.setBounds((int)(prNameLBL.getLocation().getX()),(int)(prNameLBL.getLocation().getY() + prNameLBL.getSize().getHeight()),
                (int)(prNameLBL.getSize().getWidth()),(int)(prNameLBL.getSize().getHeight()));
        
        prNoteLBL.setBounds((int)(prNameTXT.getLocation().getX()),(int)(prNameTXT.getLocation().getY() + prNameTXT.getSize().getHeight() + PROJECTSMENUMARGIN),
                (int)(prNameTXT.getSize().getWidth()),(int)(prNameTXT.getSize().getHeight()));
        prNoteSCROLL.setLocation((int)(prNoteLBL.getLocation().getX()),(int)(prNoteLBL.getLocation().getY() + prNoteLBL.getSize().getHeight()));
        prNoteSCROLL.setSize((int)(prNoteLBL.getSize().getWidth()), (int)(prCancelBTN.getLocation().getY() - (prNoteSCROLL.getLocation().getY() + (PROJECTSMENUMARGIN / 2))) );
//        add(prOverviewSCROLL);
//        add(prOverviewLBL);
//        add(prNameTXT);
//        add(prNameLBL);
//        add(prNoteTXT);
//        add(prNoteLBL);
//        add(prNoteSCROLL);
    }
}
