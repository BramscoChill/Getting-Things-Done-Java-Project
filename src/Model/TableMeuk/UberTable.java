/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.TableMeuk;

import javax.swing.ListSelectionModel;
import java.awt.Toolkit;
import model.Action;
import model.Thought;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import static view.MainConstants.*;

/**
 *
 * @author Administrator
 */
public class UberTable extends JTable {    
    private UberTableModel model;
    private TableRowSorter<TableModel> sorter;
    
    public UberTable(){
        setFont(UBERTABLEFONT);
        setRowHeight(UBERTABLEROWHEIGHT);
    }
    
    public UberTable(Action[] data){
        this();
        model = new UberTableModel((Action[]) data);
        setModel(model);
        DoStuffOnStartup();
    }
    
    public UberTable(Thought[] data){
        this();
        model = null;
        model = new UberTableModel((Thought[]) data);
        setModel(model);
        DoStuffOnStartup();
        //model.get.setWidth(200);
    }    
    
    private void DoStuffOnStartup(){
        getTableHeader().setReorderingAllowed(false); // no movable colums
        setModel(model);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        
        //getTableHeader().getColumnModel().getColumns().nextElement().
        
        setBackground((Color)toolkit.getDesktopProperty("control"));
        
        sorter = new TableRowSorter<TableModel>(model);
        setRowSorter(sorter);
        
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    public void UpdateData(Action[] data){
        model.UpdateActions(data);
       
    }

    public void UpdateData(Thought[] data){
        model.UpdateThoughts(data);
    }
    
      @Override
      public void paintComponent( Graphics g )
      {
        super.paintComponent( g );

        if(PRINTEMPTYTABLEROWS){
            paintEmptyRows( g );
        }
      }


      public void paintEmptyRows( Graphics g )
      {
        Graphics newGraphics = g.create();
        newGraphics.setColor( UIManager.getColor( "Table.gridColor" ) );

        Rectangle rectOfLastRow = getCellRect( getRowCount() - 1, 0, true );
        int firstNonExistentRowY = rectOfLastRow.y; //the top Y-coordinate of the first empty tablerow

        if ( getVisibleRect().height > firstNonExistentRowY ) //only paint the grid if empty space is visible
        {
          //fill the rows alternating and paint the row-lines:
          int rowYToDraw = (firstNonExistentRowY - 1) + getRowHeight(); //minus 1 otherwise the first empty row is one pixel to high
          int actualRow = getRowCount() - 1; //to continue the stripes from the area with table-data

          while ( rowYToDraw < getHeight() )
          {
            if ( actualRow % 2 == 0 ) //kleurt het om de rij
            {
              newGraphics.setColor( TABLEROWCOLOR ); //Color.ORANGE ); //change this to another color (Color.YELLOW, anyone?) to show that only the free space is painted
              newGraphics.fillRect( 0, rowYToDraw, getWidth(), getRowHeight() );
              newGraphics.setColor( UIManager.getColor( "Table.gridColor" ) );
            } 

            newGraphics.drawLine( 0, rowYToDraw, getWidth(), rowYToDraw );

            rowYToDraw += getRowHeight();
            actualRow++;
          }


          //paint the column-lines:
          int x = 0;
          for ( int i = 0; i < getColumnCount(); i++ )
          {
            TableColumn column = getColumnModel().getColumn( i );
            x += column.getWidth(); //add the column width to the x-coordinate

            newGraphics.drawLine( x - 1, firstNonExistentRowY, x - 1, getHeight() );
          }

          newGraphics.dispose();

        } //if empty space is visible

      } //paintEmptyRows


        @Override
      public Component prepareRenderer( TableCellRenderer renderer, int row, int column )
      {
        Component c = super.prepareRenderer( renderer, row, column );

        if ( !isRowSelected( row ) )
        {
          //c.setBackground( row % 2 == 0 ? getBackground() : TABLEROWCOLOR); //Color.ORANGE );
            c.setBackground( row % 2 == 0 ? Color.WHITE : TABLEROWCOLOR); //Color.ORANGE );
        }

        return c;
      }
}
