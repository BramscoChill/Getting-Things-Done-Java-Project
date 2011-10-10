/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Dimension;
import Model.Action;
import java.util.Random;
import javax.swing.JFrame;

import static view.MainConstants.*;

/**
 *
 * @author Administrator
 */
public class ActionFrame extends JFrame {
    private Action action;
    
    public ActionFrame(){
        this(new Action());
    }
    
    public ActionFrame(Action action){
        super(ACTIONSMENUTITLE);
        this.action = action;
        setLayout(null);
        this.setResizable(true);
        setBounds(100,new Random().nextInt(200)+50,700,399);
        setMinimumSize(new Dimension(400,350));
        setMaximumSize(new Dimension(9999,400));
        
        setVisible(true);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        
    }
}
