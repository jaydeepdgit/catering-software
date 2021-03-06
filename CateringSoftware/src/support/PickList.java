/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
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
public class PickList {
    private PickList.MemberPickListView memberPickListView = new PickList.MemberPickListView(false, "Single");
    private PreparedStatement statement = null;
    private PreparedStatement validation = null;
    private ResultSet resultSet = null;
    private ResultSetMetaData resultSetMeta = null;
    private JTextField pickListComponent = null;
    private JTextField[] returnComponent = null;
    private JComponent nextComponent = null;
    private Connection dataCon = null;
    private JLayeredPane layer = null;
    private Vector column = null;
    private String[] fieldList = null;
    private int pickListColumnWidth = 30;
    private int pickListWidth = 30;
    private int[] firstAssociation = null;
    private int[] secondAssociation = null;
    private int xPos = 0;
    private int yPos = 0;
    private boolean allowBlank = false;
    public static boolean isVisible = false;
    private Library lb = new Library();

    public PickList(Connection connection, int width) {
        try {
            dataCon = connection;
            pickListColumnWidth = width;
        } catch (Exception ex) {
            lb.printToLogFile("Exception at PickList", ex);
        }
    }

    public PickList(Connection connection) {
        try {
            dataCon = connection;
        } catch (Exception ex) {
            lb.printToLogFile("Exception at PickList", ex);
        }
    }

    public void setPreparedStatement(PreparedStatement pstatement) {
        statement = pstatement;
    }

    public void setValidation(PreparedStatement pstatement) {
        validation = pstatement;
    }

    public void setPickListComponent(JTextField text) {
        pickListComponent = text;
        xPos = text.getX();
        yPos = text.getY() + text.getHeight();
    }

    public JTextField getPickListComponent() {
        return pickListComponent;
    }

    public void setLayer(JLayeredPane layer) {
        this.layer = layer;
    }

    public void setNextComponent(JComponent nextComponent) {
        this.nextComponent = nextComponent;
    }

    public void setReturnComponent(JTextField[] returnComponent) {
        this.returnComponent = returnComponent;
    }

    public void setColumn(Vector col) {
        column = col;
    }

    public void setDefaultColumnWidth(int width) {
        this.pickListColumnWidth = width;
    }

    public void setDefaultWidth(int width) {
        this.pickListWidth = width;
    }

    public void setVisible(boolean flag) {
        memberPickListView.setVisible(flag);
        if(!flag) {
            isVisible = false;
        }
    }

    public void setFirstAssociation(int[] firstAssociation) {
        this.firstAssociation = firstAssociation;
    }

    public void setSecondAssociation(int[] secondAssociation) {
        this.secondAssociation = secondAssociation;
    }

    public void setLocation(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public boolean isAllowBlank() {
        return allowBlank;
    }

    public void setAllowBlank(boolean allowBlank) {
        this.allowBlank = allowBlank;
    }

    public void pickListKeyPress(KeyEvent evt) {
        if ((evt.getKeyCode() == java.awt.event.KeyEvent.VK_DOWN) || (evt.getKeyCode() == java.awt.event.KeyEvent.VK_UP)
                || (evt.getKeyCode() == java.awt.event.KeyEvent.VK_PAGE_DOWN) || (evt.getKeyCode() == java.awt.event.KeyEvent.VK_PAGE_UP)) {

            int rowID = memberPickListView.tableView.getSelectedRow();
            int colID = memberPickListView.tableView.getSelectedColumn();

            if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_DOWN) {
                if (memberPickListView.tableView.getRowCount() > 0) {
                    rowID++;
                }
            }
            if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_UP) {
                if (memberPickListView.tableView.getRowCount() > 0) {
                    rowID--;
                }
            }
            if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_PAGE_DOWN) {
                if (memberPickListView.tableView.getRowCount() > 0) {
                    rowID += 10;
                }
            }
            if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_PAGE_UP) {
                if (memberPickListView.tableView.getRowCount() > 0) {
                    rowID -= 10;
                }
            }
            if (rowID < 0) {
                rowID = memberPickListView.tableView.getRowCount() - 1;
            }
            if (rowID >= memberPickListView.tableView.getRowCount()) {
                rowID = 0;
            }
            if (rowID >= 0 && rowID < memberPickListView.tableView.getRowCount()) {
                memberPickListView.tableView.changeSelection(rowID, colID, false, false);
            }
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (memberPickListView.isVisible()) {
                memberPickListView.showOff();
                if (!checkValidate()) {
                   if (secondAssociation != null) {
                       memberPickListView.showOffForAnotherColumn();
                   } else {
                       nextComponent.requestFocusInWindow();
                   }
                }
            } else {
                if (checkValidate()) {
                    nextComponent.requestFocusInWindow();
                }
            }
            if (secondAssociation != null) {
                if (memberPickListView.isVisible()) {
                    if (!checkValidate()) {
                        memberPickListView.showOffForAnotherColumn();
                    }
                } else {
                    nextComponent.requestFocusInWindow();
                }
            }
            isVisible = false;
        }
        memberPickListView.tableView.autoResizeTableColumns();
    }

    public void pickListKeyRelease(KeyEvent evt) {
        if(xPos == 0 || yPos == 0) {
            xPos = pickListComponent.getX();
            yPos = pickListComponent.getY() + pickListComponent.getHeight();
        }

        if (pickListComponent.getText().toUpperCase().trim().length() > 0) {
            if ((evt.getKeyCode() != java.awt.event.KeyEvent.VK_DOWN) && (evt.getKeyCode() != java.awt.event.KeyEvent.VK_UP)
                    && (evt.getKeyCode() != java.awt.event.KeyEvent.VK_PAGE_DOWN) && (evt.getKeyCode() != java.awt.event.KeyEvent.VK_PAGE_UP)) {
                if (pickListComponent.getText().length() == 1
                        && evt.getKeyCode() != KeyEvent.VK_BACK_SPACE
                        && evt.getKeyCode() != KeyEvent.VK_ENTER) {
                    setTagPickListView();
                    layer.remove(memberPickListView);
                    memberPickListView.setLocation(xPos, yPos);
                    memberPickListView.setVisible(true);
                    memberPickListView.repaint();
                    layer.add(memberPickListView);
                    memberPickListView.tableView.setTableColumnWidthDefault(pickListColumnWidth);
                } else if (evt.getKeyCode() != KeyEvent.VK_ENTER) {
                    if (memberPickListView.isVisible()) {
                        memberPickListView.find(pickListComponent.getText());
                        memberPickListView.tableView.setTableColumnWidthDefault(pickListColumnWidth);
                    } else {
                        setTagPickListView();
                        layer.remove(memberPickListView);
                        memberPickListView.setLocation(xPos, yPos);
                        memberPickListView.setVisible(true);
                        memberPickListView.repaint();
                        layer.add(memberPickListView);
                        memberPickListView.find(pickListComponent.getText());
                        memberPickListView.tableView.setTableColumnWidthDefault(pickListColumnWidth);
                    }
                }
            }
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            memberPickListView.setVisible(false);
            isVisible = false;
        }
        memberPickListView.tableView.autoResizeTableColumns();
    }

    private Vector getcolumn() {
        Vector col = null;
        try {
            resultSet = statement.executeQuery();
            resultSetMeta = resultSet.getMetaData();
            if (resultSetMeta.getColumnCount() > 0) {
                if(column == null) {
                    fieldList = new String[resultSetMeta.getColumnCount()];
                    if (firstAssociation == null) {
                        firstAssociation = new int[resultSetMeta.getColumnCount()];
                    }
                    if (secondAssociation == null) {
                        secondAssociation = new int[resultSetMeta.getColumnCount()];
                    }

                    col = new Vector<>();
                    for (int i = 0; i < resultSetMeta.getColumnCount(); i++) {
                        if (firstAssociation == null) {
                            firstAssociation[i] = i;
                        }
                        if (secondAssociation == null) {
                            secondAssociation[i] = i;
                        }
                        fieldList[i] = "";
                        col.addElement(resultSetMeta.getColumnLabel(i + 1));
                    }
                } else {
                    fieldList = new String[column.size()];
                    for(int i=0;i<column.size();i++) {
                        fieldList[i] = column.get(i).toString();
                    }
                    col = column;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return col;
    }

    private void setTagPickListView() {
        boolean[] columnEditableState = {false};
        javax.swing.text.JTextComponent[] returnToComponent = returnComponent;
        int Size = pickListWidth;
        JComponent nextFocusComponent = nextComponent;

        try {
            java.util.Vector columnHeadings = null;
            columnHeadings = getcolumn();
            memberPickListView.setEnvironment(statement, fieldList, dataCon, columnHeadings, columnEditableState, returnToComponent, firstAssociation, secondAssociation, nextFocusComponent, Size);
            memberPickListView.tableView.autoResizeTableColumns();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            columnEditableState = null;
            returnToComponent = null;
            firstAssociation = null;
            fieldList = null;
        }
    }

    private boolean checkValidate() {
        ResultSet rsLocal = null;
        boolean flag = false;
        if (validation != null) {
            try {
                if(allowBlank && returnComponent[0].getText().length() == 0) {
                    return true;
                }
                validation.setString(1, returnComponent[0].getText());
                rsLocal = validation.executeQuery();
                flag = rsLocal.next();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (rsLocal != null) {
                    try {
                        rsLocal.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(PickList.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (validation != null) {
                    try {
                        validation.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(PickList.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(PickList.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } else {
            if(allowBlank && returnComponent[0].getText().length() == 0) {
                return true;
            }
        }
        return flag;
    }

    /**
     * *************************************************
     * Member PickList View ***********************************************
     */
    private class MemberPickListView extends javax.swing.JPanel {
        JTable jtblManual;
        int nextFocus;
        boolean options_required = true;
        private PickList.MemberPickListView.TableView tableView;
        String frmName = "";
        private java.awt.Component[] returnToComponent;
        private javax.swing.JScrollPane jscrp;
        private java.util.Vector rowData;
        private java.util.Vector columnHeadings;
        private boolean[] columnEditableState;
        private int[] associatedColumn;
        private boolean insidePickListView = false;
        private javax.swing.JComponent nextFocusComponent;
        private int Size;
        private int[] hiddenColumns;
        private Connection dataConnection = null;
        private String[] fieldList = null;
        private java.util.Vector data = new java.util.Vector();
        protected java.sql.ResultSet viewRS;

        /**
         * Creates new form MemberPickListView
         */
        public MemberPickListView() {
            initComponents();
            initOtherComponents();
            addMouseListener(new PickList.MemberPickListView.CommonMouseAdapter());
        }

        /**
         * **************
         *
         * @param options_required : For Table or for TextField, pass true for
         * Table or false for TextField..
         * @param frmName : Form Name..
         *
         */
        public MemberPickListView(boolean options_required, String frmName) {
            this.options_required = options_required;
            initComponents();
            initOtherComponents();
            this.frmName = frmName;
            addMouseListener(new PickList.MemberPickListView.CommonMouseAdapter());
        }

        public boolean isInsidePickListView() {
            return insidePickListView;
        }

        private void initOtherComponents() {
            setVisible(false);
            tableView = new PickList.MemberPickListView.TableView();
            tableView.setFont(new Font("Cambria", 1, 12));
            tableView.getTableHeader().setFont(new Font("Cambria", 1, 12));
            tableView.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent ke) {
                    if (ke.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                        int rowID = tableView.getSelectedRow();
                        if (rowID == 0) {
                            rowID = tableView.getRowCount() - 1;
                        } else if (rowID > 0) {
                            rowID -= 1;
                        }
                        tableView.changeSelection(rowID, 0, false, false);
                        tableView.setValueToReturnComponent();
                        showOff();
                    }

                    if (ke.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
                        showOff();
                    }
                }
            });

            tableView.setEnabled(false);
            tableView.addMouseListener(new PickList.MemberPickListView.CommonMouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    tableView.changeSelection(tableView.rowAtPoint(e.getPoint()), 0, false, false);
                }

                public void mouseEntered(java.awt.event.MouseEvent e) {
                    insidePickListView = true;
                }

                public void mouseExited(java.awt.event.MouseEvent e) {
                    insidePickListView = false;
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }
            });

            jscrp = new javax.swing.JScrollPane(tableView);
            setSize(tableView.getWidth() + tableView.getWidth() / 20 + 100, 200);
            jscrp.setSize(tableView.getWidth() + tableView.getWidth() / 20, 200);
            jscrp.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(Color.black), javax.swing.BorderFactory.createBevelBorder(BevelBorder.LOWERED)));
            jscrp.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            jscrp.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            add(jscrp);
            jscrp.show();
        }

        @Override
        public void setLocation(int x, int y) {
            if (options_required) {
                super.setLocation(x + 5, y + 26);
            } else {
                super.setLocation(x, y);
            }
        }

        public void find(String texttosearch) {
            if (texttosearch.equalsIgnoreCase("") == false) {
                searchIn_Vector(texttosearch, 0);
            } else {
                searchIn_Vector("", 0);
            }
            if (tableView.getRowCount() > 0) {
                tableView.changeSelection(0, 0, false, false);
            }
        }

        public void searchIn_Vector(Object searchValue, int searchColumn) {
            int row = 0;
            Object object = null;
            Vector tempData = new Vector();

            try {
                for (row = 0; row < rowData.size(); row++) {
                    object = ((java.util.Vector) rowData.elementAt(row)).elementAt(searchColumn);
                    String s = object.toString().toUpperCase();
                    if (object != null && s.matches(".*"+ searchValue.toString().toUpperCase() +".*")) {
                        tempData.addElement(rowData.elementAt(row));
                    }
                }
                tableView.setData(tempData);

                if (hiddenColumns != null && hiddenColumns.length > 0) {
                    tableView.hideColumns(hiddenColumns);
                }

                if (Size != 0) {
                    tableView.autoResizeTableColumns();
                    if(tableView.getRowCount() == 0 || tableView.getRowCount() <= 10) {
                        setSize(tableView.totalWidth + 20, (tableView.getRowCount()*25)+40);
                        jscrp.setSize(tableView.totalWidth + 10, (tableView.getRowCount()*25)+30);
                    } else if(tableView.getRowCount() > 10) {
                        setSize(tableView.totalWidth + 20, 200);
                        jscrp.setSize(tableView.totalWidth + 10, 190);   
                    }
                } else {
                    tableView.autoResizeTableColumns();
                    if(tableView.getRowCount() == 0 || tableView.getRowCount() <= 10) {
                        setSize(tableView.totalWidth + 20, (tableView.getRowCount()*25)+40);
                        jscrp.setSize(tableView.totalWidth + 10, (tableView.getRowCount()*25)+30);
                    } else if(tableView.getRowCount() > 10) {
                        setSize(tableView.totalWidth + 20, 200);
                        jscrp.setSize(tableView.totalWidth + 10, 190);   
                    }
                    Size = tableView.totalWidth;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                object = null;
            }
        }

        public void setEnvironment(PreparedStatement pstatement, String[] fieldList, Connection dataCon, java.util.Vector columnHeadingsNew, boolean[] columnEditableStateNew, java.awt.Component[] returnToComponentNew, int[] associatedColumnNew, int[] another_associatedColumn, javax.swing.JComponent nextFocusComponent, int Size) {
            this.columnHeadings = columnHeadingsNew;
            this.columnEditableState = columnEditableStateNew;
            this.returnToComponent = returnToComponentNew;
            this.associatedColumn = associatedColumnNew;
            this.dataConnection = dataCon;
            this.fieldList = fieldList;
            this.nextFocusComponent = nextFocusComponent;
            this.Size = Size;

            rowData = null;
            rowData = fetchData(pstatement);
            tableView.setViewEnvironment(rowData, columnHeadings, columnEditableState, returnToComponent, associatedColumn, another_associatedColumn, hiddenColumns, Size);

            if (hiddenColumns != null && hiddenColumns.length > 0) {
                tableView.hideColumns(hiddenColumns);
            }
            tableView.setTableColumnWidthDefault(Size);
            setSize(tableView.totalWidth+20, getHeight()+20);

            jscrp.setLocation(5, 5);
            jscrp.setSize(tableView.totalWidth+10, getHeight() - 10);

            tableView.repaint();
            jscrp.repaint();
            doLayout();
            tableView.changeSelection(0, 0, true, false);
            find("");
        }

        public Vector fetchData(PreparedStatement pstatement) {
            if (dataConnection == null) {
                return data;
            }
            data.removeAllElements();
            java.util.Vector record = new java.util.Vector();
            ResultSetMetaData oraRSMetadata = null;
            int fieldWidth[] = new int[fieldList.length];
            try {
                if (pstatement != null) {
                    viewRS = pstatement.executeQuery();
                    oraRSMetadata = viewRS.getMetaData();
                    while (viewRS.next()) {
                        record = null;
                        record = new java.util.Vector();
                        for (int fieldNo = 1; fieldNo <= oraRSMetadata.getColumnCount(); fieldNo++) {
                            fieldWidth[fieldNo - 1] = oraRSMetadata.getColumnDisplaySize(fieldNo);
                            record.addElement(viewRS.getString(fieldNo));
                        }
                        data.addElement(record);
                    }
                } else {
                    for (int fieldNo = 0; fieldNo < fieldList.length; fieldNo++) {
                        record.addElement("No_Data_Found");
                    }
                    data.addElement(record);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                for (int fieldNo = 0; fieldNo < fieldList.length; fieldNo++) {
                    record.addElement("No_Data_Found");
                }
                data.addElement(record);
            } finally {
                record = null;
                oraRSMetadata = null;
                fieldWidth = null;

                try {
                    viewRS.close();
                    pstatement.close();
                } catch (Exception ex) {
                } finally {
                    pstatement = null;
                    viewRS = null;
                }
            }
            return data;
        }

        public void showOff() {
            boolean setValue = tableView.setValueToReturnComponent();
            setVisible(false);
            if (setValue) {
                nextFocusComponent.requestFocusInWindow();
            }
        }

        public void showOffForAnotherColumn() {
            tableView.setValueToReturnComponentForAnotherColumn();
            setVisible(false);
            nextFocusComponent.requestFocusInWindow();
        }
        /**
         * This method is called from within the constructor to initialize the
         * form. WARNING: Do NOT modify this code. The content of this method is
         * always regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
        private void initComponents() {
            
            setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(Color.black), javax.swing.BorderFactory.createBevelBorder(BevelBorder.LOWERED)));
            
            setEnabled(false);

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
            this.setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 396, Short.MAX_VALUE));
            layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 324, Short.MAX_VALUE));
        }// </editor-fold>                        

        // Variables declaration - do not modify                     
        // End of variables declaration                   
        class CommonMouseAdapter extends java.awt.event.MouseAdapter {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                insidePickListView = true;
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                insidePickListView = false;
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

        /**
         * ***********************************************************
         * Table View
        ***********************************************************
         */
        private class TableView extends javax.swing.JTable {
            int totalWidth = 0;
            private java.awt.Component[] returnToComponent;
            private PickList.MemberPickListView.TableView.CustomModel customModel;
            private java.util.Vector rowData = new java.util.Vector();
            private java.util.Vector columnHeadings = new java.util.Vector();
            private boolean[] columnEditableState = {false};
            private int[] associatedColumn;
            private int[] another_associatedColumn;
            private int[] hiddenColumn;

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
                    lsm.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
                    setSelectionModel(lsm);

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
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    lsm = null;
                    jth = null;
                }
            }

            public void setViewEnvironment(java.util.Vector rowData, java.util.Vector columnHeadings, boolean[] columnEditableState, java.awt.Component[] returnToComponent, int[] associatedColumn, int[] another_associatedColumn, int[] hiddenColumn, int Size) {
                this.rowData = rowData;
                this.columnHeadings = columnHeadings;
                this.columnEditableState = columnEditableState;
                this.returnToComponent = returnToComponent;
                this.associatedColumn = associatedColumn;
                this.another_associatedColumn = another_associatedColumn;
                this.hiddenColumn = hiddenColumn;

                setData(rowData);
                changeSelection(0, 0, true, false);
                setSize(totalWidth, tableView.getRowCount()*20);
            }

            private void initData() {
                customModel = new PickList.MemberPickListView.TableView.CustomModel();
                setModel(customModel);
                setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
            }

            public void setData(java.util.Vector rowData) {
                this.rowData = rowData;
                customModel.setDataVector(rowData, columnHeadings);

                /* Set Default Cellrenderer */
                try {
                    Vector temp = null;
                    if (rowData.size() > 0) {
                        temp = (java.util.Vector) rowData.elementAt(0);
                        for (int i = 0; i < temp.size(); i++) {
                            Object o = temp.elementAt(i);
                            if (o != null) {
                                if ((o instanceof java.sql.Date) || (o instanceof java.sql.Timestamp)) {
                                    TableColumn tc = getColumn(getColumnName(i));
                                    if (tc != null) {
                                        tc.setCellRenderer(new PickList.MemberPickListView.TableView.DateTableCellRender());
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            public String getCurrentSelectedCellValue(int row, int col) {
                String value = null;
                TableModel tm = null;
                Object o;

                try {
                    tm = getModel();
                    o = tm.getValueAt(row, col);
                    if (o == null) {
                        value = "";
                    } else {
                        value = o.toString();
                    }
                    tm = null;
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    tm = null;
                }
                return value;
            }

            /**
             * *
             *
             * @return boolean value to indicate the value is set or not...
             */
            public boolean setValueToReturnComponent() {
                String value = null;
                boolean bReturn = false;
                try {
                    for (int i = 0; i < returnToComponent.length; i++) {
                        if (getSelectedRow() >= 0) {
                            value = getCurrentSelectedCellValue(getSelectedRow(), associatedColumn[i]);
                            if (returnToComponent[i] instanceof JTextField) {
                                ((JTextField) returnToComponent[i]).setText(value);
                            } else if (returnToComponent[i] instanceof JLabel) {
                                ((JLabel) returnToComponent[i]).setText(value);
                            }
                            bReturn = true;
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    value = null;
                }
                return bReturn;
            }

            public void setValueToReturnComponentForAnotherColumn() {
                String value = null;
                try {
                    for (int i = 0; i < returnToComponent.length; i++) {
                        if (getSelectedRow() >= 0) {
                            value = getCurrentSelectedCellValue(getSelectedRow(), another_associatedColumn[i]);
                            if (returnToComponent[i] instanceof JTextField) {
                                ((JTextField) returnToComponent[i]).setText(value);
                            } else if (returnToComponent[i] instanceof JLabel) {
                                ((JLabel) returnToComponent[i]).setText(value);
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    value = null;
                }
            }

            public void setTableColumnWidthDefault(int size) {
                int tableColumnCount = columnModel.getColumnCount();
                javax.swing.table.TableColumn tc = null;
                javax.swing.table.JTableHeader th = getTableHeader();

                try {
                    for (int i = 0; i < tableColumnCount; i++) {
                        tc = getColumn(getColumnName(i));
                        tc.setResizable(false);
                        tc.setMaxWidth(size / getColumnCount());
                        tc.setMinWidth(size / getColumnCount());
                        tc.setWidth(size / getColumnCount());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    tc = null;
                    th = null;
                }
            }

            public void autoResizeTableColumns() {
                int tableColumnCount = columnModel.getColumnCount();
                TableColumn tc = null;
                JTableHeader th = getTableHeader();
                TableColumnModel thtcm = th.getColumnModel();
                int dataWidth = 0, headerWidth = 0;
                int maxColWidth = 0;
                boolean isHidden = false;
                totalWidth = 0;

                try {
                    for (int i = 0; i < tableColumnCount; i++) {
                        isHidden = false;
                        if (!isHidden) {
                            tc = getColumn(getColumnName(i));
                            thtcm.getColumn(i).getWidth();
                            dataWidth = maxCellWidthOfColumn(tc, i) + 10;
                            headerWidth = thtcm.getColumn(i).getWidth();
                            maxColWidth = (dataWidth > headerWidth) ? dataWidth : headerWidth;
                            totalWidth += maxColWidth;
                            tc.setResizable(false);
                            if(maxColWidth<=75){
                                maxColWidth=75;
                            }
                            tc.setMaxWidth(maxColWidth);
                            tc.setMinWidth(maxColWidth);
                            tc.setWidth(maxColWidth);
                        } else {
                            tc = getColumn(getColumnName(i));
                            tc.setWidth(0);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    tc = null;
                    th = null;
                    thtcm = null;
                }
            }

            private int maxCellWidthOfColumn(TableColumn column, int col) {
                int width = 0, maxWidth = 0, colIndex = col;
                TableCellRenderer tcr = null;
                java.awt.Component component = null;
                try {
                    for (int row = 0; row < getRowCount(); row++) {
                        tcr = null;
                        tcr = getCellRenderer(row, colIndex);
                        component = null;
                        component = tcr.getTableCellRendererComponent(this, getValueAt(row, colIndex), false, false, row, colIndex);
                        width = component.getPreferredSize().width;
                        maxWidth = width > maxWidth ? width : maxWidth;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    tcr = null;
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
                    for (int counter = 0; counter < hiddenColumn.length; counter++) {
                        thtcm.removeColumn(thtcm.getColumn(hiddenColumn[counter] - difference));
                        difference++;
                    }
                }
                jth.setColumnModel(thtcm);
                setTableHeader(jth);
                jth = null;
                thtcm = null;
            }

            ///Class Declaration...
            private class CustomModel extends DefaultTableModel {
                public CustomModel() {
                    super();
                    java.util.Vector data = new java.util.Vector();
                    java.util.Vector record = new java.util.Vector();
                    try {
                        record.addElement("***");
                        data.addElement(record);
                        record = new java.util.Vector();
                        record.addElement("Header");
                        setDataVector(rowData, record);
                    } catch (Exception ex) {
                        ex.printStackTrace();
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
                    label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
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
                            data = formatDateUtil.format((java.util.Date) value);
                        }
                        super.setValue(data);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
}