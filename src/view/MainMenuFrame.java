/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.font.FontRenderContext;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.awt.RenderingHints;
import java.awt.Graphics2D;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;
import java.util.Random;
import javax.swing.JFrame;

import static view.MainConstants.*;

/**
 *
 * @author Administrator
 */
public class MainMenuFrame extends JFrame {
    
    private JButton[] mainButtons = new JButton[4];
    private JMenuBar menuBar;
    private JMenu mFile, mExtra;
    private JMenuItem miOptions, miExit;
    
    private BufferedImage image = MENUSBACKGROUNDBI();
    private JLabel imageBackLBL;
    //private JRadioButtonMenuItem rbMenuItem;
    //private JCheckBoxMenuItem cbMenuItem;

    
    public MainMenuFrame(){
        super(MAINMENUTITLE);
        setLayout(null);
        this.setResizable(true);
        setBounds(100,new Random().nextInt(200)+50,500,500);
        
        //AddImageBack();
        
        imageBackLBL = new JLabel();
        mainButtons[0] = new JButton("Gedachten");
        mainButtons[1] = new JButton("Acties");
        mainButtons[2] = new JButton("Projecten");
        mainButtons[3] = new JButton("Geschiedenis");
        mainButtons[0].setFont(FONTBUTTONS);
        mainButtons[1].setFont(FONTBUTTONS);
        mainButtons[2].setFont(FONTBUTTONS);
        mainButtons[3].setFont(FONTBUTTONS);
        add(mainButtons[0]);
        add(mainButtons[1]);
        add(mainButtons[2]);
        add(mainButtons[3]);
        add(imageBackLBL);
        
        //this.setBackground(Color.BLACK);
        AddListeners();
        
        CreateMenuBar();
        
        setVisible(true);
        
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        setMinimumSize(new Dimension(350,350));
        
        
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
                
                float margin = MAINMENUBUTTONSMARGIN;
                
                double frameW = getBounds().getWidth();
                double frameH = getBounds().getHeight();
                
                double posX = margin;
                double posY = margin;
                
                double bW = (frameW / 2) - (margin * 2) + 5;
                double bH = (frameH / 2) - (margin * 2) - (menuBar.getBounds().getHeight() / 1.5);
                
                //System.out.println(e.getComponent().getClass().getName() + " --- Resized " + "fW: " + frameW + ", bW: " + bW);  
//                Image img = getScaledInstance(MENUSBACKGROUNDBI(),(int) frameW, (int) frameH, RenderingHints.VALUE_INTERPOLATION_BILINEAR , false);
//                imageBackLBL.setBounds(0, 0,(int) frameW, (int)frameH);
//                imageBackLBL.setIcon(new ImageIcon(img));
                
                for(int i = 0; i < mainButtons.length; i++){
                    mainButtons[i].setBounds((int)posX,(int)posY,(int)bW,(int)bH);
                    
                    if(((i+1) % 2) == 0){
                        posX = margin;
                        posY += margin + bH;
                    } else {
                        posX += margin + bW;
                    }
                }
                
                revalidate();
            }

            public void componentShown(ComponentEvent e) {
                //System.out.println(e.getComponent().getClass().getName() + " --- Shown");

            }
        });
        
        
    }
    
    private void CreateMenuBar(){
        menuBar = new JMenuBar();

        //Build the first menu.
        mFile = new JMenu("Bestand");
        mFile.setMnemonic(KeyEvent.VK_F);
        mFile.getAccessibleContext().setAccessibleDescription(
                "The only menu in this program that has menu items");
        menuBar.add(mFile);
        
        mExtra = new JMenu("Extra");
        mExtra.setMnemonic(KeyEvent.VK_E);
        mExtra.getAccessibleContext().setAccessibleDescription(
                "The only menu in this program that has menu items");
        menuBar.add(mExtra);

        //a group of JMenuItems
        miExit = new JMenuItem("Alles afsluiten",
                                 KeyEvent.VK_Q);
        miExit.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
        miExit.getAccessibleContext().setAccessibleDescription(
                "This doesn't really do anything");
        mFile.add(miExit);

        miOptions = new JMenuItem("Opties"); //,new ImageIcon("images/middle.gif"));
        miOptions.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_O, ActionEvent.ALT_MASK));
        mExtra.add(miOptions);
        
        setJMenuBar(menuBar);

//        menuItem = new JMenuItem(new ImageIcon("images/middle.gif"));
//        menuItem.setMnemonic(KeyEvent.VK_D);
//        menu.add(menuItem);

        //a group of radio button menu items
//        menu.addSeparator();
//        ButtonGroup group = new ButtonGroup();
//        rbMenuItem = new JRadioButtonMenuItem("A radio button menu item");
//        rbMenuItem.setSelected(true);
//        rbMenuItem.setMnemonic(KeyEvent.VK_R);
//        group.add(rbMenuItem);
//        menu.add(rbMenuItem);
//
//        rbMenuItem = new JRadioButtonMenuItem("Another one");
//        rbMenuItem.setMnemonic(KeyEvent.VK_O);
//        group.add(rbMenuItem);
//        menu.add(rbMenuItem);
//
//        //a group of check box menu items
//        menu.addSeparator();
//        cbMenuItem = new JCheckBoxMenuItem("A check box menu item");
//        cbMenuItem.setMnemonic(KeyEvent.VK_C);
//        menu.add(cbMenuItem);
//
//        cbMenuItem = new JCheckBoxMenuItem("Another one");
//        cbMenuItem.setMnemonic(KeyEvent.VK_H);
//        menu.add(cbMenuItem);
//
//        //a submenu
//        menu.addSeparator();
//        submenu = new JMenu("A submenu");
//        submenu.setMnemonic(KeyEvent.VK_S);
//
//        menuItem = new JMenuItem("An item in the submenu");
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_2, ActionEvent.ALT_MASK));
//        submenu.add(menuItem);
//
//        menuItem = new JMenuItem("Another item");
//        submenu.add(menuItem);
//        menu.add(submenu);
//
//        //Build second menu in the menu bar.
//        menu = new JMenu("Another Menu");
//        menu.setMnemonic(KeyEvent.VK_N);
//        menu.getAccessibleContext().setAccessibleDescription(
//                "This menu does nothing");
//        menuBar.add(menu);

        
        

    }
    
    public JButton[] GetButtons(){
        return mainButtons;
    }
    
    public JMenuItem GetOptionsMenuItem(){
        return miOptions;
    }
    
    public JMenuItem GetExitMenuItem(){
        return miExit;
    }
    
    
    
}

