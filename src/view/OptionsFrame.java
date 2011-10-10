/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.util.Map;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Dimension;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import javax.swing.JButton;
import model.Options;
import java.util.Random;
import javax.swing.JFrame;
import static view.MainConstants.*;

/**
 *
 * @author Administrator
 */
public class OptionsFrame extends JFrame {
    
    private JButton saveButton, cancelButton, previousButton;
    private JLabel optionsTitle, googleUsernameLBL, googlePasswordLBL, startupScreenLBL;
    private JTextField googleUsername, googlePassword;
    private JComboBox startupScreen;
    
    //om ervoor te zorgen dat alle instellingen opgeslagen worden als het scherm gesloten wordt
    private Boolean doSave = true;
    
    public OptionsFrame(){
        super(OPTIONSMENUTITLE);
        setLayout(null);
        this.setResizable(true);
        setBounds(100,new Random().nextInt(200)+50,700,399);
        
        setVisible(true);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        SetButtons();
        
        AddListeners(); 
        
        //1ex update, laat alles dus zien
        UpdateScreenBounds();
        
        LoadOptions();
        
    }
    
    private void SetButtons(){
        setMinimumSize(new Dimension(400,350));
        setMaximumSize(new Dimension(9999,400));
        
        saveButton = new JButton("Opslaan");
        cancelButton = new JButton("Annuleren");
        previousButton = new JButton();
        googleUsername = new JTextField();
        googleUsername.setFont(OPTIONSMENUFONTTEXTFIELDS);
        googlePassword = new JTextField();
        googlePassword.setFont(OPTIONSMENUFONTTEXTFIELDS);
        startupScreen = new JComboBox();
        startupScreen.setBackground(Color.WHITE);
        
        for(int i = 0; i < OPTIONSSTARTUPSCREENVALUES.length; i++){
            startupScreen.addItem(OPTIONSSTARTUPSCREENVALUES[i]);
        }
        
        startupScreen.setSelectedIndex(0);
        
        optionsTitle = new JLabel("Opties",JLabel.CENTER);
        optionsTitle.setFont(FONTTITLE);
        //optionsTitle.setBackground(Color.GREEN);
        //optionsTitle.setOpaque(true);
        googleUsernameLBL = new JLabel("Google Gebruikersnaam (zonder @gmail.com)");
//        googleUsernameLBL.setBackground(Color.GREEN);
//        googleUsernameLBL.setOpaque(true);
        googlePasswordLBL = new JLabel("Google Wachtwoord");
        startupScreenLBL = new JLabel("Opstart scherm");
        
        add(optionsTitle);
        add(googleUsernameLBL);
        add(googlePasswordLBL);
        add(startupScreenLBL);
        add(saveButton);
        add(cancelButton);
        add(previousButton);
        add(googleUsername);
        add(googlePassword);
        add(startupScreen);
        
        previousButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/buttonicons/Actions-go-previous-icon2.png"))); // NOI18N
    }
    
    private void UpdateScreenBounds(){
                float margin = OPTIONSMENUMARGIN;
                
                double frameW = getBounds().getWidth();
                double frameH = getBounds().getHeight();
                
                double posX = margin;
                double posY = margin;
                
                double bW = (frameW / 2) - (margin * 2);
                double bH = (frameH / 2) - (margin * 2);
                
                int labelsHeight = 30;
                int labelsWidth = 300;
                
                int bHeight = 30;
                int bWidth = 100;
                
                int previousButWH = 72;
                int previousButXpos = (int) (frameW - margin - previousButWH);
                previousButton.setBounds(previousButXpos - 5, (int)margin, previousButWH, previousButWH);
                
                //googleUsername.setBounds(null);
                
                int optionsTitleW = 300;
                int optionsTitleH = 50;
                int optionsX = (int)(((frameW/2)) - (optionsTitleW/2));
                optionsTitle.setBounds(optionsX,(int)margin,optionsTitleW,optionsTitleH);
                
                int gULx = (int) margin;
                int textfieldsWith = (int)(frameW - margin - margin) - 4;
                googleUsernameLBL.setBounds(gULx,(int) (optionsTitle.getLocation().y + margin + labelsHeight),labelsWidth, labelsHeight);
                googleUsername.setBounds(gULx,(int) (googleUsernameLBL.getLocation().y + labelsHeight),textfieldsWith, labelsHeight);
                googlePasswordLBL.setBounds(gULx,(int) (googleUsername.getLocation().y + margin + labelsHeight),labelsWidth, labelsHeight);
                googlePassword.setBounds(gULx,(int) (googlePasswordLBL.getLocation().y + labelsHeight),textfieldsWith, labelsHeight);
                
                startupScreenLBL.setBounds(gULx,(int) (googlePassword.getLocation().y + margin + labelsHeight),labelsWidth, labelsHeight);
                startupScreen.setBounds(gULx,(int) (startupScreenLBL.getLocation().y + labelsHeight),labelsWidth, labelsHeight);
                
                saveButton.setBounds((int)(frameW - bWidth - bWidth - margin - margin),(int) (frameH - (bHeight * 2.5)),bWidth, bHeight);
                cancelButton.setBounds((int)(frameW - bWidth - margin - 5),(int) (frameH - (bHeight * 2.5)),bWidth,bHeight);
                //System.out.println(" --- Resized " + "fW: " + frameW + "fH: " + frameH + ", optX: " + optionsX);  
                
                
                //zit een bug in setMaximumSize, dit is de workarround
                Robot robbie;
                try
                {
                    robbie = new Robot() ;
                }
                catch (AWTException ex) // not supported on all platforms
                {
                    robbie = null ;
                }
                Point loc = this.getLocationOnScreen();
                Point mouse = MouseInfo.getPointerInfo().getLocation();
                int MAX_X = (int)this.getMaximumSize().getWidth();
                int MAX_Y = (int)this.getMaximumSize().getHeight();
                                    
                if (robbie != null){
                   robbie.mouseMove( (int)Math.min( mouse.getX(),loc.getX()+MAX_X ),
                                 (int)Math.min( mouse.getY(),loc.getY()+MAX_Y ) ) ;
                   setSize( Math.min(MAX_X, getWidth()), 
                          Math.min(MAX_Y, getHeight()) );
                }else{
                   setSize( Math.min(MAX_X, getWidth()), 
                          Math.min(MAX_Y, getHeight()) );
                }
                
                revalidate();
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
        
        saveButton.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                doSave = true;
                DoExit();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                doSave = false;
                DoExit();
            }
        });

        
    }
    
    //sluit dit scherm af en slaat van te voren alles op als dat moet
    public void DoExit(){
        if(doSave){
            SetOptionsValues();
            OPTIONS.SaveOptions(); //slaat de opties op voor het scherm gesloten word
        }
        setVisible(false);
        this.processWindowEvent( new WindowEvent(this, WindowEvent.WINDOW_CLOSING) );
        //dispose();
    }
    
    //knalt alle waardes uit het scherm in de opties
    private void SetOptionsValues(){
       OPTIONS.setGCUsername(googleUsername.getText());
       OPTIONS.setGCPassword(googlePassword.getText());
       //options.setLastOpenedScreen(Options.MenuScreen.THOUGHTS);
        
       //System.out.println(OPTIONSMENUSCREENVALUES.get((String)startupScreen.getSelectedItem()));
       OPTIONS.setLastOpenedScreen(OPTIONSMENUSCREENVALUES.get((String)startupScreen.getSelectedItem()));
    }
    
    private void LoadOptions(){
        googleUsername.setText(OPTIONS.getGClUsername());
        googlePassword.setText(OPTIONS.getGClPassword());
        for (Map.Entry<String,MenuScreen> entry : OPTIONSMENUSCREENVALUES.entrySet()) {
            if(entry.getValue() == OPTIONS.getLastOpenedScreen()){
                startupScreen.setSelectedItem(entry.getKey());
                System.out.println("selected item: " + OPTIONS.getLastOpenedScreen().name() + ", " + OPTIONSMENUSCREENVALUES.get(entry.getKey()));
                break;
            }
        }
    }

}
