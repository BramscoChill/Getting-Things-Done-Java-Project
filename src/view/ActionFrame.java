/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

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
public class ActionFrame extends JFrame {
    private Action action;
    
    
    public ActionFrame(Action[] actions){
        super(ACTIONSMENUTITLE);
        this.action = action;
        setLayout(null);
        this.setResizable(true);
        setLocation(100,new Random().nextInt(200)+50);
        setMinimumSize(new Dimension(500,500));
        
        setVisible(true);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        
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
        
    }
}
