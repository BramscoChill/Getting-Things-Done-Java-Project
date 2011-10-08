/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import Model.TableMeuk.UberTablePanel;
import Model.Thought;
import javax.swing.JFrame;

/**
 *
 * @author Administrator
 */
public class ThoughtsFrame extends JFrame {
    
    public ThoughtsFrame(){
        super("GTD App");
        setLayout(null);
        
        //******************************//
        //************TEST**************//
        setBounds(100,100,500,500);
        
        Thought[] thoughts = new Thought[5];
        for(int i = 0; i < thoughts.length; i++){
            thoughts[i] = new Thought(i+1, "" + i + "bamiii " + i+3*34334, java.sql.Timestamp.valueOf("2011-10-06 11:04:49"));
        }
        
        UberTablePanel tablePanel = new UberTablePanel(thoughts,300,300);
        
        add(tablePanel);
        
        tablePanel.UpdateSize(400, 400);
        //************TEST**************//
        //******************************//
        
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
