/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package master;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.event.InternalFrameEvent;
import cateringsoftware.DeskFrame;
import support.Constants;
import support.HeaderIntFrame1;
import support.Library;
import support.ReportTable;
import support.SmallNavigation;
import support.VoucherDisplay;

/**
 *
 * @author @JD@
 */
public class UnitMaster extends javax.swing.JInternalFrame {
    private SmallNavigation navLoad = null;
    private Library lb = new Library();
    private Connection dataConnection = DeskFrame.connMpAdmin;
    private int id = 0;
    private ReportTable unitMaster;

    /**
     * Creates new form UnitMaster
     */
    public UnitMaster() {
        initComponents();
        addNavigation();
        addValidation();
        setCompEnable(false);
        lb.setStatusComboBox(jcmbStatus);
        setVoucher("Last");
        makeChildTable();
        setTitle(Constants.UNIT_MASTER_FORM_NAME);
    }

    private void makeChildTable() {
        unitMaster = new ReportTable();

        unitMaster.AddColumn(0, "Unit Id", 100, java.lang.String.class, null, false);
        unitMaster.AddColumn(1, "Unit Name", 300, java.lang.String.class, null, false);
        unitMaster.AddColumn(2, "Unit Symbol", 100, java.lang.String.class, null, false);
        unitMaster.makeTable();
    }

    public void setID(int id) {
        this.id = id;
        setVoucher("edit");
    }

    private void onViewVoucher() {
        this.dispose();

        String sql = "SELECT id, name, symbol FROM unit_master";
        unitMaster.setColumnValue(new int[]{1, 2, 3});
        String view_title = Constants.UNIT_MASTER_FORM_NAME +" VIEW";

        HeaderIntFrame1 rptDetail = new HeaderIntFrame1(dataConnection, id+"", view_title, unitMaster, sql, Constants.UNIT_MASTER_FORM_ID, 1, this,this.getTitle());
        rptDetail.makeView();
        rptDetail.setVisible(true);

        Component c = DeskFrame.tabbedPane.add(view_title, rptDetail);
        c.setName(view_title);
        DeskFrame.tabbedPane.setSelectedComponent(c);
    }

    public void setStartupFocus() {
        jtxtUnitName.requestFocusInWindow();
    }

    private void addValidation() {
        FieldValidation valid = new FieldValidation();
        jtxtUnitID.setInputVerifier(valid);
        jtxtUnitName.setInputVerifier(valid);
        jtxtUnitSymbol.setInputVerifier(valid);
    }

    class FieldValidation extends InputVerifier {
        @Override
        public boolean verify(JComponent input) {
            boolean val = false;
            if (input.equals(jtxtUnitID)) {
                val = fielddValid(input);
            } else if (input.equals(jtxtUnitName)) {
                val = fielddValid(input);
            } else if (input.equals(jtxtUnitSymbol)) {
                val = fielddValid(input);
            }
            return val;
        }
    }

    private boolean fielddValid(Component comp) {
        navLoad.setMessage("");
        if (comp == jtxtUnitName) {
            if (navLoad.getMode().equalsIgnoreCase("N") || navLoad.getMode().equalsIgnoreCase("E")) {
                if (lb.isBlank(comp)) {
                    navLoad.setMessage("Unit Name should not blank");
                    comp.requestFocusInWindow();
                    return false;
                }
            }
            if (navLoad.getMode().equalsIgnoreCase("N")) {
                if (lb.isExist("unit_master", "name", jtxtUnitName.getText(), dataConnection)) {
                    navLoad.setMessage("Unit Name already exist!");
                    comp.requestFocusInWindow();
                    return false;
                }
            } else if (navLoad.getMode().equalsIgnoreCase("E")) {
                if (lb.isExistForEdit("unit_master", "name", jtxtUnitName.getText(), "id", String.valueOf(id), dataConnection)) {
                    navLoad.setMessage("Unit Name already exist!");
                    comp.requestFocusInWindow();
                    return false;
                }
            }
        }
        if(comp == jtxtUnitSymbol) {
            if (navLoad.getMode().equalsIgnoreCase("N") || navLoad.getMode().equalsIgnoreCase("E")) {
                if (lb.isBlank(comp)) {
                    navLoad.setMessage("Unit Symbol should not blank");
                    comp.requestFocusInWindow();
                    return false;
                }
            }
        }
        return true;
    }

    private void setCompEnable(boolean flag) {
        jtxtUnitID.setEnabled(!flag);
        jtxtUnitName.setEnabled(flag);
        jtxtUnitSymbol.setEnabled(flag);
        jcmbStatus.setEnabled(flag);
        jtxtUnitName.requestFocusInWindow();
    }

    private void setCompText(String text) {
        jtxtUnitID.setText(text);
        jtxtUnitName.setText(text);
        jtxtUnitSymbol.setText(text);
    }

    private boolean validateForm(){
        boolean flag = fielddValid(jtxtUnitName);
        flag = flag && fielddValid(jtxtUnitSymbol);
        return flag;
    }

    private void addNavigation(){
        class Navigation extends SmallNavigation{
            @Override
            public void callNew() {
                setMode("N");
                setSaveFlag(false);
                setCompText("");
                setCompEnable(true);
            }

            @Override
            public void callEdit() {
                setMode("E");
                setSaveFlag(false);
                setCompEnable(true);
            }

            @Override
            public void callSave() {
                try {
                    setSaveFlag(false);
                    boolean valid = validateForm();
                    if(valid) {
                        dataConnection.setAutoCommit(false);
                        saveVoucher();
                        dataConnection.commit();
                        dataConnection.setAutoCommit(true);
                        navLoad.setSaveFlag(true);
                        if(navLoad.getMode().equalsIgnoreCase("N")) {
                            setVoucher("Last");
                        } else if(navLoad.getMode().equalsIgnoreCase("E")) {
                            setVoucher("Edit");
                        }
                        navLoad.setMode("");
                        navLoad.setFirstFocus();
                    }
                } catch (SQLException ex) {
                    try {
                        dataConnection.rollback();
                        dataConnection.setAutoCommit(true);
                        lb.printToLogFile("Error at save In Unit Master", ex);
                    } catch (SQLException ex1) {
                        lb.printToLogFile("Error at rollback save In Unit Master", ex1);
                    }
                }
            }

            @Override
            public void callDelete() {
                try {
//                    if(!lb.isExist("itm_mst", "id", id+"")) {
                        lb.confirmDialog(Constants.DELETE_THIS + "Unit "+ jtxtUnitName.getText() +"?");
                        if(lb.type) {
                            dataConnection.setAutoCommit(false);
                            delete();
                            dataConnection.commit();
                            dataConnection.setAutoCommit(true);
                            setVoucher("Last");
                        }
//                    } else {
//                        navLoad.setMessage("Unit Name is in use");
//                    }
                    navLoad.setFirstFocus();
                } catch (SQLException ex) {
                    try {
                        dataConnection.rollback();
                        dataConnection.setAutoCommit(true);
                        lb.printToLogFile("Error at delete In Unit Master", ex);
                    } catch (SQLException ex1) {
                        lb.printToLogFile("Error at rollback delete In Unit Master", ex1);
                    }
                }
                setSaveFlag(true);
                navLoad.setFirstFocus();
            }

            @Override
            public void callView() {
                onViewVoucher();
            }

            @Override
            public void callFirst() {
                setVoucher("First");
            }

            @Override
            public void callPrevious() {
                setVoucher("Previous");
            }

            @Override
            public void callNext() {
                setVoucher("Next");
            }

            @Override
            public void callLast() {
                setVoucher("Last");
            }

            @Override
            public void callClose() {
                cancelOrClose();
            }

            @Override
            public void callPrint() {
                try {
                    VoucherDisplay vd = new VoucherDisplay(String.valueOf(id), "UM");
                    DeskFrame.addOnScreen(vd, Constants.UNIT_MASTER_FORM_NAME +" PRINT");
                } catch(Exception ex) {
                    lb.printToLogFile("Exception at callPrint In Unit master", ex);
                }
            }

            @Override
            public void setComponentTextFromResultSet() {
                try {
                    id = navLoad.viewData.getInt("id");
                    jtxtUnitID.setText(id+"");
                    jtxtUnitName.setText(navLoad.viewData.getString("name"));
                    jtxtUnitSymbol.setText(navLoad.viewData.getString("symbol"));
                    jcmbStatus.setSelectedItem(lb.getStatusData(viewData.getInt("fk_status_id")+"", "N"));
                    jlblUserName.setText(lb.getUserName(navLoad.viewData.getString("user_cd"), "N"));
                    jlblEditNo.setText(navLoad.viewData.getString("edit_no"));
                    jlblLstUpdate.setText(lb.getTimeStamp(viewData.getTimestamp("time_stamp")));
                } catch (SQLException ex) {
                    lb.printToLogFile("Error at setComponentTextFromResultSet In Unit Master", ex);
                }
            }

            @Override
            public void setComponentEnabledDisabled(boolean flag) {
                jtxtUnitID.setEnabled(!flag);
                jtxtUnitName.setEnabled(flag);
                jtxtUnitSymbol.setEnabled(flag);
                jcmbStatus.setEnabled(flag);
                jtxtUnitName.requestFocusInWindow();
            }
        }
        navLoad = new Navigation();
        navLoad.setVisible(true);
        jPanel1.add(navLoad);
        jPanel1.setVisible(true);
    }

    private void setVoucher(String move) {
        try {
            String sql = "SELECT * FROM unit_master";
            if (move.equalsIgnoreCase("first")) {
                sql += " WHERE id = (SELECT MIN(id) FROM unit_master)";
            } else if (move.equalsIgnoreCase("previous")) {
                sql += " WHERE id = (SELECT MAX(id) FROM unit_master WHERE id < '"+ id +"')";
            } else if (move.equalsIgnoreCase("next")) {
                sql += " WHERE id = (SELECT MIN(id) FROM unit_master WHERE id > '"+ id +"')";
            } else if (move.equalsIgnoreCase("last")) {
                sql += " WHERE id = (SELECT MAX(id) FROM unit_master)";
            } else if (move.equalsIgnoreCase("edit")) {
                sql += " WHERE id = '"+ id +"'";
            }
            navLoad.viewData = navLoad.fetchData(sql);
            if (navLoad.viewData.next()) {
                navLoad.setComponentTextFromResultSet();
            } else {
                if(move.equalsIgnoreCase("last")) {
                    setCompText("");
                }
            }
            setCompEnable(false);
            navLoad.setFirstFocus();
            lb.setPermission(navLoad, Constants.UNIT_MASTER_FORM_ID);
        } catch (Exception ex) {
            lb.printToLogFile("Error at setVoucher In Unit Master", ex);
        }
    }

    private void delete() throws SQLException {
        PreparedStatement psLocal = null;

        psLocal = dataConnection.prepareStatement("DELETE FROM unit_master WHERE id = ?");
        psLocal.setInt(1, id);
        psLocal.executeUpdate();
    }

    private void saveVoucher() throws SQLException {
        PreparedStatement psLocal = null;

        if(navLoad.getMode().equalsIgnoreCase("N")) {
            psLocal = dataConnection.prepareStatement("INSERT INTO unit_master(name, symbol, fk_status_id, edit_no, user_cd, id) VALUES (?, ?, ?, 0, ?, ?)");
            id = lb.generateKey("unit_master", "id");
        } else if(navLoad.getMode().equalsIgnoreCase("E")) {
            psLocal = dataConnection.prepareStatement("UPDATE unit_master SET name = ?, symbol = ?, fk_status_id = ?, user_cd = ?, edit_no = edit_no + 1, time_stamp = CURRENT_TIMESTAMP WHERE id = ?");
        }
        psLocal.setString(1, jtxtUnitName.getText()); // name
        psLocal.setString(2, jtxtUnitSymbol.getText()); // symbol
        psLocal.setString(3, lb.getStatusData(jcmbStatus.getSelectedItem().toString(), "C")); // fk_status_id
        psLocal.setInt(4, DeskFrame.user_id); // user_cd
        psLocal.setInt(5, id); // id
        psLocal.executeUpdate();
    }

    private void cancelOrClose() {
        if(navLoad.getSaveFlag()) {
            this.dispose();
        } else{
            navLoad.setMode("");
            setCompEnable(false);
            setVoucher("Edit");
            navLoad.setMessage("");
            navLoad.setSaveFlag(true);
        }
    }

    @Override
    public void dispose() {
        if (isVisible()) {
            setVisible(false);
        }
        if (isSelected()) {
            try {
                setSelected(false);
            } catch (PropertyVetoException pve) {
                lb.printToLogFile("Exception at dispose in Unit Master", pve);
            }
        }
        if (!isClosed) {
          firePropertyChange(IS_CLOSED_PROPERTY, Boolean.FALSE, Boolean.TRUE);
          isClosed = true;
        }
        fireInternalFrameEvent(InternalFrameEvent.INTERNAL_FRAME_CLOSED);
        DeskFrame.removeFromScreen(DeskFrame.tabbedPane.getSelectedIndex());
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jtxtUnitID = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jtxtUnitName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jtxtUnitSymbol = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jlblLstUpdate = new javax.swing.JLabel();
        jlblEditNo = new javax.swing.JLabel();
        jlblUserName = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jcmbStatus = new javax.swing.JComboBox();

        jPanel1.setBackground(new java.awt.Color(253, 243, 243));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(3, 3, 1, 1, new java.awt.Color(235, 35, 35)));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(253, 243, 243));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 3, 3, new java.awt.Color(235, 35, 35)), "Unit Information", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 2, 16), new java.awt.Color(0, 0, 255))); // NOI18N

        jtxtUnitID.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtUnitID.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtUnitID.setMinimumSize(new java.awt.Dimension(6, 25));
        jtxtUnitID.setPreferredSize(new java.awt.Dimension(6, 25));
        jtxtUnitID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtUnitIDFocusGained(evt);
            }
        });
        jtxtUnitID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtUnitIDKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtUnitIDKeyTyped(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel1.setText("Unit CD");
        jLabel1.setMaximumSize(new java.awt.Dimension(33, 25));
        jLabel1.setMinimumSize(new java.awt.Dimension(33, 25));
        jLabel1.setPreferredSize(new java.awt.Dimension(33, 25));

        jtxtUnitName.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtUnitName.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 0, 0)));
        jtxtUnitName.setMinimumSize(new java.awt.Dimension(6, 25));
        jtxtUnitName.setPreferredSize(new java.awt.Dimension(6, 25));
        jtxtUnitName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtUnitNameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtUnitNameFocusLost(evt);
            }
        });
        jtxtUnitName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtUnitNameKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtUnitNameKeyTyped(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 0, 0));
        jLabel2.setText("Unit Name");
        jLabel2.setMaximumSize(new java.awt.Dimension(49, 25));
        jLabel2.setMinimumSize(new java.awt.Dimension(49, 25));
        jLabel2.setPreferredSize(new java.awt.Dimension(49, 25));

        jLabel3.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 0, 0));
        jLabel3.setText("Unit Symbol");
        jLabel3.setMaximumSize(new java.awt.Dimension(56, 25));
        jLabel3.setMinimumSize(new java.awt.Dimension(56, 25));
        jLabel3.setPreferredSize(new java.awt.Dimension(56, 25));

        jtxtUnitSymbol.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtUnitSymbol.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 0, 0)));
        jtxtUnitSymbol.setMinimumSize(new java.awt.Dimension(6, 25));
        jtxtUnitSymbol.setPreferredSize(new java.awt.Dimension(6, 25));
        jtxtUnitSymbol.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtUnitSymbolFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtUnitSymbolFocusLost(evt);
            }
        });
        jtxtUnitSymbol.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtUnitSymbolKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtUnitSymbolKeyTyped(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel6.setText("User Name:");

        jLabel7.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel7.setText("Edit No:");

        jLabel8.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel8.setText("Last Updated:");

        jlblLstUpdate.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        jlblEditNo.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        jlblUserName.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        jLabel4.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel4.setText("Status");
        jLabel4.setMaximumSize(new java.awt.Dimension(56, 25));
        jLabel4.setMinimumSize(new java.awt.Dimension(56, 25));
        jLabel4.setPreferredSize(new java.awt.Dimension(56, 25));

        jcmbStatus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jcmbStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Active", "Deactive" }));
        jcmbStatus.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jcmbStatus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jcmbStatusKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jlblEditNo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                            .addComponent(jlblUserName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jlblLstUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jtxtUnitID, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtUnitName, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                            .addComponent(jtxtUnitSymbol, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jcmbStatus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(85, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel2, jLabel3, jLabel6, jLabel7, jLabel8});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtxtUnitID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtxtUnitName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtUnitSymbol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jcmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlblUserName, javax.swing.GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlblEditNo, javax.swing.GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlblLstUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel1, jLabel2, jLabel3, jtxtUnitID, jtxtUnitName, jtxtUnitSymbol});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel6, jlblUserName});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel7, jlblEditNo});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel8, jlblLstUpdate});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel4, jcmbStatus});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtxtUnitNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtUnitNameKeyPressed
        lb.enterEvent(evt, jtxtUnitSymbol);
    }//GEN-LAST:event_jtxtUnitNameKeyPressed

    private void jtxtUnitNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtUnitNameKeyTyped
        lb.fixLength(evt, 50);
    }//GEN-LAST:event_jtxtUnitNameKeyTyped

    private void jtxtUnitNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtUnitNameFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtUnitNameFocusGained

    private void jtxtUnitNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtUnitNameFocusLost
        lb.toUpper(evt);
    }//GEN-LAST:event_jtxtUnitNameFocusLost

    private void jtxtUnitSymbolFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtUnitSymbolFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtUnitSymbolFocusGained

    private void jtxtUnitSymbolFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtUnitSymbolFocusLost
        lb.toUpper(evt);
    }//GEN-LAST:event_jtxtUnitSymbolFocusLost

    private void jtxtUnitSymbolKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtUnitSymbolKeyPressed
        lb.enterEvent(evt, jcmbStatus);
    }//GEN-LAST:event_jtxtUnitSymbolKeyPressed

    private void jtxtUnitSymbolKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtUnitSymbolKeyTyped
        lb.fixLength(evt, 30);
    }//GEN-LAST:event_jtxtUnitSymbolKeyTyped

    private void jtxtUnitIDFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtUnitIDFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtUnitIDFocusGained

    private void jtxtUnitIDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtUnitIDKeyPressed
        if(navLoad.getMode().equalsIgnoreCase("")) {
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                if(lb.isExist("unit_master", "id", jtxtUnitID.getText(), dataConnection)) {
                    id = Integer.parseInt(jtxtUnitID.getText());
                    navLoad.setMessage("");
                    setVoucher("edit");
                } else {
                    navLoad.setMessage("Unit CD is invalid");
                }
            }
            jtxtUnitID.requestFocusInWindow();
        }
    }//GEN-LAST:event_jtxtUnitIDKeyPressed

    private void jtxtUnitIDKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtUnitIDKeyTyped
        lb.onlyInteger(evt, 7);
    }//GEN-LAST:event_jtxtUnitIDKeyTyped

    private void jcmbStatusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jcmbStatusKeyPressed
        if(lb.isEnter(evt)) {
            evt.consume();
            navLoad.setSaveFocus();
        }
    }//GEN-LAST:event_jcmbStatusKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JComboBox jcmbStatus;
    private javax.swing.JLabel jlblEditNo;
    private javax.swing.JLabel jlblLstUpdate;
    private javax.swing.JLabel jlblUserName;
    private javax.swing.JTextField jtxtUnitID;
    private javax.swing.JTextField jtxtUnitName;
    private javax.swing.JTextField jtxtUnitSymbol;
    // End of variables declaration//GEN-END:variables
}