/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.TableMeuk;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.JScrollPane;
import Model.Action;
import Model.Thought;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import net.miginfocom.swing.MigLayout;

import static view.MainConstants.*;
import static controller.Main.*;

/**
 *
 * @author Administrator
 */
public class UberTablePanel extends JPanel {
    
    private UberTable table;
    private JComboBox[] filterBoxes;
    private JScrollPane scrollpaneTable;
    
    public UberTablePanel(Object[] data, int p_w, int p_h){
        setLayout(null);
        setBounds(0,0,p_w,p_h);
        
        //checkt welke data het panel moet gaan weergeven
        //en stelt het juisten aantal comboboxen in
        if(data instanceof Action[]){
            table = new UberTable((Action[]) data);
            filterBoxes = new JComboBox[8];
        } else if(data instanceof Thought[]){
            table = new UberTable((Thought[]) data);
            filterBoxes = new JComboBox[2];
        } else {
            System.out.println("Verkeerde info doorgestuurt naar uber table! - klasseinfo: " + this.toString());
        }
        
        scrollpaneTable = new JScrollPane(table);
        
        //stelt de bounds in van de het scrollpane
        scrollpaneTable.setBounds(0,HEIGHTOFCOMBOBOXESTABLE,(int)getBounds().getWidth(),(int)getBounds().getHeight()-(HEIGHTOFCOMBOBOXESTABLE));
        
        int posx = 0;
        for(int i = 0; i < filterBoxes.length; i++){
            int comboWidth = table.getTableHeader().getColumnModel().getColumn(i).getWidth()*2;
            filterBoxes[i] = new JComboBox();
            filterBoxes[i].setBounds(posx, 0, comboWidth, HEIGHTOFCOMBOBOXESTABLE);
            add(filterBoxes[i]);
            posx += comboWidth;
        }
        
        add(scrollpaneTable);
        SetListeners();
        
        setVisible(true);
    }
    
    //update alle componenten aan de hand van de size
    public void UpdateSize(int w, int h){
        setBounds(0,0,w,h);
        //table.setBounds(0,HEIGHTOFCOMBOBOXESTABLE + HEIGHTOFTABLEHEADER,(int)getBounds().getWidth(),(int)getBounds().getHeight()-(HEIGHTOFCOMBOBOXESTABLE + HEIGHTOFTABLEHEADER));
        //table.getTableHeader().setBounds(0,HEIGHTOFCOMBOBOXESTABLE,(int)getBounds().getWidth(),HEIGHTOFTABLEHEADER);
        scrollpaneTable.setBounds(0,HEIGHTOFCOMBOBOXESTABLE,(int)getBounds().getWidth(),(int)getBounds().getHeight()-(HEIGHTOFCOMBOBOXESTABLE));
        int posx = 0;
        for(int i = 0; i < filterBoxes.length; i++){
            int comboWidth = table.getTableHeader().getColumnModel().getColumn(i).getWidth()*2;
            filterBoxes[i].setBounds(posx, 0, comboWidth, HEIGHTOFCOMBOBOXESTABLE);
            posx += comboWidth;
        }
    }
    
    private void SetListeners(){
        TableColumnModelListener tableColumnModelListener = new TableColumnModelListener() {
      public void columnAdded(TableColumnModelEvent e) {
        System.out.println("Added");
      }

      public void columnMarginChanged(ChangeEvent e) {
          int amountColumns = table.getColumnModel().getColumnCount();
          //System.out.print("Margins - ");
          int posx = 0;
          for(int i = 0; i < amountColumns; i++){
              int comboWidth = table.getTableHeader().getColumnModel().getColumn(i).getWidth();
              //System.out.print("" + i + ": " + table.getColumnModel().getColumn(i).getWidth() + ", ");
              filterBoxes[i].setBounds(posx, 0, comboWidth, HEIGHTOFCOMBOBOXESTABLE);
              posx += comboWidth;
          }
          revalidate();
          //System.out.println(""); 
      }

      public void columnMoved(TableColumnModelEvent e) {
        System.out.println("Moved");
      }

      public void columnRemoved(TableColumnModelEvent e) {
        System.out.println("Removed");
      }

      public void columnSelectionChanged(ListSelectionEvent e) {
        System.out.println("Selection Changed");
      }
    };
        
        table.getColumnModel().addColumnModelListener(tableColumnModelListener);
        
        //mouse listener als je er op dubbelklikt, dan haalt ie het ID op om er wat mee te doen
        table.addMouseListener(new MouseAdapter() {
           public void mouseClicked(MouseEvent e) {
              if (e.getClickCount() == 2) {
                 UberTable target = (UberTable)e.getSource();
                 int row = target.getSelectedRow();
                 int column = target.getSelectedColumn();
                 UberTableModel daModel = (UberTableModel) table.getModel();
                 int daID = Integer.parseInt(daModel.getIDAt(row).toString());
                 //@TODO inbouwen dat hier iets komt dat je de gedachte kan bewerken
                 
                 //controller.getModel().GetThought(daID).GetNote()
                 System.out.println("Double clicked: row: " + row + ", col: " + column + ", id: " + daID);
                 }
           }
        });
    }
}
