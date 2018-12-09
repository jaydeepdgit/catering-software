/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import cateringsoftware.DeskFrame;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import support.Constants;
import support.Library;
import support.PickList;
import support.VoucherDisplay;

/**
 *
 * @author @JD@
 */
public class CompanySetting extends javax.swing.JInternalFrame {
    Connection accountcon = DeskFrame.connMpAdmin;
    PickList cash = null;
    PickList labInc = null;
    PickList labExp = null;
    PickList salesAcnt = null;
    PickList purAcnt = null;
    Library lb = new Library();
    String initial = Constants.COMPANY_SETTING_INITIAL;
    String Syspath = System.getProperty("user.dir");

    /**
     * Creates new form CompanySetting
     */
    public CompanySetting() {
        initComponents();
        cash = new PickList(accountcon);
        labInc = new PickList(accountcon);
        labExp = new PickList(accountcon);
        salesAcnt = new PickList(accountcon);
        purAcnt = new PickList(accountcon);
        setPickList();
        setComponentEnabledDisabled(false);
        setTextFromRS();
        setPermission();
        setIconToPanel();
        lb.registerShortKeys(getRootPane(), jbtnClose, jbtnEdit, jbtnSave, jbtnPrint);
        setTitle(Constants.COMPANY_SETTING_FORM_NAME);
    }

    private void setIconToPanel() {
        Syspath += File.separator + "Resources" + File.separator + "Images" + File.separator;
        jbtnPrint.setIcon(new ImageIcon(Syspath +"print.png"));
        jbtnEdit.setIcon(new ImageIcon(Syspath +"edit.png"));
        jbtnSave.setIcon(new ImageIcon(Syspath +"save.png"));
        jbtnClose.setIcon(new ImageIcon(Syspath +"close.png"));
    }

    private void setPermission(){
        jbtnPrint.setEnabled(UserRights.getRight(Constants.COMPANY_SETTING_FORM_ID, "PRINT"));
        jbtnEdit.setEnabled(UserRights.getRight(Constants.COMPANY_SETTING_FORM_ID, "EDIT"));
        jbtnSave.setEnabled(UserRights.getRight(Constants.COMPANY_SETTING_FORM_ID, "EDIT"));
    }

    private void setPickList() {
        cash.setLayer(getLayeredPane());
        cash.setPickListComponent(jtxtCashAc);
        cash.setNextComponent(jcmbVoucher);

        labInc.setLayer(getLayeredPane());
        labInc.setPickListComponent(jtxtLabInc);
        labInc.setNextComponent(jtxtLabExp);

        labExp.setLayer(getLayeredPane());
        labExp.setPickListComponent(jtxtLabExp);
        labExp.setNextComponent(jtxtSalesAccount);

        salesAcnt.setLayer(getLayeredPane());
        salesAcnt.setPickListComponent(jtxtSalesAccount);
        salesAcnt.setNextComponent(jtxtPurchaseAccount);

        purAcnt.setLayer(getLayeredPane());
        purAcnt.setPickListComponent(jtxtPurchaseAccount);
        purAcnt.setNextComponent(jbtnSave);
    }

    @Override
    public void dispose() {
        try {
            DeskFrame.removeFromScreen(DeskFrame.tabbedPane.getSelectedIndex());
            super.dispose();
        } catch (Exception ex) {
            lb.printToLogFile("Exception at dispose In CompanySetting", ex);
        }
    }

    private void setComponentEnabledDisabled(boolean flag) {
        jtxtCompanyName.setEnabled(flag);
        jtxtCashAc.setEnabled(flag);
        jtxtLabInc.setEnabled(flag);
        jtxtLabExp.setEnabled(flag);
        jtxtSalesAccount.setEnabled(flag);
        jtxtPurchaseAccount.setEnabled(flag);
        jtxtContactPerson.setEnabled(flag);
        jtxtWebsite.setEnabled(flag);
        jcmbDigit.setEnabled(flag);
        jcmbVoucher.setEnabled(flag);
        jtxtImagePath.setEnabled(flag);
        jtxtAddress1.setEnabled(flag);
        jtxtAddress2.setEnabled(flag);
        jtxtCorrAddress1.setEnabled(flag);
        jtxtCorrAddress2.setEnabled(flag);
        jtxtMobileNo.setEnabled(flag);
        jtxtPhoneNo.setEnabled(flag);
        jtxtFaxNo.setEnabled(flag);
        jtxtEmail.setEnabled(flag);
        jtxtCSTNo.setEnabled(flag);
        jtxtPANNo.setEnabled(flag);
        jtxtTINNo.setEnabled(flag);
        jtxtTaxNo.setEnabled(flag);
        jtxtBankName.setEnabled(flag);
        jtxtACNo.setEnabled(flag);
        jtxtBranchName.setEnabled(flag);
        jtxtIfscCode.setEnabled(flag);
        jbtnEdit.setEnabled(!flag);
        jbtnSave.setEnabled(flag);
        if (flag) {
            jbtnClose.setText("CANCEL");
        } else {
            jbtnClose.setText("CLOSE");
        }
    }

    private void cancelOrClose() {
        if (jbtnClose.getText().equalsIgnoreCase("CLOSE")) {
            this.dispose();
        } else {
            jbtnClose.setText("CLOSE");
            setComponentEnabledDisabled(false);
            jbtnEdit.requestFocusInWindow();
        }
    }

    private void setTextFromRS() {
        jtxtCompanyName.setText(DeskFrame.clSysEnv.getCMPN_NAME());
        jtxtCashAc.setText(lb.getAccountCode(DeskFrame.clSysEnv.getCASH_AC(), "N"));
        jcmbVoucher.setSelectedIndex(Integer.parseInt(DeskFrame.clSysEnv.getINVOICE_TYPE()));
        jcmbDigit.setSelectedItem(DeskFrame.clSysEnv.getDIGIT());
        jtxtImagePath.setText(DeskFrame.clSysEnv.getIMAGE_PATH());
        jtxtLabInc.setText(lb.getAccountCode(DeskFrame.clSysEnv.getLAB_INC_AC(), "N"));
        jtxtLabExp.setText(lb.getAccountCode(DeskFrame.clSysEnv.getLAB_EXP_AC(), "N"));
        jtxtSalesAccount.setText(lb.getAccountCode(DeskFrame.clSysEnv.getSALE_AC(), "N"));
        jtxtPurchaseAccount.setText(lb.getAccountCode(DeskFrame.clSysEnv.getPURCHASE_AC(), "N"));
        jtxtAddress1.setText(DeskFrame.clSysEnv.getADD1());
        jtxtAddress2.setText(DeskFrame.clSysEnv.getADD2());
        jtxtCorrAddress1.setText(DeskFrame.clSysEnv.getCORRADD1());
        jtxtCorrAddress2.setText(DeskFrame.clSysEnv.getCORRADD2());
        jtxtMobileNo.setText(DeskFrame.clSysEnv.getMOB_NO());
        jtxtPhoneNo.setText(DeskFrame.clSysEnv.getPHONE_NO());
        jtxtFaxNo.setText(DeskFrame.clSysEnv.getFAX_NO());
        jtxtEmail.setText(DeskFrame.clSysEnv.getEMAIL());
        jtxtCSTNo.setText(DeskFrame.clSysEnv.getCST_NO());
        jtxtPANNo.setText(DeskFrame.clSysEnv.getPAN_NO());
        jtxtTINNo.setText(DeskFrame.clSysEnv.getTIN_NO());
        jtxtTaxNo.setText(DeskFrame.clSysEnv.getTAX_NO());
        jtxtBankName.setText(DeskFrame.clSysEnv.getBANK_NAME());
        jtxtACNo.setText(DeskFrame.clSysEnv.getAC_NO());
        jtxtBranchName.setText(DeskFrame.clSysEnv.getBRANCH_NAME());
        jtxtIfscCode.setText(DeskFrame.clSysEnv.getIFSC_CODE());
        jtxtContactPerson.setText(DeskFrame.clSysEnv.getCONTACT_PERSON());
        jtxtWebsite.setText(DeskFrame.clSysEnv.getWEBSITE());   
    }

    private void saveVoucher() {
        try {
            String sql = "UPDATE company_master SET company_name = ?, invoice_type = ?, digit = ?, image_path = ?, add1 = ?, add2 = ?, " +
                " mob_no = ?, phone_no = ?, fax_no = ?, email = ?, cst_no = ?, pan_no = ?, tin_no = ?, tax_no = ?, bank_name = ?, " +
                " ac_no = ?, branch_name = ?, cash_ac_cd = ?, lab_inc_ac = ?, lab_exp_ac = ?, sale_ac = ?, purchase_ac = ?, " +
                " contact_person = ?, website = ?, ifsc_code = ?, corraddress1 = ?, corraddress2 = ? WHERE ac_year = ? AND mnth = ?";
            PreparedStatement pstLocal = accountcon.prepareStatement(sql);
            pstLocal.setString(1, jtxtCompanyName.getText()); // Company Name
            pstLocal.setString(2, jcmbVoucher.getSelectedIndex() + ""); // Invoice Type
            pstLocal.setString(3, jcmbDigit.getSelectedItem().toString()); // Digit
            pstLocal.setString(4, jtxtImagePath.getText()); // Image Path
            pstLocal.setString(5, jtxtAddress1.getText()); // Address 1
            pstLocal.setString(6, jtxtAddress2.getText()); // Address 2
            pstLocal.setString(7, jtxtMobileNo.getText()); // Mobile No
            pstLocal.setString(8, jtxtPhoneNo.getText()); // Phone No
            pstLocal.setString(9, jtxtFaxNo.getText()); // Fax No
            pstLocal.setString(10, jtxtEmail.getText()); // Email
            pstLocal.setString(11, jtxtCSTNo.getText()); // CST No
            pstLocal.setString(12, jtxtPANNo.getText()); // PAN No
            pstLocal.setString(13, jtxtTINNo.getText()); // TIN No
            pstLocal.setString(14, jtxtTaxNo.getText()); // Tax No
            pstLocal.setString(15, jtxtBankName.getText()); // Bank Name
            pstLocal.setString(16, jtxtACNo.getText()); // Account No
            pstLocal.setString(17, jtxtBranchName.getText()); // Branch Name
            pstLocal.setString(18, lb.getAccountCode(jtxtCashAc.getText(), "C")); // Cash Account CD
            pstLocal.setString(19, lb.getAccountCode(jtxtLabInc.getText(), "C")); // Labour Income Account CD
            pstLocal.setString(20, lb.getAccountCode(jtxtLabExp.getText(), "C")); // Labour Expense Account CD
            pstLocal.setString(21, lb.getAccountCode(jtxtSalesAccount.getText(), "C"));  // Sales Account
            pstLocal.setString(22, lb.getAccountCode(jtxtPurchaseAccount.getText(), "C"));  // Purchase Account No
            pstLocal.setString(23, jtxtContactPerson.getText()); // Contact Person
            pstLocal.setString(24, jtxtWebsite.getText()); // Website
            pstLocal.setString(25, jtxtIfscCode.getText()); // IFSC Code
            pstLocal.setString(26, jtxtCorrAddress1.getText()); // Corresponding Address 1
            pstLocal.setString(27, jtxtCorrAddress2.getText()); // Corresponding Address 2
            pstLocal.setString(28, DeskFrame.dbYear); // Year
            pstLocal.setString(29, DeskFrame.month); // Month
            pstLocal.executeUpdate();
            setComponentEnabledDisabled(false);
            JOptionPane.showMessageDialog(this, "Please restart your application to apply changes", Constants.COMPANY_SETTING_FORM_NAME, JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            lb.printToLogFile("Exception at saveVoucher In CompanySetting", ex);
            setComponentEnabledDisabled(false);
        }
        jbtnClose.requestFocusInWindow();
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
        jTabbedPane1 = new javax.swing.JTabbedPane();
        JpanelCompany = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jtxtCompanyName = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jtxtCashAc = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jcmbVoucher = new javax.swing.JComboBox();
        jcmbDigit = new javax.swing.JComboBox();
        jLabel22 = new javax.swing.JLabel();
        jtxtImagePath = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jtxtLabInc = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jtxtLabExp = new javax.swing.JTextField();
        jtxtSalesAccount = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jtxtPurchaseAccount = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jtxtContactPerson = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jtxtWebsite = new javax.swing.JTextField();
        JpanelProfile = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jtxtAddress1 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jtxtAddress2 = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jtxtMobileNo = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jtxtPhoneNo = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jtxtFaxNo = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jtxtEmail = new javax.swing.JTextField();
        jtxtCSTNo = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jtxtTINNo = new javax.swing.JTextField();
        jtxtPANNo = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jtxtTaxNo = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jtxtCorrAddress1 = new javax.swing.JTextField();
        jtxtCorrAddress2 = new javax.swing.JTextField();
        JpanelBankDetails = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jtxtBankName = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        jtxtACNo = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        jtxtBranchName = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jtxtIfscCode = new javax.swing.JTextField();
        jbtnSave = new javax.swing.JButton();
        jbtnEdit = new javax.swing.JButton();
        jbtnClose = new javax.swing.JButton();
        jbtnPrint = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(253, 243, 243));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 3, 3, new java.awt.Color(235, 35, 35)));

        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTabbedPane1.setMinimumSize(new java.awt.Dimension(82, 46));

        JpanelCompany.setBackground(new java.awt.Color(253, 243, 243));
        JpanelCompany.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        jLabel1.setText("Name");

        jtxtCompanyName.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtCompanyName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtCompanyName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtCompanyNameFocusGained(evt);
            }
        });
        jtxtCompanyName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtCompanyNameKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtCompanyNameKeyTyped(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Cash A/C");

        jtxtCashAc.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtCashAc.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtCashAc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtCashAcFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtCashAcFocusLost(evt);
            }
        });
        jtxtCashAc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtCashAcKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxtCashAcKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtCashAcKeyTyped(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel21.setText("Digit");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel19.setText("Voucher Type");

        jcmbVoucher.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jcmbVoucher.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Type A-6", "Thermal Printer" }));
        jcmbVoucher.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jcmbVoucher.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jcmbVoucherKeyPressed(evt);
            }
        });

        jcmbDigit.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jcmbDigit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2", "3" }));
        jcmbDigit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jcmbDigit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jcmbDigitKeyPressed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel22.setText("Image Path");

        jtxtImagePath.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtImagePath.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtImagePath.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtImagePathFocusGained(evt);
            }
        });
        jtxtImagePath.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtImagePathKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtImagePathKeyTyped(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel3.setText("Lab Income A/C");

        jtxtLabInc.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtLabInc.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtLabInc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtLabIncFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtLabIncFocusLost(evt);
            }
        });
        jtxtLabInc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtLabIncKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxtLabIncKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtLabIncKeyTyped(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel4.setText("LabExpense A/C");

        jtxtLabExp.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtLabExp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtLabExp.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtLabExpFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtLabExpFocusLost(evt);
            }
        });
        jtxtLabExp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtLabExpKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxtLabExpKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtLabExpKeyTyped(evt);
            }
        });

        jtxtSalesAccount.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtSalesAccount.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtSalesAccount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtSalesAccountFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtSalesAccountFocusLost(evt);
            }
        });
        jtxtSalesAccount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtSalesAccountKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxtSalesAccountKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtSalesAccountKeyTyped(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel6.setText("Sales Account");

        jLabel16.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel16.setText("Purchase Account");

        jtxtPurchaseAccount.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtPurchaseAccount.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtPurchaseAccount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtPurchaseAccountFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtPurchaseAccountFocusLost(evt);
            }
        });
        jtxtPurchaseAccount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtPurchaseAccountKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxtPurchaseAccountKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtPurchaseAccountKeyTyped(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        jLabel5.setText("Contact Person");

        jtxtContactPerson.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtContactPerson.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtContactPerson.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtContactPersonFocusGained(evt);
            }
        });
        jtxtContactPerson.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtContactPersonKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtContactPersonKeyTyped(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Arial Unicode MS", 0, 14)); // NOI18N
        jLabel23.setText("Website");

        jtxtWebsite.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtWebsite.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtWebsite.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtWebsiteFocusGained(evt);
            }
        });
        jtxtWebsite.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtWebsiteKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtWebsiteKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout JpanelCompanyLayout = new javax.swing.GroupLayout(JpanelCompany);
        JpanelCompany.setLayout(JpanelCompanyLayout);
        JpanelCompanyLayout.setHorizontalGroup(
            JpanelCompanyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JpanelCompanyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JpanelCompanyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JpanelCompanyLayout.createSequentialGroup()
                        .addGroup(JpanelCompanyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(JpanelCompanyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtxtCompanyName, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtCashAc, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(JpanelCompanyLayout.createSequentialGroup()
                        .addGroup(JpanelCompanyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(JpanelCompanyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jtxtLabInc, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                            .addComponent(jtxtLabExp, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                            .addComponent(jtxtSalesAccount)
                            .addComponent(jtxtPurchaseAccount)))
                    .addGroup(JpanelCompanyLayout.createSequentialGroup()
                        .addGroup(JpanelCompanyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(JpanelCompanyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jtxtImagePath)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JpanelCompanyLayout.createSequentialGroup()
                                .addGroup(JpanelCompanyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jcmbDigit, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jcmbVoucher, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(117, 117, 117))))
                    .addGroup(JpanelCompanyLayout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jtxtContactPerson, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(JpanelCompanyLayout.createSequentialGroup()
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jtxtWebsite, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        JpanelCompanyLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel10, jLabel16, jLabel19, jLabel21, jLabel22, jLabel3, jLabel4, jLabel5, jLabel6});

        JpanelCompanyLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jtxtCashAc, jtxtCompanyName, jtxtImagePath, jtxtLabExp, jtxtLabInc, jtxtSalesAccount});

        JpanelCompanyLayout.setVerticalGroup(
            JpanelCompanyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JpanelCompanyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JpanelCompanyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtCompanyName, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JpanelCompanyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtCashAc, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JpanelCompanyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcmbVoucher, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JpanelCompanyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcmbDigit, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JpanelCompanyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtImagePath, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JpanelCompanyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtLabInc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JpanelCompanyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtLabExp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JpanelCompanyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtSalesAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JpanelCompanyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtPurchaseAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JpanelCompanyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtContactPerson, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JpanelCompanyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtxtWebsite, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        JpanelCompanyLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel19, jcmbVoucher});

        JpanelCompanyLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel21, jcmbDigit});

        JpanelCompanyLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel22, jtxtImagePath});

        JpanelCompanyLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel16, jLabel4, jLabel6, jtxtCashAc, jtxtCompanyName, jtxtLabExp, jtxtLabInc, jtxtPurchaseAccount, jtxtSalesAccount});

        jTabbedPane1.addTab("COMPANY    ", JpanelCompany);

        jLabel2.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel2.setText("Address 1");

        jtxtAddress1.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtAddress1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtAddress1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtAddress1FocusGained(evt);
            }
        });
        jtxtAddress1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtAddress1KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtAddress1KeyTyped(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel11.setText("Address 2");

        jtxtAddress2.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtAddress2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtAddress2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtAddress2FocusGained(evt);
            }
        });
        jtxtAddress2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtAddress2KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtAddress2KeyTyped(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel24.setText("Mobile No");

        jtxtMobileNo.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtMobileNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtMobileNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtMobileNoFocusGained(evt);
            }
        });
        jtxtMobileNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtMobileNoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtMobileNoKeyTyped(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel7.setText("Phone No");

        jtxtPhoneNo.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtPhoneNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtPhoneNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtPhoneNoFocusGained(evt);
            }
        });
        jtxtPhoneNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtPhoneNoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtPhoneNoKeyTyped(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel8.setText("Fax No");

        jtxtFaxNo.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtFaxNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtFaxNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtFaxNoFocusGained(evt);
            }
        });
        jtxtFaxNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtFaxNoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtFaxNoKeyTyped(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel9.setText("Email");

        jtxtEmail.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtEmail.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtEmail.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtEmailFocusGained(evt);
            }
        });
        jtxtEmail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtEmailKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtEmailKeyTyped(evt);
            }
        });

        jtxtCSTNo.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtCSTNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtCSTNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtCSTNoFocusGained(evt);
            }
        });
        jtxtCSTNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtCSTNoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtCSTNoKeyTyped(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel12.setText("CST No");

        jLabel13.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel13.setText("GST No");

        jtxtTINNo.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtTINNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtTINNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtTINNoFocusGained(evt);
            }
        });
        jtxtTINNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtTINNoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtTINNoKeyTyped(evt);
            }
        });

        jtxtPANNo.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtPANNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtPANNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtPANNoFocusGained(evt);
            }
        });
        jtxtPANNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtPANNoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtPANNoKeyTyped(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel14.setText("PAN No");

        jLabel15.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel15.setText("TAX No");

        jtxtTaxNo.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtTaxNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtTaxNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtTaxNoFocusGained(evt);
            }
        });
        jtxtTaxNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtTaxNoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtTaxNoKeyTyped(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel17.setText("Corr. Address 2");

        jLabel18.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel18.setText("Corr. Address 1");

        jtxtCorrAddress1.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtCorrAddress1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtCorrAddress1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtCorrAddress1FocusGained(evt);
            }
        });
        jtxtCorrAddress1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtCorrAddress1KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtCorrAddress1KeyTyped(evt);
            }
        });

        jtxtCorrAddress2.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtCorrAddress2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtCorrAddress2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtCorrAddress2FocusGained(evt);
            }
        });
        jtxtCorrAddress2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtCorrAddress2KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtCorrAddress2KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout JpanelProfileLayout = new javax.swing.GroupLayout(JpanelProfile);
        JpanelProfile.setLayout(JpanelProfileLayout);
        JpanelProfileLayout.setHorizontalGroup(
            JpanelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JpanelProfileLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JpanelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JpanelProfileLayout.createSequentialGroup()
                        .addGroup(JpanelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(JpanelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtxtAddress1, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtAddress2, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtCorrAddress1, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtCorrAddress2, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(JpanelProfileLayout.createSequentialGroup()
                        .addGroup(JpanelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(JpanelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtxtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtCSTNo, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtTINNo, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtPhoneNo, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                            .addComponent(jtxtFaxNo)
                            .addComponent(jtxtMobileNo, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtPANNo, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtTaxNo, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        JpanelProfileLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel11, jLabel12, jLabel13, jLabel14, jLabel15, jLabel2, jLabel24, jLabel7, jLabel8, jLabel9});

        JpanelProfileLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jtxtFaxNo, jtxtMobileNo, jtxtPhoneNo});

        JpanelProfileLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jtxtCSTNo, jtxtEmail, jtxtPANNo, jtxtTINNo, jtxtTaxNo});

        JpanelProfileLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jtxtAddress1, jtxtAddress2});

        JpanelProfileLayout.setVerticalGroup(
            JpanelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JpanelProfileLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JpanelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtAddress1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JpanelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtxtAddress2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JpanelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtCorrAddress1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JpanelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtxtCorrAddress2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JpanelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                    .addComponent(jtxtMobileNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JpanelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtPhoneNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JpanelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtFaxNo, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JpanelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtEmail))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JpanelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtCSTNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JpanelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtTINNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JpanelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtPANNo, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JpanelProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtTaxNo, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(64, 64, 64))
        );

        JpanelProfileLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel11, jLabel12, jLabel13, jLabel14, jLabel2, jLabel24, jLabel7, jLabel8, jLabel9, jtxtAddress1, jtxtAddress2, jtxtCSTNo, jtxtEmail, jtxtFaxNo, jtxtMobileNo, jtxtPANNo, jtxtPhoneNo, jtxtTINNo});

        JpanelProfileLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel15, jtxtTaxNo});

        JpanelProfileLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel17, jLabel18, jtxtCorrAddress1, jtxtCorrAddress2});

        jTabbedPane1.addTab("PROFILE      ", JpanelProfile);

        jLabel36.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel36.setText("Bank Name");

        jtxtBankName.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtBankName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtBankName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtBankNameFocusGained(evt);
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

        jLabel37.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel37.setText("A/C No.");

        jtxtACNo.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtACNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtACNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtACNoFocusGained(evt);
            }
        });
        jtxtACNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtACNoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtACNoKeyTyped(evt);
            }
        });

        jLabel38.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel38.setText("Branch Name");

        jtxtBranchName.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtBranchName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtBranchName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtBranchNameFocusGained(evt);
            }
        });
        jtxtBranchName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtBranchNameKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtBranchNameKeyTyped(evt);
            }
        });

        jLabel39.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel39.setText("Ifsc Code");

        jtxtIfscCode.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtIfscCode.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtIfscCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtIfscCodeFocusGained(evt);
            }
        });
        jtxtIfscCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtIfscCodeKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtIfscCodeKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout JpanelBankDetailsLayout = new javax.swing.GroupLayout(JpanelBankDetails);
        JpanelBankDetails.setLayout(JpanelBankDetailsLayout);
        JpanelBankDetailsLayout.setHorizontalGroup(
            JpanelBankDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JpanelBankDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JpanelBankDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(JpanelBankDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtxtBankName, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtACNo, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtBranchName, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtIfscCode, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        JpanelBankDetailsLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jtxtACNo, jtxtBankName, jtxtBranchName, jtxtIfscCode});

        JpanelBankDetailsLayout.setVerticalGroup(
            JpanelBankDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JpanelBankDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JpanelBankDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtBankName, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JpanelBankDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtACNo, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JpanelBankDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtBranchName, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JpanelBankDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtxtIfscCode, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        JpanelBankDetailsLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel36, jLabel37, jLabel38, jLabel39, jtxtACNo, jtxtBankName, jtxtBranchName, jtxtIfscCode});

        jTabbedPane1.addTab("BANK DETAILS", JpanelBankDetails);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jbtnSave.setBackground(new java.awt.Color(204, 255, 204));
        jbtnSave.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jbtnSave.setForeground(new java.awt.Color(235, 35, 35));
        jbtnSave.setMnemonic('S');
        jbtnSave.setText("SAVE");
        jbtnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnSaveActionPerformed(evt);
            }
        });
        jbtnSave.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jbtnSaveKeyPressed(evt);
            }
        });

        jbtnEdit.setBackground(new java.awt.Color(204, 255, 204));
        jbtnEdit.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jbtnEdit.setForeground(new java.awt.Color(235, 35, 35));
        jbtnEdit.setMnemonic('E');
        jbtnEdit.setText("EDIT");
        jbtnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnEditActionPerformed(evt);
            }
        });
        jbtnEdit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jbtnEditKeyPressed(evt);
            }
        });

        jbtnClose.setBackground(new java.awt.Color(204, 255, 204));
        jbtnClose.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jbtnClose.setForeground(new java.awt.Color(235, 35, 35));
        jbtnClose.setMnemonic('C');
        jbtnClose.setText("CLOSE");
        jbtnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnCloseActionPerformed(evt);
            }
        });
        jbtnClose.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jbtnCloseKeyPressed(evt);
            }
        });

        jbtnPrint.setBackground(new java.awt.Color(204, 255, 204));
        jbtnPrint.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jbtnPrint.setForeground(new java.awt.Color(235, 35, 35));
        jbtnPrint.setMnemonic('P');
        jbtnPrint.setText("PRINT");
        jbtnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnPrintActionPerformed(evt);
            }
        });
        jbtnPrint.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jbtnPrintKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jbtnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(74, 74, 74)
                        .addComponent(jbtnEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtnSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jbtnClose, jbtnEdit, jbtnPrint, jbtnSave});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbtnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnEdit)
                    .addComponent(jbtnSave)
                    .addComponent(jbtnClose))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jbtnClose, jbtnEdit, jbtnPrint, jbtnSave});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnCloseActionPerformed
        cancelOrClose();
    }//GEN-LAST:event_jbtnCloseActionPerformed

    private void jbtnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnEditActionPerformed
        setComponentEnabledDisabled(true);
        jtxtCompanyName.requestFocusInWindow();
    }//GEN-LAST:event_jbtnEditActionPerformed

    private void jbtnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSaveActionPerformed
        saveVoucher();
    }//GEN-LAST:event_jbtnSaveActionPerformed

    private void jbtnSaveKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jbtnSaveKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            evt.consume();
            saveVoucher();
        }
    }//GEN-LAST:event_jbtnSaveKeyPressed

    private void jbtnEditKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jbtnEditKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            evt.consume();
            setComponentEnabledDisabled(true);
            jtxtCompanyName.requestFocusInWindow();
        }
    }//GEN-LAST:event_jbtnEditKeyPressed

    private void jbtnCloseKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jbtnCloseKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            evt.consume();
            cancelOrClose();
        }
    }//GEN-LAST:event_jbtnCloseKeyPressed

    private void jcmbDigitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jcmbDigitKeyPressed
        lb.enterFocus(evt, jtxtImagePath);
    }//GEN-LAST:event_jcmbDigitKeyPressed

    private void jcmbVoucherKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jcmbVoucherKeyPressed
        lb.enterFocus(evt, jcmbDigit);
    }//GEN-LAST:event_jcmbVoucherKeyPressed

    private void jtxtCashAcKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtCashAcKeyReleased
        try {
            cash.setReturnComponent(new JTextField[]{jtxtCashAc});
            PreparedStatement pstLocal = accountcon.prepareStatement("SELECT name FROM account_master WHERE name LIKE '%"+ jtxtCashAc.getText().toUpperCase() +"%'");
            cash.setPreparedStatement(pstLocal);
            cash.setValidation(accountcon.prepareStatement("SELECT * FROM account_master WHERE name = ?"));
            cash.pickListKeyRelease(evt);
        } catch (Exception ex) {
            lb.printToLogFile("Exception at jtxtCashAcKeyReleased at CompanySetting", ex);
        }
    }//GEN-LAST:event_jtxtCashAcKeyReleased

    private void jtxtCashAcKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtCashAcKeyPressed
        cash.setLocation(jtxtCashAc.getX() + JpanelCompany.getX() + jPanel1.getX(), jtxtCashAc.getY() + jtxtCashAc.getHeight() + JpanelCompany.getY() + jPanel1.getY());
        cash.setReturnComponent(new JTextField[]{jtxtCashAc});
        cash.pickListKeyPress(evt);
    }//GEN-LAST:event_jtxtCashAcKeyPressed

    private void jtxtCashAcFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtCashAcFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtCashAcFocusGained

    private void jtxtCompanyNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtCompanyNameKeyPressed
        lb.enterEvent(evt, jtxtCashAc);
    }//GEN-LAST:event_jtxtCompanyNameKeyPressed

    private void jtxtCompanyNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtCompanyNameFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtCompanyNameFocusGained

    private void jtxtAddress1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtAddress1FocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtAddress1FocusGained

    private void jtxtAddress1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtAddress1KeyPressed
        lb.enterFocus(evt, jtxtAddress2);
    }//GEN-LAST:event_jtxtAddress1KeyPressed

    private void jtxtAddress2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtAddress2FocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtAddress2FocusGained

    private void jtxtAddress2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtAddress2KeyPressed
        lb.enterFocus(evt, jtxtCorrAddress1);
    }//GEN-LAST:event_jtxtAddress2KeyPressed

    private void jtxtPhoneNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtPhoneNoKeyPressed
        lb.enterFocus(evt, jtxtFaxNo);
    }//GEN-LAST:event_jtxtPhoneNoKeyPressed

    private void jtxtFaxNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtFaxNoKeyPressed
        lb.enterFocus(evt, jtxtEmail);
    }//GEN-LAST:event_jtxtFaxNoKeyPressed

    private void jtxtEmailKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtEmailKeyPressed
        lb.enterFocus(evt, jtxtCSTNo);
    }//GEN-LAST:event_jtxtEmailKeyPressed

    private void jtxtCSTNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtCSTNoKeyPressed
        lb.enterFocus(evt, jtxtTINNo);
    }//GEN-LAST:event_jtxtCSTNoKeyPressed

    private void jtxtImagePathFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtImagePathFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtImagePathFocusGained

    private void jtxtImagePathKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtImagePathKeyPressed
        lb.enterFocus(evt, jtxtLabInc);
    }//GEN-LAST:event_jtxtImagePathKeyPressed

    private void jtxtMobileNoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtMobileNoFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtMobileNoFocusGained

    private void jtxtMobileNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtMobileNoKeyPressed
        lb.enterFocus(evt, jtxtPhoneNo);
    }//GEN-LAST:event_jtxtMobileNoKeyPressed

    private void jtxtPhoneNoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtPhoneNoFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtPhoneNoFocusGained

    private void jtxtFaxNoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtFaxNoFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtFaxNoFocusGained

    private void jtxtEmailFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtEmailFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtEmailFocusGained

    private void jtxtCSTNoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtCSTNoFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtCSTNoFocusGained

    private void jtxtTINNoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtTINNoFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtTINNoFocusGained

    private void jtxtTINNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtTINNoKeyPressed
        lb.enterEvent(evt, jtxtPANNo);
    }//GEN-LAST:event_jtxtTINNoKeyPressed

    private void jtxtPANNoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtPANNoFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtPANNoFocusGained

    private void jtxtPANNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtPANNoKeyPressed
        lb.enterEvent(evt, jtxtTaxNo);
    }//GEN-LAST:event_jtxtPANNoKeyPressed

    private void jtxtLabIncKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtLabIncKeyPressed
        labInc.setLocation(jtxtLabInc.getX() + JpanelCompany.getX() + jPanel1.getX(), jtxtLabInc.getY() + jtxtLabInc.getHeight() + JpanelCompany.getY() + jPanel1.getY());
        labInc.pickListKeyPress(evt);
    }//GEN-LAST:event_jtxtLabIncKeyPressed

    private void jtxtLabIncKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtLabIncKeyReleased
        try {
            labInc.setReturnComponent(new JTextField[]{jtxtLabInc});
            PreparedStatement pstLocal = accountcon.prepareStatement("SELECT name FROM account_master WHERE name LIKE '%"+ jtxtLabInc.getText().toUpperCase() +"%'");
            labInc.setPreparedStatement(pstLocal);
            labInc.setValidation(accountcon.prepareStatement("SELECT * FROM account_master WHERE name = ?"));
            labInc.pickListKeyRelease(evt);
        } catch (Exception ex) {
            lb.printToLogFile("Exception at jtxtLabIncKeyReleased at CompanySetting", ex);
        }
    }//GEN-LAST:event_jtxtLabIncKeyReleased

    private void jtxtLabExpKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtLabExpKeyPressed
        labExp.setLocation(jtxtLabExp.getX() + JpanelCompany.getX() + jPanel1.getX(), jtxtLabExp.getY() + jtxtLabExp.getHeight() + JpanelCompany.getY() + jPanel1.getY());
        labExp.pickListKeyPress(evt);
    }//GEN-LAST:event_jtxtLabExpKeyPressed

    private void jtxtLabExpKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtLabExpKeyReleased
        try {
            labExp.setReturnComponent(new JTextField[]{jtxtLabExp});
            PreparedStatement pstLocal = accountcon.prepareStatement("SELECT name FROM account_master WHERE name LIKE '%"+ jtxtLabExp.getText().toUpperCase() +"%'");
            labExp.setPreparedStatement(pstLocal);
            labExp.setValidation(accountcon.prepareStatement("SELECT * FROM account_master WHERE name = ?"));
            labExp.pickListKeyRelease(evt);
        } catch (Exception ex) {
            lb.printToLogFile("Exception at jtxtLabExpKeyReleased at CompanySetting", ex);
        }
    }//GEN-LAST:event_jtxtLabExpKeyReleased

    private void jtxtSalesAccountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtSalesAccountKeyPressed
        salesAcnt.setLayer(getLayeredPane());
        salesAcnt.setPickListComponent(jtxtSalesAccount);
        salesAcnt.setNextComponent(jtxtPurchaseAccount);
        salesAcnt.setLocation(jtxtSalesAccount.getX() + JpanelCompany.getX() + jPanel1.getX(), jtxtSalesAccount.getY() + jtxtSalesAccount.getHeight() + JpanelCompany.getY() + jPanel1.getY());
        salesAcnt.pickListKeyPress(evt);
    }//GEN-LAST:event_jtxtSalesAccountKeyPressed

    private void jtxtSalesAccountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtSalesAccountKeyReleased
        try {
            salesAcnt.setReturnComponent(new JTextField[]{jtxtSalesAccount});
            PreparedStatement pstLocal = accountcon.prepareStatement("SELECT name FROM account_master WHERE name LIKE '%"+ jtxtSalesAccount.getText().toUpperCase() +"%'");
            salesAcnt.setPreparedStatement(pstLocal);
            salesAcnt.setValidation(accountcon.prepareStatement("SELECT * FROM account_master WHERE name = ?"));
            salesAcnt.pickListKeyRelease(evt);
        } catch (Exception ex) {
            lb.printToLogFile("Exception at jtxtSalesAccountKeyReleased In CompanySetting", ex);
        }
    }//GEN-LAST:event_jtxtSalesAccountKeyReleased

    private void jtxtPurchaseAccountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtPurchaseAccountKeyPressed
        purAcnt.setLayer(getLayeredPane());
        purAcnt.setPickListComponent(jtxtPurchaseAccount);
        purAcnt.setNextComponent(jtxtContactPerson);
        purAcnt.setLocation(jtxtPurchaseAccount.getX() + JpanelCompany.getX() + jPanel1.getX(), jtxtPurchaseAccount.getY() + jtxtPurchaseAccount.getHeight() + JpanelCompany.getY() + jPanel1.getY());
        purAcnt.pickListKeyPress(evt);
    }//GEN-LAST:event_jtxtPurchaseAccountKeyPressed

    private void jtxtPurchaseAccountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtPurchaseAccountKeyReleased
        try {
            purAcnt.setReturnComponent(new JTextField[]{jtxtPurchaseAccount});
            PreparedStatement pstLocal = accountcon.prepareStatement("SELECT name FROM account_master WHERE name LIKE '%"+ jtxtPurchaseAccount.getText().toUpperCase() +"%'");
            purAcnt.setPreparedStatement(pstLocal);
            purAcnt.setValidation(accountcon.prepareStatement("SELECT * FROM account_master WHERE name = ?"));
            purAcnt.pickListKeyRelease(evt);
        } catch (Exception ex) {
            lb.printToLogFile("Exception at jtxtPurchaseAccountKeyReleased In CompanySetting", ex);
        }
    }//GEN-LAST:event_jtxtPurchaseAccountKeyReleased

    private void jbtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnPrintActionPerformed
        VoucherDisplay vd = new VoucherDisplay("", initial);
        DeskFrame.addOnScreen(vd, "COMPANY PRINT");
    }//GEN-LAST:event_jbtnPrintActionPerformed

    private void jbtnPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jbtnPrintKeyPressed
        VoucherDisplay vd = new VoucherDisplay("", initial);
        DeskFrame.addOnScreen(vd, "COMPANY PRINT");
    }//GEN-LAST:event_jbtnPrintKeyPressed

    private void jtxtTaxNoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtTaxNoFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtTaxNoFocusGained

    private void jtxtTaxNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtTaxNoKeyPressed
        lb.enterEvent(evt, jtxtBankName);
    }//GEN-LAST:event_jtxtTaxNoKeyPressed

    private void jtxtCompanyNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtCompanyNameKeyTyped
        lb.fixLength(evt, 100);
    }//GEN-LAST:event_jtxtCompanyNameKeyTyped

    private void jtxtCashAcKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtCashAcKeyTyped
        lb.fixLength(evt, 100);
    }//GEN-LAST:event_jtxtCashAcKeyTyped

    private void jtxtCashAcFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtCashAcFocusLost
        cash.setVisible(false);
    }//GEN-LAST:event_jtxtCashAcFocusLost

    private void jtxtImagePathKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtImagePathKeyTyped
        lb.fixLength(evt, 255);
    }//GEN-LAST:event_jtxtImagePathKeyTyped

    private void jtxtLabIncFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtLabIncFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtLabIncFocusGained

    private void jtxtLabIncKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtLabIncKeyTyped
        lb.fixLength(evt, 100);
    }//GEN-LAST:event_jtxtLabIncKeyTyped

    private void jtxtLabIncFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtLabIncFocusLost
        labInc.setVisible(false);
    }//GEN-LAST:event_jtxtLabIncFocusLost

    private void jtxtLabExpFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtLabExpFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtLabExpFocusGained

    private void jtxtLabExpFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtLabExpFocusLost
        labExp.setVisible(false);
    }//GEN-LAST:event_jtxtLabExpFocusLost

    private void jtxtLabExpKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtLabExpKeyTyped
        lb.fixLength(evt, 100);
    }//GEN-LAST:event_jtxtLabExpKeyTyped

    private void jtxtSalesAccountFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtSalesAccountFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtSalesAccountFocusGained

    private void jtxtSalesAccountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtSalesAccountFocusLost
        salesAcnt.setVisible(false);
    }//GEN-LAST:event_jtxtSalesAccountFocusLost

    private void jtxtPurchaseAccountFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtPurchaseAccountFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtPurchaseAccountFocusGained

    private void jtxtPurchaseAccountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtPurchaseAccountFocusLost
        purAcnt.setVisible(false);
    }//GEN-LAST:event_jtxtPurchaseAccountFocusLost

    private void jtxtPurchaseAccountKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtPurchaseAccountKeyTyped
        lb.fixLength(evt, 100);
    }//GEN-LAST:event_jtxtPurchaseAccountKeyTyped

    private void jtxtSalesAccountKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtSalesAccountKeyTyped
        lb.fixLength(evt, 100);
    }//GEN-LAST:event_jtxtSalesAccountKeyTyped

    private void jtxtAddress1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtAddress1KeyTyped
        lb.fixLength(evt, 500);
    }//GEN-LAST:event_jtxtAddress1KeyTyped

    private void jtxtAddress2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtAddress2KeyTyped
        lb.fixLength(evt, 500);
    }//GEN-LAST:event_jtxtAddress2KeyTyped

    private void jtxtMobileNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtMobileNoKeyTyped
        lb.fixLength(evt, 17);
    }//GEN-LAST:event_jtxtMobileNoKeyTyped

    private void jtxtPhoneNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtPhoneNoKeyTyped
        lb.fixLength(evt, 17);
    }//GEN-LAST:event_jtxtPhoneNoKeyTyped

    private void jtxtFaxNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtFaxNoKeyTyped
        lb.fixLength(evt, 17);
    }//GEN-LAST:event_jtxtFaxNoKeyTyped

    private void jtxtEmailKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtEmailKeyTyped
        lb.fixLength(evt, 70);
    }//GEN-LAST:event_jtxtEmailKeyTyped

    private void jtxtCSTNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtCSTNoKeyTyped
        lb.fixLength(evt, 20);
    }//GEN-LAST:event_jtxtCSTNoKeyTyped

    private void jtxtTINNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtTINNoKeyTyped
        lb.fixLength(evt, 20);
    }//GEN-LAST:event_jtxtTINNoKeyTyped

    private void jtxtPANNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtPANNoKeyTyped
        lb.fixLength(evt, 20);
    }//GEN-LAST:event_jtxtPANNoKeyTyped

    private void jtxtTaxNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtTaxNoKeyTyped
        lb.fixLength(evt, 20);
    }//GEN-LAST:event_jtxtTaxNoKeyTyped

    private void jtxtContactPersonFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtContactPersonFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtContactPersonFocusGained

    private void jtxtContactPersonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtContactPersonKeyPressed
        lb.enterEvent(evt, jtxtWebsite);
    }//GEN-LAST:event_jtxtContactPersonKeyPressed

    private void jtxtContactPersonKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtContactPersonKeyTyped
        lb.fixLength(evt, 200);
    }//GEN-LAST:event_jtxtContactPersonKeyTyped

    private void jtxtWebsiteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtWebsiteFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtWebsiteFocusGained

    private void jtxtWebsiteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtWebsiteKeyPressed
        lb.enterEvent(evt, jbtnSave);
    }//GEN-LAST:event_jtxtWebsiteKeyPressed

    private void jtxtWebsiteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtWebsiteKeyTyped
        lb.fixLength(evt, 255);
    }//GEN-LAST:event_jtxtWebsiteKeyTyped

    private void jtxtBankNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtBankNameFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtBankNameFocusGained

    private void jtxtBankNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtBankNameKeyPressed
        lb.enterEvent(evt, jtxtACNo);
    }//GEN-LAST:event_jtxtBankNameKeyPressed

    private void jtxtBankNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtBankNameKeyTyped
        lb.fixLength(evt, 100);
    }//GEN-LAST:event_jtxtBankNameKeyTyped

    private void jtxtACNoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtACNoFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtACNoFocusGained

    private void jtxtACNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtACNoKeyPressed
        lb.enterEvent(evt, jtxtBranchName);
    }//GEN-LAST:event_jtxtACNoKeyPressed

    private void jtxtACNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtACNoKeyTyped
        lb.fixLength(evt, 20);
    }//GEN-LAST:event_jtxtACNoKeyTyped

    private void jtxtBranchNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtBranchNameFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtBranchNameFocusGained

    private void jtxtBranchNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtBranchNameKeyPressed
        lb.enterEvent(evt, jtxtIfscCode);
    }//GEN-LAST:event_jtxtBranchNameKeyPressed

    private void jtxtBranchNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtBranchNameKeyTyped
        lb.fixLength(evt, 100);
    }//GEN-LAST:event_jtxtBranchNameKeyTyped

    private void jtxtIfscCodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtIfscCodeFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtIfscCodeFocusGained

    private void jtxtIfscCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtIfscCodeKeyPressed
        lb.enterEvent(evt, jbtnSave);
    }//GEN-LAST:event_jtxtIfscCodeKeyPressed

    private void jtxtIfscCodeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtIfscCodeKeyTyped
        lb.fixLength(evt, 20);
    }//GEN-LAST:event_jtxtIfscCodeKeyTyped

    private void jtxtCorrAddress1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtCorrAddress1FocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtCorrAddress1FocusGained

    private void jtxtCorrAddress1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtCorrAddress1KeyPressed
        lb.enterEvent(evt, jtxtCorrAddress2);
    }//GEN-LAST:event_jtxtCorrAddress1KeyPressed

    private void jtxtCorrAddress1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtCorrAddress1KeyTyped
        lb.fixLength(evt, 500);
    }//GEN-LAST:event_jtxtCorrAddress1KeyTyped

    private void jtxtCorrAddress2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtCorrAddress2FocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtCorrAddress2FocusGained

    private void jtxtCorrAddress2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtCorrAddress2KeyPressed
        lb.enterEvent(evt, jtxtMobileNo);
    }//GEN-LAST:event_jtxtCorrAddress2KeyPressed

    private void jtxtCorrAddress2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtCorrAddress2KeyTyped
        lb.fixLength(evt, 500);
    }//GEN-LAST:event_jtxtCorrAddress2KeyTyped

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel JpanelBankDetails;
    private javax.swing.JPanel JpanelCompany;
    private javax.swing.JPanel JpanelProfile;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton jbtnClose;
    private javax.swing.JButton jbtnEdit;
    private javax.swing.JButton jbtnPrint;
    private javax.swing.JButton jbtnSave;
    private javax.swing.JComboBox jcmbDigit;
    private javax.swing.JComboBox jcmbVoucher;
    private javax.swing.JTextField jtxtACNo;
    private javax.swing.JTextField jtxtAddress1;
    private javax.swing.JTextField jtxtAddress2;
    private javax.swing.JTextField jtxtBankName;
    private javax.swing.JTextField jtxtBranchName;
    private javax.swing.JTextField jtxtCSTNo;
    private javax.swing.JTextField jtxtCashAc;
    private javax.swing.JTextField jtxtCompanyName;
    private javax.swing.JTextField jtxtContactPerson;
    private javax.swing.JTextField jtxtCorrAddress1;
    private javax.swing.JTextField jtxtCorrAddress2;
    private javax.swing.JTextField jtxtEmail;
    private javax.swing.JTextField jtxtFaxNo;
    private javax.swing.JTextField jtxtIfscCode;
    private javax.swing.JTextField jtxtImagePath;
    private javax.swing.JTextField jtxtLabExp;
    private javax.swing.JTextField jtxtLabInc;
    private javax.swing.JTextField jtxtMobileNo;
    private javax.swing.JTextField jtxtPANNo;
    private javax.swing.JTextField jtxtPhoneNo;
    private javax.swing.JTextField jtxtPurchaseAccount;
    private javax.swing.JTextField jtxtSalesAccount;
    private javax.swing.JTextField jtxtTINNo;
    private javax.swing.JTextField jtxtTaxNo;
    private javax.swing.JTextField jtxtWebsite;
    // End of variables declaration//GEN-END:variables
}