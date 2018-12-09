/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.awt.Color;
import cateringsoftware.DeskFrame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import cateringsoftware.MainClass;
import support.Constants;
import support.Library;
import support.SmallNavigation1;
import support.VoucherDisplay;

/**
 *
 * @author @JD@
 */
public class ManageUserView extends javax.swing.JInternalFrame {
    DefaultTableModel dtm = null;
    SmallNavigation1 navLoad = null;
    Library lb = new Library();
    private Connection dataConnection = DeskFrame.connMpAdmin;

    /**
     * Creates new form ManageUserView
     */
    public ManageUserView() {
        initOtherComponents();
    }

    public ManageUserView(String cntry_name) {
        initOtherComponents();
        navLoad.callNew();
    }

    private void initOtherComponents() {
        initComponents();
        connectToNavigation();
        dtm = (DefaultTableModel) jTable1.getModel();
        setData();
        setPermission();
        lb.serchOnViewTable(jTable1, getContentPane(), 12, 98);
        if (jTable1.getRowCount() > 0) {
            jTable1.requestFocusInWindow();
            jTable1.setRowSelectionInterval(0, 0);
        }
        setTitle(Constants.MANAGE_USER_FORM_NAME);
    }

    private void setPermission() {
        UserRights.setUserRightsToPanel(navLoad, Constants.MANAGE_USER_FORM_ID);
        jTable1.setBackground(new Color(253, 243, 243));
    }

    private void close() {
        this.dispose();
    }

    @Override
    public void dispose() {
        try {
            DeskFrame.removeFromScreen(DeskFrame.tabbedPane.getSelectedIndex());
            super.dispose();
        } catch (Exception ex) {
            lb.printToLogFile("Exception at dispose In Manage User View", ex);
        }
    }

    private void showDailogeAdd() {
        ManageUserController gp = new ManageUserController(null, true, this, "", "", 0);
        gp.setLocationRelativeTo(null);
        gp.show();
    }

    private void showDailogeEdit(String user_name, String password, int code) {
        ManageUserController gp = new ManageUserController(null, true, this, user_name, password, code);
        gp.setLocationRelativeTo(null);
        gp.show();
    }

    private void connectToNavigation() {
        class navigation extends SmallNavigation1 {
            @Override
            public void callNew() {
                navLoad.setMode("N");
                showDailogeAdd();
            }

            @Override
            public void callEdit() {
                int row = jTable1.getSelectedRow();
                if (row != -1 && !jTable1.getValueAt(row, 0).toString().equals("")) {
                    navLoad.setMode("E");
                    lb.confirmDialog("Do you want to edit this user?");
                    if (lb.type) {
                        showDailogeEdit(jTable1.getValueAt(row, 1).toString(), jTable1.getValueAt(row, 2).toString(), Integer.parseInt(jTable1.getValueAt(row, 0).toString()));
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select row from table to modify");
                }
            }

            @Override
            public void callDelete() {
                SwingWorker workerForjbtnGenerate = new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        int row = jTable1.getSelectedRow();
                        if (row != -1) {
                            lb.confirmDialog(Constants.DELETE_RECORD);
                            if (lb.type) {
                                try {
                                    PreparedStatement ps = dataConnection.prepareStatement("DELETE FROM user_master WHERE id = ?");
                                    ps.setString(1, jTable1.getValueAt(row, 0).toString());
                                    ps.executeUpdate();

                                    ps = dataConnection.prepareStatement("DELETE FROM user_rights WHERE user_cd = ?");
                                    ps.setString(1, jTable1.getValueAt(row, 0).toString());
                                    ps.executeUpdate();
                                    lb.addGlassPane(navLoad);
                                    dtm.removeRow(row);
                                } catch (Exception ex) {
                                    System.out.println(ex.getMessage());
                                } finally {
                                    lb.removeGlassPane(navLoad);
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Please select row from table to modify");
                        }
                        return null;
                    }
                };
                workerForjbtnGenerate.execute();
            }

            @Override
            public void callClose() {
                close();
            }

            @Override
            public void callPrint() {
                try {
                    VoucherDisplay vd = new VoucherDisplay("", "MU");
                    DeskFrame.addOnScreen(vd, Constants.MANAGE_USER_FORM_NAME +" PRINT");
                } catch(Exception ex) {
                    lb.printToLogFile("Exception at printVoucher In Manage User View", ex);
                }
            }
        }
        navLoad = new navigation();
        jPanel2.add(navLoad);
        navLoad.setVisible(true);
    }

    public boolean addUpdateManageUserMaster(String username, String password, int user_cd) {
        PreparedStatement psLocal = null;
        ResultSet rsLocal = null;

        PreparedStatement psLocal2 = null;
        ResultSet rsLocal2 = null;
        try {
            if(navLoad.getMode().equalsIgnoreCase("N")){
                psLocal = dataConnection.prepareStatement("INSERT INTO user_master (username, password) VALUES (?, ?)");
            } else if(navLoad.getMode().equalsIgnoreCase("E")){
                psLocal = dataConnection.prepareStatement("UPDATE user_master SET username = ?, password = ? WHERE id = ?");
                psLocal.setInt(3, user_cd); // USER CD
            }
            psLocal.setString(1, username); // USERNAME
            psLocal.setString(2, password); // PASSWORD
            psLocal.executeUpdate();

            if (navLoad.getMode().equalsIgnoreCase("N")) {
                psLocal = dataConnection.prepareStatement("SELECT * FROM user_master WHERE username = ?");
                psLocal.setString(1, username); // USERNAME
                rsLocal = psLocal.executeQuery();

                if (rsLocal.next()) {
                    user_cd = rsLocal.getInt("id"); // USER CD
                    lb.closeResultSet(rsLocal);
                    psLocal = dataConnection.prepareStatement("INSERT INTO user_rights (user_cd, form_cd)VALUES (?, ?)");
                    psLocal.setInt(1, user_cd); // USER CD

                    psLocal2 = dataConnection.prepareStatement("SELECT id FROM form_master");
                    rsLocal2 = psLocal2.executeQuery();
                    while (rsLocal2.next()) {
                        psLocal.setInt(2, rsLocal2.getInt("id")); // FORM CD
                        psLocal.executeUpdate();
                    }
                    lb.closeResultSet(rsLocal2);
                    lb.closeStatement(psLocal2);
                } else {
                    navLoad.setMessage("User not created");
                }
                lb.closeResultSet(rsLocal);
                lb.closeStatement(psLocal);
            }
            setData();
        } catch (Exception ex) {
            lb.printToLogFile("Exception at addUpdateManageUserMaster In Manage User View", ex);
            JOptionPane.showMessageDialog(null, ex.getCause().getMessage());
            return false;
        } finally {
            lb.removeGlassPane(navLoad);
        }
        return true;
    }

    private void setData() {
        try {
            String sql = "SELECT * FROM user_master ORDER BY id";
            PreparedStatement psLocal = dataConnection.prepareStatement(sql);
            ResultSet rsLocal = psLocal.executeQuery();
            dtm.setRowCount(0);
            while(rsLocal.next()) {
                Vector row = new Vector();
                row.add(rsLocal.getString("id"));
                row.add(rsLocal.getString("username"));
                row.add(rsLocal.getString("password"));
                dtm.addRow(row);
            }
        } catch (Exception ex) {
            lb.printToLogFile("Exception at setData In Manage User View", ex);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();

        jPanel1.setBackground(new java.awt.Color(253, 243, 243));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(3, 3, 1, 1, new java.awt.Color(53, 154, 141)));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBackground(new java.awt.Color(253, 243, 243));
        jScrollPane1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(4, 110, 152), 1, true));
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(500, 402));

        jTable1.setBackground(new java.awt.Color(253, 243, 243));
        jTable1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jTable1.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "User ID", "User Name", "Pass Word"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setRowHeight(23);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable1KeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        jTable1.getColumnModel().getColumn(0).setMinWidth(0);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(0);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(1).setResizable(false);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(250);
        jTable1.getColumnModel().getColumn(2).setResizable(false);
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(250);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel2.setBackground(new java.awt.Color(253, 243, 243));
        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 3, 3, new java.awt.Color(235, 35, 35)));
        jPanel2.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if(evt.getClickCount() == 2) {
            if(MainClass.df.hasPermission(Constants.MANAGE_USER_FORM_ID)) {
                navLoad.callEdit();
            } else {
                JOptionPane.showMessageDialog(null, Constants.NO_RIGHTS_TO_VIEW, Constants.MANAGE_USER_FORM_NAME, JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        if(lb.isEnter(evt)) {
            if(MainClass.df.hasPermission(Constants.MANAGE_USER_FORM_ID)) {
                navLoad.callEdit();
            } else {
                JOptionPane.showMessageDialog(null, Constants.NO_RIGHTS_TO_VIEW, Constants.MANAGE_USER_FORM_NAME, JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_jTable1KeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}