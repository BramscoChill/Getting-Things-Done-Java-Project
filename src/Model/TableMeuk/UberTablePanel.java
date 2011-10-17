/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.TableMeuk;

import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableModel;
import javax.swing.JTextField;
import java.util.ArrayList;
import javax.swing.RowFilter;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.JScrollPane;
import model.Action;
import model.Thought;
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
    
    private Object selectedObject;
    private String type;
    
    public ActionListener selectionChangedTableDoubleClick;
    public ActionListener selectionChangedTableOneClick;
    
    //rowfilters
    private List<RowFilter<Object,Object>> rfs;
    

    
    public UberTablePanel(Object[] data, int p_w, int p_h){
        setLayout(null);
        setBounds(0,0,p_w,p_h);
        
        //checkt welke data het panel moet gaan weergeven
        //en stelt het juisten aantal comboboxen in
        if(data instanceof Action[]){
            table = new UberTable((Action[]) data);
            filterBoxes = new JComboBox[6];
            type = "action"; 
            rfs = new ArrayList<RowFilter<Object,Object>>(8);
        } else if(data instanceof Thought[]){
            table = new UberTable((Thought[]) data);
            filterBoxes = new JComboBox[2];
            type = "thought";
            rfs = new ArrayList<RowFilter<Object,Object>>(2);
            
        } else {
            //System.out.println("Verkeerde info doorgestuurt naar uber table! - klasseinfo: " + this.toString());
        }
        
        scrollpaneTable = new JScrollPane(table);
        
        //stelt de bounds in van de het scrollpane
        scrollpaneTable.setBounds(0,HEIGHTOFCOMBOBOXESTABLE,(int)getBounds().getWidth(),(int)getBounds().getHeight()-(HEIGHTOFCOMBOBOXESTABLE));
        
        int posx = 0;
        for(int i = 0; i < filterBoxes.length; i++){
            //int comboWidth = table.getTableHeader().getColumnModel().getColumn(i).getWidth()*2;
            filterBoxes[i] = new JComboBox();
            filterBoxes[i].setBackground(Color.WHITE);
            filterBoxes[i].setEditable(true);
            rfs.add(RowFilter.regexFilter("", i));
            final int finalI = i;
            JTextComponent comboEditorComponent = (JTextComponent)filterBoxes[i].getEditor().getEditorComponent();
            comboEditorComponent.getDocument().addDocumentListener(new DocumentListener() { 
              public void changedUpdate(DocumentEvent e) { 
                UpdateFilter(finalI); 
              } 
              public void removeUpdate(DocumentEvent e) { 
                UpdateFilter(finalI); 
              } 
              public void insertUpdate(DocumentEvent e) { 
                UpdateFilter(finalI); 
              } 

 
            }); 
            
            filterBoxes[i].addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                //System.out.println("Combo selection changed!");
                String input = filterBoxes[finalI].getSelectedItem().toString().trim().toLowerCase();
                UpdateFilter(finalI, input); 
            }
        });
            //filterBoxes[i].setBounds(posx, 0, comboWidth, HEIGHTOFCOMBOBOXESTABLE);
            add(filterBoxes[i]);
            //posx += comboWidth;
        }
        
        add(scrollpaneTable);
        SetListeners();
        
        
        //zet de colum size in, afhankelijk van het type
        if(data instanceof Action[]){
            table.getColumnModel().getColumn(0).setPreferredWidth(300);
            //table.getColumnModel().getColumn(table.getColumnModel().getColumnCount()-1).setPreferredWidth(15);
            
        } else if(data instanceof Thought[]){
            
        } else {
            //System.out.println("Verkeerde info doorgestuurt naar uber table! - klasseinfo: " + this.toString());
        }
        
        
        setVisible(true);
    }
    
    
    //update alle componenten aan de hand van de size
    public void UpdateSize(int x, int y, int w, int h){
        setBounds(x,y,w,h);
        //table.setBounds(0,HEIGHTOFCOMBOBOXESTABLE + HEIGHTOFTABLEHEADER,(int)getBounds().getWidth(),(int)getBounds().getHeight()-(HEIGHTOFCOMBOBOXESTABLE + HEIGHTOFTABLEHEADER));
        //table.getTableHeader().setBounds(0,HEIGHTOFCOMBOBOXESTABLE,(int)getBounds().getWidth(),HEIGHTOFTABLEHEADER);
        scrollpaneTable.setBounds(0,HEIGHTOFCOMBOBOXESTABLE,(int)getBounds().getWidth(),(int)getBounds().getHeight()-(HEIGHTOFCOMBOBOXESTABLE));
        int posx = 0;
        for(int i = 0; i < filterBoxes.length; i++){
            int comboWidth = table.getTableHeader().getColumnModel().getColumn(i).getWidth();
            filterBoxes[i].setBounds(posx, 0, comboWidth, HEIGHTOFCOMBOBOXESTABLE);
            posx += comboWidth;
        }
    }
    
    public void UpdateActions(Action[] data){
        //table = new UberTable(data);
        table.UpdateData(data);
    }
    
    public void UpdateThoughts(Thought[] data){
        table.UpdateData(data);
    }
    
    private void SetListeners(){
        TableColumnModelListener tableColumnModelListener = new TableColumnModelListener() {
      public void columnAdded(TableColumnModelEvent e) {
        //System.out.println("Added");
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
        //System.out.println("Moved");
      }

      public void columnRemoved(TableColumnModelEvent e) {
        //System.out.println("Removed");
      }

      public void columnSelectionChanged(ListSelectionEvent e) {
        //System.out.println("Selection Changed");
      }
    };
        
        table.getColumnModel().addColumnModelListener(tableColumnModelListener);
        
        //mouse listener als je er op dubbelklikt, dan haalt ie het ID op om er wat mee te doen
        table.addMouseListener(new MouseAdapter() {
           public void mouseClicked(MouseEvent e) {
              if (e.getClickCount() == 2) {
                  SetCurrentObject();
                 
                 if(selectionChangedTableDoubleClick != null){
                     selectionChangedTableDoubleClick.actionPerformed(null);
                 }
                 
                 //System.out.println("Double clicked: row: " + row + ", col: " + column + ", id: " + daID);
                 } else if(e.getClickCount() == 1) {

                 }
           }
        });
        
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() { 
            public void valueChanged(ListSelectionEvent e) {
                //als de selectie weggaat roept ie deze ook aan, er moet wel een selectie geplaatst worden!
                if(table.getSelectedRow() > -1){
                
                     SetCurrentObject();
                     System.out.println("one click selectionChangedTableOneClick");
                     if(selectionChangedTableOneClick != null){
                         selectionChangedTableOneClick.actionPerformed(null);
                     }
                }
            }
        });
        
        
    }
    
    private void SetCurrentObject(){
        try{
         UberTable target = table;//(UberTable)e;
         int row = target.getSelectedRow();
         int column = target.getSelectedColumn();
         UberTableModel daModel = (UberTableModel) table.getModel();
         int daID = Integer.parseInt(daModel.getIDAt(row).toString());

         System.out.println("row: " + row + ", daID: " + daID);

         if("thought".equals(type)){
             setSelectedObject(controller.GetModel().GetThought(daID));

         } else if("action".equals(type)){
             setSelectedObject(controller.GetModel().GetAction(daID));
         } else {

         }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
    
    public Object getSelectedObject() {
        return selectedObject;
    }

    public void setSelectedObject(Object selectedObject) {
        this.selectedObject = selectedObject;
    }
    
    public void DeselectAll(){
        table.getSelectionModel().clearSelection();
    }              
    public void UpdateFilter(int columnIndex, String input) { 
        //er moeten wel rijen zijn om op te filteren!
        if(table.getRowCount() > 0){
            //trekt de input uit de combo
            //JTextField inputField = (JTextField) filterBoxes[columnIndex].getEditor().getEditorComponent();
            //String input = inputField.getText().trim().toLowerCase();
            if(! input.isEmpty()){

              rfs.set(columnIndex,RowFilter.regexFilter(input, columnIndex));
            //                      for(int i = 0; i < rfs.size(); i++) {
            //                          if(rfs.get(i) != null){
            //                              
            //                          }
            //                      }
            } else {
              rfs.set(columnIndex,RowFilter.regexFilter("", columnIndex)); 
            }
            RowFilter<Object,Object> af = RowFilter.andFilter(rfs);
            ((TableRowSorter<TableModel>)table.getRowSorter()).setRowFilter(af);

            //System.out.println("Combo Box CHANGED: " + columnIndex + "\nInput: " + input);
        }
    }
    
    public void UpdateFilter(int columnIndex) { 
        //trekt de input uit de combo
        JTextField inputField = (JTextField) filterBoxes[columnIndex].getEditor().getEditorComponent();
        String input = inputField.getText().trim().toLowerCase();
        UpdateFilter(columnIndex, input);
    }
    
}
