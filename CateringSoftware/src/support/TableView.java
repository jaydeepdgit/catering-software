/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 *
 * @author @JD@
 */
public class TableView extends javax.swing.JTable {
    int totalWidth = 0;

    private java.awt.Component[] returnToComponent;
    private CustomModel customModel ;
    private java.util.Vector rowData = new java.util.Vector() ;
    private java.util.Vector columnHeadings = new java.util.Vector() ;
    private boolean[] columnEditableState = {false};
    private int[] associatedColumn ;
    private int[] another_associatedColumn ;
    private int[] setColumn ;
    private int[] hiddenColumn ;
    private Library lb = new Library();

    public TableView() {
        super();
        initView();
    }

    private void initView() {
        ListSelectionModel lsm = null;
        JTableHeader jth = null;
        try {
            sizeColumnsToFit(-1);
            setCellSelectionEnabled(true);
            setRowSelectionAllowed(true);

            lsm = getSelectionModel();
            lsm.setSelectionMode( javax.swing.ListSelectionModel.SINGLE_SELECTION) ;
            setSelectionModel(lsm) ;

            setGridColor(java.awt.Color.black);

            setSelectionBackground(new java.awt.Color(202, 223, 247));
            setSelectionForeground(java.awt.Color.black);
            initData();
            jth = getTableHeader();

            // these lines hide specified column
            jth.setResizingAllowed(false);
            jth.setReorderingAllowed(false);
            setCellSelectionEnabled(true);
            setTableHeader(jth);
        } catch(Exception ex) {
            lb.printToLogFile("Exception of initView in TableView : ",ex);
        } finally {
            lsm = null;
            jth = null;
        }
    }

    public void setViewEnvironment(java.util.Vector rowData, java.util.Vector columnHeadings, boolean[] columnEditableState, java.awt.Component[] returnToComponent, int[] associatedColumn, int[] hiddenColumn) {
        this.rowData = rowData ;
        this.columnHeadings      = columnHeadings ;
        this.columnEditableState = columnEditableState ;
        this.returnToComponent   = returnToComponent;
        this.associatedColumn    = associatedColumn ;
        this.hiddenColumn        = hiddenColumn;

        setData(rowData);
        autoResizeTableColumns();
        changeSelection(0,0,true,false);
        setSize(columnModel.getTotalColumnWidth(),180);
    }

    //for another associated column
    public void setViewEnvironment(java.util.Vector rowData, java.util.Vector columnHeadings, boolean[] columnEditableState, java.awt.Component[] returnToComponent, int[] associatedColumn, int [] another_associatedColumn , int[] hiddenColumn,int Size) {
        this.rowData = rowData ;
        this.columnHeadings      = columnHeadings ;
        this.columnEditableState = columnEditableState ;
        this.returnToComponent   = returnToComponent;
        this.associatedColumn    = associatedColumn ;
        this.another_associatedColumn = another_associatedColumn;
        this.hiddenColumn        = hiddenColumn;

        setData(rowData);
        changeSelection(0,0,true,false);
        setSize(Size,180);
    }

    public void setViewEnvironment(java.util.Vector rowData, java.util.Vector columnHeadings, boolean[] columnEditableState, java.awt.Component[] returnToComponent, int[] associatedColumn, int[] hiddenColumn,int Size) {
        this.rowData = rowData ;
        this.columnHeadings      = columnHeadings ;
        this.columnEditableState = columnEditableState ;
        this.returnToComponent   = returnToComponent;
        this.associatedColumn    = associatedColumn ;
        this.hiddenColumn        = hiddenColumn;

        setData(rowData);
        changeSelection(0,0,true,false);
        setSize(Size,180);
    }

    public void setViewEnvironmentForTable(java.util.Vector rowData, java.util.Vector columnHeadings, boolean[] columnEditableState, int[] setColumn, int[] associatedColumn,int[] hiddenColumn ) {
        this.rowData = rowData ;
        this.columnHeadings      = columnHeadings ;
        this.columnEditableState = columnEditableState ;
        this.setColumn           = setColumn;
        this.associatedColumn    = associatedColumn ;
        this.hiddenColumn        = hiddenColumn;

        setData(rowData);
        autoResizeTableColumns();
        changeSelection(0,0,true,false);
        setSize(columnModel.getTotalColumnWidth(),180);
    }

    private void initData() {
        customModel = new CustomModel();
        setModel(customModel);
        setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
    }

    public void setData(java.util.Vector rowData) {
        this.rowData = rowData ;
        customModel.setDataVector(rowData, columnHeadings);

        /* Set Default Cellrenderer */
        try {
            Vector temp = null;
            if (rowData.size() > 0) {
                temp = (java.util.Vector)rowData.elementAt(0);
                for(int i= 0; i < temp.size() ; i++) {
                    Object o = temp.elementAt(i);
                    if ( o != null) {
                        if ((o instanceof java.sql.Date) || (o instanceof java.sql.Timestamp)) {
                            TableColumn tc =  getColumn(getColumnName(i));
                            if (tc != null) {
                                tc.setCellRenderer(new DateTableCellRender());
                            }
                        }
                        if((o instanceof java.lang.Double)) {
                            TableColumn tc =  getColumn(getColumnName(i));
                            if (tc != null) {
                                tc.setCellRenderer(new StatusColumnCellRenderer());
                            }
                        }
                    }
                }
            }
        } catch(Exception ex) {
            lb.printToLogFile("Exception in setData of TableView : ",ex);
        }
    }

    public void changeTableAssociations(int currentLocation, int newLocation, java.util.Vector rd) {
        Object[] fieldList = columnHeadings.toArray();
        String stringToChange = (String)fieldList [currentLocation];
        String stringToMove   = (String)fieldList [newLocation];
        int swapVal = 0;
        try {
            fieldList [currentLocation] = stringToMove ;
            fieldList [newLocation] = stringToChange;
            columnHeadings = null;
            columnHeadings = new java.util.Vector();
            if ( associatedColumn.length > newLocation && associatedColumn.length > currentLocation){
                swapVal = associatedColumn[currentLocation] ;
                associatedColumn[currentLocation] = associatedColumn[newLocation];
                associatedColumn[newLocation] = swapVal ;
            } else{
                associatedColumn[0] = currentLocation;
            }
            for(int i = 0; i< fieldList.length; i++){
                columnHeadings.addElement((String)fieldList[i]);
            }

            setData(rd);
            autoResizeTableColumns();
            hideColumns(hiddenColumn);
        } catch(Exception ex) {
            lb.printToLogFile("Exception of changeTableAssociations in TableView : ",ex);
        } finally {
            fieldList = null;
            stringToChange = null;
            stringToMove   = null;
        }
    }

    public String getCurrentSelectedCellValue(int row, int col) {
        String value = null;
        TableModel tm = null;
        Object o;

        try {
            tm = getModel() ;
            o = tm.getValueAt(row, col);
            if (o == null) {
                value = "";
            } else{
                value = o.toString();
            }
            tm = null;
        } catch(Exception ex) {
            lb.printToLogFile("Exception of getCurrentSelectedCellValue in TableView : ",ex);
        } finally {
            tm  = null;
        }
        return value;
    }

    /***
     *
     * @return boolean value to indicate the value is set or not...
     */
    public boolean setValueToReturnComponent() {
        String value = null;
        boolean bReturn = false;
        try {
            for(int i = 0; i < returnToComponent.length; i++) {
                if (getSelectedRow() >= 0) {
                    value = getCurrentSelectedCellValue(getSelectedRow(), associatedColumn[i]);
                    if(returnToComponent[i] instanceof JTextField)
                        ((JTextField)returnToComponent[i]).setText(value);
                    else if(returnToComponent[i] instanceof JLabel)
                        ((JLabel)returnToComponent[i]).setText(value);
                    bReturn = true;
                }
            }
        } catch(Exception ex) {
            lb.printToLogFile("Exception of setValueToReturnComponent in TableView : ",ex);
        } finally {
            value = null;
        }
        return bReturn;
    }

    public void setValueToReturnComponentForAnotherColumn() {
        String value = null;
        try {
            for(int i = 0; i < returnToComponent.length; i++) {
                if (getSelectedRow() >= 0) {
                    value = getCurrentSelectedCellValue(getSelectedRow(), another_associatedColumn[i]);
                    if(returnToComponent[i] instanceof JTextField)
                        ((JTextField)returnToComponent[i]).setText(value);
                    else if(returnToComponent[i] instanceof JLabel)
                        ((JLabel)returnToComponent[i]).setText(value);
                }
            }
        } catch(Exception ex) {
            lb.printToLogFile("Exception of setValueToReturnComponentForAnotherColumn in TableView : ",ex);
        } finally{
            value = null;
        }
    }

    public void setValueToReturnComponentForTable(JTable jtblManual) {
        String value = null;
        try {
            for(int i = 0; i < setColumn.length; i++) {
                if (getSelectedRow() >= 0) {
                    value = getCurrentSelectedCellValue(getSelectedRow(), associatedColumn[i]);
                    jtblManual.changeSelection(jtblManual.getSelectedRow(),setColumn[i], false,false);
                    jtblManual.setValueAt(value, jtblManual.getSelectedRow(), setColumn[i]);
                }
            }
        } catch(Exception ex){
            lb.printToLogFile("Exception of setValueToReturnComponentForTable in TableView : ",ex);
        } finally{
            value = null;
        }
    }

    public void setTableColumnWidthDefault(int size) {
        int tableColumnCount = columnModel.getColumnCount();
        javax.swing.table.TableColumn tc = null;

        try {
            for (int i = 0; i < tableColumnCount; i++) {
                tc = getColumn(getColumnName(i));
                tc.setResizable(false);
                tc.setMaxWidth(size / getColumnCount());
                tc.setMinWidth(size / getColumnCount());
                tc.setWidth(size / getColumnCount());
            }
        } catch (Exception ex) {
            lb.printToLogFile("Exception in TableView : ",ex);
        } finally {
            tc = null;
        }
    }

    public void autoResizeTableColumns() {
        int tableColumnCount = columnModel.getColumnCount();
        TableColumn tc = null;
        JTableHeader th = getTableHeader();
        TableColumnModel thtcm = th.getColumnModel();
        int dataWidth = 0, headerWidth =0;
        int maxColWidth = 0;
        boolean isHidden = false;
        totalWidth = 0;

        try {
            for(int i = 0 ; i < tableColumnCount ; i++) {
                isHidden = false;
                if (!isHidden) {
                    tc = getColumn(getColumnName(i));
                    thtcm.getColumn(i).getWidth();
                    dataWidth = maxCellWidthOfColumn(tc,i)+10;
                    headerWidth =thtcm.getColumn(i).getWidth() ;
                    maxColWidth = (dataWidth > headerWidth) ? dataWidth : headerWidth ;
                    totalWidth += maxColWidth;
                    tc.setResizable(false);
                    tc.setMaxWidth(maxColWidth);
                    tc.setMinWidth(maxColWidth);
                    tc.setWidth(maxColWidth);
                } else {
                    tc = getColumn(getColumnName(i));
                    tc.setWidth(0);
                }
            }
        } catch(Exception ex){
            lb.printToLogFile("Exception of autoResizeTableColumns in TableView : ",ex);
        } finally{
            tc = null;
            th = null;
            thtcm = null;
        }
    }

    private int maxCellWidthOfColumn(TableColumn column, int col) {
        int  width = 0, maxWidth = 0, colIndex = col ;
        TableCellRenderer tcr = null ;
        java.awt.Component component = null;
        try {
            for(int row=0; row < getRowCount(); row++ ) {
                tcr = null;
                tcr = getCellRenderer( row, colIndex );
                component = null;
                component = tcr.getTableCellRendererComponent(this, getValueAt( row, colIndex ), false, false, row, colIndex );
                width = component.getPreferredSize().width;
                maxWidth = width > maxWidth ? width : maxWidth ;
            }
        } catch(Exception ex){
            lb.printToLogFile("Exception of maxCellWidthOfColumn in TableView : ",ex);
        } finally{
            tcr = null ;
            component = null;
        }
        return maxWidth;
    }

    public void hideColumns(int[] columnNumber) {
        this.hiddenColumn = columnNumber;
        JTableHeader jth = getTableHeader();
        TableColumnModel thtcm = jth.getColumnModel();
        int difference = 0;
        if (hiddenColumn.length > 0) {
            for(int counter =0 ; counter < hiddenColumn.length; counter++) {
                thtcm.removeColumn(thtcm.getColumn(hiddenColumn[counter]-difference));
                difference++;
            }
        }
        jth.setColumnModel(thtcm);
        setTableHeader(jth);
        jth = null;
        thtcm = null;
    }

    public void closeTableView() {
        returnToComponent = null;
        customModel = null;
        rowData = null;
        columnHeadings = null;
        columnEditableState = null;
        setColumn=null;
        associatedColumn = null;
        hiddenColumn = null;
    }

    ///Class Declaration...
    private class CustomModel extends DefaultTableModel {
        public CustomModel() {
            super();
            java.util.Vector data = new java.util.Vector ();
            java.util.Vector record = new java.util.Vector ();
            try {
                record.addElement("***");
                data.addElement(record);
                record = new java.util.Vector ();
                record.addElement("Header");
                setDataVector(rowData, record);
            } catch(Exception ex) {
                lb.printToLogFile("Exception of CustomModel in TableView : ",ex);
            } finally {
                data = null;
                record = null;
            }
        }

        public CustomModel(java.util.Vector data, java.util.Vector columnNames) {
            super(data, columnNames);
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return columnEditableState[col];
        }
    }

    // Class Declaration...
    class DateTableCellRender extends DefaultTableCellRenderer {
        private String format = "dd/MM/yyyy";

        @Override
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            javax.swing.JLabel label = (javax.swing.JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setHorizontalAlignment (javax.swing.SwingConstants.CENTER);
            return label; //super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column);
        }
        
        public void setFormat(String format) {
            this.format = format;
            format = null;
        }

        public void setValue(Object value) {
            String data = "";
            try {
                SimpleDateFormat formatDateUtil = new java.text.SimpleDateFormat(format);
                if (value != null) {
                    data = formatDateUtil.format((java.util.Date)value);
                }
                super.setValue(data);
            } catch(Exception ex) {
                lb.printToLogFile("Exception of Formatting Error in TableView : ",ex);
            }
        }
    }

    private class StatusColumnCellRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            // Cells are by default rendered as a JLabel.
            JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

            // Get the status for the current row.
            TableModel tableModel = (TableModel) table.getModel();

            if (Double.parseDouble(tableModel.getValueAt(row, col).toString()) >= 0) {
                l.setForeground(new Color(255, 0, 0));
                l.setText(lb.getcustomFormat(Double.parseDouble(tableModel.getValueAt(row, col).toString())));
                l.setFont(l.getFont().deriveFont(Font.BOLD));
            } else {
                l.setForeground(new Color(0, 0, 255));
                l.setText(lb.getcustomFormat(Double.parseDouble(tableModel.getValueAt(row, col).toString())*-1));
                l.setFont(l.getFont().deriveFont(Font.BOLD));
            }
            // Return the JLabel which renders the cell.
            return l;
        }
    }
}