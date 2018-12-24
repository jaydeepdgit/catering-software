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
import support.PickList;
import support.ReportTable;
import support.SmallNavigation;
import support.VoucherDisplay;

/**
 *
 * @author @JD@
 */
public class RawSubCategory extends javax.swing.JInternalFrame {
    private Connection dataConnection = DeskFrame.connMpAdmin;
    private SmallNavigation navLoad;
    private Library lb = new Library();
    private String id = "";
    private ReportTable viewTable = null;
    PickList rawMaincategoryList = null;

    /**
     * Creates new form RawMainCategory
     */
    public RawSubCategory() {
        initComponents();
        connectNavigation();
        navLoad.setComponentEnabledDisabled(false);
        rawMaincategoryList = new PickList(dataConnection);
        setVoucher("last");
        tableForView();
        setPickListView();
        addValidation();
        setTitle(Constants.SUB_CATEGORY_FORM_NAME);
    }

    private void tableForView() {
        viewTable = new ReportTable();

        viewTable.AddColumn(0, "Name", 600, java.lang.String.class, null, false);
        viewTable.makeTable();
    }

    private void setPickListView() {
        rawMaincategoryList.setLayer(getLayeredPane());
        rawMaincategoryList.setPickListComponent(jtxtMain);
        rawMaincategoryList.setNextComponent(jtxtGuj);
        rawMaincategoryList.setReturnComponent(new JTextField[]{jtxtMain});
    }
    
    public void setID(String code) {
        id = lb.getRawMainCategory(code, "C", "Sub");
        setVoucher("Edit");
    }

    private void onViewVoucher() {
        this.dispose();

        String sql = "SELECT name_en FROM raw_material_sub";
        viewTable.setColumnValue(new int[]{1});
        String view_title = Constants.SUB_CATEGORY_FORM_NAME +" VIEW";

        HeaderIntFrame1 rptDetail = new HeaderIntFrame1(dataConnection, lb.getRawMainCategory(id, "N", "Sub"), view_title, viewTable, sql, Constants.SUB_CATEGORY_RW_FORM_ID, 1, this,this.getTitle());
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
            lb.printToLogFile("Exception at dispose In Raw Sub Category", ex);
        }
    }

    private void addValidation() {
        fieldvalidation fldvalidation = new fieldvalidation();
        jtxtName.setInputVerifier(fldvalidation);
        jtxtMain.setInputVerifier(fldvalidation);
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
                    if (lb.isExist("raw_material_sub", "name_en", jtxtName.getText(), dataConnection)) {
                        navLoad.setMessage("Name is already exist!");
                        comp.requestFocusInWindow();
                        return false;
                    }
                } else if (navLoad.getMode().equalsIgnoreCase("E")) {
                    if (lb.isExistForEdit("raw_material_sub", "name_en", jtxtName.getText(), "id", String.valueOf(id), dataConnection)) {
                        navLoad.setMessage("Name is already exist!");
                        comp.requestFocusInWindow();
                        return false;
                    }
                }
            }
            if(comp == jtxtMain){
                String mainText = jtxtMain.getText();
                if (navLoad.getMode().equalsIgnoreCase("N") || navLoad.getMode().equalsIgnoreCase("E")) {
                    if (lb.isBlank(comp)) {
                        navLoad.setMessage("Name should not be blank");
                        comp.requestFocusInWindow();
                        return false;
                    }
                }
                String code = lb.getRawMainCategory(mainText, "C", "main");
                if(code.equalsIgnoreCase("0") || code.equalsIgnoreCase("")) {
                    navLoad.setMessage("Main category is invalid");
                    jtxtMain.requestFocusInWindow();
                    return false;
                }
            }
        }
        return true;
    }

    private boolean validateForm() {
        boolean flag = true;
        flag = flag && fldValid(jtxtName);
        flag = flag && fldValid(jtxtMain);
        return flag;
    }

    public void setGroupid(String id) {
        this.id = id;
        setVoucher("edit");
    }

    private void setVoucher(String tag) {
        try {
            navLoad.setComponentEnabledDisabled(false);
            String sql = "SELECT * FROM raw_material_sub";
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
            lb.setPermission(navLoad, Constants.SUB_CATEGORY_RW_FORM_ID);
        } catch (Exception ex) {
            lb.printToLogFile("Exception at setVoucher In Raw Sub Category", ex);
        }
    }

    private void onPrintVoucher() {
        try {
            VoucherDisplay vd = new VoucherDisplay(id, Constants.SUB_CATEGORY_RM_INITIAL);
            DeskFrame.addOnScreen(vd, Constants.SUB_CATEGORY_FORM_NAME +" PRINT");
        } catch(Exception ex) {
            lb.printToLogFile("Exception at onPrintVoucher In Raw Sub Category", ex);
        }
    }

    private void cancelOrClose() {
        if (navLoad.getSaveFlag()) {
            this.dispose();
        } else {
            rawMaincategoryList.setVisible(false);
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
        rawMaincategoryList.setVisible(false);
        try {
            dataConnection.setAutoCommit(false);
            if (navLoad.getMode().equalsIgnoreCase("N")) {
                id = lb.generateKey("raw_material_sub", "id", Constants.SUB_CATEGORY_RM_INITIAL, 7);
                sql = "INSERT INTO raw_material_sub(name_en, name_gu, name_hi, user_cd, fk_raw_material_main_id, fk_status_id, id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            } else if (navLoad.getMode().equalsIgnoreCase("E")) {
                sql = "UPDATE raw_material_sub SET name_en = ?, name_gu = ?, name_hi = ?, user_cd = ?, fk_raw_material_main_id = ?, fk_status_id = ?, edit_no = edit_no + 1,"
                        + "time_stamp = CURRENT_TIMESTAMP WHERE id=?";
            }
            pstLocal = dataConnection.prepareStatement(sql);
            pstLocal.setString(1, jtxtName.getText().trim().toUpperCase()); // name
            pstLocal.setString(2, jtxtGuj.getText().trim()); // gujarati name
            pstLocal.setString(3, jtxtHindi.getText().trim()); // hindi name
            pstLocal.setInt(4, DeskFrame.user_id); // user_cd
            pstLocal.setString(5, lb.getRawMainCategory(jtxtMain.getText(), "c", "main"));
            pstLocal.setString(6, lb.getStatusData(jcmbStatus.getSelectedItem().toString(), "C")); // fk_status_id
            pstLocal.setString(7, id); // id
            data = pstLocal.executeUpdate();
            dataConnection.commit();
            dataConnection.setAutoCommit(true);
        } catch (SQLException ex) {
            try {
                dataConnection.rollback();
                dataConnection.setAutoCommit(true);
                lb.printToLogFile("Error at save In Raw Sub Category", ex);
            } catch (SQLException ex1) {
                lb.printToLogFile("Error at rollback save In Raw Sub Category", ex1);
            }
        }
        return data;
    }

    private void setComponenttextToBlank() {
        jtxtID.setText("");
        jtxtName.setText("");
        jtxtMain.setText("");
        jtxtGuj.setText("");
        jtxtHindi.setText("");
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
                    setComponentEnabledDisabled(true);
                    jtxtName.requestFocusInWindow();
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
                        lb.printToLogFile("Error at save in Raw Sub Category", ex);
                        try {
                            dataConnection.rollback();
                            dataConnection.setAutoCommit(true);
                        } catch(Exception ex1){
                            lb.printToLogFile("Error at rollback save in Raw Sub Category", ex1);
                        }
                    }
                }
            }

            @Override
            public void callDelete() {
                lb.confirmDialog(Constants.DELETE_RECORD);
                if (lb.type) {
                    try {
                        dataConnection.setAutoCommit(false);
                        String sql = "DELETE FROM raw_material_sub WHERE id = ?";
                        PreparedStatement pstLocal = dataConnection.prepareStatement(sql);
                        pstLocal.setString(1, id);
                        pstLocal.executeUpdate();
                        setVoucher("Previous");
                        dataConnection.commit();
                        dataConnection.setAutoCommit(true);
                    } catch (Exception ex) {
                        lb.printToLogFile("Exception at callDelete In Raw Sub Category", ex);
                        try {
                            dataConnection.rollback();
                            dataConnection.setAutoCommit(true);
                        } catch (Exception ex1) {
                            lb.printToLogFile("Exception at rollback callDelete In Raw Sub Category", ex1);
                        }
                    }
                } else {
                    navLoad.setSaveFocus();
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
                    jtxtName.setText(viewData.getString("name_en"));
                    jtxtMain.setText(lb.getRawMainCategory(viewData.getString("fk_raw_material_main_id"), "N", "main"));
                    jtxtGuj.setText(viewData.getString("name_gu"));
                    jtxtHindi.setText(viewData.getString("name_hi"));
                    jcmbStatus.setSelectedItem(lb.getStatusData(viewData.getInt("fk_status_id")+"", "N"));
                    jlblUserName.setText(lb.getUserName(viewData.getString("user_cd"), "N"));
                    jlblEditNo.setText(viewData.getString("edit_no"));
                    jlblLstUpdate.setText(lb.getTimeStamp(viewData.getTimestamp("time_stamp")));
                } catch (Exception ex) {
                    lb.printToLogFile("Exception at setComponentTextFromResultSet In Raw Sub Category", ex);
                }
            }

            @Override
            public void setComponentEnabledDisabled(boolean flag) {
                jtxtID.setEnabled(!flag);
                jtxtName.setEnabled(flag);
                jtxtMain.setEnabled(flag);
                jtxtGuj.setEditable(flag);
                jtxtHindi.setEnabled(flag);
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
        jLabel3 = new javax.swing.JLabel();
        jtxtMain = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jtxtGuj = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jtxtHindi = new javax.swing.JTextField();

        setClosable(true);

        jpanelNavigation.setBackground(new java.awt.Color(253, 243, 243));
        jpanelNavigation.setBorder(javax.swing.BorderFactory.createMatteBorder(3, 3, 1, 1, new java.awt.Color(235, 35, 35)));
        jpanelNavigation.setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(253, 243, 243));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 3, 3, new java.awt.Color(235, 35, 35)), "Raw Material Sub Category", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 2, 16), new java.awt.Color(0, 0, 255))); // NOI18N

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

        jLabel3.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 0, 0));
        jLabel3.setText("Main Category");

        jtxtMain.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtMain.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 0, 0)));
        jtxtMain.setMinimumSize(new java.awt.Dimension(6, 25));
        jtxtMain.setName(""); // NOI18N
        jtxtMain.setPreferredSize(new java.awt.Dimension(6, 25));
        jtxtMain.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtMainFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtMainFocusLost(evt);
            }
        });
        jtxtMain.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtMainKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxtMainKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtMainKeyTyped(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 0, 0));
        jLabel9.setText("Gujarati Name");

        jtxtGuj.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        jtxtGuj.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 0, 0)));
        jtxtGuj.setMinimumSize(new java.awt.Dimension(6, 25));
        jtxtGuj.setName(""); // NOI18N
        jtxtGuj.setPreferredSize(new java.awt.Dimension(6, 25));
        jtxtGuj.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtGujFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtGujFocusLost(evt);
            }
        });
        jtxtGuj.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtGujKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxtGujKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtGujKeyTyped(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 0, 0));
        jLabel10.setText("Hindi Name");

        jtxtHindi.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        jtxtHindi.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 0, 0)));
        jtxtHindi.setMinimumSize(new java.awt.Dimension(6, 25));
        jtxtHindi.setName(""); // NOI18N
        jtxtHindi.setPreferredSize(new java.awt.Dimension(6, 25));
        jtxtHindi.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtHindiFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtHindiFocusLost(evt);
            }
        });
        jtxtHindi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtHindiKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxtHindiKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtHindiKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtxtName, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtID, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jtxtMain, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel6)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGap(24, 24, 24)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jlblLstUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jlblEditNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jlblUserName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jcmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jtxtHindi, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jtxtGuj, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(254, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel2, jLabel4, jLabel5});

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
                    .addComponent(jtxtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtGuj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtHindi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                .addGap(19, 19, 19))
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
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpanelNavigation, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                if(lb.isExist("raw_material_sub", "id", jtxtID.getText(), dataConnection)) {
                    id = jtxtID.getText();
                    navLoad.setMessage("");
                    setVoucher("edit");
                } else {
                    navLoad.setMessage("Raw Sub Category ID is invalid");
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
        lb.enterEvent(evt, jtxtMain);
    }//GEN-LAST:event_jtxtNameKeyPressed

    private void jtxtMainFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtMainFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtMainFocusGained

    private void jtxtMainFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtMainFocusLost
        rawMaincategoryList.setVisible(false);
    }//GEN-LAST:event_jtxtMainFocusLost

    private void jtxtMainKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtMainKeyPressed
        rawMaincategoryList.setLocation(jtxtMain.getX() + jPanel1.getX(), jPanel1.getY() + jtxtMain.getY() + jtxtMain.getHeight());
        rawMaincategoryList.pickListKeyPress(evt);
    }//GEN-LAST:event_jtxtMainKeyPressed

    private void jtxtMainKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtMainKeyTyped
        lb.fixLength(evt, 100);
    }//GEN-LAST:event_jtxtMainKeyTyped

    private void jtxtMainKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtMainKeyReleased
        try {
        PreparedStatement pstLocal = dataConnection.prepareStatement("SELECT name_en FROM raw_material_main WHERE name_en LIKE '%" + jtxtMain.getText().toUpperCase() + "%' AND fk_status_id = 1");
        rawMaincategoryList.setPreparedStatement(pstLocal);
        rawMaincategoryList.setValidation(dataConnection.prepareStatement("SELECT name_en FROM raw_material_main WHERE fk_status_id = 1 AND name_en = ?"));
        rawMaincategoryList.pickListKeyRelease(evt);
    } catch (Exception ex) {
        lb.printToLogFile("Exception at jtxtMainKeyReleased In Raw Sub Category", ex);
    }
    }//GEN-LAST:event_jtxtMainKeyReleased

    private void jtxtGujFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtGujFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtGujFocusGained

    private void jtxtGujFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtGujFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtGujFocusLost

    private void jtxtGujKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtGujKeyPressed
        lb.enterEvent(evt, jtxtHindi);
    }//GEN-LAST:event_jtxtGujKeyPressed

    private void jtxtGujKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtGujKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtGujKeyReleased

    private void jtxtGujKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtGujKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtGujKeyTyped

    private void jtxtHindiFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtHindiFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtHindiFocusGained

    private void jtxtHindiFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtHindiFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtHindiFocusLost

    private void jtxtHindiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtHindiKeyPressed
        lb.enterEvent(evt, jcmbStatus);
    }//GEN-LAST:event_jtxtHindiKeyPressed

    private void jtxtHindiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtHindiKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtHindiKeyReleased

    private void jtxtHindiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtHindiKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtHindiKeyTyped

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel10;
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
    private javax.swing.JTextField jtxtGuj;
    private javax.swing.JTextField jtxtHindi;
    private javax.swing.JTextField jtxtID;
    private javax.swing.JTextField jtxtMain;
    private javax.swing.JTextField jtxtName;
    // End of variables declaration//GEN-END:variables
}