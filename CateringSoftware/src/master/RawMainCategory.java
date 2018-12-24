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
public class RawMainCategory extends javax.swing.JInternalFrame {
    private Connection dataConnection = DeskFrame.connMpAdmin;
    private SmallNavigation navLoad;
    private Library lb = new Library();
    private String id = "";
    private ReportTable viewTable = null;

    /**
     * Creates new form RawMainCategory
     */
    public RawMainCategory() {
        initComponents();
        connectNavigation();
        navLoad.setComponentEnabledDisabled(false);
        setVoucher("last");
        tableForView();
        addValidation();
        setTitle(Constants.MAIN_CATEGORY_FORM_NAME);
    }

    private void tableForView() {
        viewTable = new ReportTable();

        viewTable.AddColumn(0, "Name", 600, java.lang.String.class, null, false);
        viewTable.makeTable();
    }

    public void setID(String code) {
        id = lb.getRawMainCategory(code, "C", "main");
        setVoucher("Edit");
    }

    private void onViewVoucher() {
        this.dispose();

        String sql = "SELECT name_en FROM raw_material_main";
        viewTable.setColumnValue(new int[]{1});
        String view_title = Constants.MAIN_CATEGORY_FORM_NAME +" VIEW";

        HeaderIntFrame1 rptDetail = new HeaderIntFrame1(dataConnection, lb.getRawMainCategory(id, "N", "main"), view_title, viewTable, sql, Constants.MAIN_CATEGORY_RW_FORM_ID, 1, this,this.getTitle());
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
            lb.printToLogFile("Exception at dispose In Raw main category", ex);
        }
    }

    private void addValidation() {
        fieldvalidation fldvalidation = new fieldvalidation();
        jtxtEngName.setInputVerifier(fldvalidation);
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
            if (comp == jtxtEngName) {
                if (navLoad.getMode().equalsIgnoreCase("N") || navLoad.getMode().equalsIgnoreCase("E")) {
                    if (lb.isBlank(comp)) {
                        navLoad.setMessage("Name should not be blank");
                        comp.requestFocusInWindow();
                        return false;
                    }
                }
                if (navLoad.getMode().equalsIgnoreCase("N")) {
                    if (lb.isExist("raw_material_main", "name_en", jtxtEngName.getText(), dataConnection)) {
                        navLoad.setMessage("Name is already exist!");
                        comp.requestFocusInWindow();
                        return false;
                    }
                } else if (navLoad.getMode().equalsIgnoreCase("E")) {
                    if (lb.isExistForEdit("raw_material_main", "name_en", jtxtEngName.getText(), "id", String.valueOf(id), dataConnection)) {
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
        flag = flag && fldValid(jtxtEngName);
        return flag;
    }

    public void setGroupid(String id) {
        this.id = id;
        setVoucher("edit");
    }

    private void setVoucher(String tag) {
        try {
            navLoad.setComponentEnabledDisabled(false);
            String sql = "SELECT * FROM raw_material_main";
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
            lb.setPermission(navLoad, Constants.MAIN_CATEGORY_RW_FORM_ID);
        } catch (Exception ex) {
            lb.printToLogFile("Exception at setVoucher In Raw main category", ex);
        }
    }

    private void onPrintVoucher() {
        try {
            VoucherDisplay vd = new VoucherDisplay(id, Constants.MAIN_CATEGORY_RM_INITIAL);
            DeskFrame.addOnScreen(vd, Constants.MAIN_CATEGORY_FORM_NAME +" PRINT");
        } catch(Exception ex) {
            lb.printToLogFile("Exception at onPrintVoucher In Raw main category", ex);
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
                id = lb.generateKey("raw_material_main", "id", Constants.MAIN_CATEGORY_RM_INITIAL, 7);
                sql = "INSERT INTO raw_material_main(name_en, name_gu, name_hi, user_cd, fk_order_step_category_id, fk_status_id, id) " +
                    "VALUES (?, ?, ?, ?, 1, ?, ?)";
            } else if (navLoad.getMode().equalsIgnoreCase("E")) {
                sql = "UPDATE raw_material_main SET name_en = ?, name_gu=?, name_hi=?, user_cd = ?, fk_order_step_category_id = 1, fk_status_id = ?, edit_no = edit_no + 1,"
                        + "time_stamp = CURRENT_TIMESTAMP WHERE id=?";
            }
            pstLocal = dataConnection.prepareStatement(sql);
            pstLocal.setString(1, jtxtEngName.getText().trim().toUpperCase()); // english name
            pstLocal.setString(2, jtxtGujName.getText().trim()); // gujarati name
            pstLocal.setString(3, jtxtHinName.getText().trim()); // hindi name
            pstLocal.setInt(4, DeskFrame.user_id); // user_cd
            pstLocal.setString(5, lb.getStatusData(jcmbStatus.getSelectedItem().toString(), "C")); // fk_status_id
            pstLocal.setString(6, id); // id
            data = pstLocal.executeUpdate();
            dataConnection.commit();
            dataConnection.setAutoCommit(true);
        } catch (SQLException ex) {
            try {
                dataConnection.rollback();
                dataConnection.setAutoCommit(true);
                lb.printToLogFile("Error at save In Raw main category", ex);
            } catch (SQLException ex1) {
                lb.printToLogFile("Error at rollback save In Raw main category", ex1);
            }
        }
        return data;
    }

    private void setComponenttextToBlank() {
        jtxtID.setText("");
        jtxtEngName.setText("");
        jtxtGujName.setText("");
        jtxtHinName.setText("");
    }

    private void connectNavigation() {
        class smallNavigation extends SmallNavigation {
            @Override
            public void callNew() {
                setComponenttextToBlank();
                setComponentEnabledDisabled(true);
                jtxtEngName.requestFocusInWindow();
                navLoad.setSaveFlag(false);
                navLoad.setMode("N");
            }

            @Override
            public void callEdit() {
                    setComponentEnabledDisabled(true);
                    jtxtEngName.requestFocusInWindow();
                    navLoad.setSaveFlag(false);
                    navLoad.setMode("E");
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
                        lb.printToLogFile("Error at save in Raw main category", ex);
                        try {
                            dataConnection.rollback();
                            dataConnection.setAutoCommit(true);
                        } catch(Exception ex1){
                            lb.printToLogFile("Error at rollback save in Raw main category", ex1);
                        }
                    }
                }
            }

            @Override
            public void callDelete() {
                if (lb.getData("id", "raw_material_sub", "fk_raw_material_main_id", id).equalsIgnoreCase("")) {
                    lb.confirmDialog(Constants.DELETE_RECORD);
                    if (lb.type) {
                        try {
                            dataConnection.setAutoCommit(false);
                            String sql = "DELETE FROM raw_material_main WHERE id = ?";
                            PreparedStatement pstLocal = dataConnection.prepareStatement(sql);
                            pstLocal.setString(1, id);
                            pstLocal.executeUpdate();
                            setVoucher("Previous");
                            dataConnection.commit();
                            dataConnection.setAutoCommit(true);
                        } catch (Exception ex) {
                            lb.printToLogFile("Exception at callDelete In Raw main category", ex);
                            try {
                                dataConnection.rollback();
                                dataConnection.setAutoCommit(true);
                            } catch (Exception ex1) {
                                lb.printToLogFile("Exception at rollback callDelete In Raw main category", ex1);
                            }
                        }
                    } else {
                        navLoad.setSaveFocus();
                    }
                } else {
                    navLoad.setMessage("Category is used in other forms.You can not delete this group");
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
                    jtxtEngName.setText(viewData.getString("name_en"));
                    jtxtGujName.setText(viewData.getString("name_gu"));
                    jtxtHinName.setText(viewData.getString("name_hi"));
                    jcmbStatus.setSelectedItem(lb.getStatusData(viewData.getInt("fk_status_id")+"", "N"));
                    jlblUserName.setText(lb.getUserName(viewData.getString("user_cd"), "N"));
                    jlblEditNo.setText(viewData.getString("edit_no"));
                    jlblLstUpdate.setText(lb.getTimeStamp(viewData.getTimestamp("time_stamp")));
                } catch (Exception ex) {
                    lb.printToLogFile("Exception at setComponentTextFromResultSet In Raw main category", ex);
                }
            }

            @Override
            public void setComponentEnabledDisabled(boolean flag) {
                jtxtID.setEnabled(!flag);
                jtxtEngName.setEnabled(flag);
                jtxtGujName.setEditable(flag);
                jtxtHinName.setEnabled(flag);
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
        jtxtEngName = new javax.swing.JTextField();
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
        jLabel3 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jtxtGujName = new javax.swing.JTextField();
        jtxtHinName = new javax.swing.JTextField();

        setClosable(true);
        setPreferredSize(new java.awt.Dimension(728, 477));

        jpanelNavigation.setBackground(new java.awt.Color(253, 243, 243));
        jpanelNavigation.setBorder(javax.swing.BorderFactory.createMatteBorder(3, 3, 1, 1, new java.awt.Color(235, 35, 35)));
        jpanelNavigation.setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(253, 243, 243));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 3, 3, new java.awt.Color(235, 35, 35)), "Raw Material Category", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 2, 16), new java.awt.Color(0, 0, 255))); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(550, 301));

        jLabel2.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 0, 0));
        jLabel2.setText("Name");

        jtxtEngName.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtEngName.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 0, 0)));
        jtxtEngName.setMinimumSize(new java.awt.Dimension(6, 25));
        jtxtEngName.setName(""); // NOI18N
        jtxtEngName.setPreferredSize(new java.awt.Dimension(6, 25));
        jtxtEngName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtEngNameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtEngNameFocusLost(evt);
            }
        });
        jtxtEngName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtEngNameKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtEngNameKeyTyped(evt);
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

        jLabel3.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel3.setText("Gujarati Name");

        jLabel9.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel9.setText("Hindi Name");

        jtxtGujName.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        jtxtGujName.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 0, 0)));
        jtxtGujName.setMinimumSize(new java.awt.Dimension(6, 25));
        jtxtGujName.setName(""); // NOI18N
        jtxtGujName.setPreferredSize(new java.awt.Dimension(6, 25));
        jtxtGujName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtGujNameActionPerformed(evt);
            }
        });
        jtxtGujName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtGujNameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtGujNameFocusLost(evt);
            }
        });
        jtxtGujName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtGujNameKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtGujNameKeyTyped(evt);
            }
        });

        jtxtHinName.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        jtxtHinName.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 0, 0)));
        jtxtHinName.setMinimumSize(new java.awt.Dimension(6, 25));
        jtxtHinName.setName(""); // NOI18N
        jtxtHinName.setPreferredSize(new java.awt.Dimension(6, 25));
        jtxtHinName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtHinNameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtHinNameFocusLost(evt);
            }
        });
        jtxtHinName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtHinNameKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtHinNameKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jtxtEngName, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(277, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE))
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jlblLstUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jlblEditNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jlblUserName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jcmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jtxtHinName, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                                    .addComponent(jtxtGujName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jtxtID, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtEngName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtxtGujName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtxtHinName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jcmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
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
                .addGap(20, 20, 20))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel6, jlblUserName});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel7, jlblEditNo});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel8, jlblLstUpdate});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel4, jtxtID});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 692, Short.MAX_VALUE)
                    .addComponent(jpanelNavigation, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jpanelNavigation, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtxtEngNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtEngNameFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtEngNameFocusGained

    private void jtxtEngNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtEngNameFocusLost
        jtxtEngName.setText(jtxtEngName.getText().toUpperCase());
    }//GEN-LAST:event_jtxtEngNameFocusLost

    private void jtxtIDFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtIDFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtIDFocusGained

    private void jtxtIDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtIDKeyPressed
        if(navLoad.getMode().equalsIgnoreCase("")) {
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                if(lb.isExist("raw_material_main", "id", jtxtID.getText(), dataConnection)) {
                    id = jtxtID.getText();
                    navLoad.setMessage("");
                    setVoucher("edit");
                } else {
                    navLoad.setMessage("Raw main category ID is invalid");
                }
            }
            jtxtID.requestFocusInWindow();
        }
    }//GEN-LAST:event_jtxtIDKeyPressed

    private void jtxtIDKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtIDKeyTyped
        lb.fixLength(evt, 7);
    }//GEN-LAST:event_jtxtIDKeyTyped

    private void jtxtEngNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtEngNameKeyTyped
        lb.fixLength(evt, 50);
    }//GEN-LAST:event_jtxtEngNameKeyTyped

    private void jcmbStatusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jcmbStatusKeyPressed
        if(lb.isEnter(evt)) {
            evt.consume();
            navLoad.setSaveFocus();
        }
    }//GEN-LAST:event_jcmbStatusKeyPressed

    private void jtxtEngNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtEngNameKeyPressed
        lb.enterEvent(evt, jtxtGujName);
    }//GEN-LAST:event_jtxtEngNameKeyPressed

    private void jtxtGujNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtGujNameFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtGujNameFocusGained

    private void jtxtGujNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtGujNameFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtGujNameFocusLost

    private void jtxtGujNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtGujNameKeyPressed
        lb.enterEvent(evt, jtxtHinName);
    }//GEN-LAST:event_jtxtGujNameKeyPressed

    private void jtxtGujNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtGujNameKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtGujNameKeyTyped

    private void jtxtHinNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtHinNameFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtHinNameFocusGained

    private void jtxtHinNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtHinNameFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtHinNameFocusLost

    private void jtxtHinNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtHinNameKeyPressed
        lb.enterEvent(evt, jcmbStatus);
    }//GEN-LAST:event_jtxtHinNameKeyPressed

    private void jtxtHinNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtHinNameKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtHinNameKeyTyped

    private void jtxtGujNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtGujNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtGujNameActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JComboBox jcmbStatus;
    private javax.swing.JLabel jlblEditNo;
    private javax.swing.JLabel jlblLstUpdate;
    private javax.swing.JLabel jlblUserName;
    private javax.swing.JPanel jpanelNavigation;
    private javax.swing.JTextField jtxtEngName;
    private javax.swing.JTextField jtxtGujName;
    private javax.swing.JTextField jtxtHinName;
    private javax.swing.JTextField jtxtID;
    // End of variables declaration//GEN-END:variables
}