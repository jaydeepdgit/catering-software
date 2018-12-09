/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameEvent;
import cateringsoftware.DeskFrame;
import support.Constants;
import support.HeaderIntFrame1;
import support.Library;
import support.OurDateChooser;
import support.PickList;
import support.ReportTable;
import support.SmallNavigation;
import support.VoucherDisplay;

/**
 *
 * @author @JD@
 */
public class CheckPrint extends javax.swing.JInternalFrame {
    private SmallNavigation navLoad = null;
    private Library lb = new Library();
    private String id = "", initial = Constants.CHECK_PRINT_INITIAL;
    private ReportTable checkMaster;
    private PickList accountPickList = null;
    private Connection dataConnection = DeskFrame.connMpAdmin;
    PickList bankPickList = null;

    /**
     * Creates new form CheckPrint
     */
    public CheckPrint() {
        initComponents();
        initOtherComponents();
        setVoucher("last");
    }

    public CheckPrint(String id) {
        initComponents();
        jtxtCheckCD.setText(id.substring(initial.length()));
        this.id = initial + jtxtCheckCD.getText();
        initOtherComponents();
        setVoucher("Edit");
    }

    private void initOtherComponents() {
        addNavigation();
        addValidation();
        setCompEnable(false);
        makeChildTable();
        setPermission();
        setPickListView();
        jlblImgName.setVisible(false);
        lb.setStaticImage("satyamev.png", jlblImg, jlblImgName);
        jlblStart.setText(initial);
        setTitle(Constants.CHECK_PRINT_FORM_NAME);
    }

    private void setPickListView() {
        bankPickList = new PickList(dataConnection);

        bankPickList.setLayer(getLayeredPane());
        bankPickList.setPickListComponent(jtxtBankName);
        bankPickList.setNextComponent(jcmbACPay);
        bankPickList.setReturnComponent(new JTextField[]{jtxtBankName});

        accountPickList = new PickList(dataConnection);

        accountPickList.setLayer(getLayeredPane());
        accountPickList.setPickListComponent(jtxtPartyName);
        accountPickList.setNextComponent(jtxtCDate);
    }

    private void setPermission() {
        UserRights.setUserRightsToPanel(navLoad, Constants.CHECK_PRINT_FORM_ID);
    }

    private void makeChildTable(){
        checkMaster = new ReportTable();
        checkMaster.AddColumn(0, "Check CD", 100, java.lang.String.class, null, false);
        checkMaster.AddColumn(1, "Party Name", 300, java.lang.String.class, null, false);
        checkMaster.AddColumn(2, "Check Date", 100, java.lang.String.class, null, false);
        checkMaster.AddColumn(3, "Amount", 100, java.lang.String.class, null, false);
        checkMaster.AddColumn(4, "Chq No", 100, java.lang.String.class, null, false);
        checkMaster.AddColumn(5, "Bank Name", 300, java.lang.String.class, null, false);
        checkMaster.makeTable();
    }

    public void setID(String id) {
        this.id = id;
        setVoucher("edit");
    }

    private void onViewVoucher() {
        this.dispose();

        String sql = "SELECT cp.id, cp.party_name, cp.cheque_date, cp.amount, cp.cheque_no, bm.name " +
            "FROM cheque_print cp LEFT JOIN bank_master bm ON bm.id = cp.fk_bank_id";
        checkMaster.setColumnValue(new int[]{1, 2, 3, 4, 5, 6});
        String view_title = Constants.CHECK_PRINT_FORM_NAME +" VIEW";

        HeaderIntFrame1 rptDetail = new HeaderIntFrame1(dataConnection, id+"", view_title, checkMaster, sql, Constants.CHECK_PRINT_FORM_ID, 1, this,this.getTitle());
        rptDetail.makeView();
        rptDetail.setVisible(true);

        Component c = DeskFrame.tabbedPane.add(view_title, rptDetail);
        c.setName(view_title);
        DeskFrame.tabbedPane.setSelectedComponent(c);
    }

    public void setStartupFocus(){
        jtxtPartyName.requestFocusInWindow();
    }

    private void addValidation(){
        FieldValidation valid = new FieldValidation();
        jtxtCheckCD.setInputVerifier(valid);
        jtxtPartyName.setInputVerifier(valid);
        jtxtCDate.setInputVerifier(valid);
        jtxtAmount.setInputVerifier(valid);
        jtxtChqNo.setInputVerifier(valid);
        jtxtBankName.setInputVerifier(valid);
    }

    class FieldValidation extends InputVerifier {
        @Override
        public boolean verify(JComponent input) {
            boolean val = false;
            if (input.equals(jtxtCheckCD)) {
                val = fielddValid(input);
            } else if (input.equals(jtxtPartyName)) {
                val = fielddValid(input);
            } else if (input.equals(jtxtCDate)) {
                val = fielddValid(input);
            } else if (input.equals(jtxtAmount)) {
                val = fielddValid(input);
            } else if (input.equals(jtxtChqNo)) {
                val = fielddValid(input);
            } else if (input.equals(jtxtBankName)) {
                val = fielddValid(input);
            } 
            return val;
        }
    }

    private boolean fielddValid(Component comp) {
        navLoad.setMessage("");
        if (comp == jtxtPartyName) {
            if (navLoad.getMode().equalsIgnoreCase("N") || navLoad.getMode().equalsIgnoreCase("E")) {
                if (lb.isBlank(comp)) {
                    navLoad.setMessage("Party Name should not blank");
                    comp.requestFocusInWindow();
                    return false;
                }
            }
        }
        if (!lb.checkDate(jtxtCDate)) {
            navLoad.setMessage(Constants.INVALID_DATE);
            jtxtCDate.requestFocusInWindow();
            return false;
        }
        if(lb.replaceAll(jtxtAmount.getText()) < 0) {
            navLoad.setMessage(Constants.INVALID_AMOUNT);
            jtxtAmount.requestFocusInWindow();
            return false;
        }
        if (comp == jtxtChqNo) {
            if (navLoad.getMode().equalsIgnoreCase("N") || navLoad.getMode().equalsIgnoreCase("E")) {
                if (lb.isBlank(comp)) {
                    navLoad.setMessage("Check No should not blank");
                    comp.requestFocusInWindow();
                    return false;
                }
            }
        }
        if (comp == jtxtBankName) {
            if (navLoad.getMode().equalsIgnoreCase("N") || navLoad.getMode().equalsIgnoreCase("E")) {
                if (lb.isBlank(comp)) {
                    navLoad.setMessage("Bank Name should not blank");
                    comp.requestFocusInWindow();
                    return false;
                }
            }
        }
        return true;
    }

    private void setCompEnable(boolean flag) {
        jtxtCheckCD.setEnabled(!flag);
        jtxtPartyName.setEnabled(flag);
        jtxtCDate.setEnabled(flag);
        jtxtAmount.setEnabled(flag);
        jtxtChqNo.setEnabled(flag);
        jtxtBankName.setEnabled(flag);
        jcmbACPay.setEnabled(flag);
        jtxtPartyName.requestFocusInWindow();
    }

    private void setCompText(String text) {
        jtxtCheckCD.setText(text);
        jtxtPartyName.setText(text);
        jtxtCDate.setText(text);
        jtxtAmount.setText(text);
        jtxtChqNo.setText(text);
        jtxtBankName.setText(text);
        lb.setDateChooserProperty(jtxtCDate);
    }

    private boolean validateForm() {
        boolean flag = fielddValid(jtxtPartyName);
        return flag;
    }

    private void addNavigation() {
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
                        if(navLoad.getMode().equalsIgnoreCase("N")){
                            setVoucher("Last");
                        } else if(navLoad.getMode().equalsIgnoreCase("E")){
                            setVoucher("Edit");
                        }
                        navLoad.setMode("");
                        navLoad.setFirstFocus();
                    }
                } catch (Exception ex) {
                    try {
                        dataConnection.rollback();
                        dataConnection.setAutoCommit(true);
                        lb.printToLogFile("Error at save In Check Print", ex);
                    } catch (SQLException ex1) {
                        lb.printToLogFile("Error at rollback save In Check Print", ex1);
                    }
                }
            }

            @Override
            public void callDelete() {
                try {
                    lb.confirmDialog(Constants.DELETE_THIS +"check details " + jtxtPartyName.getText() + " ?");
                    if(lb.type) {
                        dataConnection.setAutoCommit(false);
                        delete();
                        dataConnection.commit();
                        dataConnection.setAutoCommit(true);
                        setVoucher("Last");
                    }
                    navLoad.setFirstFocus();
                } catch (SQLException ex) {
                    try {
                        dataConnection.rollback();
                        dataConnection.setAutoCommit(true);
                        lb.printToLogFile("Error at delete In Check Print", ex);
                    } catch (SQLException ex1) {
                        lb.printToLogFile("Error at rollback delete In Check Print", ex1);
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
                closeORcancel();
            }

            @Override
            public void callPrint() {
                try {
                    VoucherDisplay vd = new VoucherDisplay(id, initial);
                    DeskFrame.addOnScreen(vd, Constants.CHECK_PRINT_FORM_NAME +" REPORT");
                } catch(Exception ex) {
                    lb.printToLogFile("Exception at callPrint In Check Print", ex);
                }
            }

            @Override
            public void setComponentTextFromResultSet() {
                try {
                    id = navLoad.viewData.getString("id");
                    jtxtCheckCD.setText(id.substring(initial.length()));
                    jtxtPartyName.setText(navLoad.viewData.getString("party_name"));
                    jtxtCDate.setText(lb.ConvertDateFormetForDBForConcurrency(navLoad.viewData.getString("cheque_date")));
                    jlblDay.setText(lb.setDay(jtxtCDate));
                    jtxtAmount.setText(navLoad.viewData.getString("amount"));
                    jtxtChqNo.setText(navLoad.viewData.getString("cheque_no"));
                    jtxtBankName.setText(lb.getBankName(navLoad.viewData.getString("fk_bank_id"), "N"));
                    if(navLoad.viewData.getInt("ac_pay") == 1) {
                        jcmbACPay.setSelected(true);
                    } else {
                        jcmbACPay.setSelected(false);
                    }
                    jlblUserName.setText(lb.getUserName(navLoad.viewData.getString("user_cd"), "N"));
                    jlblEditNo.setText(navLoad.viewData.getString("edit_no"));
                    jlblLstUpdate.setText(lb.getTimeStamp(viewData.getTimestamp("time_stamp")));
                } catch (Exception ex) {
                    lb.printToLogFile("Error at setComponentTextFromResultSet In Check Print", ex);
                }
            }

            @Override
            public void setComponentEnabledDisabled(boolean flag) {
                jtxtCheckCD.setEnabled(!flag);
                jtxtPartyName.setEnabled(flag);
                jtxtCDate.setEnabled(flag);
                jtxtAmount.setEnabled(flag);
                jtxtChqNo.setEnabled(flag);
                jtxtBankName.setEnabled(flag);
                jtxtPartyName.requestFocusInWindow();
            }
        }
        navLoad = new Navigation();
        navLoad.setVisible(true);
        jPanel1.add(navLoad);
        jPanel1.setVisible(true);
    }

    private void setVoucher(String tag){
        try {
            String sql = "SELECT * FROM cheque_print";
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
            navLoad.viewData = navLoad.fetchData(sql);
            if (navLoad.viewData.next()) {
                navLoad.setComponentTextFromResultSet();
            } else {
                if(tag.equalsIgnoreCase("last")) {
                    setCompText("");
                }
            }
            setCompEnable(false);
            navLoad.setLastFocus();
        } catch (Exception ex) {
            lb.printToLogFile("Error at setVoucher In Check Print", ex);
        }
    }

    private void delete() throws SQLException{
        PreparedStatement psLocal = null;

        psLocal = dataConnection.prepareStatement("DELETE FROM cheque_print WHERE id = ?");
        psLocal.setString(1, id);
        psLocal.executeUpdate();
    }

    private void saveVoucher() throws SQLException, ParseException {
        PreparedStatement psLocal = null;

        if(navLoad.getMode().equalsIgnoreCase("N")){
            psLocal = dataConnection.prepareStatement("INSERT INTO cheque_print(party_name, cheque_date, amount, cheque_no, fk_bank_id, ac_pay, user_cd, id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            id = lb.generateKey("cheque_print", "id", initial, 7);
        } else if(navLoad.getMode().equalsIgnoreCase("E")) {
            psLocal = dataConnection.prepareStatement("UPDATE cheque_print SET party_name = ?, cheque_date = ?, amount = ?, cheque_no = ?, fk_bank_id = ?, ac_pay = ?, user_cd = ?, edit_no = edit_no + 1, time_stamp = CURRENT_TIMESTAMP WHERE id = ?");
        }
        int ac_pay = 0;
        if(jcmbACPay.isSelected()) {
            ac_pay = 1;
        } else {
            ac_pay = 0;
        }
        psLocal.setString(1, jtxtPartyName.getText()); // Party Name
        psLocal.setString(2, lb.tempConvertFormatForDBorConcurrency(jtxtCDate.getText())); // Chq Date 
        psLocal.setString(3, jtxtAmount.getText()); // Amount
        psLocal.setString(4, jtxtChqNo.getText()); // Chq No
        psLocal.setString(5, lb.getBankName(jtxtBankName.getText(), "C")); // Bank Name
        psLocal.setInt(6, ac_pay); // Account Pay
        psLocal.setInt(7, DeskFrame.user_id); // User CD
        psLocal.setString(8, id); // Check CD
        psLocal.executeUpdate();
    }

    private void closeORcancel() {
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
                lb.printToLogFile("Exception at dispose In Check Print", pve);
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
        jtxtCheckCD = new javax.swing.JTextField();
        jtxtPartyName = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jlblLstUpdate = new javax.swing.JLabel();
        jlblEditNo = new javax.swing.JLabel();
        jlblUserName = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jtxtCDate = new javax.swing.JTextField();
        jBillDateBtn = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jtxtAmount = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jtxtChqNo = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jtxtBankName = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jlblImg = new javax.swing.JLabel();
        jlblImgName = new javax.swing.JLabel();
        jlblDay = new javax.swing.JLabel();
        jcmbACPay = new javax.swing.JCheckBox();
        jlblStart = new javax.swing.JLabel();

        jPanel1.setBackground(new java.awt.Color(253, 243, 243));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(3, 3, 1, 1, new java.awt.Color(235, 35, 35)));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(253, 243, 243));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 3, 3, new java.awt.Color(235, 35, 35)), "Check Print Information", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 2, 16), new java.awt.Color(0, 0, 255))); // NOI18N

        jLabel2.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 0, 0));
        jLabel2.setText("Party Name");

        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel1.setText("Check CD");

        jtxtCheckCD.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtCheckCD.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtCheckCD.setMinimumSize(new java.awt.Dimension(6, 25));
        jtxtCheckCD.setPreferredSize(new java.awt.Dimension(6, 25));
        jtxtCheckCD.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtCheckCDFocusGained(evt);
            }
        });
        jtxtCheckCD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtCheckCDKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtCheckCDKeyTyped(evt);
            }
        });

        jtxtPartyName.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtPartyName.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 0, 0)));
        jtxtPartyName.setMinimumSize(new java.awt.Dimension(6, 25));
        jtxtPartyName.setPreferredSize(new java.awt.Dimension(6, 25));
        jtxtPartyName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtPartyNameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtPartyNameFocusLost(evt);
            }
        });
        jtxtPartyName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtPartyNameKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxtPartyNameKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtPartyNameKeyTyped(evt);
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

        jLabel3.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 0, 0));
        jLabel3.setText("Check Date");

        jtxtCDate.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtCDate.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 0, 0)));
        jtxtCDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtCDateFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtCDateFocusLost(evt);
            }
        });
        jtxtCDate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtCDateKeyPressed(evt);
            }
        });

        jBillDateBtn.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jBillDateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBillDateBtnActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 0, 0));
        jLabel4.setText("Amount");

        jtxtAmount.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtAmount.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 0, 0)));
        jtxtAmount.setMinimumSize(new java.awt.Dimension(6, 25));
        jtxtAmount.setPreferredSize(new java.awt.Dimension(6, 25));
        jtxtAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtAmountFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtAmountFocusLost(evt);
            }
        });
        jtxtAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtAmountKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtAmountKeyTyped(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 0, 0));
        jLabel5.setText("Chq No");

        jtxtChqNo.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtChqNo.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 0, 0)));
        jtxtChqNo.setMinimumSize(new java.awt.Dimension(6, 25));
        jtxtChqNo.setPreferredSize(new java.awt.Dimension(6, 25));
        jtxtChqNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtChqNoFocusGained(evt);
            }
        });
        jtxtChqNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtChqNoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtChqNoKeyTyped(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 0, 0));
        jLabel9.setText("Bank Name");

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
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxtBankNameKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtBankNameKeyTyped(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(215, 227, 208));
        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(4, 110, 152), 1, true));
        jPanel3.setPreferredSize(new java.awt.Dimension(200, 200));

        jlblImg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblImg.setText("Image");
        jlblImg.setMaximumSize(new java.awt.Dimension(113, 131));
        jlblImg.setMinimumSize(new java.awt.Dimension(113, 131));
        jlblImg.setPreferredSize(new java.awt.Dimension(113, 131));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlblImg, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlblImg, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jlblImgName.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(4, 110, 152), 1, true));

        jlblDay.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jlblDay.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));

        jcmbACPay.setText("A/C Pay");
        jcmbACPay.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jcmbACPayKeyPressed(evt);
            }
        });

        jlblStart.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jlblStart.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblStart.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtxtPartyName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jtxtBankName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jlblStart, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtxtCheckCD, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jcmbACPay)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jtxtCDate, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jBillDateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jlblDay, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jtxtAmount, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                                        .addComponent(jtxtChqNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                            .addComponent(jlblImgName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jlblEditNo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jlblUserName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(184, 184, 184))
                            .addComponent(jlblLstUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(163, 163, 163))))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel2, jLabel3, jLabel4, jLabel5, jLabel6, jLabel7, jLabel8, jLabel9});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlblImgName, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jtxtCheckCD, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                            .addComponent(jlblStart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jtxtPartyName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jtxtCDate)
                            .addComponent(jBillDateBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
                            .addComponent(jlblDay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jtxtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jtxtChqNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jtxtBankName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jcmbACPay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jBillDateBtn, jLabel1, jLabel2, jLabel3, jlblDay, jtxtCDate, jtxtCheckCD, jtxtPartyName});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel6, jLabel7, jLabel8, jlblEditNo, jlblLstUpdate, jlblUserName});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel4, jLabel5, jLabel9, jtxtAmount, jtxtBankName, jtxtChqNo});

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

    private void jtxtPartyNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtPartyNameKeyPressed
        accountPickList.setLocation(jtxtPartyName.getX() + jPanel2.getX(), jtxtPartyName.getY() + jtxtPartyName.getHeight() + jPanel2.getY());
        accountPickList.pickListKeyPress(evt);
        lb.enterEvent(evt, jtxtCDate);
    }//GEN-LAST:event_jtxtPartyNameKeyPressed

    private void jtxtPartyNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtPartyNameKeyTyped
        lb.fixLength(evt, 255);
    }//GEN-LAST:event_jtxtPartyNameKeyTyped

    private void jtxtPartyNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtPartyNameFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtPartyNameFocusGained

    private void jtxtPartyNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtPartyNameFocusLost
        accountPickList.setVisible(false);
        lb.toUpper(evt);
    }//GEN-LAST:event_jtxtPartyNameFocusLost

    private void jtxtCheckCDKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtCheckCDKeyPressed
        if(navLoad.getMode().equalsIgnoreCase("")) {
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                if(lb.isExist("cheque_print", "id", initial + jtxtCheckCD.getText(), dataConnection)) {
                    id = initial + jtxtCheckCD.getText();
                    navLoad.setMessage("");
                    setVoucher("Edit");
                } else {
                    navLoad.setMessage(Constants.INVALID_VOUCHER_NO);
                }
            }
            jtxtCheckCD.requestFocusInWindow();
        }
    }//GEN-LAST:event_jtxtCheckCDKeyPressed

    private void jtxtCheckCDFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtCheckCDFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtCheckCDFocusGained

    private void jtxtCheckCDKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtCheckCDKeyTyped
        lb.fixLength(evt, 7 - initial.length());
    }//GEN-LAST:event_jtxtCheckCDKeyTyped

    private void jtxtCDateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtCDateFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtCDateFocusGained

    private void jtxtCDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtCDateFocusLost
        if(lb.checkDate(jtxtCDate)) {
            jlblDay.setText(lb.setDay(jtxtCDate));
        } else {
            navLoad.setMessage(Constants.CORRECT_DATE);
        }
    }//GEN-LAST:event_jtxtCDateFocusLost

    private void jtxtCDateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtCDateKeyPressed
        lb.enterFocus(evt, jtxtAmount);
    }//GEN-LAST:event_jtxtCDateKeyPressed

    private void jBillDateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBillDateBtnActionPerformed
        OurDateChooser odc = new OurDateChooser();
        odc.setnextFocus(jtxtCDate);
        odc.setFormat("dd/MM/yyyy");
        JPanel jp = new JPanel();
        this.add(jp);
        jp.setBounds(jtxtCDate.getX(), jtxtCDate.getY() + 145, jtxtCDate.getX() + odc.getWidth(), jtxtCDate.getY() + odc.getHeight());
        odc.setLocation(0, 0);
        odc.showDialog(jp, Constants.SELECT_DATE);
    }//GEN-LAST:event_jBillDateBtnActionPerformed

    private void jtxtAmountFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtAmountFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtAmountFocusGained

    private void jtxtAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtAmountFocusLost
        lb.toDouble(evt);
    }//GEN-LAST:event_jtxtAmountFocusLost

    private void jtxtAmountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtAmountKeyPressed
        lb.enterEvent(evt, jtxtChqNo);
    }//GEN-LAST:event_jtxtAmountKeyPressed

    private void jtxtAmountKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtAmountKeyTyped
        lb.onlyNumber(evt, 10);
    }//GEN-LAST:event_jtxtAmountKeyTyped

    private void jtxtChqNoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtChqNoFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtChqNoFocusGained

    private void jtxtChqNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtChqNoKeyPressed
        lb.enterEvent(evt, jtxtBankName);
    }//GEN-LAST:event_jtxtChqNoKeyPressed

    private void jtxtChqNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtChqNoKeyTyped
        lb.fixLength(evt, 10);
    }//GEN-LAST:event_jtxtChqNoKeyTyped

    private void jtxtBankNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtBankNameFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtBankNameFocusGained

    private void jtxtBankNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtBankNameFocusLost
        bankPickList.setVisible(false);
    }//GEN-LAST:event_jtxtBankNameFocusLost

    private void jtxtBankNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtBankNameKeyPressed
        bankPickList.setLocation(jtxtBankName.getX() + jPanel2.getX(), jtxtBankName.getY() + jtxtBankName.getHeight() + jPanel2.getY());
        bankPickList.pickListKeyPress(evt);
        lb.enterEvent(evt, jcmbACPay);
    }//GEN-LAST:event_jtxtBankNameKeyPressed

    private void jtxtBankNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtBankNameKeyTyped
        lb.fixLength(evt, 200);
    }//GEN-LAST:event_jtxtBankNameKeyTyped

    private void jtxtBankNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtBankNameKeyReleased
        try {
            PreparedStatement pstLocal = dataConnection.prepareStatement("SELECT name FROM bank_master WHERE name LIKE '%"+ jtxtBankName.getText() +"%'");
            bankPickList.setPreparedStatement(pstLocal);
            bankPickList.setValidation(dataConnection.prepareStatement("SELECT name FROM bank_master WHERE name = ?"));
            bankPickList.pickListKeyRelease(evt);
        } catch (Exception ex) {
            lb.printToLogFile("Exception at jtxtBankNameKeyReleased In Check Print", ex);
        }
    }//GEN-LAST:event_jtxtBankNameKeyReleased

    private void jcmbACPayKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jcmbACPayKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            navLoad.setSaveFocus();
        }
    }//GEN-LAST:event_jcmbACPayKeyPressed

    private void jtxtPartyNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtPartyNameKeyReleased
        try {
            accountPickList.setReturnComponent(new JTextField[]{jtxtPartyName});
            PreparedStatement pstLocal = dataConnection.prepareStatement("SELECT name FROM account_master WHERE name LIKE '%"+ jtxtPartyName.getText().toUpperCase() +"%'");
            accountPickList.setValidation(dataConnection.prepareStatement("SELECT name FROM account_master WHERE name = ?"));
            accountPickList.setPreparedStatement(pstLocal);
            accountPickList.pickListKeyRelease(evt);
        } catch (Exception ex) {
            lb.printToLogFile("Exception at jtxtACAliasKeyReleased In Check Print", ex);
        }
    }//GEN-LAST:event_jtxtPartyNameKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBillDateBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JCheckBox jcmbACPay;
    private javax.swing.JLabel jlblDay;
    private javax.swing.JLabel jlblEditNo;
    private javax.swing.JLabel jlblImg;
    private javax.swing.JLabel jlblImgName;
    private javax.swing.JLabel jlblLstUpdate;
    private javax.swing.JLabel jlblStart;
    private javax.swing.JLabel jlblUserName;
    private javax.swing.JTextField jtxtAmount;
    private javax.swing.JTextField jtxtBankName;
    private javax.swing.JTextField jtxtCDate;
    private javax.swing.JTextField jtxtCheckCD;
    private javax.swing.JTextField jtxtChqNo;
    private javax.swing.JTextField jtxtPartyName;
    // End of variables declaration//GEN-END:variables
}