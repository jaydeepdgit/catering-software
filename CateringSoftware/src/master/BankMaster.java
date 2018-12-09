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
public class BankMaster extends javax.swing.JInternalFrame {
    private Connection dataConnection = DeskFrame.connMpAdmin;
    private SmallNavigation navLoad = null;
    private Library lb = new Library();
    private String id = "";
    private ReportTable bankMaster;

    /**
     * Creates new form BankMaster
     */
    public BankMaster() {
        initComponents();
        addNavigation();
        addValidation();
        setCompEnable(false);
        setVoucher("Last");
        makeChildTable();
        setTitle(Constants.BANK_MASTER_FORM_NAME);
    }

    private void makeChildTable() {
        bankMaster = new ReportTable();

        bankMaster.AddColumn(0, "Bank CD", 100,java.lang.String.class, null, false);
        bankMaster.AddColumn(1, "Bank Name", 300,java.lang.String.class, null, false);
        bankMaster.makeTable();
    }

    public void setID(String id) {
        this.id = id;
        setVoucher("edit");
    }

    private void onViewVoucher() {
        this.dispose();

        String sql = "SELECT * FROM bank_master";
        bankMaster.setColumnValue(new int[]{1, 2});
        String view_title = Constants.BANK_MASTER_FORM_NAME +" VIEW";

        HeaderIntFrame1 rptDetail = new HeaderIntFrame1(dataConnection, id +"", view_title, bankMaster, sql, Constants.BANK_MASTER_FORM_ID, 1, this,this.getTitle());
        rptDetail.makeView();
        rptDetail.setVisible(true);

        Component c = DeskFrame.tabbedPane.add(view_title, rptDetail);
        c.setName(view_title);
        DeskFrame.tabbedPane.setSelectedComponent(c);
    }

    public void setStartupFocus() {
        jtxtBankName.requestFocusInWindow();
    }

    private void addValidation() {
        FieldValidation valid = new FieldValidation();
        jtxtBankCD.setInputVerifier(valid);
        jtxtBankName.setInputVerifier(valid);
    }

    class FieldValidation extends InputVerifier {
        @Override
        public boolean verify(JComponent input) {
            boolean val = false;
            if (input.equals(jtxtBankCD)) {
                val = fielddValid(input);
            } else if (input.equals(jtxtBankName)) {
                val = fielddValid(input);
            } 
            return val;
        }
    }

    private boolean fielddValid(Component comp) {
        navLoad.setMessage("");
        if (comp == jtxtBankName) {
            if (navLoad.getMode().equalsIgnoreCase("N") || navLoad.getMode().equalsIgnoreCase("E")) {
                if (lb.isBlank(comp)) {
                    navLoad.setMessage("Bank Name should not blank");
                    comp.requestFocusInWindow();
                    return false;
                }
            }

            if (navLoad.getMode().equalsIgnoreCase("N")) {
                if (lb.isExist("bank_master", "name", jtxtBankName.getText(), dataConnection)) {
                    navLoad.setMessage("Bank Name already exist!");
                    comp.requestFocusInWindow();
                    return false;
                }
            } else if (navLoad.getMode().equalsIgnoreCase("E")) {
                if (lb.isExistForEdit("bank_master", "name", jtxtBankName.getText(), "id", String.valueOf(id), dataConnection)) {
                    navLoad.setMessage("Bank Name already exist!");
                    comp.requestFocusInWindow();
                    return false;
                }
            }
        }
        return true;
    }

    private void setCompEnable(boolean flag) {
        jtxtBankCD.setEnabled(!flag);
        jtxtBankName.setEnabled(flag);
        jtxtBankName.requestFocusInWindow();
    }

    private void setCompText(String text) {
        jtxtBankCD.setText(text);
        jtxtBankName.setText(text);
    }

    private boolean validateForm() {
        boolean flag = fielddValid(jtxtBankName);
        return flag;
    }

    private void addNavigation() {
        class Navigation extends SmallNavigation {
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
                boolean valid = validateForm();
                if(valid) {
                    try {
                        setSaveFlag(false);

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
                    } catch (SQLException ex) {
                        try {
                            dataConnection.rollback();
                            dataConnection.setAutoCommit(true);
                            lb.printToLogFile("Error at save In Bank Master", ex);
                        } catch (SQLException ex1) {
                            lb.printToLogFile("Error at rollback save In Bank Master", ex1);
                        }
                    }
                }
            }

            @Override
            public void callDelete() {
                try {
                    if(!lb.isExist("cheque_print", "fk_bank_id", id +"")) {
                        lb.confirmDialog("Do you want to delete bank " + jtxtBankName.getText() + "?");
                        if(lb.type) {
                            dataConnection.setAutoCommit(false);
                            delete();
                            dataConnection.commit();
                            dataConnection.setAutoCommit(true);
                            setVoucher("Last");
                        }
                    } else {
                        navLoad.setMessage("Bank Name is in use");
                    }
                } catch (SQLException ex) {
                    try {
                        dataConnection.rollback();
                        dataConnection.setAutoCommit(true);
                        lb.printToLogFile("Error at delete In Bank Master", ex);
                    } catch (SQLException ex1) {
                        lb.printToLogFile("Error at rollback delete In Bank Master", ex1);
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
                    VoucherDisplay vd = new VoucherDisplay(id, ""+ Constants.BANK_MASTER_INITIAL +"M");
                    DeskFrame.addOnScreen(vd, Constants.BANK_MASTER_FORM_NAME +" PRINT");
                } catch(Exception ex) {
                    lb.printToLogFile("Exception at callPrint In Bank Master", ex);
                }
            }

            @Override
            public void setComponentTextFromResultSet() {
                try {
                    id = viewData.getString("id");
                    jtxtBankCD.setText(id+"");
                    jtxtBankName.setText(viewData.getString("name"));
                    jlblUserName.setText(lb.getUserName(viewData.getString("user_cd"), "N"));
                    jlblEditNo.setText(viewData.getString("edit_no"));
                    jlblLstUpdate.setText(lb.getTimeStamp(viewData.getTimestamp("time_stamp")));
                } catch (SQLException ex) {
                    lb.printToLogFile("Error at setComponentTextFromResultSet In Bank Master", ex);
                }
            }

            @Override
            public void setComponentEnabledDisabled(boolean flag) {
                jtxtBankCD.setEnabled(!flag);
                jtxtBankName.setEnabled(flag);
                jtxtBankName.requestFocusInWindow();
            }
        }
        navLoad = new Navigation();
        navLoad.setVisible(true);
        jPanel1.add(navLoad);
        jPanel1.setVisible(true);
    }

    private void setVoucher(String move) {
        try {
            String sql = "SELECT * FROM bank_master";
            if (move.equalsIgnoreCase("first")) {
                sql += " WHERE id = (SELECT MIN(id) FROM bank_master)";
            } else if (move.equalsIgnoreCase("previous")) {
                sql += " WHERE id = (SELECT MAX(id) FROM bank_master WHERE id < '"+ id +"')";
            } else if (move.equalsIgnoreCase("next")) {
                sql += " WHERE id = (SELECT MIN(id) FROM bank_master WHERE id > '"+ id +"')";
            } else if (move.equalsIgnoreCase("last")) {
                sql += " WHERE id = (SELECT MAX(id) FROM bank_master)";
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
            lb.setPermission(navLoad, Constants.BANK_MASTER_FORM_ID);
        } catch (Exception ex) {
            lb.printToLogFile("Error at setVoucher In Bank Master", ex);
        }
    }

    private void delete() throws SQLException {
        PreparedStatement psLocal = null;

        psLocal = dataConnection.prepareStatement("DELETE FROM bank_master WHERE id = ?");
        psLocal.setString(1, id);
        psLocal.executeUpdate();
    }

    private void saveVoucher() throws SQLException {
        PreparedStatement psLocal = null;

        if(navLoad.getMode().equalsIgnoreCase("N")) {
            psLocal = dataConnection.prepareStatement("INSERT INTO bank_master(name, user_cd, id) VALUES (?, ?, ?)");
            id = lb.generateKey("bank_master", "id", Constants.BANK_MASTER_INITIAL, 7);
        } else if(navLoad.getMode().equalsIgnoreCase("E")) {
            psLocal = dataConnection.prepareStatement("UPDATE bank_master SET name = ?, user_cd = ?, edit_no = edit_no + 1, time_stamp = CURRENT_TIMESTAMP WHERE id = ?");
        }
        psLocal.setString(1, jtxtBankName.getText()); // name
        psLocal.setInt(2, DeskFrame.user_id); // user_cd
        psLocal.setString(3, id); // id
        psLocal.executeUpdate();
    }

    private void cancelOrClose() {
        if(navLoad.getSaveFlag()) {
            this.dispose();
        } else {
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
                lb.printToLogFile("Exception at dispose in Bank Master", pve);
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
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jtxtBankCD = new javax.swing.JTextField();
        jtxtBankName = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jlblLstUpdate = new javax.swing.JLabel();
        jlblEditNo = new javax.swing.JLabel();
        jlblUserName = new javax.swing.JLabel();

        jPanel1.setBackground(new java.awt.Color(253, 243, 243));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(3, 3, 1, 1, new java.awt.Color(235, 35, 35)));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(253, 243, 243));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 3, 3, new java.awt.Color(235, 35, 35)), "Bank Information", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 2, 16), new java.awt.Color(0, 0, 255))); // NOI18N

        jLabel2.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 0, 0));
        jLabel2.setText("Bank Name");

        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel1.setText("Bank CD");

        jtxtBankCD.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtBankCD.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtBankCD.setMinimumSize(new java.awt.Dimension(6, 25));
        jtxtBankCD.setPreferredSize(new java.awt.Dimension(6, 25));
        jtxtBankCD.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtBankCDFocusGained(evt);
            }
        });
        jtxtBankCD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtBankCDKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtBankCDKeyTyped(evt);
            }
        });

        jtxtBankName.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtBankName.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 0, 0)));
        jtxtBankName.setMinimumSize(new java.awt.Dimension(6, 25));
        jtxtBankName.setPreferredSize(new java.awt.Dimension(6, 25));
        jtxtBankName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtBankNameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtBankNameFocusLost(evt);
            }
        });
        jtxtBankName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtBankNameKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtBankNameKeyTyped(evt);
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jlblEditNo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jlblUserName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jlblLstUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtxtBankCD, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtBankName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(91, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel2, jLabel6, jLabel7, jLabel8});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jtxtBankCD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jtxtBankName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jlblUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jlblEditNo, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jlblLstUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel1, jLabel2, jtxtBankCD, jtxtBankName});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel6, jLabel7, jLabel8, jlblEditNo, jlblLstUpdate, jlblUserName});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
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

    private void jtxtBankNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtBankNameKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            navLoad.setSaveFocus();
        }
    }//GEN-LAST:event_jtxtBankNameKeyPressed

    private void jtxtBankNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtBankNameKeyTyped
        lb.fixLength(evt, 200);
    }//GEN-LAST:event_jtxtBankNameKeyTyped

    private void jtxtBankNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtBankNameFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtBankNameFocusGained

    private void jtxtBankNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtBankNameFocusLost
        lb.toUpper(evt);
    }//GEN-LAST:event_jtxtBankNameFocusLost

    private void jtxtBankCDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtBankCDKeyPressed
        if(navLoad.getMode().equalsIgnoreCase("")) {
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                if(lb.isExist("bank_master", "id", jtxtBankCD.getText(), dataConnection)) {
                    id = jtxtBankCD.getText();
                    navLoad.setMessage("");
                    setVoucher("edit");
                } else {
                    navLoad.setMessage("Bank CD is invalid");
                }
            }
            jtxtBankCD.requestFocusInWindow();
        }
    }//GEN-LAST:event_jtxtBankCDKeyPressed

    private void jtxtBankCDFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtBankCDFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtBankCDFocusGained

    private void jtxtBankCDKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtBankCDKeyTyped
        lb.fixLength(evt, 7);
    }//GEN-LAST:event_jtxtBankCDKeyTyped

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel jlblEditNo;
    private javax.swing.JLabel jlblLstUpdate;
    private javax.swing.JLabel jlblUserName;
    private javax.swing.JTextField jtxtBankCD;
    private javax.swing.JTextField jtxtBankName;
    // End of variables declaration//GEN-END:variables
}