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
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
public class TaxMaster extends javax.swing.JInternalFrame {
    private Connection dataConnection = DeskFrame.connMpAdmin;
    private SmallNavigation navLoad;
    private Library lb = new Library();
    public String id = "";
    private PreparedStatement ps = null;
    private ReportTable taxMaster = null;

    /**
     * Creates new form TaxMaster
     */
    public TaxMaster() {
        initComponents();
        initOther();
        setTitle(Constants.TAX_MASTER_FORM_NAME);
    }

    private void initOther() {
        addNavigation();
        addValidation();
        makeChildTable();
        lb.setStatusComboBox(jcmbStatus);
        setVoucher("last");
    }

    private void addValidation() {
        fieldvalidation validation = new fieldvalidation();
        jtxtTax.setInputVerifier(validation);
        jtxtTaxName.setInputVerifier(validation);
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

    private boolean validateForm() {
        boolean flag = true;
        flag = flag && fldValid(jtxtTaxName);
        flag = flag && fldValid(jtxtTax);
        return flag;
    }

    private boolean fldValid(Component comp) {
        navLoad.setMessage("");
        if (comp == jtxtTax) {
            if (lb.isBlank(comp)) {
                navLoad.setMessage("Tax(%) should not be blank");
                return false;
            } else {
                Pattern p = Pattern.compile(".*[a-zA-Z].*");
                Matcher m = p.matcher(jtxtTax.getText());
                if (m.find()) {
                    navLoad.setMessage("Tax cannot contain characters");
                    return false;
                }
            }
        }
        if (comp == jtxtTaxName) {
            if (navLoad.getMode().equalsIgnoreCase("N") || navLoad.getMode().equalsIgnoreCase("E")) {
                if (lb.isBlank(comp)) {
                    navLoad.setMessage("Tax Name should not blank");
                    comp.requestFocusInWindow();
                    return false;
                }
            }
            if (navLoad.getMode().equalsIgnoreCase("N")) {
                if (lb.isExist("tax_master", "name", jtxtTaxName.getText(), dataConnection)) {
                    navLoad.setMessage("Tax Name is already exist!");
                    comp.requestFocusInWindow();
                    return false;
                }
            } else if (navLoad.getMode().equalsIgnoreCase("E")) {
                if (lb.isExistForEdit("tax_master", "name", jtxtTaxName.getText(), "id", String.valueOf(id), dataConnection)) {
                    navLoad.setMessage("Tax Name is already exist!");
                    comp.requestFocusInWindow();
                    return false;
                }
            }
        }
        return true;
    }

    public void setId(String id) {
        this.id = id;
        setVoucher("Edit");
    }

    @Override
    public void dispose() {
        try {
            DeskFrame.removeFromScreen(DeskFrame.tabbedPane.getSelectedIndex());
            super.dispose();
        } catch (Exception ex) {
            lb.printToLogFile("Exception at dispose In Tax Master", ex);
        }
    }

    private void makeChildTable() {
        taxMaster = new ReportTable();

        taxMaster.AddColumn(0, "SR No", 100, java.lang.String.class, null, false);
        taxMaster.AddColumn(1, "Tax Name", 300, java.lang.String.class, null, false);
        taxMaster.AddColumn(2, "Tax %", 150, java.lang.String.class, null, false);
        taxMaster.makeTable();
    }

    private void addNavigation() {
        class smallnavig extends SmallNavigation {
            @Override
            public void callNew() {
                setComponentEnabledDisabled(true);
                setComponentText("");
                setSaveFlag(false);
                setMode("N");
                jtxtTaxName.requestFocusInWindow();
            }

            @Override
            public void callEdit() {
                setComponentEnabledDisabled(true);
                setSaveFlag(false);
                setMode("E");
                jtxtTaxName.requestFocusInWindow();
            }

            @Override
            public void callSave() {
                if(validateForm()) {
                    try {
                        valueUpdateToDatabase(false);
                        if (getMode().equalsIgnoreCase("N")) {
                            setVoucher("Last");
                        } else if (getMode().equalsIgnoreCase("E")) {
                            setVoucher("Edit");
                        }
                        cancelOrClose();
                    } catch (Exception ex) {
                        lb.printToLogFile("Error at callSave In Tax Master", ex);
                    }
                }
            }

            @Override
            public void callDelete() {
                try {
//                    if(lb.isExist("slsdt", "add_cd", id+"") || lb.isExist("slsdt", "vat_cd", id+"") || lb.isExist("slsdt", "igst_cd", id+"") || 
//                        lb.isExist("prdt", "add_cd", id+"") || lb.isExist("prdt", "vat_cd", id+"") || lb.isExist("prdt", "igst_cd", id+"")) {
//                        navLoad.setMessage("Tax Master is in used");
//                    } else {
                        lb.confirmDialog(Constants.DELETE_THIS + "Tax Name "+ jtxtTaxName.getText() +" ?");
                        if(lb.type) {
                            dataConnection.setAutoCommit(false);
                            delete();
                            dataConnection.commit();
                            dataConnection.setAutoCommit(true);
                            setVoucher("Last");
                        }
//                    }
                } catch (SQLException ex) {
                    try {
                        dataConnection.rollback();
                        dataConnection.setAutoCommit(true);
                        lb.printToLogFile("Error at delete In Tax Master", ex);
                    } catch (SQLException ex1) {
                        lb.printToLogFile("Error at rollback delete In Tax Master", ex1);
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
                    VoucherDisplay vd = new VoucherDisplay(String.valueOf(id), Constants.TAX_MASTER_INITIAL +"M");
                    DeskFrame.addOnScreen(vd, Constants.TAX_MASTER_FORM_NAME +" PRINT");
                } catch(Exception ex) {
                    lb.printToLogFile("Exception at callPrint In Tax Master", ex);
                }
            }

            @Override
            public void setComponentEnabledDisabled(boolean bFlag) {
                jtxtTaxCD.setEnabled(!bFlag);
                jtxtTaxName.setEnabled(bFlag);
                jtxtTax.setEnabled(bFlag);
                jcmbStatus.setEnabled(bFlag);
            }

            public int valueUpdateToDatabase(boolean bPrepareStatement) {
                try {
                    dataConnection.setAutoCommit(false);
                    saveVoucher();
                    dataConnection.commit();
                    dataConnection.setAutoCommit(true);
                } catch (Exception ex) {
                    lb.printToLogFile("Exception at valueUpdateToDatabase in Tax Master", ex);
                    try {
                        dataConnection.rollback();
                        dataConnection.setAutoCommit(true);
                    } catch (Exception ex1) {
                        lb.printToLogFile("Exception at rollback valueUpdateToDatabase in Tax Master", ex1);
                    }
                }
                return 0;
            }

            @Override
            public void setComponentTextFromResultSet() {
                try {
                    id = viewData.getString("id");
                    jtxtTaxCD.setText(id);
                    jtxtTaxName.setText(viewData.getString("name"));
                    jtxtTax.setText(lb.Convert2DecFmt(viewData.getDouble("tax")));
                    jcmbStatus.setSelectedItem(lb.getStatusData(viewData.getInt("fk_status_id")+"", "N"));
                    jlblUserName.setText(lb.getUserName(viewData.getString("user_cd"), "N"));
                    jlblEditNo.setText(viewData.getString("edit_no"));
                    jlblLstUpdate.setText(lb.timestamp.format(new Date(viewData.getTimestamp("time_stamp").getTime())));
                } catch (SQLException ex) {
                    lb.printToLogFile("Exception at setComponentTextFromResultSet In Tax Master", ex);
                }
            }
        }
        navLoad = new smallnavig();
        jPanel1.add(navLoad);
        navLoad.setVisible(true);
    }

    public void setComponentText(String strText) {
        jtxtTaxCD.setText(strText);
        jtxtTaxName.setText(strText);
        jtxtTax.setText(strText);
    }

    private void setVoucher(String tag) {
        try {
            navLoad.setComponentEnabledDisabled(false);
            String sql = "SELECT * FROM tax_master";
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
            navLoad.viewData = lb.fetchData(sql);
            if (navLoad.viewData.next()) {
                navLoad.setComponentTextFromResultSet();
            } else {
                if(tag.equalsIgnoreCase("last")) {
                    setComponentText("");
                }
            }
        } catch (Exception e) {
            lb.printToLogFile("Exception at setVoucher In Tax Master", e);
        }
        lb.setPermission(navLoad, Constants.TAX_MASTER_FORM_ID);
    }

    private void onViewVoucher() {
        this.dispose();

        String sql = "SELECT id, name, tax FROM tax_master";
        taxMaster.setColumnValue(new int[]{1, 2, 3});
        String view_title = Constants.TAX_MASTER_FORM_NAME +" VIEW";

        HeaderIntFrame1 rptDetail = new HeaderIntFrame1(dataConnection, lb.getTaxCode(id, "C"), view_title, taxMaster, sql, Constants.TAX_MASTER_FORM_ID, 1, this, this.getTitle());
        rptDetail.makeView();
        rptDetail.setVisible(true);

        Component c = DeskFrame.tabbedPane.add(view_title, rptDetail);
        c.setName(view_title);
        DeskFrame.tabbedPane.setSelectedComponent(c);
    }

    private void cancelOrClose() {
        if (navLoad.getSaveFlag()) {
            this.dispose();
        } else {
            navLoad.setMode("");
            navLoad.setComponentEnabledDisabled(false);
            navLoad.setSaveFlag(true);
            setVoucher("Edit");
        }
    }

    private void delete() throws SQLException {
        PreparedStatement psLocal = null;

        psLocal = dataConnection.prepareStatement("DELETE FROM tax_master WHERE id = ?");
        psLocal.setString(1, id); // id
        psLocal.executeUpdate();
    }

    private void saveVoucher() throws SQLException {
        String sql = "";
        if (navLoad.getMode().equalsIgnoreCase("N")) {
            sql = "INSERT INTO tax_master (name, tax, fk_status_id, edit_no, user_cd, id) VALUES(?, ?, ?, 0, ?, ?)";
            id = lb.generateKey("tax_master", "id", Constants.TAX_MASTER_INITIAL, 7);
        } else if (navLoad.getMode().equalsIgnoreCase("E")) {
            sql = "UPDATE tax_master SET name = ?, tax = ?, fk_status_id = ?, time_stamp = CURRENT_TIMESTAMP, edit_no = edit_no + 1, user_cd = ? WHERE id = ?";
        }
        ps = dataConnection.prepareStatement(sql);
        try {
            ps.setString(1, jtxtTaxName.getText()); // name
            ps.setString(2, jtxtTax.getText()); // tax
            ps.setString(3, lb.getStatusData(jcmbStatus.getSelectedItem().toString(), "C")); // fk_status_id
            ps.setInt(4, DeskFrame.user_id); // user_cd
            ps.setString(5, id); // id
            ps.execute();
        } catch (SQLException ex) {
            lb.printToLogFile("error at saveVoucher In Tax Master", ex);
        }
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jtxtTaxName = new javax.swing.JTextField();
        jtxtTax = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jlblLstUpdate = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jlblUserName = new javax.swing.JLabel();
        jlblEditNo = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jtxtTaxCD = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jcmbStatus = new javax.swing.JComboBox();

        setClosable(true);

        jPanel1.setBackground(new java.awt.Color(253, 243, 243));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(3, 3, 1, 1, new java.awt.Color(235, 35, 35)));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        jPanel2.setBackground(new java.awt.Color(253, 243, 243));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 3, 3, new java.awt.Color(235, 35, 35)), "Tax Information", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 2, 16), new java.awt.Color(0, 0, 255))); // NOI18N
        jPanel2.setToolTipText("");

        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 0, 0));
        jLabel1.setText("Tax Name");
        jLabel1.setMaximumSize(new java.awt.Dimension(48, 25));
        jLabel1.setMinimumSize(new java.awt.Dimension(48, 25));
        jLabel1.setPreferredSize(new java.awt.Dimension(48, 25));

        jLabel2.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 0, 0));
        jLabel2.setText("Tax(%)");
        jLabel2.setMaximumSize(new java.awt.Dimension(37, 25));
        jLabel2.setMinimumSize(new java.awt.Dimension(37, 25));
        jLabel2.setPreferredSize(new java.awt.Dimension(37, 25));

        jtxtTaxName.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtTaxName.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 0, 0)));
        jtxtTaxName.setMinimumSize(new java.awt.Dimension(6, 25));
        jtxtTaxName.setPreferredSize(new java.awt.Dimension(6, 25));
        jtxtTaxName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtTaxNameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtTaxNameFocusLost(evt);
            }
        });
        jtxtTaxName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtTaxNameKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtTaxNameKeyTyped(evt);
            }
        });

        jtxtTax.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtTax.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 0, 0)));
        jtxtTax.setMinimumSize(new java.awt.Dimension(6, 25));
        jtxtTax.setPreferredSize(new java.awt.Dimension(6, 25));
        jtxtTax.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtTaxFocusGained(evt);
            }
        });
        jtxtTax.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtTaxKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtTaxKeyTyped(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel9.setText("Edit No:");

        jlblLstUpdate.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        jLabel11.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel11.setText("Last Updated:");

        jLabel10.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel10.setText("User Name:");

        jlblUserName.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        jlblEditNo.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        jLabel3.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel3.setText("Tax CD");
        jLabel3.setMaximumSize(new java.awt.Dimension(35, 25));
        jLabel3.setMinimumSize(new java.awt.Dimension(35, 25));
        jLabel3.setPreferredSize(new java.awt.Dimension(35, 25));

        jtxtTaxCD.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtTaxCD.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtTaxCD.setMinimumSize(new java.awt.Dimension(6, 25));
        jtxtTaxCD.setPreferredSize(new java.awt.Dimension(6, 25));
        jtxtTaxCD.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtTaxCDFocusGained(evt);
            }
        });
        jtxtTaxCD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtTaxCDKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtTaxCDKeyTyped(evt);
            }
        });

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
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jtxtTaxCD, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jlblEditNo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jlblUserName, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                                .addComponent(jlblLstUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jtxtTaxName, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                            .addComponent(jtxtTax, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jcmbStatus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jlblEditNo, jlblLstUpdate, jlblUserName});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel10, jLabel11, jLabel2, jLabel3, jLabel9});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtTaxCD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jtxtTaxName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jtxtTax, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jcmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE)
                    .addComponent(jlblUserName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlblEditNo, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlblLstUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel10, jLabel11, jLabel9, jlblEditNo, jlblLstUpdate, jlblUserName});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel1, jLabel2, jtxtTax, jtxtTaxName});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel3, jtxtTaxCD});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel4, jcmbStatus});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
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

    private void jtxtTaxNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtTaxNameKeyPressed
        lb.enterFocus(evt, jtxtTax);
    }//GEN-LAST:event_jtxtTaxNameKeyPressed

    private void jtxtTaxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtTaxKeyPressed
        lb.enterEvent(evt, jcmbStatus);
    }//GEN-LAST:event_jtxtTaxKeyPressed

    private void jtxtTaxNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtTaxNameFocusLost
        lb.Uppercase(jtxtTaxName);
    }//GEN-LAST:event_jtxtTaxNameFocusLost

    private void jtxtTaxKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtTaxKeyTyped
        lb.onlyNumber(evt, 6);
    }//GEN-LAST:event_jtxtTaxKeyTyped

    private void jtxtTaxNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtTaxNameFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtTaxNameFocusGained

    private void jtxtTaxFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtTaxFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtTaxFocusGained

    private void jtxtTaxCDFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtTaxCDFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtTaxCDFocusGained

    private void jtxtTaxCDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtTaxCDKeyPressed
        if(navLoad.getMode().equalsIgnoreCase("")) {
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                if(lb.isExist("tax_master", "id", jtxtTaxCD.getText(), dataConnection)) {
                    id = jtxtTaxCD.getText();
                    navLoad.setMessage("");
                    setVoucher("edit");
                } else {
                    navLoad.setMessage("Tax CD is invalid");
                }
            }
            jtxtTaxCD.requestFocusInWindow();
        }
    }//GEN-LAST:event_jtxtTaxCDKeyPressed

    private void jtxtTaxCDKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtTaxCDKeyTyped
        lb.fixLength(evt, 7);
    }//GEN-LAST:event_jtxtTaxCDKeyTyped

    private void jtxtTaxNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtTaxNameKeyTyped
        lb.fixLength(evt, 50);
    }//GEN-LAST:event_jtxtTaxNameKeyTyped

    private void jcmbStatusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jcmbStatusKeyPressed
        if(lb.isEnter(evt)) {
            evt.consume();
            navLoad.setSaveFocus();
        }
    }//GEN-LAST:event_jcmbStatusKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JComboBox jcmbStatus;
    private javax.swing.JLabel jlblEditNo;
    private javax.swing.JLabel jlblLstUpdate;
    private javax.swing.JLabel jlblUserName;
    private javax.swing.JTextField jtxtTax;
    private javax.swing.JTextField jtxtTaxCD;
    private javax.swing.JTextField jtxtTaxName;
    // End of variables declaration//GEN-END:variables
}