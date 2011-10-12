/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Dimension;
import java.util.Random;
import Model.TableMeuk.UberTablePanel;
import model.Thought;
import javax.swing.JFrame;

import static view.MainConstants.*;

/**
 *
 * @author Administrator
 */
public class ThoughtsFrame extends JFrame {
    private UberTablePanel tablePanel;
    
    public ThoughtsFrame(){
        super(THOUGHTSMENUTITLE);
        setLayout(null);
        this.setResizable(true);
        setBounds(100,new Random().nextInt(200)+50,700,399);
        setMinimumSize(new Dimension(400,350));
        setMaximumSize(new Dimension(9999,400));
        
        setVisible(true);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        //******************************//
        //************TEST**************//
        setBounds(100,100,500,500);
        
        Thought[] thoughts = new Thought[5];
        for(int i = 0; i < thoughts.length; i++){
            thoughts[i] = new Thought(i+1, "" + i + "bamiii " + i+3*34334, java.sql.Timestamp.valueOf("2011-10-06 11:04:49"));
        }
        
        UberTablePanel tablePanel = new UberTablePanel(new Thought[]{new Thought()},300,300);
        
        add(tablePanel);
        
        tablePanel.UpdateSize(400, 400);
        //************TEST**************//
        //******************************//

    }
    
    public void UpdateThoughts(Thought[] thoughts){
        tablePanel.UpdateThoughts(thoughts);
    }
}
