/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.TableMeuk;

import model.Action;
import model.Thought;
import javax.swing.table.AbstractTableModel;

import static view.MainConstants.*;

/**
 *
 * @author Administrator
 */
public class UberTableModel extends AbstractTableModel {
    private String[] columnNames;
    private Object[][] data;
    
    
    public UberTableModel(Thought[] data){
        UpdateThoughts(data);
    }
    
    public UberTableModel(Action[] data){
        UpdateActions(data);
    }
    
    public void UpdateThoughts(Thought[] data){
        columnNames = new String[2];
        columnNames[0] = "Gedachte";
        columnNames[1] = "Datum";
        
        this.data = new Object[data.length][3];
        //System.out.println("date len: " + data.length);
        for(int i = 0; i < data.length; i++){
            //System.out.println("i: " + i + ", data ID is null: " + (data[i] == null) + ", this.data is null: " + (this.data[i][0] == null));
            this.data[i][0] = new Integer(data[i].GetID());
            this.data[i][1] = (data[i].GetNote());
            this.data[i][2] = data[i].GetTimestamp().toString();
            //System.out.println("this.data[i][1]: " + this.data[i][1]);
        }
        //System.out.println("updated thoughts uberTableModel");
        
        //kan wel eens misgaan als de table leeg is
        try{
            fireTableDataChanged();
        } catch (Exception ex){
            System.out.println("fireTableDataChanged faaltje!");
        }
    }
    
    public void UpdateActions(Action[] data){
        columnNames = new String[6];
        columnNames[0] = "Beschrijving";
        //columnNames[1] = "Notities";
        columnNames[1] = "Status";
        columnNames[2] = "Context";
        columnNames[3] = "Project";
        columnNames[4] = "Datum";
        columnNames[5] = "Datum Gewijzicht";
        //columnNames[6] = "Klaar";
        
        this.data = new Object[data.length][9];
        for(int i = 0; i < data.length; i++){
            this.data[i][0] = new Integer(data[i].getID());
            this.data[i][1] = (data[i].getDescription());
            //this.data[i][2] = data[i].getNote();
            this.data[i][2] = (data[i].getStatus() != null && data[i].getStatus().getID() != -1) ? data[i].getStatus().getName() : "";
            this.data[i][3] = (data[i].getContext() != null && data[i].getContext().getID() != -1) ? data[i].getContext().getName() : "";
            this.data[i][4] = (data[i].getProject() != null && data[i].getProject().getID() != -1) ? data[i].getProject().getName() : "";
            //this.data[i][5] = data[i].getDatumTijd().toString();
            this.data[i][5] = (MakeStringTimestampOfTimestamp(data[i].getDatumTijd()));
            System.out.println("getDatumTijd().toString(): " + data[i].getDatumTijd().getYear());
            this.data[i][6] = (data[i].getStatusChanged() != null) ? (MakeStringTimestampOfTimestamp(data[i].getStatusChanged())) : "";
            System.out.println("data[i].getStatusChanged(): " + data[i].getStatusChanged().getYear());
            //this.data[i][7] = data[i].isDone();
        }
        
        //kan wel eens misgaan als de table leeg is
        try{
            fireTableDataChanged();
        } catch (Exception ex){
            System.out.println("fireTableDataChanged faaltje!");
        }
    }
    

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        //+1 omdat het id bewaart moet blijven maar niet te zien moet zijn
        return data[row][col+1];
    }
    
    //haalt het ID op
    public Object getIDAt(int row) {
        //+1 omdat het id bewaart moet blijven maar niet te zien moet zijn
        return data[row][0];
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
//        if (col < 2) {
//            return false;
//        } else {
//            return true;
//        }
        return false;
    }

    /*
     * +1 omdat het id bewaart moet blijven maar niet te zien moet zijn
     */
    public void setValueAt(Object value, int row, int col) {
        data[row][col+1] = value;
        fireTableCellUpdated(row, col+1);
    }
}
