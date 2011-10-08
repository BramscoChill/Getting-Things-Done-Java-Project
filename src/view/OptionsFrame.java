/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

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
    
    private Options options = new Options();
    private JButton saveButton = new JButton("Opslaan");
    
    public OptionsFrame(){
        super(OPTIONSMENUTITLE);
        setLayout(null);
        this.setResizable(true);
        setBounds(100,new Random().nextInt(200)+50,700,500);


        
        setVisible(true);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter(){
           public void windowOpened( WindowEvent e ){
                //field1.requestFocus();
             }
           public void windowClosing( WindowEvent e ){
                   setVisible(false);
                   dispose();
           }
        }); 
    }
    
    public Options GetOptions(){
        return options;
    }
    
    public JButton GetSaveButton(){
        return saveButton;
    }
    
}
