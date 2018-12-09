/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

import java.io.File;
import cateringsoftware.DeskFrame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import javax.swing.ImageIcon;
import net.sf.jasperreports.engine.JasperPrint;

/**
 *
 * @author @JD@
 */
public class VoucherDisplay extends javax.swing.JInternalFrame {
    Library lb = new Library();
    String ref_no = "";
    String tag = "";
    String from = "";
    int mode = 0, type = 0;
    Connection dataConnecrtion = DeskFrame.connMpAdmin;
    SysEnv sysEnv = DeskFrame.clSysEnv;
    JasperPrint print = null;
    String Syspath = System.getProperty("user.dir");
    String email = "";
    String bill_type = "(Original/Duplicate/Triplicate)";

    /**
     * Creates new form VoucherDisplay
     */
    public VoucherDisplay() {
        initComponents();
        registerShortKeys();
    }

    public VoucherDisplay(String ref_no, String tag) {
        initComponents();
        registerShortKeys();
        this.ref_no = ref_no;
        this.tag = tag;
        getVoucher(tag, ref_no, true);
    }

    private void getVoucher(String tag, String ref_no, boolean flag) {
        if(tag.equalsIgnoreCase(""+ Constants.ACCOUNT_TYPE_INITIAL)) {
            accountTypeMasterReport(ref_no);
        } else if(tag.equalsIgnoreCase(""+ Constants.ACCOUNT_MASTER_INITIAL +"M")) {
            accountMasterReport(ref_no);
        } else if(tag.equalsIgnoreCase(Constants.COMPANY_SETTING_INITIAL)) {
            companyMasterReport(ref_no);
        } else if(tag.equalsIgnoreCase("UM")) {
            unitMasterReport(ref_no);
        } else if(tag.equalsIgnoreCase(""+ Constants.TAX_MASTER_INITIAL +"M")) {
            taxMasterReport(ref_no);
        } else if(tag.equalsIgnoreCase(""+ Constants.BANK_MASTER_INITIAL +"M")) {
            bankMasterReport(ref_no);
        } else if(tag.equalsIgnoreCase("MU")) {
            manageUserReport(ref_no);
        } else if(tag.equalsIgnoreCase(Constants.CHECK_PRINT_INITIAL)) {
            checkPrintReport(ref_no);
        } else if(tag.equalsIgnoreCase("UR")) {
            userRightsReport(ref_no);
        }
    }

    private void accountTypeMasterReport(String ref_no) {
        String sql = "SELECT g.id, g.name, (SELECT g1.name FROM account_type g1 "
                +"WHERE g.head_grp = g1.id) AS headg_name FROM account_type g ORDER BY g.name";
        HashMap params = new HashMap();
        params.put("dir", System.getProperty("user.dir"));
        params.put("digit", lb.getDigit());
        params.put("cname", DeskFrame.clSysEnv.getCMPN_NAME());
        params.put("cadd1", DeskFrame.clSysEnv.getADD1());
        params.put("cadd2", DeskFrame.clSysEnv.getADD2());
        params.put("ccorradd1", DeskFrame.clSysEnv.getCORRADD1());
        params.put("ccorradd2", DeskFrame.clSysEnv.getCORRADD2());
        params.put("cmobno", DeskFrame.clSysEnv.getMOB_NO());
        try {
            PreparedStatement pstLocal = dataConnecrtion.prepareStatement(sql);
            ResultSet rsLocal = pstLocal.executeQuery();
            print = lb.reportGenerator("AccountType.jasper", params, rsLocal, jPanel1);
            lb.closeResultSet(rsLocal);
            lb.closeStatement(pstLocal);
        } catch (Exception ex) {
            lb.printToLogFile("Exception at accountTypeMasterReport In Voucher Display", ex);
        }
    }

    private void accountMasterReport(String ref_no) {
        String sql = "SELECT a.id, a.name, g.name as group_name, a.opening_rs, a.account_effect_rs, a.mobile_no1, a.phone_no1, a.fax_no, " +
            "a.email_id, a.address1, a.address2, a.contact_prsn, a.refby, a.shortname " +
            "FROM account_master a LEFT JOIN account_type g ON a.fk_account_type_id = g.id " +
            "WHERE a.fk_account_type_id = g.id ORDER BY g.name, a.name";
        HashMap params = new HashMap();
        params.put("dir", System.getProperty("user.dir"));
        params.put("digit", lb.getDigit());
        params.put("cname", DeskFrame.clSysEnv.getCMPN_NAME());
        params.put("cadd1", DeskFrame.clSysEnv.getADD1());
        params.put("cadd2", DeskFrame.clSysEnv.getADD2());
        params.put("ccorradd1", DeskFrame.clSysEnv.getCORRADD1());
        params.put("ccorradd2", DeskFrame.clSysEnv.getCORRADD2());
        params.put("cmobno", DeskFrame.clSysEnv.getMOB_NO());
        try {
            PreparedStatement pstLocal = dataConnecrtion.prepareStatement(sql);
            ResultSet rsLocal = pstLocal.executeQuery();
            print = lb.reportGenerator("AccountMaster.jasper", params, rsLocal, jPanel1);
            lb.closeResultSet(rsLocal);
            lb.closeStatement(pstLocal);
        } catch (Exception ex) {
            lb.printToLogFile("Exception at accountMasterReport In Voucher Display", ex);
        }
    }

    private void companyMasterReport(String ref_no) {
        String sql = "SELECT c.company_name, c.add1, c.add2, " +
            "c.mob_no, c.phone_no, c.fax_no, c.email, c.cst_no, c.pan_no, c.tin_no, c.sh_name " +
            "FROM company_master c ORDER BY c.company_name";
        HashMap params = new HashMap();
        params.put("dir", System.getProperty("user.dir"));
        params.put("digit", lb.getDigit());
        params.put("cname", DeskFrame.clSysEnv.getCMPN_NAME());
        params.put("cadd1", DeskFrame.clSysEnv.getADD1());
        params.put("cadd2", DeskFrame.clSysEnv.getADD2());
        params.put("ccorradd1", DeskFrame.clSysEnv.getCORRADD1());
        params.put("ccorradd2", DeskFrame.clSysEnv.getCORRADD2());
        params.put("cmobno", DeskFrame.clSysEnv.getMOB_NO());
        try {
            PreparedStatement pstLocal = dataConnecrtion.prepareStatement(sql);
            ResultSet rsLocal = pstLocal.executeQuery();
            print = lb.reportGenerator("CompanyMaster.jasper", params, rsLocal, jPanel1);
            lb.closeResultSet(rsLocal);
            lb.closeStatement(pstLocal);
        } catch (Exception ex) {
            lb.printToLogFile("Exception at companyMasterReport In Voucher Display", ex);
        }
    }

    private void unitMasterReport(String ref_no) {
        String sql = "SELECT id, name, symbol FROM unit_master ORDER BY name";
        HashMap params = new HashMap();
        params.put("dir", System.getProperty("user.dir"));
        params.put("digit", lb.getDigit());
        params.put("cname", DeskFrame.clSysEnv.getCMPN_NAME());
        params.put("cadd1", DeskFrame.clSysEnv.getADD1());
        params.put("cadd2", DeskFrame.clSysEnv.getADD2());
        params.put("ccorradd1", DeskFrame.clSysEnv.getCORRADD1());
        params.put("ccorradd2", DeskFrame.clSysEnv.getCORRADD2());
        params.put("cmobno", DeskFrame.clSysEnv.getMOB_NO());
        try {
            PreparedStatement pstLocal = dataConnecrtion.prepareStatement(sql);
            ResultSet rsLocal = pstLocal.executeQuery();
            print = lb.reportGenerator("UnitMaster.jasper", params, rsLocal, jPanel1);
            lb.closeResultSet(rsLocal);
            lb.closeStatement(pstLocal);
        } catch (Exception ex) {
            lb.printToLogFile("Exception at unitMasterReport In Voucher Display", ex);
        }
    }

    private void taxMasterReport(String ref_no) {
        String sql = "SELECT id, name, tax FROM tax_master ORDER BY name";
        HashMap params = new HashMap();
        params.put("dir", System.getProperty("user.dir"));
        params.put("digit", lb.getDigit());
        params.put("cname", DeskFrame.clSysEnv.getCMPN_NAME());
        params.put("cadd1", DeskFrame.clSysEnv.getADD1());
        params.put("cadd2", DeskFrame.clSysEnv.getADD2());
        params.put("ccorradd1", DeskFrame.clSysEnv.getCORRADD1());
        params.put("ccorradd2", DeskFrame.clSysEnv.getCORRADD2());
        params.put("cmobno", DeskFrame.clSysEnv.getMOB_NO());
        try {
            PreparedStatement pstLocal = dataConnecrtion.prepareStatement(sql);
            ResultSet rsLocal = pstLocal.executeQuery();
            print = lb.reportGenerator("TaxMaster.jasper", params, rsLocal, jPanel1);
            lb.closeResultSet(rsLocal);
            lb.closeStatement(pstLocal);
        } catch (Exception ex) {
            lb.printToLogFile("Exception at taxMasterReport In Voucher Display", ex);
        }
    }

    private void bankMasterReport(String ref_no) {
        String sql = "SELECT id, name FROM bank_master ORDER BY name";
        HashMap params = new HashMap();
        params.put("dir", System.getProperty("user.dir"));
        params.put("digit", lb.getDigit());
        params.put("cname", DeskFrame.clSysEnv.getCMPN_NAME());
        params.put("cadd1", DeskFrame.clSysEnv.getADD1());
        params.put("cadd2", DeskFrame.clSysEnv.getADD2());
        params.put("ccorradd1", DeskFrame.clSysEnv.getCORRADD1());
        params.put("ccorradd2", DeskFrame.clSysEnv.getCORRADD2());
        params.put("cmobno", DeskFrame.clSysEnv.getMOB_NO());
        try {
            PreparedStatement pstLocal = dataConnecrtion.prepareStatement(sql);
            ResultSet rsLocal = pstLocal.executeQuery();
            print = lb.reportGenerator("BankMaster.jasper", params, rsLocal, jPanel1);
            lb.closeResultSet(rsLocal);
            lb.closeStatement(pstLocal);
        } catch (Exception ex) {
            lb.printToLogFile("Exception at bankMasterReport In Voucher Display", ex);
        }
    }

    private void manageUserReport(String ref_no) {
        String sql = "SELECT id, username FROM user_master ORDER BY username";
        HashMap params = new HashMap();
        params.put("dir", System.getProperty("user.dir"));
        params.put("digit", lb.getDigit());
        params.put("cname", DeskFrame.clSysEnv.getCMPN_NAME());
        params.put("cadd1", DeskFrame.clSysEnv.getADD1());
        params.put("cadd2", DeskFrame.clSysEnv.getADD2());
        params.put("ccorradd1", DeskFrame.clSysEnv.getCORRADD1());
        params.put("ccorradd2", DeskFrame.clSysEnv.getCORRADD2());
        params.put("cmobno", DeskFrame.clSysEnv.getMOB_NO());
        try {
            PreparedStatement pstLocal = dataConnecrtion.prepareStatement(sql);
            ResultSet rsLocal = pstLocal.executeQuery();
            print = lb.reportGenerator("ManageUserRpt.jasper", params, rsLocal, jPanel1);
            lb.closeResultSet(rsLocal);
            lb.closeStatement(pstLocal);
        } catch (Exception ex) {
            lb.printToLogFile("Exception at manageUserReport In Voucher Display", ex);
        }
    }

    private void checkPrintReport(String ref_no) {
        String sql = "SELECT cp.id, cp.party_name, cp.cheque_date, cp.amount, cp.cheque_no, bm.name, "
                +"cp.ac_pay FROM cheque_print cp LEFT JOIN bank_master bm ON bm.id = cp.fk_bank_id "
                +"WHERE cp.id = '"+ ref_no +"' ORDER BY cp.cheque_date";
        HashMap params = new HashMap();
        params.put("dir", System.getProperty("user.dir"));
        params.put("digit", lb.getDigit());
        params.put("cname", DeskFrame.clSysEnv.getCMPN_NAME());
        params.put("cadd1", DeskFrame.clSysEnv.getADD1());
        params.put("cadd2", DeskFrame.clSysEnv.getADD2());
        params.put("ccorradd1", DeskFrame.clSysEnv.getCORRADD1());
        params.put("ccorradd2", DeskFrame.clSysEnv.getCORRADD2());
        params.put("cmobno", DeskFrame.clSysEnv.getMOB_NO());
        AmountInWords amt = new AmountInWords();
        try {
            String sql1 = "SELECT * FROM cheque_print WHERE id = '"+ ref_no +"'";
            PreparedStatement ps = dataConnecrtion.prepareStatement(sql1);
            ResultSet rs = ps.executeQuery();
            String id = "";
            if (rs.next()) {
                id = rs.getString("id");
                params.put("word", amt.convertToWords(rs.getInt("amount")));
            }
            PreparedStatement pstLocal = dataConnecrtion.prepareStatement(sql);
            ResultSet rsLocal = pstLocal.executeQuery();
            print = lb.reportGenerator(""+ id +".jasper", params, rsLocal, jPanel1);
            lb.closeResultSet(rsLocal);
            lb.closeStatement(pstLocal);
        } catch (Exception ex) {
            lb.printToLogFile("Exception at checkPrintReport In Voucher Display", ex);
        }
    }

    private void userRightsReport(String ref_no) {
        String sql = "SELECT ur.user_cd, um.username, mm.name AS menu_name, fm.name AS form_name, ur.views, ur.edit, " +
            "ur.adds, ur.deletes, ur.print, ur.navigate_view FROM menu_master mm, form_master fm, user_rights ur, " +
            "user_master um WHERE mm.id = fm.fk_menu_id AND fm.id = ur.form_cd AND ur.user_cd = um.id " +
            "ORDER BY ur.user_cd, mm.id, fm.id";
        HashMap params = new HashMap();
        params.put("dir", System.getProperty("user.dir"));
        params.put("digit", lb.getDigit());
        params.put("cname", DeskFrame.clSysEnv.getCMPN_NAME());
        params.put("cadd1", DeskFrame.clSysEnv.getADD1());
        params.put("cadd2", DeskFrame.clSysEnv.getADD2());
        params.put("ccorradd1", DeskFrame.clSysEnv.getCORRADD1());
        params.put("ccorradd2", DeskFrame.clSysEnv.getCORRADD2());
        params.put("cmobno", DeskFrame.clSysEnv.getMOB_NO());
        try {
            PreparedStatement pstLocal = dataConnecrtion.prepareStatement(sql);
            ResultSet rsLocal = pstLocal.executeQuery();
            print = lb.reportGenerator("UserRights.jasper", params, rsLocal, jPanel1);
            lb.closeResultSet(rsLocal);
            lb.closeStatement(pstLocal);
        } catch (Exception ex) {
            lb.printToLogFile("Exception at userRightsReport In Voucher Display", ex);
        }
    }

    private void setIconToPnael() {
        Syspath += File.separator + "Resources" + File.separator + "Images" + File.separator;
        jbtnPrint.setIcon(new ImageIcon(Syspath +"print.png"));
        jbtnClose.setIcon(new ImageIcon(Syspath +"close.png"));
    }

    private void registerShortKeys() {
        lb.setCloseShortcut(this, jbtnClose);
        setIconToPnael();
    }

    @Override
    public void dispose() {
        try {
            DeskFrame.removeFromScreen(DeskFrame.tabbedPane.getSelectedIndex());
            super.dispose();
        } catch (Exception ex) {
            lb.printToLogFile("Exception at dispose In Voucher Display", ex);
        }
    }

    private void printVoucher() {
        try {
            getVoucher(tag, ref_no,false);
        } catch (Exception ex) {
            lb.printToLogFile("Exception at printVoucher In Voucher Display", ex);
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
        jbtnPrint = new javax.swing.JButton();
        jbtnClose = new javax.swing.JButton();

        setTitle("VOUCHER DISPLAY");

        jPanel1.setBackground(new java.awt.Color(253, 243, 243));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 3, 3, new java.awt.Color(235, 35, 35)));
        jPanel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        jbtnPrint.setBackground(new java.awt.Color(204, 255, 204));
        jbtnPrint.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jbtnPrint.setMnemonic('P');
        jbtnPrint.setText("PRINT");
        jbtnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnPrintActionPerformed(evt);
            }
        });

        jbtnClose.setBackground(new java.awt.Color(204, 255, 204));
        jbtnClose.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jbtnClose.setMnemonic('C');
        jbtnClose.setText("CLOSE");
        jbtnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 765, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jbtnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jbtnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jbtnClose, jbtnPrint});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbtnClose, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtnPrint, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jbtnClose, jbtnPrint});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnPrintActionPerformed
        printVoucher();
        this.dispose();
    }//GEN-LAST:event_jbtnPrintActionPerformed

    private void jbtnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnCloseActionPerformed
        this.dispose();
    }//GEN-LAST:event_jbtnCloseActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton jbtnClose;
    private javax.swing.JButton jbtnPrint;
    // End of variables declaration//GEN-END:variables
}