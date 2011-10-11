/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.TableMeuk;

import Model.Action;
import Model.Thought;
import javax.swing.table.AbstractTableModel;

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
        columnNames = new String[8];
        columnNames[0] = "Beschrijving";
        columnNames[1] = "Notities";
        columnNames[2] = "Status";
        columnNames[3] = "Context";
        columnNames[4] = "Project";
        columnNames[5] = "Datum";
        columnNames[6] = "Datum Gewijzicht";
        columnNames[7] = "Klaar";
        
        this.data = new Object[data.length][9];
        for(int i = 0; i < data.length; i++){
            this.data[i][0] = new Integer(data[i].getID());
            this.data[i][1] = (data[i].getDescription());
            this.data[i][2] = data[i].getNote();
            this.data[i][3] = (data[i].getStatus() != null && data[i].getStatus().getID() != -1) ? data[i].getStatus().getName() : "";
            this.data[i][4] = (data[i].getContext() != null && data[i].getContext().getID() != -1) ? data[i].getContext().getName() : "";
            this.data[i][5] = (data[i].getProject() != null && data[i].getProject().getID() != -1) ? data[i].getProject().getName() : "";
            this.data[i][6] = data[i].getDatumTijd();
            this.data[i][7] = data[i].getStatusChanged();
            this.data[i][8] = data[i].isDone();
        }
        fireTableDataChanged();
    }
    
    public void UpdateThoughts(Thought[] data){
        columnNames = new String[2];
        columnNames[0] = "Gedachte";
        columnNames[1] = "Datum";
        
        this.data = new Object[data.length][3];
        for(int i = 0; i < data.length; i++){
            this.data[i][0] = new Integer(data[i].GetID());
            this.data[i][1] = (data[i].GetNote());
            this.data[i][2] = data[i].GetTimestamp();
        }
        fireTableDataChanged();
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
