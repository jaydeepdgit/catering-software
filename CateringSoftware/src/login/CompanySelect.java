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
import support.Constants;
import support.Library;
import support.ShowCurrentTime;

/**
 *
 * @author @JD@
 */
public class CompanySelect extends javax.swing.JInternalFrame {
    boolean flag = false;
    private DeskFrame df = null;
    String Syspath = System.getProperty("user.dir");
    Library lb = new Library();

    /**
     * Creates new form CompanySelect
     */
    public CompanySelect() {
        initComponents();
        setIconToPnael();
        jlblWelcome.setText("Welcome ! "+ Constants.SOFTWARE_NAME);
    }

    public void setStartupFocus() {
        jcomboName.requestFocusInWindow();
    }

    private void setIconToPnael() {
        Syspath += File.separator + "Resources" + File.separator + "Images" + File.separator;
        jbtnSelect.setIcon(new ImageIcon(Syspath + "select.png"));
        jlblLogo.setIcon(new ImageIcon(Syspath + "pwr_logo.png"));
        jlbldecorationSystem.setIcon(new ImageIcon(Syspath + "decorationSystem.png"));
    }

    public CompanySelect(DeskFrame df) {
        initComponents();
        this.df = df;
        fillCombo();
        setIconToPnael();
        setToolBar();
    }

    private void setToolBar() {
        new ShowCurrentTime(jlblTime);
        Date dNow = new Date( );
        SimpleDateFormat ft = 
        new SimpleDateFormat ("EEEE dd, MMMM yyyy");

        jlblDate.setText(ft.format(dNow));
    }

    private void fillCombo() {
        try {
            String sql1 = "SELECT DISTINCT(company_name) FROM db_master";
            String sql2 = "SELECT DISTINCT(dbmonth) FROM db_master";
            String sql3 = "SELECT DISTINCT(dbyear) FROM db_master";
            PreparedStatement pstLocal1 = DeskFrame.connMpMain.prepareStatement(sql1);
            ResultSet rsLocal1 = pstLocal1.executeQuery();
            PreparedStatement pstLocal2 = DeskFrame.connMpMain.prepareStatement(sql2);
            ResultSet rsLocal2 = pstLocal2.executeQuery();
            PreparedStatement pstLocal3 = DeskFrame.connMpMain.prepareStatement(sql3);
            ResultSet rsLocal3 = pstLocal3.executeQuery();
            jcomboMonth.removeAllItems();
            jcomboName.removeAllItems();
            jcomboYear.removeAllItems();
            while (rsLocal1.next()) {
                jcomboName.addItem(rsLocal1.getString(1));
            }
            while (rsLocal2.next()) {
                jcomboMonth.addItem(rsLocal2.getString(1));
            }
            while (rsLocal3.next()) {
                jcomboYear.addItem(rsLocal3.getString(1));
            }
            if (rsLocal1 != null) {
                rsLocal1.close();
            }
            if (pstLocal1 != null) {
                pstLocal1.close();
            }
            if (rsLocal2 != null) {
                rsLocal2.close();
            }
            if (pstLocal2 != null) {
                pstLocal2.close();
            }
            if (rsLocal3 != null) {
                rsLocal3.close();
            }
            if (pstLocal3 != null) {
                pstLocal3.close();
            }
            flag = true;
            jcomboNameItemStateChanged(null);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage() + " \n System will exit", "Connection", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    private void jbtnSelectActionPerformedRoutine() {
        try {
            String sql = "SELECT dbname FROM db_master WHERE UPPER(company_name) = ? AND dbmonth = ? AND dbyear = ?";
            PreparedStatement pstLocal = DeskFrame.connMpMain.prepareStatement(sql);
            pstLocal.setString(1, jcomboName.getSelectedItem().toString().toUpperCase());
            pstLocal.setString(2, jcomboMonth.getSelectedItem().toString());
            pstLocal.setString(3, jcomboYear.getSelectedItem().toString());
            ResultSet rsLocal = pstLocal.executeQuery();
            if (rsLocal.next()) {
                connectToDatabase(rsLocal.getString(1));
                this.dispose();
                df.login.setEnabled(true);
                Login l1 = new Login(df);

                DeskFrame.removeFromScreen(DeskFrame.tabbedPane.getSelectedIndex());
                DeskFrame.addOnScreen(l1, Constants.LOGIN_FORM_NAME);
                DeskFrame.lb = new Library();
                df.setTitle(DeskFrame.TITLE + " :: " + jcomboName.getSelectedItem().toString() + " :: " + jcomboMonth.getSelectedItem() + " :: " + jcomboYear.getSelectedItem());

                l1.setStartupFocus();
                DeskFrame.month = jcomboMonth.getSelectedItem().toString();
                DeskFrame.dbYear = jcomboYear.getSelectedItem().toString();
            } else {
                JOptionPane.showMessageDialog(this.getRootPane(), "Requested database is not in local disk. \n Please download from server or check your selection", "Selection", JOptionPane.ERROR_MESSAGE);
            }
            if (rsLocal != null) {
                rsLocal.close();
            }
            if (pstLocal != null) {
                pstLocal.close();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage() + " \n System will exit", "Connection", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    private void connectToDatabase(String db) {
        try {
            DeskFrame.connMpAdmin = DeskFrame.getConnection(db);
            if (DeskFrame.connMpAdmin != null && companySetUp()) {
                DeskFrame.connMpAdmin = DeskFrame.getConnection(db);
                DeskFrame.dbName = db;
            } else {
                JOptionPane.showMessageDialog(this, "Can not set up company", "Connecting Database", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage() + " \n System will exit", "Connection", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
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
        jlblWelcome = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jcomboYear = new javax.swing.JComboBox();
        jcomboMonth = new javax.swing.JComboBox();
        jcomboName = new javax.swing.JComboBox();
        jbtnSelect = new javax.swing.JButton();
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
        setPreferredSize(new java.awt.Dimension(938, 465));

        jPanel1.setBackground(new java.awt.Color(174, 203, 228));

        jlblWelcome.setBackground(new java.awt.Color(35, 68, 101));
        jlblWelcome.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jlblWelcome.setForeground(new java.awt.Color(35, 68, 101));
        jlblWelcome.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblWelcome.setText("Welcome ! CATERING MANAGEMENT");

        jPanel4.setBackground(new java.awt.Color(34, 66, 100));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("COMPANY SELECT HERE");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
        );

        jPanel5.setBackground(new java.awt.Color(60, 113, 169));

        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("COMPANY");

        jLabel2.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("MONTH");

        jLabel3.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("YEAR");

        jcomboYear.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jcomboYear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jcomboYearKeyPressed(evt);
            }
        });

        jcomboMonth.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jcomboMonth.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jcomboMonthKeyPressed(evt);
            }
        });

        jcomboName.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jcomboName.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcomboNameItemStateChanged(evt);
            }
        });
        jcomboName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jcomboNameKeyPressed(evt);
            }
        });

        jbtnSelect.setBackground(new java.awt.Color(34, 66, 100));
        jbtnSelect.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jbtnSelect.setForeground(new java.awt.Color(255, 255, 255));
        jbtnSelect.setMnemonic('S');
        jbtnSelect.setText("SELECT");
        jbtnSelect.setBorder(null);
        jbtnSelect.setOpaque(false);
        jbtnSelect.setVerifyInputWhenFocusTarget(false);
        jbtnSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnSelectActionPerformed(evt);
            }
        });
        jbtnSelect.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jbtnSelectKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcomboYear, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jcomboMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jcomboName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addComponent(jbtnSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcomboName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcomboMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcomboYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jbtnSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );

        jPanel5Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel1, jcomboName});

        jPanel5Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel2, jcomboMonth});

        jPanel5Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel3, jcomboYear});

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(287, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlblWelcome, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(287, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addComponent(jlblWelcome)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
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
                        .addComponent(jlblDate, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jcomboYearKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jcomboYearKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jbtnSelect.requestFocusInWindow();
        }
    }//GEN-LAST:event_jcomboYearKeyPressed

    private void jcomboMonthKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jcomboMonthKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jcomboYear.requestFocusInWindow();
        }
    }//GEN-LAST:event_jcomboMonthKeyPressed

    private void jcomboNameItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcomboNameItemStateChanged
        try {
            if (flag) {
                String sql2 = "SELECT DISTINCT(dbmonth) FROM db_master WHERE company_name = '"+ jcomboName.getSelectedItem().toString() +"'";
                String sql3 = "SELECT DISTINCT(dbyear) FROM db_master WHERE company_name = '"+ jcomboName.getSelectedItem().toString() +"'";
                jcomboMonth.removeAllItems();
                jcomboYear.removeAllItems();
                PreparedStatement pstLocal2 = DeskFrame.connMpMain.prepareStatement(sql2);
                ResultSet rsLocal2 = pstLocal2.executeQuery();
                PreparedStatement pstLocal3 = DeskFrame.connMpMain.prepareStatement(sql3);
                ResultSet rsLocal3 = pstLocal3.executeQuery();
                while (rsLocal2.next()) {
                    jcomboMonth.addItem(rsLocal2.getString(1));
                }
                while (rsLocal3.next()) {
                    jcomboYear.addItem(rsLocal3.getString(1));
                }
                if (rsLocal2 != null) {
                    rsLocal2.close();
                }
                if (pstLocal2 != null) {
                    pstLocal2.close();
                }
                if (rsLocal3 != null) {
                    rsLocal3.close();
                }
                if (pstLocal3 != null) {
                    pstLocal3.close();
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }//GEN-LAST:event_jcomboNameItemStateChanged

    private void jcomboNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jcomboNameKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jcomboMonth.requestFocusInWindow();
        }
    }//GEN-LAST:event_jcomboNameKeyPressed

    private void jbtnSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSelectActionPerformed
        jbtnSelectActionPerformedRoutine();
    }//GEN-LAST:event_jbtnSelectActionPerformed

    private void jbtnSelectKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jbtnSelectKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jbtnSelectActionPerformedRoutine();
        }
    }//GEN-LAST:event_jbtnSelectKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JButton jbtnSelect;
    private javax.swing.JComboBox jcomboMonth;
    private javax.swing.JComboBox jcomboName;
    private javax.swing.JComboBox jcomboYear;
    private javax.swing.JLabel jlblDate;
    private javax.swing.JLabel jlblLogo;
    private javax.swing.JLabel jlblTime;
    private javax.swing.JLabel jlblWelcome;
    private javax.swing.JLabel jlbldecorationSystem;
    // End of variables declaration//GEN-END:variables
    private boolean companySetUp() {
        return true;
    }
}