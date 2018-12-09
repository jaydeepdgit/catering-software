/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

/**
 *
 * @author @JD@
 */
public class MemberPickListView extends javax.swing.JPanel {
    JTable jtblManual;
    int nextFocus;
    boolean options_required = true;
    public NavigationPanel navLoad = null;
    public TableView tableView;
    String frmName = "";
    private java.awt.Component[] returnToComponent;
    private javax.swing.JScrollPane jscrp;
    public ViewData viewData;
    private java.util.Vector rowData;
    private java.util.Vector columnHeadings;
    private boolean[] columnEditableState;
    private int[] associatedColumn ;
    private int[] setColumn ;
    private boolean insidePickListView = false;
    private javax.swing.JComponent nextFocusComponent;
    private int Size;
    private int[] hiddenColumns;
    private Library lb = new Library();

    /** Creates new form MemberPickListView */
    public MemberPickListView() {
        initComponents ();
        initOtherComponents();
        addMouseListener(new CommonMouseAdapter());
    }
    /****************
     *
     * @param options_required : For Table or for TextField, pass true for Table or false for TextField..
     * @param frmName : Form Name..
     * 
     */
    public MemberPickListView(boolean options_required,String frmName) {
        this.options_required = options_required;
        initComponents ();
        initOtherComponents();
        this.frmName = frmName;
        addMouseListener(new CommonMouseAdapter());
    }

    public boolean isInsidePickListView() {
        return insidePickListView;
    }

    private void initOtherComponents() {
        setVisible(false);
        tableView = new TableView();
        tableView.setFont(new Font("Cambria", 1, 12));
        tableView.getTableHeader().setFont(new Font("Cambria", 1, 12));

        tableView.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent ke) {
                if (ke.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    int rowID = tableView.getSelectedRow();
                    if (rowID == 0){
                        rowID = tableView.getRowCount()-1;
                    } else if (rowID > 0){
                        rowID -= 1;
                    }
                    tableView.changeSelection(rowID, 0, false, false);
                    tableView.setValueToReturnComponent();
                    showOff() ;
                }

                if (ke.getKeyCode()==java.awt.event.KeyEvent.VK_ESCAPE) {
                    showOff() ;
                }
            }
        });

        tableView.setEnabled(false);
        tableView.addMouseListener(new CommonMouseAdapter () {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                tableView.changeSelection(tableView.rowAtPoint(e.getPoint()), 0, false, false);
            }
            public void mouseEntered(java.awt.event.MouseEvent e) {
                insidePickListView = true ;
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                insidePickListView = false ;
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }
        });

        jscrp = new javax.swing.JScrollPane (tableView);
        setSize(tableView.getWidth()+tableView.getWidth()/20 + 100, 200);
        jscrp.setSize(tableView.getWidth()+tableView.getWidth()/20, 200);
        jscrp.setHorizontalScrollBarPolicy( javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jscrp.setVerticalScrollBarPolicy( javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(jscrp);
        jscrp.show();
    }

    @Override
    public void setLocation(int x, int y) {
        if(options_required)
            super.setLocation(x+5, y+26);
        else
            super.setLocation(x, y);
    }

    public void refreshData() {
        if (viewData != null && tableView != null) {
            rowData = viewData.fetchData();
            tableView.setData(rowData);
            tableView.repaint();
            jscrp.repaint();
        }
    }

    public void find(String texttosearch) {
        if(texttosearch.equalsIgnoreCase("") == false)
            searchIn_Vector(texttosearch, 0);
        else
            searchIn_Vector("", 0);
        if(tableView.getRowCount() > 0)
            tableView.changeSelection(0,0,false, false);
    }

    public int searchIn(Object searchValue, int searchColumn) {
        int row = 0;
        Object object = null;
        int returnPosition = tableView.getSelectedRow() ;

        try {
            for(row = 0;row < rowData.size(); row++) {
                object = ((java.util.Vector)rowData.elementAt(row)).elementAt(searchColumn);
                String s = object.toString().toUpperCase();
                if (object != null && s.startsWith(searchValue.toString().toUpperCase())) {
                    returnPosition = row;
                    break ;
                }
            }
            tableView.autoResizeTableColumns();
        } catch(Exception ex) {
            lb.printToLogFile ("Exception at MemberPickListView : " ,ex);
        } finally {
            object = null;
        }
        return returnPosition;
    }

    public void searchIn_Vector(Object searchValue, int searchColumn) {
        int row = 0;
        Object object = null;
        Vector tempData = new Vector();

        try {
            for(row = 0;row < rowData.size(); row++) {
                object = ((java.util.Vector)rowData.elementAt(row)).elementAt(searchColumn);
                String s = object.toString().toUpperCase();
                if (object != null && s.startsWith(searchValue.toString().toUpperCase())) {
                    tempData.addElement(rowData.elementAt(row));
                }
            }
            tableView.setData(tempData);

            if (hiddenColumns != null && hiddenColumns.length > 0) {
                tableView.hideColumns(hiddenColumns);
            }

            if(Size != 0) {
                tableView.autoResizeTableColumns();
                setSize(Size+20,getHeight());
                jscrp.setSize(Size+10,getHeight());
            } else {
                tableView.autoResizeTableColumns();
                setSize(tableView.totalWidth+20,getHeight());
                jscrp.setSize(tableView.totalWidth+10,getHeight());
                Size = tableView.totalWidth;
            }
        } catch(Exception ex) {
           lb.printToLogFile("Exception at searchIn_Vector in Member Pick List View",ex);
        } finally{
            object = null;
        }
    }

    //setEnvironment from mihir for Table --starts
    public void setEnvironmentForTable(String query, ViewData viewDataNew , java.util.Vector columnHeadingsNew, boolean[] columnEditableStateNew, int[] setColumnNew, int[] associatedColumnNew,int [] another_associatedColumnNew,JTable jtblManual,int nextFocus) {
        this.columnHeadings      = columnHeadingsNew ;
        this.columnEditableState = columnEditableStateNew ;
        this.setColumn           = setColumnNew ;
        this.associatedColumn    = associatedColumnNew ;
        this.viewData            = viewDataNew ;
        this.jtblManual          = jtblManual;
        this.nextFocus           = nextFocus;

        rowData = viewData.fetchData(query);
        tableView.setViewEnvironmentForTable(rowData, columnHeadings, columnEditableState, setColumn, associatedColumn,hiddenColumns);
        if (hiddenColumns != null && hiddenColumns.length > 0) {
            tableView.hideColumns(hiddenColumns);
        }

        tableView.autoResizeTableColumns();
        setSize(tableView.totalWidth+20,getHeight());
        jscrp.setLocation(5,60);

        jscrp.setLocation(5,5);
        jscrp.setSize(tableView.totalWidth+10,getHeight());

        tableView.repaint();
        jscrp.repaint();
        doLayout();
        tableView.changeSelection(0,0,true, false);
        find("");
    }

    public void setEnvironmentForTable(ViewData viewDataNew , java.util.Vector columnHeadingsNew, boolean[] columnEditableStateNew, int[] setColumnNew, int[] associatedColumnNew,JTable jtblManual,int nextFocus) {
        this.columnHeadings      = columnHeadingsNew ;
        this.columnEditableState = columnEditableStateNew ;
        this.setColumn           = setColumnNew ;
        this.associatedColumn    = associatedColumnNew ;
        this.viewData            = viewDataNew ;
        this.jtblManual          = jtblManual;
        this.nextFocus           = nextFocus;

        rowData = viewData.fetchData();
        tableView.setViewEnvironmentForTable(rowData, columnHeadings, columnEditableState, setColumn, associatedColumn,hiddenColumns);
        if (hiddenColumns != null && hiddenColumns.length > 0) {
            tableView.hideColumns(hiddenColumns);
        }

        tableView.autoResizeTableColumns();
        setSize(tableView.totalWidth+20,getHeight());
        jscrp.setLocation(5,60);

        jscrp.setLocation(5,5);
        jscrp.setSize(tableView.totalWidth+10,getHeight());

        tableView.changeSelection(0,0,true, false);
        tableView.repaint();
        jscrp.repaint();
        doLayout();
        tableView.changeSelection(0,0,true, false);
        find("");
    }

    public void onlyFillTable(Vector viewDataNew , java.util.Vector columnHeadingsNew, boolean[] columnEditableStateNew, int[] setColumnNew, int[] associatedColumnNew,JTable jtblManual,int nextFocus) {
        this.columnHeadings      = columnHeadingsNew ;
        this.columnEditableState = columnEditableStateNew ;
        this.setColumn           = setColumnNew ;
        this.associatedColumn    = associatedColumnNew ;
        this.jtblManual          = jtblManual;
        this.nextFocus           = nextFocus;

        tableView.setViewEnvironmentForTable(viewDataNew, columnHeadings, columnEditableState, setColumn, associatedColumn,hiddenColumns);
        if (hiddenColumns != null && hiddenColumns.length > 0) {
            tableView.hideColumns(hiddenColumns);
        }
        setSize(tableView.totalWidth+20,getHeight()-10);
        jscrp.setLocation(5,60);

        jscrp.setLocation(5,5);
        jscrp.setSize(tableView.totalWidth+10,getHeight()-10);

        tableView.changeSelection(0,0,true, false);//daivish
        tableView.repaint();
        jscrp.repaint();
        doLayout();
        tableView.changeSelection(0,0,true, false);
    }

    // setEnvironment from mihir for TextFields --starts
    public void setEnvironment(String query , ViewData viewDataNew, java.util.Vector columnHeadingsNew, boolean[] columnEditableStateNew, java.awt.Component[] returnToComponentNew, int[] associatedColumnNew , int [] another_associatedColumn , javax.swing.JComponent nextFocusComponent, int Size) {
        this.columnHeadings      = columnHeadingsNew ;
        this.columnEditableState = columnEditableStateNew ;
        this.returnToComponent   = returnToComponentNew ;
        this.associatedColumn    = associatedColumnNew ;
        this.viewData            = viewDataNew ;
        this.nextFocusComponent  = nextFocusComponent;
        this.Size                = Size;

        rowData = viewData.fetchData(query);
        tableView.setViewEnvironment(rowData, columnHeadings, columnEditableState, returnToComponent, associatedColumn,another_associatedColumn ,hiddenColumns,Size);

        if (hiddenColumns != null && hiddenColumns.length > 0) {
            tableView.hideColumns(hiddenColumns);
        }
        tableView.setTableColumnWidthDefault(Size);
        setSize(Size + 10, getHeight());

        jscrp.setLocation(5,5);
        jscrp.setSize(Size, getHeight() - 5);

        tableView.repaint();
        jscrp.repaint();
        doLayout();
        tableView.changeSelection(0, 0, true, false);
        find("");
    }
    // setEnvironment for TextFields --ends

    public void setEnvironment(ViewData viewDataNew , java.util.Vector columnHeadingsNew, boolean[] columnEditableStateNew, java.awt.Component[] returnToComponentNew, int[] associatedColumnNew ) {
        this.columnHeadings      = columnHeadingsNew ;
        this.columnEditableState = columnEditableStateNew ;
        this.returnToComponent   = returnToComponentNew ;
        this.associatedColumn    = associatedColumnNew ;
        this.viewData            = viewDataNew ;

        rowData = viewData.fetchData();
        tableView.setViewEnvironment(rowData, columnHeadings, columnEditableState, returnToComponent, associatedColumn, hiddenColumns);
        if (hiddenColumns != null && hiddenColumns.length > 0) {
            tableView.hideColumns(hiddenColumns);
        }

        setSize(tableView.getWidth()+tableView.getWidth()/20+10, getHeight());

        jscrp.setLocation(5,5);
        jscrp.setSize(tableView.getWidth()+tableView.getWidth()/20, getHeight()-5);

        tableView.repaint();
        jscrp.repaint();
        doLayout();
        tableView.changeSelection(0,0,true, false);
        find("");
    }

    public void setEnvironment(ViewData viewDataNew , java.util.Vector columnHeadingsNew, boolean[] columnEditableStateNew, java.awt.Component[] returnToComponentNew, int[] associatedColumnNew , javax.swing.JComponent nextFocusComponent, int Size) {
        this.columnHeadings      = columnHeadingsNew ;
        this.columnEditableState = columnEditableStateNew ;
        this.returnToComponent   = returnToComponentNew ;
        this.associatedColumn    = associatedColumnNew ;
        this.viewData            = viewDataNew ;
        this.nextFocusComponent  = nextFocusComponent;
        this.Size                = Size;

        rowData = viewData.fetchData();
        tableView.setViewEnvironment(rowData, columnHeadings, columnEditableState, returnToComponent, associatedColumn, hiddenColumns,Size);
        if (hiddenColumns != null && hiddenColumns.length > 0) {
            tableView.hideColumns(hiddenColumns);
        }
        tableView.setTableColumnWidthDefault(Size);
        setSize(Size+10,getHeight());

        jscrp.setLocation(5,5);
        jscrp.setSize(tableView.getWidth()+tableView.getWidth()/20, getHeight()-5);

        tableView.repaint();
        jscrp.repaint();
        doLayout();
        tableView.changeSelection(0,0,true, false);
        find("");
    }

    public void setEnvironmentFilter(ViewData viewDataNew , java.util.Vector columnHeadingsNew, boolean[] columnEditableStateNew, java.awt.Component[] returnToComponentNew, int[] associatedColumnNew , javax.swing.JComponent nextFocusComponent, int Size,String strType) {
        this.columnHeadings      = columnHeadingsNew ;
        this.columnEditableState = columnEditableStateNew ;
        this.returnToComponent   = returnToComponentNew ;
        this.associatedColumn    = associatedColumnNew ;
        this.viewData            = viewDataNew ;
        this.nextFocusComponent  = nextFocusComponent;
        this.Size                = Size;

        rowData = viewData.fetchDataForStock(strType); //Changed by Piyush
        tableView.setViewEnvironment(rowData, columnHeadings, columnEditableState, returnToComponent, associatedColumn, hiddenColumns,Size);
        if (hiddenColumns != null && hiddenColumns.length > 0) {
            tableView.hideColumns(hiddenColumns);
        }
        tableView.setTableColumnWidthDefault(Size);
        setSize(Size+10,getHeight());

        jscrp.setLocation(5,5);
        jscrp.setSize(tableView.getWidth()+tableView.getWidth()/20, getHeight()-5);
        tableView.repaint();
        jscrp.repaint();
        doLayout();
        tableView.changeSelection(0,0,true, false);
        find("");
    }

    public void setFocus() {
        requestFocus();
        tableView.requestFocus();
    }

    public void setViewData(ViewData viewData) {
        if (viewData != null && tableView != null) {
            this.viewData = viewData ;
            refreshData();
        }
    }

    public void setHiddenColumns(int [] hiddenColumns) {
        this.hiddenColumns = hiddenColumns;
    }

    public void showOff() {
        boolean setValue = tableView.setValueToReturnComponent();
        PickList.isVisible = false;
        setVisible(false);
        if(setValue)
            nextFocusComponent.requestFocusInWindow();
    }

    // From @JD@
    public void showOffForAnotherColumn() {
        tableView.setValueToReturnComponentForAnotherColumn();
        setVisible(false);
        nextFocusComponent.requestFocusInWindow();
    }

    public boolean setUniqueRow(int[] index,String frmName) {
        boolean errFlag = false;
        String strPrd = jtblManual.getValueAt(jtblManual.getSelectedRow(), index[0]).toString().trim();
        String strBatch = jtblManual.getValueAt(jtblManual.getSelectedRow(), index[1]).toString().trim();
        boolean bExist = false;
        for(int iRow = 0; iRow < jtblManual.getRowCount(); iRow++) {
            if(iRow != jtblManual.getSelectedRow()) {
                if(jtblManual.getValueAt(iRow, 1).toString().trim().equalsIgnoreCase(strPrd)
                        && jtblManual.getValueAt(iRow, 3).toString().trim().equalsIgnoreCase(strBatch)) {
                    JOptionPane.showMessageDialog(null, "This product is already listed...");
                    bExist = true;
                    jtblManual.setValueAt("",jtblManual.getSelectedRow(), 3);
                    jtblManual.requestFocusInWindow();
                    jtblManual.changeSelection(jtblManual.getSelectedRow(), 3, false, false);
                    break;
                }
            }
        }
        if(!bExist) {
            editTableCell(jtblManual, jtblManual.getSelectedRow(), nextFocus);
        }
        return errFlag;
    }

    public void showOffTable() {
        if(tableView.getRowCount() > 0)
            tableView.setValueToReturnComponentForTable(jtblManual);
        setVisible(false);

        try {
            if(tableView.getRowCount() > 0) {
                if(navLoad != null) {
                    if(!(jtblManual.getValueAt(jtblManual.getSelectedRow(), 3).toString().trim().equals(""))) {
                        String strProduct = jtblManual.getValueAt(jtblManual.getSelectedRow(), 1).toString().trim();
                        String strBatch = jtblManual.getValueAt(jtblManual.getSelectedRow(), 3).toString().trim();
                        boolean bExist = false;
                        for(int iRow = 0; iRow < jtblManual.getRowCount(); iRow++) {
                            if(iRow != jtblManual.getSelectedRow()) {
                                if(jtblManual.getValueAt(iRow, 1).toString().trim().equalsIgnoreCase(strProduct) &&
                                    jtblManual.getValueAt(iRow, 3).toString().trim().equalsIgnoreCase(strBatch)) {
                                    JOptionPane.showMessageDialog(null, "This product is already listed...");
                                    bExist = true;
                                    jtblManual.setValueAt("", jtblManual.getSelectedRow(), 3);
                                    jtblManual.requestFocusInWindow();
                                    jtblManual.changeSelection(jtblManual.getSelectedRow(), 3, false, false);
                                    break;
                                }
                            }
                        }
                        if(!bExist) {
                            editTableCell(jtblManual, jtblManual.getSelectedRow(), nextFocus);
                        }
                    }
                } else {
                    editTableCell(jtblManual, jtblManual.getSelectedRow(), nextFocus);
                }
            }
        } catch(Exception ex) {
            lb.printToLogFile("Exception at Set Focus in Column of JTable..: " ,ex);
        }
    }

    public void editTableCell(JTable tableViewOnPanel,int iRow,int iColumn) {
        try {
            if(tableViewOnPanel.isCellEditable(iRow, iColumn)) {
                tableViewOnPanel.requestFocusInWindow();
                tableViewOnPanel.changeSelection(tableViewOnPanel.getSelectedRow(), iColumn,false,false);
                tableViewOnPanel.editCellAt(tableViewOnPanel.getSelectedRow(), iColumn);
                DefaultCellEditor df = (DefaultCellEditor) tableViewOnPanel.getCellEditor();
                JTextField jc = (JTextField) df.getComponent();
                JTextField jt = (JTextField) df.getTableCellEditorComponent(tableViewOnPanel, jc.getText(), true, iRow, iColumn);
                jt.setText(jt.getText().trim());
                Highlighter hc = jt.getHighlighter();
                DefaultHighlighter.DefaultHighlightPainter high_painter = new DefaultHighlighter.DefaultHighlightPainter(new Color(198, 198, 250));
                hc.addHighlight(0, jt.getText().length(), high_painter);
                jt.select(0, jt.getText().length());
                jt.selectAll();
            }
        } catch (Exception ex) {
            lb.printToLogFile("ERror in MemberPickListView : " + "icol :" + iColumn ,ex);
        }
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(253, 243, 243));
        setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 3, 3, new java.awt.Color(235, 35, 35)));
        setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 396, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 324, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    class CommonMouseAdapter extends java.awt.event.MouseAdapter {
        public void mouseEntered(java.awt.event.MouseEvent e) {
            insidePickListView = true ;
        }
        public void mouseExited(java.awt.event.MouseEvent e) {
            insidePickListView = false ;
        }
        @Override
        public void mouseClicked(MouseEvent e) {
        }
        @Override
        public void mousePressed(MouseEvent e) {
        }
        @Override
        public void mouseReleased(MouseEvent e) {
        }
    }

    class CommonMouseMotionAdapter extends java.awt.event.MouseMotionAdapter {
        public void mouseDraged(java.awt.event.MouseEvent e) {
        }
        public void mouseMoved(java.awt.event.MouseEvent e) {
        }
    }
}