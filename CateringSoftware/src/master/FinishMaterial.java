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
import java.sql.ResultSet;
import javax.swing.border.TitledBorder;
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
public class FinishMaterial extends javax.swing.JInternalFrame {
    private Connection dataConnection = DeskFrame.connMpAdmin;
    private SmallNavigation navLoad;
    private Library lb = new Library();
    private String id = "";
    private ReportTable viewTable = null;
    final private Constants.Tables table;
    PickList mainCategoryPickList = null;
    PickList foodTypePickList = null;
    PickList weightUnitPickList = null;
    PickList rawMaterialPickList = null;
    PickList rawMaterialUnitPickList = null;
    
    public FinishMaterial(Constants.Tables currentTable) throws Exception {
        if (currentTable == null) {
            throw new Exception("Invalid Parameter value");
        }
        this.table = currentTable;
        setTitle(table.NAME);
        initComponents();
        initOtherComponents();
        try {
            TitledBorder tb = (TitledBorder) jPanel1.getBorder();
            tb.setTitle(table.FRAME_TITLE + " INFORMATION");
        } catch (Exception e) {
            lb.printToLogFile("Exception while setting title for jpanel " + table.FRAME_TITLE, e);
        }
        connectNavigation();
        navLoad.setComponentEnabledDisabled(false);
        setVoucher("last");
        tableForView();
        addValidation();
    }
    
    private void initOtherComponents() {
        mainCategoryPickList = new PickList(dataConnection);
        foodTypePickList = new PickList(dataConnection);
        weightUnitPickList = new PickList(dataConnection);
        rawMaterialPickList = new PickList(dataConnection);
        rawMaterialUnitPickList = new PickList(dataConnection);
        setPickListView();
    }
    
    private void setPickListView() {
        mainCategoryPickList.setLayer(getLayeredPane());
        mainCategoryPickList.setPickListComponent(jtxtMainCategory);
        mainCategoryPickList.setNextComponent(jtxtFoodType);
        mainCategoryPickList.setReturnComponent(new JTextField[]{jtxtMainCategory});
        
        foodTypePickList.setLayer(getLayeredPane());
        foodTypePickList.setPickListComponent(jtxtFoodType);
        foodTypePickList.setNextComponent(jtxtWeight);
        foodTypePickList.setReturnComponent(new JTextField[]{jtxtFoodType});
        
        weightUnitPickList.setLayer(getLayeredPane());
        weightUnitPickList.setPickListComponent(jtxtWeightUnit);
        weightUnitPickList.setNextComponent(jcmbStatus);
        weightUnitPickList.setReturnComponent(new JTextField[]{jtxtWeightUnit});
        
        rawMaterialPickList.setLayer(getLayeredPane());
        rawMaterialPickList.setPickListComponent(jtxtRawMaterialName);
        rawMaterialPickList.setNextComponent(jtxtRawMaterialUnit);
        rawMaterialPickList.setReturnComponent(new JTextField[]{jtxtRawMaterialName});
        
        rawMaterialUnitPickList.setLayer(getLayeredPane());
        rawMaterialUnitPickList.setPickListComponent(jtxtRawMaterialUnit);
        rawMaterialUnitPickList.setNextComponent(jcmbStatus);
        rawMaterialUnitPickList.setReturnComponent(new JTextField[]{jtxtRawMaterialUnit});
    }

    private FinishMaterial() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    private void tableForView() {
        viewTable = new ReportTable();

        viewTable.AddColumn(0, "Name", 600, java.lang.String.class, null, false);
        viewTable.makeTable();
    }

    public void setID(String code) {
        id = getValueType(code, "C");
        setVoucher("Edit");
    }

    private void onViewVoucher() {
        this.dispose();

        String sql = "SELECT name_en, name_hi, name_gu FROM " + table.NAME;
        viewTable.setColumnValue(new int[]{1});
        String view_title = table.FRAME_TITLE +" VIEW";

        HeaderIntFrame1 rptDetail = new HeaderIntFrame1(dataConnection, getValueType(id, "N"), view_title, viewTable, sql, table.FORM_ID, 1, this,this.getTitle());
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
            lb.printToLogFile("Exception at dispose In " + table.FRAME_TITLE , ex);
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
                    if (lb.isExist(table.NAME, "name_en", jtxtName.getText(), dataConnection)) {
                        navLoad.setMessage("Name is already exist!");
                        comp.requestFocusInWindow();
                        return false;
                    }
                } else if (navLoad.getMode().equalsIgnoreCase("E")) {
                    if (lb.isExistForEdit(table.NAME, "name_en", jtxtName.getText(), "id", String.valueOf(id), dataConnection)) {
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
            String sql = "SELECT * FROM " + table.NAME;
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
            lb.setPermission(navLoad, table.FORM_ID);
        } catch (Exception ex) {
            lb.printToLogFile("Exception at setVoucher In " + table.FRAME_TITLE, ex);
        }
    }

    private void onPrintVoucher() {
        try {
            VoucherDisplay vd = new VoucherDisplay(id, table.PREFIX);
            DeskFrame.addOnScreen(vd, table.FRAME_TITLE +" PRINT");
        } catch(Exception ex) {
            lb.printToLogFile("Exception at onPrintVoucher In " + table.FRAME_TITLE, ex);
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

    public String getValueType(String strVal, String tag) {
        PreparedStatement pstLocal = null;
        ResultSet rsLocal = null;
        String returnVal = "";
        String sql = "";
        if (strVal.trim().equalsIgnoreCase("") && tag.equalsIgnoreCase("C")) {
            return "";
        }
        try {
            if (tag.equalsIgnoreCase("C")) {
                sql = "SELECT id FROM " + table.NAME + " WHERE name_en='"+ strVal +"'";
            } else if (tag.equalsIgnoreCase("N")) {
                sql = "SELECT name_en FROM " + table.NAME + " WHERE id='"+ strVal +"'";
            }

            if (sql != null) {
                pstLocal = dataConnection.prepareStatement(sql);
                rsLocal = pstLocal.executeQuery();
                while (rsLocal.next()) {
                    returnVal = rsLocal.getString(1);
                }
                lb.closeResultSet(rsLocal);
                lb.closeStatement(pstLocal);
            }
        } catch (Exception ex) {
            lb.printToLogFile("Exception at getValueType In " + table.FRAME_TITLE, ex);
        }
        return returnVal;
    }

    private int saveVoucher() {
        PreparedStatement pstLocal = null;
        String sql = "";
        int data = 0;
        try {
            int index = 0;
            dataConnection.setAutoCommit(false);
            if (navLoad.getMode().equalsIgnoreCase("N")) {
                id = lb.generateKey(table.NAME, "id", table.PREFIX, 7);
                sql = "INSERT INTO "+ table.NAME +"(edit_no, name_en, name_hi, name_gu, user_cd, fk_status_id, id) " +
                    "VALUES (0, ?, ?, ?, ?, ?, ?)";
            } else if (navLoad.getMode().equalsIgnoreCase("E")) {
                sql = "UPDATE "+ table.NAME +" SET name_en = ?, name_hi = ?, name_gu = ?, user_cd = ?, fk_status_id = ?, edit_no = edit_no + 1,"
                        + "time_stamp = CURRENT_TIMESTAMP WHERE id=?";
            }
            pstLocal = dataConnection.prepareStatement(sql);
            pstLocal.setString(++index, jtxtName.getText().trim().toUpperCase()); // name_en
            pstLocal.setString(++index, jtxtNameHi.getText()); // name_hi
            pstLocal.setString(++index, jtxtNameGu.getText()); // name_gu
            pstLocal.setInt(++index, DeskFrame.user_id); // user_cd
            pstLocal.setString(++index, lb.getStatusData(jcmbStatus.getSelectedItem().toString(), "C")); // fk_status_id
            pstLocal.setString(++index, id); // id
            data = pstLocal.executeUpdate();
            dataConnection.commit();
            dataConnection.setAutoCommit(true);
        } catch (SQLException ex) {
            try {
                dataConnection.rollback();
                dataConnection.setAutoCommit(true);
                lb.printToLogFile("Error at save In " + table.FRAME_TITLE, ex);
            } catch (SQLException ex1) {
                lb.printToLogFile("Error at rollback save In " + table.FRAME_TITLE, ex1);
            }
        }
        return data;
    }

    private void setComponenttextToBlank() {
        jtxtID.setText("");
        jtxtName.setText("");
        jtxtNameHi.setText("");
        jtxtNameGu.setText("");
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
                        lb.printToLogFile("Error at save in " + table.FRAME_TITLE, ex);
                        try {
                            dataConnection.rollback();
                            dataConnection.setAutoCommit(true);
                        } catch(Exception ex1){
                            lb.printToLogFile("Error at rollback save in " + table.FRAME_TITLE, ex1);
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
                                String sql = "DELETE FROM "+ table.NAME +" WHERE id = ?";
                                PreparedStatement pstLocal = dataConnection.prepareStatement(sql);
                                pstLocal.setString(1, id);
                                pstLocal.executeUpdate();
                                setVoucher("Previous");
                                dataConnection.commit();
                                dataConnection.setAutoCommit(true);
                            } catch (Exception ex) {
                                lb.printToLogFile("Exception at callDelete In " + table.FRAME_TITLE, ex);
                                try {
                                    dataConnection.rollback();
                                    dataConnection.setAutoCommit(true);
                                } catch (Exception ex1) {
                                    lb.printToLogFile("Exception at rollback callDelete In " + table.FRAME_TITLE, ex1);
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
                    jtxtName.setText(viewData.getString("name_en"));
					jtxtNameHi.setText(viewData.getString("name_hi"));
					jtxtNameGu.setText(viewData.getString("name_gu"));
                    jcmbStatus.setSelectedItem(lb.getStatusData(viewData.getInt("fk_status_id")+"", "N"));
                    jlblUserName.setText(lb.getUserName(viewData.getString("user_cd"), "N"));
                    jlblEditNo.setText(viewData.getString("edit_no"));
                    jlblLstUpdate.setText(lb.getTimeStamp(viewData.getTimestamp("time_stamp")));
                } catch (Exception ex) {
                    lb.printToLogFile("Exception at setComponentTextFromResultSet In " + table.FRAME_TITLE, ex);
                }
            }

            @Override
            public void setComponentEnabledDisabled(boolean flag) {
                jtxtID.setEnabled(!flag);
                jtxtName.setEnabled(flag);
                jtxtNameHi.setEnabled(flag);
                jtxtNameGu.setEnabled(flag);
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
        jLabel4 = new javax.swing.JLabel();
        jtxtID = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jcmbStatus = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jtxtNameHi = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jtxtNameGu = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jtxtMainCategory = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jtxtFoodType = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jtxtWeight = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jtxtWeightUnit = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jtxtRawMaterialName = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jtxtRawMaterialUnit = new javax.swing.JTextField();
        jbtnImage = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jlblUserName = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jlblEditNo = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jlblLstUpdate = new javax.swing.JLabel();

        setClosable(true);

        jpanelNavigation.setBackground(new java.awt.Color(253, 243, 243));
        jpanelNavigation.setBorder(javax.swing.BorderFactory.createMatteBorder(3, 3, 1, 1, new java.awt.Color(235, 35, 35)));
        jpanelNavigation.setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(253, 243, 243));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 3, 3, new java.awt.Color(235, 35, 35)), "Account Type Information", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 2, 16), new java.awt.Color(0, 0, 255))); // NOI18N
        jPanel1.setToolTipText("");

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
        jtxtName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtNameActionPerformed(evt);
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
        jLabel3.setText("Name Hin");

        jtxtNameHi.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        jtxtNameHi.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtNameHi.setMinimumSize(new java.awt.Dimension(6, 25));
        jtxtNameHi.setName(""); // NOI18N
        jtxtNameHi.setPreferredSize(new java.awt.Dimension(6, 25));
        jtxtNameHi.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtNameHiFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtNameHiFocusLost(evt);
            }
        });
        jtxtNameHi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtNameHiKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtNameHiKeyTyped(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel9.setText("Name Guj");

        jtxtNameGu.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        jtxtNameGu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtNameGu.setMinimumSize(new java.awt.Dimension(6, 25));
        jtxtNameGu.setName(""); // NOI18N
        jtxtNameGu.setPreferredSize(new java.awt.Dimension(6, 25));
        jtxtNameGu.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtNameGuFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtNameGuFocusLost(evt);
            }
        });
        jtxtNameGu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtNameGuKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtNameGuKeyTyped(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 0, 0));
        jLabel11.setText("Main Category");

        jtxtMainCategory.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        jtxtMainCategory.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 0, 0)));
        jtxtMainCategory.setMinimumSize(new java.awt.Dimension(6, 25));
        jtxtMainCategory.setName(""); // NOI18N
        jtxtMainCategory.setPreferredSize(new java.awt.Dimension(6, 25));
        jtxtMainCategory.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtMainCategoryFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtMainCategoryFocusLost(evt);
            }
        });
        jtxtMainCategory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtMainCategoryKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxtMainCategoryKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtMainCategoryKeyTyped(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 0, 0));
        jLabel12.setText("Food Type");

        jtxtFoodType.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        jtxtFoodType.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 0, 0)));
        jtxtFoodType.setMinimumSize(new java.awt.Dimension(6, 25));
        jtxtFoodType.setName(""); // NOI18N
        jtxtFoodType.setPreferredSize(new java.awt.Dimension(6, 25));
        jtxtFoodType.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtFoodTypeFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtFoodTypeFocusLost(evt);
            }
        });
        jtxtFoodType.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtFoodTypeKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxtFoodTypeKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtFoodTypeKeyTyped(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel13.setText("Weight");

        jtxtWeight.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        jtxtWeight.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtWeight.setMinimumSize(new java.awt.Dimension(6, 25));
        jtxtWeight.setName(""); // NOI18N
        jtxtWeight.setPreferredSize(new java.awt.Dimension(6, 25));
        jtxtWeight.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtWeightFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtWeightFocusLost(evt);
            }
        });
        jtxtWeight.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtWeightKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtWeightKeyTyped(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel14.setText("Weight Unit");

        jtxtWeightUnit.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        jtxtWeightUnit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtWeightUnit.setMinimumSize(new java.awt.Dimension(6, 25));
        jtxtWeightUnit.setName(""); // NOI18N
        jtxtWeightUnit.setPreferredSize(new java.awt.Dimension(6, 25));
        jtxtWeightUnit.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtWeightUnitFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtWeightUnitFocusLost(evt);
            }
        });
        jtxtWeightUnit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtWeightUnitKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxtWeightUnitKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtWeightUnitKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jtxtWeight, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jtxtMainCategory, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jtxtNameGu, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jtxtID, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcmbStatus, 0, 360, Short.MAX_VALUE)
                    .addComponent(jtxtName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jtxtFoodType, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                    .addComponent(jtxtNameHi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jtxtWeightUnit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtNameGu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtMainCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtWeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jcmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jtxtNameHi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(jtxtFoodType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtxtWeightUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(3, 3, 3))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel2, jtxtName});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel4, jtxtID});

        jPanel3.setBackground(new java.awt.Color(253, 243, 243));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 3, 3, new java.awt.Color(235, 35, 35)), "Raw Material Information", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 2, 16), new java.awt.Color(0, 0, 255)));
        jPanel3.setToolTipText("");

        jLabel15.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 0, 0));
        jLabel15.setText("Raw Material");

        jtxtRawMaterialName.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtRawMaterialName.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 0, 0)));
        jtxtRawMaterialName.setMinimumSize(new java.awt.Dimension(6, 25));
        jtxtRawMaterialName.setName(""); // NOI18N
        jtxtRawMaterialName.setPreferredSize(new java.awt.Dimension(6, 25));
        jtxtRawMaterialName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtRawMaterialNameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtRawMaterialNameFocusLost(evt);
            }
        });
        jtxtRawMaterialName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtRawMaterialNameActionPerformed(evt);
            }
        });
        jtxtRawMaterialName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtRawMaterialNameKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxtRawMaterialNameKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtRawMaterialNameKeyTyped(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel32.setText("Upload Image");

        jLabel33.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel33.setText("Weight Unit");

        jtxtRawMaterialUnit.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        jtxtRawMaterialUnit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtRawMaterialUnit.setMinimumSize(new java.awt.Dimension(6, 25));
        jtxtRawMaterialUnit.setName(""); // NOI18N
        jtxtRawMaterialUnit.setPreferredSize(new java.awt.Dimension(6, 25));
        jtxtRawMaterialUnit.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtRawMaterialUnitFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtRawMaterialUnitFocusLost(evt);
            }
        });
        jtxtRawMaterialUnit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtRawMaterialUnitKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxtRawMaterialUnitKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtRawMaterialUnitKeyTyped(evt);
            }
        });

        jbtnImage.setText("Browse");
        jbtnImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnImageActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jtxtRawMaterialName, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jtxtRawMaterialUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jbtnImage))
                .addGap(18, 18, 18))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jtxtRawMaterialName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jbtnImage)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jtxtRawMaterialUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(42, 42, 42))
        );

        jbtnImage.getAccessibleContext().setAccessibleName("");

        jLabel6.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel6.setText("User Name:");

        jlblUserName.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        jLabel7.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel7.setText("Edit No:");

        jlblEditNo.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        jLabel8.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel8.setText("Last Updated:");

        jlblLstUpdate.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlblUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlblEditNo, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlblLstUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpanelNavigation, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlblUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jlblEditNo, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jlblLstUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jpanelNavigation, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel6, jlblUserName});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel7, jlblEditNo});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel8, jlblLstUpdate});

        jPanel1.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtxtWeightUnitKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtWeightUnitKeyTyped
        lb.fixLength(evt, 50);
    }//GEN-LAST:event_jtxtWeightUnitKeyTyped

    private void jtxtWeightUnitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtWeightUnitKeyPressed
        setKeyTypedForPickList(weightUnitPickList, evt, jtxtWeightUnit, jPanel1);
    }//GEN-LAST:event_jtxtWeightUnitKeyPressed

    private void jtxtWeightUnitFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtWeightUnitFocusLost
        weightUnitPickList.setVisible(false);
    }//GEN-LAST:event_jtxtWeightUnitFocusLost

    private void jtxtWeightUnitFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtWeightUnitFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtWeightUnitFocusGained

    private void jtxtWeightKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtWeightKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtWeightKeyTyped

    private void jtxtWeightKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtWeightKeyPressed
        lb.enterEvent(evt, jtxtWeightUnit);
    }//GEN-LAST:event_jtxtWeightKeyPressed

    private void jtxtWeightFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtWeightFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtWeightFocusLost

    private void jtxtWeightFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtWeightFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtWeightFocusGained

    private void jtxtFoodTypeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtFoodTypeKeyTyped
        lb.fixLength(evt, 50);
    }//GEN-LAST:event_jtxtFoodTypeKeyTyped

    private void jtxtFoodTypeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtFoodTypeKeyPressed
        setKeyTypedForPickList(foodTypePickList, evt, jtxtFoodType, jPanel1);
    }//GEN-LAST:event_jtxtFoodTypeKeyPressed

    private void jtxtFoodTypeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtFoodTypeFocusLost
        foodTypePickList.setVisible(false);
    }//GEN-LAST:event_jtxtFoodTypeFocusLost

    private void jtxtFoodTypeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtFoodTypeFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtFoodTypeFocusGained

    private void jtxtMainCategoryKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtMainCategoryKeyTyped
        lb.fixLength(evt, 50);
    }//GEN-LAST:event_jtxtMainCategoryKeyTyped

    private void setKeyTypedForPickList(PickList pickList, java.awt.event.KeyEvent evt, javax.swing.JTextField field, javax.swing.JPanel jpanel) {
        pickList.setLocation(field.getX() + jPanel1.getX(), jpanel.getY() + field.getY() + field.getHeight());
        pickList.pickListKeyPress(evt);
    }
    
    private void jtxtMainCategoryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtMainCategoryKeyPressed
        setKeyTypedForPickList(mainCategoryPickList, evt, jtxtMainCategory, jPanel1);
    }//GEN-LAST:event_jtxtMainCategoryKeyPressed

    private void jtxtMainCategoryFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtMainCategoryFocusLost
        mainCategoryPickList.setVisible(false);
    }//GEN-LAST:event_jtxtMainCategoryFocusLost

    private void jtxtMainCategoryFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtMainCategoryFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtMainCategoryFocusGained

    private void jtxtNameGuKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtNameGuKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtNameGuKeyTyped

    private void jtxtNameGuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtNameGuKeyPressed
        lb.enterEvent(evt, jtxtMainCategory);
    }//GEN-LAST:event_jtxtNameGuKeyPressed

    private void jtxtNameGuFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtNameGuFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtNameGuFocusLost

    private void jtxtNameGuFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtNameGuFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtNameGuFocusGained

    private void jtxtNameHiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtNameHiKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtNameHiKeyTyped

    private void jtxtNameHiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtNameHiKeyPressed
        lb.enterEvent(evt, jtxtNameGu);
    }//GEN-LAST:event_jtxtNameHiKeyPressed

    private void jtxtNameHiFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtNameHiFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtNameHiFocusLost

    private void jtxtNameHiFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtNameHiFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtNameHiFocusGained

    private void jcmbStatusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jcmbStatusKeyPressed
        lb.enterEvent(evt, jtxtRawMaterialName);
    }//GEN-LAST:event_jcmbStatusKeyPressed

    private void jtxtIDKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtIDKeyTyped
        lb.fixLength(evt, 7);
    }//GEN-LAST:event_jtxtIDKeyTyped

    private void jtxtIDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtIDKeyPressed
        if(navLoad.getMode().equalsIgnoreCase("")) {
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                if(lb.isExist(table.NAME, "id", jtxtID.getText(), dataConnection)) {
                    id = jtxtID.getText();
                    navLoad.setMessage("");
                    setVoucher("edit");
                } else {
                    navLoad.setMessage(table.FRAME_TITLE + " ID is invalid");
                }
            }
            jtxtID.requestFocusInWindow();
        }
    }//GEN-LAST:event_jtxtIDKeyPressed

    private void jtxtIDFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtIDFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtIDFocusGained

    private void jtxtNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtNameKeyTyped
        lb.fixLength(evt, 50);
    }//GEN-LAST:event_jtxtNameKeyTyped

    private void jtxtNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtNameKeyPressed
        lb.enterEvent(evt, jtxtNameHi);
    }//GEN-LAST:event_jtxtNameKeyPressed

    private void jtxtNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtNameActionPerformed

    private void jtxtNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtNameFocusLost
        jtxtName.setText(jtxtName.getText().toUpperCase());
    }//GEN-LAST:event_jtxtNameFocusLost

    private void jtxtNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtNameFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtNameFocusGained

    private void jtxtRawMaterialNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtRawMaterialNameFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtRawMaterialNameFocusGained

    private void jtxtRawMaterialNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtRawMaterialNameFocusLost
        rawMaterialPickList.setVisible(false);
    }//GEN-LAST:event_jtxtRawMaterialNameFocusLost

    private void jtxtRawMaterialNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtRawMaterialNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtRawMaterialNameActionPerformed

    private void jtxtRawMaterialNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtRawMaterialNameKeyPressed
        lb.enterEvent(evt, jtxtRawMaterialUnit);
//        setKeyTypedForPickList(foodTypePickList, evt, jtxtID, jPanel1);
    }//GEN-LAST:event_jtxtRawMaterialNameKeyPressed

    private void jtxtRawMaterialNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtRawMaterialNameKeyTyped
        lb.fixLength(evt, 50);
    }//GEN-LAST:event_jtxtRawMaterialNameKeyTyped

    private void jtxtRawMaterialUnitFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtRawMaterialUnitFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtRawMaterialUnitFocusGained

    private void jtxtRawMaterialUnitFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtRawMaterialUnitFocusLost
    }//GEN-LAST:event_jtxtRawMaterialUnitFocusLost

    private void jtxtRawMaterialUnitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtRawMaterialUnitKeyPressed
        lb.enterEvent(evt, jbtnImage);
    }//GEN-LAST:event_jtxtRawMaterialUnitKeyPressed

    private void jtxtRawMaterialUnitKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtRawMaterialUnitKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtRawMaterialUnitKeyTyped

    private void jtxtMainCategoryKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtMainCategoryKeyReleased
        setKeyReleaseForPickList("finish_item_main", jtxtMainCategory.getText(), mainCategoryPickList, evt);
    }//GEN-LAST:event_jtxtMainCategoryKeyReleased

    private void jtxtFoodTypeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtFoodTypeKeyReleased
        setKeyReleaseForPickList("food_type", jtxtFoodType.getText(), foodTypePickList, evt);
    }//GEN-LAST:event_jtxtFoodTypeKeyReleased

    private void jtxtWeightUnitKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtWeightUnitKeyReleased
        setKeyReleaseForPickList("unit_master", jtxtWeightUnit.getText(), weightUnitPickList, evt);
    }//GEN-LAST:event_jtxtWeightUnitKeyReleased

    private void jtxtRawMaterialNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtRawMaterialNameKeyReleased
        setKeyReleaseForPickList("raw_material", jtxtRawMaterialName.getText(), rawMaterialPickList, evt);
    }//GEN-LAST:event_jtxtRawMaterialNameKeyReleased

    private void jbtnImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnImageActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbtnImageActionPerformed

    private void jtxtRawMaterialUnitKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtRawMaterialUnitKeyReleased
        setKeyReleaseForPickList("unit_master", jtxtRawMaterialUnit.getText(), rawMaterialUnitPickList, evt);
    }//GEN-LAST:event_jtxtRawMaterialUnitKeyReleased

    private void setKeyReleaseForPickList(String tableName, String text, PickList pickList, KeyEvent evt) {
        try {
            PreparedStatement pstLocal = dataConnection.prepareStatement("SELECT name_en FROM "+tableName+" WHERE name_en LIKE '%" + text.toUpperCase() + "%' AND fk_status_id = 1");
            pickList.setPreparedStatement(pstLocal);
            pickList.setValidation(dataConnection.prepareStatement("SELECT name_en FROM "+tableName+" WHERE fk_status_id = 1 AND name_en = ?"));
            pickList.pickListKeyRelease(evt);
        } catch (Exception ex) {
            lb.printToLogFile("Exception at setKeyReleaseForPickList In "+ table.FRAME_TITLE, ex);
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JButton jbtnImage;
    private javax.swing.JComboBox jcmbStatus;
    private javax.swing.JLabel jlblEditNo;
    private javax.swing.JLabel jlblLstUpdate;
    private javax.swing.JLabel jlblUserName;
    private javax.swing.JPanel jpanelNavigation;
    private javax.swing.JTextField jtxtFoodType;
    private javax.swing.JTextField jtxtID;
    private javax.swing.JTextField jtxtMainCategory;
    private javax.swing.JTextField jtxtName;
    private javax.swing.JTextField jtxtNameGu;
    private javax.swing.JTextField jtxtNameHi;
    private javax.swing.JTextField jtxtRawMaterialName;
    private javax.swing.JTextField jtxtRawMaterialUnit;
    private javax.swing.JTextField jtxtWeight;
    private javax.swing.JTextField jtxtWeightUnit;
    // End of variables declaration//GEN-END:variables
}