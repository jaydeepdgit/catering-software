/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package master;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;
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
public class AccountType extends javax.swing.JInternalFrame {
    private Connection dataConnection = DeskFrame.connMpAdmin;
    private SmallNavigation navLoad;
    private Library lb = new Library();
    private String id = "";
    private ReportTable viewTable = null;

    /**
     * Creates new form AccountType
     */
    public AccountType() {
        initComponents();
        connectNavigation();
        navLoad.setComponentEnabledDisabled(false);
        setVoucher("last");
        tableForView();
        addValidation();
        setTitle(Constants.ACCOUNT_TYPE_FORM_NAME);
    }

    private void tableForView() {
        viewTable = new ReportTable();

        viewTable.AddColumn(0, "Name", 600, java.lang.String.class, null, false);
        viewTable.makeTable();
    }

    public void setID(String code) {
        id = lb.getAccountType(code, "C");
        setVoucher("Edit");
    }

    private void onViewVoucher() {
        this.dispose();

        String sql = "SELECT name FROM account_type";
        viewTable.setColumnValue(new int[]{1});
        String view_title = Constants.ACCOUNT_TYPE_FORM_NAME +" VIEW";

        HeaderIntFrame1 rptDetail = new HeaderIntFrame1(dataConnection, lb.getAccountType(id, "N"), view_title, viewTable, sql, Constants.ACCOUNT_TYPE_FORM_ID, 1, this,this.getTitle());
        rptDetail.makeView();
        rptDetail.setVisible(true);

        Component c = DeskFrame.tabbedPane.add(view_title, rptDetail);
        c.setName(view_title);
        DeskFrame.tabbedPane.setSelectedComponent(c);
    }

    @Override
    public void dispose() {
        try {
            DeskFrame.removeFromScreen(DeskFrame.tabbedPane.getSelectedIndex());
            super.dispose();
        } catch (Exception ex) {
            lb.printToLogFile("Exception at dispose In Account Type", ex);
        }
    }

    private void addValidation() {
        fieldvalidation fldvalidation = new fieldvalidation();
        jtxtName.setInputVerifier(fldvalidation);
    }

    class fieldvalidation extends InputVerifier {
        @Override
        public boolean verify(JComponent input) {
            boolean val = false;
            ((JTextField) input).setText(((JTextField) input).getText().toUpperCase());
            val = fldValid(input);
            return val;
        }
    }

    private boolean fldValid(Component comp) {
        navLoad.setMessage("");
        if (!navLoad.getMode().equalsIgnoreCase("")) {
            if (comp == jtxtName) {
                if (navLoad.getMode().equalsIgnoreCase("N") || navLoad.getMode().equalsIgnoreCase("E")) {
                    if (lb.isBlank(comp)) {
                        navLoad.setMessage("Name should not be blank");
                        comp.requestFocusInWindow();
                        return false;
                    }
                }
                if (navLoad.getMode().equalsIgnoreCase("N")) {
                    if (lb.isExist("account_type", "name", jtxtName.getText(), dataConnection)) {
                        navLoad.setMessage("Name is already exist!");
                        comp.requestFocusInWindow();
                        return false;
                    }
                } else if (navLoad.getMode().equalsIgnoreCase("E")) {
                    if (lb.isExistForEdit("account_type", "name", jtxtName.getText(), "id", String.valueOf(id), dataConnection)) {
                        navLoad.setMessage("Name is already exist!");
                        comp.requestFocusInWindow();
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean validateForm() {
        boolean flag = true;
        flag = flag && fldValid(jtxtName);
        return flag;
    }

    public void setGroupid(String id) {
        this.id = id;
        setVoucher("edit");
    }

    private void setVoucher(String tag) {
        try {
            navLoad.setComponentEnabledDisabled(false);
            String sql = "SELECT * FROM account_type";
            if (tag.equalsIgnoreCase("first")) {
                sql += " ORDER BY id";
            } else if (tag.equalsIgnoreCase("previous")) {
                sql += " WHERE id < '"+ id +"' ORDER BY id DESC";
            } else if (tag.equalsIgnoreCase("next")) {
                sql += " WHERE id > '"+ id +"'";
            } else if (tag.equalsIgnoreCase("last")) {
                sql += " ORDER BY id DESC";
            } else if (tag.equalsIgnoreCase("edit")) {
                sql += " WHERE id = '"+ id +"'";
            }
            navLoad.viewData = lb.fetchData(sql, dataConnection);
            if (navLoad.viewData.next()) {
                navLoad.setComponentTextFromResultSet();
            } else {
                if(tag.equalsIgnoreCase("last")) {
                    setComponenttextToBlank();
                }
            }
            navLoad.setComponentEnabledDisabled(false);
            navLoad.setLastFocus();
            lb.setPermission(navLoad, Constants.ACCOUNT_TYPE_FORM_ID);
        } catch (Exception ex) {
            lb.printToLogFile("Exception at setVoucher In Account Type", ex);
        }
    }

    private void onPrintVoucher() {
        try {
            VoucherDisplay vd = new VoucherDisplay(id, Constants.ACCOUNT_TYPE_INITIAL);
            DeskFrame.addOnScreen(vd, Constants.ACCOUNT_TYPE_FORM_NAME +" PRINT");
        } catch(Exception ex) {
            lb.printToLogFile("Exception at onPrintVoucher In Account Type", ex);
        }
    }

    private void cancelOrClose() {
        if (navLoad.getSaveFlag()) {
            this.dispose();
        } else {
            navLoad.setMode("");
            navLoad.setComponentEnabledDisabled(false);
            navLoad.setMessage("");
            navLoad.setSaveFlag(true);
        }
    }

    private int saveVoucher() {
        PreparedStatement pstLocal = null;
        String sql = "";
        int data = 0;
        try {
            dataConnection.setAutoCommit(false);
            if (navLoad.getMode().equalsIgnoreCase("N")) {
                id = lb.generateKey("account_type", "id", Constants.ACCOUNT_TYPE_INITIAL, 7);
                sql = "INSERT INTO account_type(name, user_cd, head, head_grp, fk_status_id = ?, acc_eff, id) " +
                    "VALUES (?, ?, 1, 0, 0, ?)";
            } else if (navLoad.getMode().equalsIgnoreCase("E")) {
                sql = "UPDATE account_type SET name = ?, user_cd = ?, head_grp = 0, fk_status_id = ?, edit_no = edit_no + 1,"
                        + "time_stamp = CURRENT_TIMESTAMP WHERE id=?";
            }
            pstLocal = dataConnection.prepareStatement(sql);
            pstLocal.setString(1, jtxtName.getText().trim().toUpperCase()); // name
            pstLocal.setInt(2, DeskFrame.user_id); // user_cd
            pstLocal.setString(3, lb.getStatusData(jcmbStatus.getSelectedItem().toString(), "C")); // fk_status_id
            pstLocal.setString(4, id); // id
            data = pstLocal.executeUpdate();
            dataConnection.commit();
            dataConnection.setAutoCommit(true);
        } catch (SQLException ex) {
            try {
                dataConnection.rollback();
                dataConnection.setAutoCommit(true);
                lb.printToLogFile("Error at save In Account Type", ex);
            } catch (SQLException ex1) {
                lb.printToLogFile("Error at rollback save In Account Type", ex1);
            }
        }
        return data;
    }

    private void setComponenttextToBlank() {
        jtxtID.setText("");
        jtxtName.setText("");
    }

    private void connectNavigation() {
        class smallNavigation extends SmallNavigation {
            @Override
            public void callNew() {
                setComponenttextToBlank();
                setComponentEnabledDisabled(true);
                jtxtName.requestFocusInWindow();
                navLoad.setSaveFlag(false);
                navLoad.setMode("N");
            }

            @Override
            public void callEdit() {
                if (lb.checkAccountType(id)) {
                    setComponentEnabledDisabled(true);
                    jtxtName.requestFocusInWindow();
                    navLoad.setSaveFlag(false);
                    navLoad.setMode("E");
                } else {
                    navLoad.setMessage("You can not edit default group.");
                }
            }

            @Override
            public void callSave() {
                if(validateForm()) {
                    try {
                        saveVoucher();
                        setSaveFlag(true);
                        if(navLoad.getMode().equalsIgnoreCase("N")) {
                            setVoucher("Last");
                        } else {
                            setVoucher("Edit");
                        }
                    } catch(Exception ex) {
                        lb.printToLogFile("Error at save in Account Type", ex);
                        try {
                            dataConnection.rollback();
                            dataConnection.setAutoCommit(true);
                        } catch(Exception ex1){
                            lb.printToLogFile("Error at rollback save in Account Type", ex1);
                        }
                    }
                }
            }

            @Override
            public void callDelete() {
                if (lb.checkAccountType(id)) {
                    if (lb.getData("id", "account_master", "fk_account_type_id", id).equalsIgnoreCase("")) {
                        lb.confirmDialog(Constants.DELETE_RECORD);
                        if (lb.type) {
                            try {
                                dataConnection.setAutoCommit(false);
                                String sql = "DELETE FROM account_type WHERE id = ?";
                                PreparedStatement pstLocal = dataConnection.prepareStatement(sql);
                                pstLocal.setString(1, id);
                                pstLocal.executeUpdate();
                                setVoucher("Previous");
                                dataConnection.commit();
                                dataConnection.setAutoCommit(true);
                            } catch (Exception ex) {
                                lb.printToLogFile("Exception at callDelete In Account Type", ex);
                                try {
                                    dataConnection.rollback();
                                    dataConnection.setAutoCommit(true);
                                } catch (Exception ex1) {
                                    lb.printToLogFile("Exception at rollback callDelete In Account Type", ex1);
                                }
                            }
                        } else {
                            navLoad.setSaveFocus();
                        }
                    } else {
                        navLoad.setMessage("Group is used in other forms.You can not delete this group");
                    }
                } else {
                    navLoad.setMessage("You can not delete default group.");
                }
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
                setVoucher("Edit");
            }

            @Override
            public void callPrint() {
                onPrintVoucher();
            }

            @Override
            public void setComponentTextFromResultSet() {
                try {
                    id = viewData.getString("id");
                    jtxtID.setText(id);
                    jtxtName.setText(viewData.getString("name"));
                    jcmbStatus.setSelectedItem(lb.getStatusData(viewData.getInt("fk_status_id")+"", "N"));
                    jlblUserName.setText(lb.getUserName(viewData.getString("user_cd"), "N"));
                    jlblEditNo.setText(viewData.getString("edit_no"));
                    jlblLstUpdate.setText(lb.getTimeStamp(viewData.getTimestamp("time_stamp")));
                } catch (Exception ex) {
                    lb.printToLogFile("Exception at setComponentTextFromResultSet In Account Type", ex);
                }
            }

            @Override
            public void setComponentEnabledDisabled(boolean flag) {
                jtxtID.setEnabled(!flag);
                jtxtName.setEnabled(flag);
                jcmbStatus.setEnabled(flag);
            }
        }
        navLoad = new smallNavigation();
        jpanelNavigation.add(navLoad);
        navLoad.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpanelNavigation = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jtxtName = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jlblLstUpdate = new javax.swing.JLabel();
        jlblEditNo = new javax.swing.JLabel();
        jlblUserName = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jtxtID = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jcmbStatus = new javax.swing.JComboBox();

        setClosable(true);

        jpanelNavigation.setBackground(new java.awt.Color(253, 243, 243));
        jpanelNavigation.setBorder(javax.swing.BorderFactory.createMatteBorder(3, 3, 1, 1, new java.awt.Color(235, 35, 35)));
        jpanelNavigation.setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(253, 243, 243));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 3, 3, new java.awt.Color(235, 35, 35)), "Account Type Information", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 2, 16), new java.awt.Color(0, 0, 255))); // NOI18N

        jLabel2.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 0, 0));
        jLabel2.setText("Name");

        jtxtName.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtName.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 0, 0)));
        jtxtName.setMinimumSize(new java.awt.Dimension(6, 25));
        jtxtName.setName(""); // NOI18N
        jtxtName.setPreferredSize(new java.awt.Dimension(6, 25));
        jtxtName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtNameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtNameFocusLost(evt);
            }
        });
        jtxtName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtNameKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtNameKeyTyped(evt);
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
        jLabel4.setText("ID");

        jtxtID.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtID.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtID.setMinimumSize(new java.awt.Dimension(6, 25));
        jtxtID.setPreferredSize(new java.awt.Dimension(6, 25));
        jtxtID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtIDFocusGained(evt);
            }
        });
        jtxtID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtIDKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtIDKeyTyped(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel5.setText("Status");
        jLabel5.setMaximumSize(new java.awt.Dimension(56, 25));
        jLabel5.setMinimumSize(new java.awt.Dimension(56, 25));
        jLabel5.setPreferredSize(new java.awt.Dimension(56, 25));

        jcmbStatus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jcmbStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Active", "Deactive" }));
        jcmbStatus.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jcmbStatus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jcmbStatusKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jtxtID, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtName, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                            .addComponent(jcmbStatus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(159, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlblLstUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jlblEditNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jlblUserName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(411, Short.MAX_VALUE))))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel2, jLabel4, jLabel5});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlblUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jlblEditNo, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jlblLstUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel6, jlblUserName});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel7, jlblEditNo});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel8, jlblLstUpdate});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel2, jtxtName});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel4, jtxtID});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpanelNavigation, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jpanelNavigation, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtxtNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtNameFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtNameFocusGained

    private void jtxtNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtNameFocusLost
        jtxtName.setText(jtxtName.getText().toUpperCase());
    }//GEN-LAST:event_jtxtNameFocusLost

    private void jtxtIDFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtIDFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtIDFocusGained

    private void jtxtIDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtIDKeyPressed
        if(navLoad.getMode().equalsIgnoreCase("")) {
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                if(lb.isExist("account_type", "id", jtxtID.getText(), dataConnection)) {
                    id = jtxtID.getText();
                    navLoad.setMessage("");
                    setVoucher("edit");
                } else {
                    navLoad.setMessage("Account Type ID is invalid");
                }
            }
            jtxtID.requestFocusInWindow();
        }
    }//GEN-LAST:event_jtxtIDKeyPressed

    private void jtxtIDKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtIDKeyTyped
        lb.fixLength(evt, 7);
    }//GEN-LAST:event_jtxtIDKeyTyped

    private void jtxtNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtNameKeyTyped
        lb.fixLength(evt, 50);
    }//GEN-LAST:event_jtxtNameKeyTyped

    private void jcmbStatusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jcmbStatusKeyPressed
        if(lb.isEnter(evt)) {
            evt.consume();
            navLoad.setSaveFocus();
        }
    }//GEN-LAST:event_jcmbStatusKeyPressed

    private void jtxtNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtNameKeyPressed
        lb.enterEvent(evt, jcmbStatus);
    }//GEN-LAST:event_jtxtNameKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JComboBox jcmbStatus;
    private javax.swing.JLabel jlblEditNo;
    private javax.swing.JLabel jlblLstUpdate;
    private javax.swing.JLabel jlblUserName;
    private javax.swing.JPanel jpanelNavigation;
    private javax.swing.JTextField jtxtID;
    private javax.swing.JTextField jtxtName;
    // End of variables declaration//GEN-END:variables
}