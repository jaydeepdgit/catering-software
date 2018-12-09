/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package login;

import cateringsoftware.DeskFrame;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import cateringsoftware.MainClass;
import support.Constants;
import support.Library;
import support.ShowCurrentTime;
import support.SysEnv;
import utility.DateSetting;

/**
 *
 * @author @JD@
 */
public class Login extends javax.swing.JInternalFrame {
    public boolean loginStatus = false;
    private DeskFrame df = null;
    String oldDir = System.getProperty("user.dir");
    public static String MIDCode = "";
    String Syspath = System.getProperty("user.dir");
    Library lb = new Library();

    /**
     * Creates new form Login
     */
    public Login() {
        initComponents();
        this.loginStatus = false;
        setIconToPnael();
        setToolBar();
        jlblWelcome.setText("Welcome ! "+ Constants.SOFTWARE_NAME);
        setTitle(Constants.LOGIN_FORM_NAME);
    }

    public void setStartupFocus() {
        jcmbUserName.requestFocusInWindow();
    }

    private void setIconToPnael() {
        Syspath += File.separator + "Resources" + File.separator + "Images" + File.separator;
        jbtnLogin.setIcon(new ImageIcon(Syspath + "login.png"));
        jbtnClose.setIcon(new ImageIcon(Syspath + "close.png"));
        jlblLogo.setIcon(new ImageIcon(Syspath + "pwr_logo.png"));
        jlbldecorationSystem.setIcon(new ImageIcon(Syspath + "decorationSystem.png"));
    }

    public Login(DeskFrame df) {
        initComponents();
        this.loginStatus = false;
        this.df = df;
        fillCombo();
        setIconToPnael();
        setToolBar();
    }

    private void setToolBar() {
        new ShowCurrentTime(jlblTime);
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat ("EEEE dd, MMMM yyyy");

        jlblDate.setText(ft.format(dNow));
    }

    private boolean companySetUp() {
        SysEnv clSysEnv = DeskFrame.clSysEnv;
        boolean flag = false;
        try {
            String sql = "SELECT * FROM company_master";
            PreparedStatement pstLocal = DeskFrame.connMpAdmin.prepareStatement(sql);
            ResultSet rsLocal = pstLocal.executeQuery();
            if (rsLocal.next()) {
                clSysEnv.setCMPN_NAME(rsLocal.getString("company_name"));
                clSysEnv.setAC_YEAR(rsLocal.getString("ac_year"));
                clSysEnv.setMNTH(rsLocal.getString("mnth"));
                clSysEnv.setSH_NAME(rsLocal.getString("sh_name"));
                clSysEnv.setINVOICE_TYPE(rsLocal.getString("invoice_type"));
                clSysEnv.setIMAGE_PATH(rsLocal.getString("image_path"));
                clSysEnv.setADD1(rsLocal.getString("add1"));
                clSysEnv.setADD2(rsLocal.getString("add2"));
                clSysEnv.setCORRADD1(rsLocal.getString("corraddress1"));
                clSysEnv.setCORRADD2(rsLocal.getString("corraddress2"));
                clSysEnv.setMOB_NO(rsLocal.getString("mob_no"));
                clSysEnv.setPHONE_NO(rsLocal.getString("phone_no"));
                clSysEnv.setLICENCE_NO(rsLocal.getString("licence_no"));
                clSysEnv.setEMAIL(rsLocal.getString("email"));
                clSysEnv.setFAX_NO(rsLocal.getString("fax_no"));
                clSysEnv.setPAN_NO(rsLocal.getString("pan_no"));
                clSysEnv.setTIN_NO(rsLocal.getString("tin_no"));
                clSysEnv.setCST_NO(rsLocal.getString("cst_no"));
                clSysEnv.setTAX_NO(rsLocal.getString("tax_no"));
                clSysEnv.setBANK_NAME(rsLocal.getString("bank_name"));
                clSysEnv.setAC_NO(rsLocal.getString("ac_no"));
                clSysEnv.setBRANCH_NAME(rsLocal.getString("branch_name"));
                clSysEnv.setCASH_AC(rsLocal.getString("cash_ac_cd"));
                clSysEnv.setSALE_AC(rsLocal.getString("sale_ac"));
                clSysEnv.setPURCHASE_AC(rsLocal.getString("purchase_ac"));
                clSysEnv.setLAB_INC_AC(rsLocal.getString("lab_inc_ac"));
                clSysEnv.setLAB_EXP_AC(rsLocal.getString("lab_exp_ac"));
                clSysEnv.setCONTACT_PERSON(rsLocal.getString("contact_person"));
                clSysEnv.setWEBSITE(rsLocal.getString("website"));
                clSysEnv.setMYPWD(rsLocal.getString("mypwd"));
                clSysEnv.setDIGIT(rsLocal.getString("digit"));
                clSysEnv.setDELETE_PWD(rsLocal.getString("delete_pwd"));
                clSysEnv.setIFSC_CODE(rsLocal.getString("ifsc_code"));

                DeskFrame.digit = rsLocal.getInt("digit");
                DeskFrame.dbYear = rsLocal.getString("ac_year");
                DeskFrame.month = rsLocal.getString("mnth");
                DeskFrame.DELETE_PWD = rsLocal.getString("delete_pwd");
                return true;
            }
        } catch (Exception ex) {
            lb.printToLogFile("error at companySetUp In Login", ex);
            return false;
        }
        return flag;
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
        jlblWelcome = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jbtnLogin = new javax.swing.JButton();
        jbtnClose = new javax.swing.JButton();
        jtxtPwd = new javax.swing.JPasswordField();
        jcmbUserName = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jlblTime = new javax.swing.JLabel();
        jlblDate = new javax.swing.JLabel();
        jlbldecorationSystem = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jlblLogo = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();

        setOpaque(true);

        jPanel1.setBackground(new java.awt.Color(174, 203, 228));
        jPanel1.setPreferredSize(new java.awt.Dimension(922, 275));

        jlblWelcome.setBackground(new java.awt.Color(35, 68, 101));
        jlblWelcome.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jlblWelcome.setForeground(new java.awt.Color(35, 68, 101));
        jlblWelcome.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblWelcome.setText("Welcome ! CATERING MANAGEMENT");

        jPanel4.setBackground(new java.awt.Color(34, 66, 100));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("LOGIN HERE");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
        );

        jPanel5.setBackground(new java.awt.Color(60, 113, 169));

        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("USER NAME : ");

        jLabel2.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("PASS WORD : ");

        jbtnLogin.setBackground(new java.awt.Color(34, 66, 100));
        jbtnLogin.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jbtnLogin.setForeground(new java.awt.Color(255, 255, 255));
        jbtnLogin.setMnemonic('L');
        jbtnLogin.setText("LOGIN");
        jbtnLogin.setBorder(null);
        jbtnLogin.setOpaque(false);
        jbtnLogin.setVerifyInputWhenFocusTarget(false);
        jbtnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnLoginActionPerformed(evt);
            }
        });
        jbtnLogin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jbtnLoginKeyPressed(evt);
            }
        });

        jbtnClose.setBackground(new java.awt.Color(34, 66, 100));
        jbtnClose.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jbtnClose.setForeground(new java.awt.Color(255, 255, 255));
        jbtnClose.setMnemonic('C');
        jbtnClose.setText("CLOSE");
        jbtnClose.setBorder(null);
        jbtnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnCloseActionPerformed(evt);
            }
        });

        jtxtPwd.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtPwd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtPwdKeyPressed(evt);
            }
        });

        jcmbUserName.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jcmbUserName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jcmbUserNameKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jbtnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jcmbUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtPwd, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jPanel5Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jcmbUserName, jtxtPwd});

        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jcmbUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jtxtPwd))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbtnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel5Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel1, jLabel2, jcmbUserName, jtxtPwd});

        jPanel5Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jbtnClose, jbtnLogin});

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlblWelcome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(118, Short.MAX_VALUE)
                .addComponent(jlblWelcome)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(118, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(52, 105, 152));

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setText("info@powertechitsolution.net");

        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel10.setText("dhameliya.jaydeep@gmail.com");

        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Support : ");

        jlblTime.setForeground(new java.awt.Color(255, 255, 255));
        jlblTime.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jlblTime.setText("info");

        jlblDate.setForeground(new java.awt.Color(255, 255, 255));
        jlblDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlblDate.setText("info@pownet");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlbldecorationSystem, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jlblDate, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jlblTime, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlblTime)
                    .addComponent(jlblDate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addContainerGap())
            .addComponent(jlbldecorationSystem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel3.setBackground(new java.awt.Color(52, 105, 152));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("<html><body><span>Developed By </span></body></html>");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("<html><b>HELP LINE NO. : <strong style='font-size:13px;'>+919727397009 / +917405116442</strong></b> &copy; 2018 POWERTECH IT SOLUTION. All Rights Reserved.</html>");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 132, Short.MAX_VALUE)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addComponent(jlblLogo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 427, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnCloseActionPerformed
        DeskFrame.tabbedPane.removeAll();
        doDefaultCloseAction();
        loginStatus = false;
        df.setUserRights(false);
    }//GEN-LAST:event_jbtnCloseActionPerformed

private void jbtnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnLoginActionPerformed
    if (lb.validateUser(jcmbUserName.getSelectedItem().toString(), jtxtPwd.getText())) {
        if (companySetUp()) {
            MainClass.ecBean.getEmailConfigDtls();
            loginStatus = true;
            DeskFrame.user_id = Integer.parseInt(lb.getUserName(jcmbUserName.getSelectedItem().toString(), "C"));
            df.setUserRights(true);
            df.setTitle(df.getTitle()+ " :: " + jcmbUserName.getSelectedItem().toString());

            DateSetting ds = new DateSetting(df);
            this.dispose();
            DeskFrame.removeFromScreen(DeskFrame.tabbedPane.getSelectedIndex());
            DeskFrame.addOnScreen(ds, Constants.SELECT_DATE);
            ds.setStartupFocus();
        } else {
            JOptionPane.showMessageDialog(this, "Company not Found.", Constants.LOGIN_FORM_NAME, JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(this, "User name Or Password is invalid", Constants.LOGIN_FORM_NAME, JOptionPane.ERROR_MESSAGE);
        jcmbUserName.requestFocusInWindow();
    }
}//GEN-LAST:event_jbtnLoginActionPerformed

    private void jcmbUserNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jcmbUserNameKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jtxtPwd.requestFocusInWindow();
        }
    }//GEN-LAST:event_jcmbUserNameKeyPressed

    private void jtxtPwdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtPwdKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jbtnLogin.requestFocusInWindow();
        }
    }//GEN-LAST:event_jtxtPwdKeyPressed

    private void jbtnLoginKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jbtnLoginKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jbtnLogin.doClick();
        }
    }//GEN-LAST:event_jbtnLoginKeyPressed

    private void fillCombo() {
        String[] h1 = lb.getUserName();
        for (int i = 0; i < h1.length; i++) {
            jcmbUserName.addItem(h1[i]);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JButton jbtnClose;
    private javax.swing.JButton jbtnLogin;
    private javax.swing.JComboBox jcmbUserName;
    private javax.swing.JLabel jlblDate;
    private javax.swing.JLabel jlblLogo;
    private javax.swing.JLabel jlblTime;
    private javax.swing.JLabel jlblWelcome;
    private javax.swing.JLabel jlbldecorationSystem;
    private javax.swing.JPasswordField jtxtPwd;
    // End of variables declaration//GEN-END:variables
}