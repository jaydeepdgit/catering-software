/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package reports;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import support.Library;
import support.PickList;
import cateringsoftware.DeskFrame;
import support.Constants;

/**
 *
 * @author @JD@
 */
public class AccountMasterList extends javax.swing.JInternalFrame {
    private Library lb = new Library();
    private PickList accounTypePickListView = null;
    private PickList accountPickListView = null;
    private Connection dataConnection = DeskFrame.connMpAdmin;
    private DefaultTableModel model = null;
    private String form_id = Constants.ACCOUNT_LIST_FORM_ID;
    private ResultSet rsLocal = null;
    private int swqry = 0;
    String Syspath = System.getProperty("user.dir");

    /**
     * Creates new form AccountMasterList
     */
    public AccountMasterList() {
        initComponents();
        model = (DefaultTableModel) jTable1.getModel();
        accounTypePickListView = new PickList(dataConnection);
        accountPickListView = new PickList(dataConnection);
        setPickListView();
        registerShortKeys();
        setPermission();
        setIconToPnael();
        jrbTabelWiseItemStateChanged(null);
        jTable1.setBackground(new Color(255, 255, 224));
        setTitle(Constants.ACCOUNT_LIST_FORM_NAME);
    }

    private void setIconToPnael() {
        Syspath += File.separator + "Resources" + File.separator + "Images" + File.separator;
        jbtnView.setIcon(new ImageIcon(Syspath + "view.png"));
        jbtnPreview.setIcon(new ImageIcon(Syspath + "preview.png"));
        jbtnClose.setIcon(new ImageIcon(Syspath + "close.png"));
    }

    private void registerShortKeys() {
        lb.setViewShortcut(this, jbtnView);
        lb.setPreviewShortcut(this, jbtnPreview);
        lb.setCloseShortcut(this, jbtnClose);
    }

    private void setPermission() {
        lb.setUserRightsToButton(jbtnPreview, form_id, "PRINT");
        lb.setUserRightsToButton(jbtnView, form_id, "VIEWS");
    }

    private void setPickListView() {
        accounTypePickListView.setLayer(getLayeredPane());
        accounTypePickListView.setPickListComponent(jtxtAccountType);
        accounTypePickListView.setNextComponent(jtxtAcccountName);
        accounTypePickListView.setDefaultWidth(210);
        accounTypePickListView.setAllowBlank(true);
        accounTypePickListView.setDefaultColumnWidth(200);
        accounTypePickListView.setLocation(122, 63);

        accountPickListView.setLayer(getLayeredPane());
        accountPickListView.setPickListComponent(jtxtAcccountName);
        accountPickListView.setNextComponent(jbtnView);
    }

    private boolean validateForm() {
        boolean flag = true;
        if (!lb.isBlank(jtxtAccountType)) {
            flag = flag && lb.isExist("account_type", "name", jtxtAccountType.getText(), dataConnection);
            if (!flag) {
                JOptionPane.showMessageDialog(this, "Invalid Account Type", DeskFrame.TITLE, JOptionPane.WARNING_MESSAGE);
            }
        }
        if (!lb.isBlank(jtxtAcccountName)) {
            flag = flag && lb.isExist("account_master", "name", jtxtAcccountName.getText(), dataConnection);
            if (!flag) {
                JOptionPane.showMessageDialog(this, "Invalid Account Name", DeskFrame.TITLE, JOptionPane.WARNING_MESSAGE);
            }
        }
        return flag;
    }

    private void lableEnable(){
        jrbOneLabel.setEnabled(jrbLabelWise.isSelected());
        jrbTwoLabel.setEnabled(jrbLabelWise.isSelected());
        jrbThreeLabel.setEnabled(jrbLabelWise.isSelected());
        jbtnView.setEnabled(!jrbLabelWise.isSelected());
    }

    @Override
    public void dispose() {
        try {
            DeskFrame.removeFromScreen(DeskFrame.tabbedPane.getSelectedIndex());
            super.dispose();
        } catch (Exception ex) {
            lb.printToLogFile("Exception at dispose In Account Master List", ex);
        }
    }

    private void makeQuery() {
        PreparedStatement psLocal = null;
        String sql = "";
        String ac_cd = "";
        if(swqry == 1) {
            for (int i = 0; i < jTable1.getRowCount(); i++) {
                if(jTable1.getValueAt(i, 0).toString().equalsIgnoreCase("true")) {
                   ac_cd += jTable1.getValueAt(i, 8).toString() + "','";
                }
            }
        }
        try {
            sql = "SELECT a.id, a.name, g.name AS group_name, a.opening_rs, a.account_effect_rs, a.mobile_no, a.phone_no, a.fax_no, " +
                "a.email_id, a.office_address1, a.office_address2, a.contact_person, a.reference_by, a.short_name, a.gst_no " +
                "FROM account_master a LEFT JOIN account_type g ON a.fk_account_type_id = g.id " +
                "WHERE a.fk_account_type_id = g.id ";
            if(!ac_cd.equalsIgnoreCase("")) {
                ac_cd = ac_cd.substring(0, ac_cd.length() - 3);
                sql += "AND a.id IN ('"+ ac_cd +"') ";
            }
            if(!jtxtAccountType.getText().equalsIgnoreCase("")) {
                sql +=" AND a.fk_account_type_id = '"+ lb.getAccountType(jtxtAccountType.getText(), "C") +"'";
            }
            if(!jtxtAcccountName.getText().equalsIgnoreCase("")) {
                sql +=" AND a.id = '"+ lb.getAccountCode(jtxtAcccountName.getText(), "C") +"'";
            }
            sql += " ORDER BY a.fk_account_type_id, a.name";
            psLocal = dataConnection.prepareStatement(sql);
            rsLocal = psLocal.executeQuery();
        } catch(Exception ex) {
            lb.printToLogFile("Exception at MakeQuery In Account Master List", ex);
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jrbTabelWise = new javax.swing.JRadioButton();
        jrbLabelWise = new javax.swing.JRadioButton();
        jrbThreeLabel = new javax.swing.JRadioButton();
        jrbTwoLabel = new javax.swing.JRadioButton();
        jbtnView = new javax.swing.JButton();
        jbtnPreview = new javax.swing.JButton();
        jbtnClose = new javax.swing.JButton();
        jtxtAccountType = new javax.swing.JTextField();
        jrbOneLabel = new javax.swing.JRadioButton();
        jtxtAcccountName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jPanel1.setBackground(new java.awt.Color(253, 243, 243));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 3, 3, new java.awt.Color(53, 154, 141)));
        jPanel1.setOpaque(false);
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        jScrollPane1.setBackground(new java.awt.Color(253, 243, 243));
        jScrollPane1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(4, 110, 152), 1, true));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(735, 384));

        jTable1.setBackground(new java.awt.Color(253, 243, 243));
        jTable1.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "A/c Name", "Group Name", "Address", "Mobile No", "Email", "Cont. Person", "Ref No", "AC CD"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setRowHeight(23);
        jScrollPane1.setViewportView(jTable1);
        jTable1.getColumnModel().getColumn(0).setMinWidth(30);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(30);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(60);
        jTable1.getColumnModel().getColumn(1).setResizable(false);
        jTable1.getColumnModel().getColumn(2).setResizable(false);
        jTable1.getColumnModel().getColumn(3).setResizable(false);
        jTable1.getColumnModel().getColumn(4).setResizable(false);
        jTable1.getColumnModel().getColumn(5).setResizable(false);
        jTable1.getColumnModel().getColumn(6).setResizable(false);
        jTable1.getColumnModel().getColumn(7).setResizable(false);
        jTable1.getColumnModel().getColumn(8).setMinWidth(0);
        jTable1.getColumnModel().getColumn(8).setPreferredWidth(0);
        jTable1.getColumnModel().getColumn(8).setMaxWidth(0);

        jPanel1.add(jScrollPane1);

        jPanel3.setBackground(new java.awt.Color(253, 243, 243));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 3, 3, new java.awt.Color(235, 35, 35)), "Details", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 2, 16), new java.awt.Color(0, 0, 255))); // NOI18N

        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel1.setText("Account Type");

        buttonGroup1.add(jrbTabelWise);
        jrbTabelWise.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jrbTabelWise.setSelected(true);
        jrbTabelWise.setText("TABEL WISE");
        jrbTabelWise.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jrbTabelWiseItemStateChanged(evt);
            }
        });

        buttonGroup1.add(jrbLabelWise);
        jrbLabelWise.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jrbLabelWise.setText("LABEL WISE");
        jrbLabelWise.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jrbLabelWiseItemStateChanged(evt);
            }
        });

        buttonGroup2.add(jrbThreeLabel);
        jrbThreeLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jrbThreeLabel.setText("THREE LABEL");

        buttonGroup2.add(jrbTwoLabel);
        jrbTwoLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jrbTwoLabel.setText("TOW LABEL");

        jbtnView.setBackground(new java.awt.Color(204, 255, 204));
        jbtnView.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jbtnView.setForeground(new java.awt.Color(235, 35, 35));
        jbtnView.setMnemonic('V');
        jbtnView.setText("VIEW RESULT");
        jbtnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnViewActionPerformed(evt);
            }
        });
        jbtnView.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jbtnViewKeyPressed(evt);
            }
        });

        jbtnPreview.setBackground(new java.awt.Color(204, 255, 204));
        jbtnPreview.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jbtnPreview.setForeground(new java.awt.Color(235, 35, 35));
        jbtnPreview.setMnemonic('P');
        jbtnPreview.setText("PREVIEW");
        jbtnPreview.setMaximumSize(new java.awt.Dimension(87, 23));
        jbtnPreview.setMinimumSize(new java.awt.Dimension(87, 23));
        jbtnPreview.setPreferredSize(new java.awt.Dimension(87, 23));
        jbtnPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnPreviewActionPerformed(evt);
            }
        });
        jbtnPreview.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jbtnPreviewKeyPressed(evt);
            }
        });

        jbtnClose.setBackground(new java.awt.Color(204, 255, 204));
        jbtnClose.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jbtnClose.setForeground(new java.awt.Color(235, 35, 35));
        jbtnClose.setMnemonic('C');
        jbtnClose.setText("CLOSE");
        jbtnClose.setMaximumSize(new java.awt.Dimension(87, 23));
        jbtnClose.setMinimumSize(new java.awt.Dimension(87, 23));
        jbtnClose.setPreferredSize(new java.awt.Dimension(87, 23));
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

        jtxtAccountType.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtAccountType.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtAccountType.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtAccountTypeFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtAccountTypeFocusLost(evt);
            }
        });
        jtxtAccountType.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtAccountTypeKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxtAccountTypeKeyReleased(evt);
            }
        });

        buttonGroup2.add(jrbOneLabel);
        jrbOneLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jrbOneLabel.setSelected(true);
        jrbOneLabel.setText("ONE LABEL");

        jtxtAcccountName.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtAcccountName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152)));
        jtxtAcccountName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtAcccountNameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtAcccountNameFocusLost(evt);
            }
        });
        jtxtAcccountName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtAcccountNameKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxtAcccountNameKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtAcccountNameKeyTyped(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel2.setText("Account Name");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtxtAccountType, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtxtAcccountName)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                            .addComponent(jbtnView, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jbtnPreview, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jbtnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap())
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                            .addComponent(jrbTabelWise, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jrbLabelWise, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(179, 179, 179)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jrbOneLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jrbTwoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jrbThreeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jbtnClose, jbtnPreview, jbtnView});

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jrbOneLabel, jrbTabelWise, jrbTwoLabel});

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtxtAccountType, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtxtAcccountName, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jbtnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jbtnPreview, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jbtnView))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jrbTabelWise)
                            .addComponent(jrbLabelWise)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jrbTwoLabel)
                                    .addComponent(jrbThreeLabel)
                                    .addComponent(jrbOneLabel))))))
                .addGap(9, 9, 9))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel1, jbtnClose, jbtnPreview, jbtnView, jtxtAccountType});

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jrbLabelWise, jrbOneLabel, jrbTabelWise, jrbThreeLabel, jrbTwoLabel});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 942, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnViewActionPerformed
        try {
            model.setRowCount(0);
            if (validateForm()) {
                swqry = 0;
                makeQuery();
                jPanel1.removeAll();
                jPanel1.add(jScrollPane1);
                while (rsLocal.next()) {
                    Vector row = new Vector();
                    row.add(true);
                    row.add(rsLocal.getString("name"));
                    row.add(rsLocal.getString("group_name"));
                    row.add(rsLocal.getString("office_address1") +" "+ rsLocal.getString("office_address2"));
                    row.add(rsLocal.getString("mobile_no"));
                    row.add(rsLocal.getString("email_id"));
                    row.add(rsLocal.getString("contact_person"));
                    row.add(rsLocal.getString("reference_by"));
                    row.add(rsLocal.getString("id"));
                    model.addRow(row);
                }
            }
        } catch (Exception ex) {
            lb.printToLogFile("Error at jbtnViewActionPerformed In Account Master List", ex);
        }
    }//GEN-LAST:event_jbtnViewActionPerformed

    private void jbtnViewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jbtnViewKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jbtnView.doClick();
            jbtnPreview.requestFocusInWindow();
        }
    }//GEN-LAST:event_jbtnViewKeyPressed

    private void jbtnPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnPreviewActionPerformed
        try {
            if (validateForm()) {
                swqry = 1;
                makeQuery();
                jPanel1.removeAll();
                jPanel1.add(jScrollPane1);
                if (jrbTabelWise.isSelected()) {
                    HashMap param = new HashMap();
                    param.put("digit", lb.getDigit());
                    param.put("cname", DeskFrame.clSysEnv.getCMPN_NAME());
                    param.put("cadd1", DeskFrame.clSysEnv.getADD1());
                    param.put("cadd2", DeskFrame.clSysEnv.getADD2());
                    param.put("ccorradd1", DeskFrame.clSysEnv.getCORRADD1());
                    param.put("ccorradd2", DeskFrame.clSysEnv.getCORRADD2());
                    param.put("cmobno", DeskFrame.clSysEnv.getMOB_NO());
                    param.put("cphno", DeskFrame.clSysEnv.getPHONE_NO());
                    param.put("cemail", DeskFrame.clSysEnv.getEMAIL());
                    param.put("cvatno", DeskFrame.clSysEnv.getTIN_NO());
                    param.put("ccstno", DeskFrame.clSysEnv.getCST_NO());
                    param.put("ctaxno", DeskFrame.clSysEnv.getTAX_NO());
                    param.put("cpanno", DeskFrame.clSysEnv.getPAN_NO());
                    lb.reportGenerator("AccountWiseList.jasper", param, rsLocal, jPanel1);
                } else {
                    HashMap param = new HashMap();
                    param.put("digit", lb.getDigit());
                    param.put("cname", DeskFrame.clSysEnv.getCMPN_NAME());
                    param.put("cadd1", DeskFrame.clSysEnv.getADD1());
                    param.put("cadd2", DeskFrame.clSysEnv.getADD2());
                    param.put("ccorradd1", DeskFrame.clSysEnv.getCORRADD1());
                    param.put("ccorradd2", DeskFrame.clSysEnv.getCORRADD2());
                    param.put("cmobno", DeskFrame.clSysEnv.getMOB_NO());
                    param.put("cphno", DeskFrame.clSysEnv.getPHONE_NO());
                    param.put("cemail", DeskFrame.clSysEnv.getEMAIL());
                    param.put("cvatno", DeskFrame.clSysEnv.getTIN_NO());
                    param.put("ccstno", DeskFrame.clSysEnv.getCST_NO());
                    param.put("ctaxno", DeskFrame.clSysEnv.getTAX_NO());
                    param.put("cpanno", DeskFrame.clSysEnv.getPAN_NO());
                    if(jrbOneLabel.isSelected()) {
                        lb.reportGenerator("CoverPrint.jasper", param, rsLocal, jPanel1);
                    } else if (jrbTwoLabel.isSelected()) {
                        lb.reportGenerator("TwoLabelParty.jasper", null, rsLocal, jPanel1);
                    } else {
                        lb.reportGenerator("ThreeLabelParty.jasper", null, rsLocal, jPanel1);
                    }
                }
            }
        } catch (Exception ex) {
            lb.printToLogFile("Error at jbtnPreviewActionPerformed In Account Master List", ex);
        }
    }//GEN-LAST:event_jbtnPreviewActionPerformed

    private void jbtnPreviewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jbtnPreviewKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jbtnPreview.doClick();
        }
    }//GEN-LAST:event_jbtnPreviewKeyPressed

    private void jbtnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnCloseActionPerformed
        this.dispose();
    }//GEN-LAST:event_jbtnCloseActionPerformed

    private void jbtnCloseKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jbtnCloseKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jbtnClose.doClick();
        }
    }//GEN-LAST:event_jbtnCloseKeyPressed

    private void jtxtAccountTypeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtAccountTypeFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtAccountTypeFocusGained

    private void jtxtAccountTypeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtAccountTypeFocusLost
        accounTypePickListView.setVisible(false);
    }//GEN-LAST:event_jtxtAccountTypeFocusLost

    private void jtxtAccountTypeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtAccountTypeKeyPressed
        accounTypePickListView.setLocation(jtxtAccountType.getX() + jPanel3.getX(), jPanel3.getY() + jtxtAccountType.getY() + jtxtAccountType.getHeight());
        accounTypePickListView.pickListKeyPress(evt);
        accounTypePickListView.setReturnComponent(new JTextField[]{jtxtAccountType});
    }//GEN-LAST:event_jtxtAccountTypeKeyPressed

    private void jtxtAccountTypeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtAccountTypeKeyReleased
        try {
            accounTypePickListView.setReturnComponent(new JTextField[]{jtxtAccountType});
            PreparedStatement pstLocal = dataConnection.prepareStatement("SELECT name FROM account_type WHERE name LIKE '%"+ jtxtAccountType.getText().toUpperCase() +"%'");
            accounTypePickListView.setPreparedStatement(pstLocal);
            accounTypePickListView.setValidation(dataConnection.prepareStatement("SELECT * FROM account_type WHERE name = ?"));
            accounTypePickListView.pickListKeyRelease(evt);
        } catch (Exception ex) {
            lb.printToLogFile("Exception at jtxtAccountTypeKeyReleased In Account Master List", ex);
        }
    }//GEN-LAST:event_jtxtAccountTypeKeyReleased

    private void jrbLabelWiseItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jrbLabelWiseItemStateChanged
        lableEnable();
    }//GEN-LAST:event_jrbLabelWiseItemStateChanged

    private void jrbTabelWiseItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jrbTabelWiseItemStateChanged
        lableEnable();
    }//GEN-LAST:event_jrbTabelWiseItemStateChanged

    private void jtxtAcccountNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtAcccountNameFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtAcccountNameFocusGained

    private void jtxtAcccountNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtAcccountNameFocusLost
        accountPickListView.setVisible(false);
    }//GEN-LAST:event_jtxtAcccountNameFocusLost

    private void jtxtAcccountNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtAcccountNameKeyPressed
        accountPickListView.setLocation(jtxtAcccountName.getX() + jPanel3.getX(), jPanel3.getY() + jtxtAcccountName.getY() + jtxtAcccountName.getHeight());
        accountPickListView.pickListKeyPress(evt);
        accountPickListView.setReturnComponent(new JTextField[]{jtxtAcccountName});
    }//GEN-LAST:event_jtxtAcccountNameKeyPressed

    private void jtxtAcccountNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtAcccountNameKeyReleased
        try {
            accountPickListView.setReturnComponent(new JTextField[]{jtxtAcccountName});
            PreparedStatement pstLocal = dataConnection.prepareStatement("SELECT name FROM account_master WHERE name LIKE '%"+ jtxtAcccountName.getText().toUpperCase() +"%'");
            accountPickListView.setPreparedStatement(pstLocal);
            accountPickListView.setValidation(dataConnection.prepareStatement("SELECT * FROM account_master WHERE name = ?"));
            accountPickListView.pickListKeyRelease(evt);
        } catch (Exception ex) {
            lb.printToLogFile("Exception at jtxtAcccountNameKeyReleased In Account Master List", ex);
        }
    }//GEN-LAST:event_jtxtAcccountNameKeyReleased

    private void jtxtAcccountNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtAcccountNameKeyTyped
        lb.fixLength(evt, 50);
    }//GEN-LAST:event_jtxtAcccountNameKeyTyped

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton jbtnClose;
    private javax.swing.JButton jbtnPreview;
    private javax.swing.JButton jbtnView;
    private javax.swing.JRadioButton jrbLabelWise;
    private javax.swing.JRadioButton jrbOneLabel;
    private javax.swing.JRadioButton jrbTabelWise;
    private javax.swing.JRadioButton jrbThreeLabel;
    private javax.swing.JRadioButton jrbTwoLabel;
    private javax.swing.JTextField jtxtAcccountName;
    private javax.swing.JTextField jtxtAccountType;
    // End of variables declaration//GEN-END:variables
}