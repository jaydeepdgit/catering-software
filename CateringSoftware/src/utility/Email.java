/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.mail.Session;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import cateringsoftware.DeskFrame;
import cateringsoftware.MainClass;
import support.Constants;
import support.EmailConfiguration;
import support.EmailMessage;
import support.Library;
import support.MemberPickListView;
import support.PartyMasterData;
import support.Validation;

/**
 *
 * @author @JD@
 */
public class Email extends javax.swing.JInternalFrame {
    Vector files = new Vector();
    EmailConfiguration configuration = null;
    Session session = null;
    EmailMessage msg = null;
    private MemberPickListView memberPickListView = new MemberPickListView(false, "AC");
    private PartyMasterData memberMasterData=new PartyMasterData();
    String Syspath=System.getProperty("user.dir");
    Connection dataConnection = DeskFrame.connMpAdmin;
    Library lb = new Library();
    String To = "";

    public Email() {
        initComponents();

        AttachFileDesc.setVisible(false);
        lb.registerShortKeys(getRootPane(), jbtnCancel, new JButton(""), new JButton(""), new JButton(""));
        setIconToPnael();
        setPermission();
        setTitle(Constants.EMAIL_FORM_NAME);
    }

    private void setPermission() {
        lb.setUserRightsToButton(jbtnSend, Constants.EMAIL_FORM_ID, "EDIT");
    }

    private void setIconToPnael() {
        Syspath += File.separator +"Resources"+ File.separator +"Images"+ File.separator;
        jbtnSend.setIcon(new ImageIcon(Syspath +"send.png"));
        jbtnCancel.setIcon(new ImageIcon(Syspath +"close.png"));
        jbtnAttachFiles.setIcon(new ImageIcon(Syspath +"attach.png"));
    }

    private void cancelOrClose() {
        this.dispose();
    }

    private void setToPickListView(String strWhere) {
        StringTokenizer st = new StringTokenizer(strWhere, ",");
        while(st.hasMoreTokens()) {
            strWhere = st.nextToken();
        }
        boolean[] columnEditableState = {false, false, false};
        javax.swing.text.JTextComponent[] returnToComponent = {jtxtTo};
        int[] associatedColumn = {0, 1, 2};
        String[] fldList = {"", "", ""};
        int[] another_associatedColumn = {1,0,2};
        int Size = (jtxtTo.getWidth());
        JComponent nextFocusComponent = jtxtTo;

        try {
            memberMasterData = new PartyMasterData(fldList, dataConnection);
            java.util.Vector columnHeadings = new java.util.Vector();
            columnHeadings.addElement("Alias");
            columnHeadings.addElement("Name");
            columnHeadings.addElement("Email");
            String sql1 = "";
            sql1 = "SELECT id, name, email_id FROM account_master WHERE UPPER(name) LIKE '%"+ strWhere +"%'  AND email_id != '' " +
                "UNION SELECT id, name, email_id FROM account_master WHERE UPPER(email_id) LIKE '%"+ strWhere +"%'  AND email_id != ''";

            memberPickListView.setEnvironment(sql1, memberMasterData, columnHeadings, columnEditableState, returnToComponent, associatedColumn, another_associatedColumn, nextFocusComponent, Size);
            memberPickListView.tableView.autoResizeTableColumns();
        } catch (Exception ex) {
            lb.printToLogFile("Exception at setToPickListView in Email..: ", ex);
        } finally {
            columnEditableState = null;
            returnToComponent = null;
            associatedColumn = null;
            fldList = null;
        }
    }

    private boolean sendMail() {
        configure();
        StringTokenizer std = new StringTokenizer(jtxtTo.getText(), ",");
        String acNames[] = new String[std.countTokens()];
        for (int i = 0; i <= acNames.length-1; i++) {
            acNames[i] = std.nextToken();
        }
        for (int q = 0; q <= acNames.length-1; q++) {
            msg = new EmailMessage(session);
            if(!acNames[q].contains("@")){
                msg.setTo(lb.getEmailId(acNames[q]));
            } else{
                msg.setTo(acNames[q]);    
            }
            msg.setDisplayName(MainClass.ecBean.getManage_email().substring(0, 8));
            //msg.cc = txtCC.getText();
            msg.setCc("");
            //msg.from = cmbFrom.getSelectedItem().toString();
            msg.setFrom(MainClass.ecBean.getManage_email());
            //msg.subject = txtSubject.getText();
            msg.setSubject(jtxtSub.getText());
            //msg.messageText = txtMessage.getText();
            msg.setMessageText(jtxtBody.getText());

            StringTokenizer st = new StringTokenizer(AttachFileDesc.getText(), ";");

            String attachmentFiles[] = new String[st.countTokens()];

            int k = 0;
            for (int i = 0; i < files.size(); i++) {
                File[] f1 = (File[]) files.get(i);
                for (int j = 0; j < f1.length; j++, k++) {
                    attachmentFiles[k] = f1[j].getAbsolutePath();
                }
            }

            msg.setFiles(attachmentFiles);

            msg.sendMessage();
        }
        return true;
    }

    public void configure() {
        try {
            String sql = "SELECT * FROM email_config WHERE id = 1";
            PreparedStatement pstLocal = dataConnection.prepareStatement(sql);
            ResultSet rsLocal = pstLocal.executeQuery();

            if(rsLocal.next()) {
                configuration = new EmailConfiguration();

                configuration.email = (rsLocal.getString("email"));
                configuration.host = (rsLocal.getString("host"));
                configuration.port = (rsLocal.getString("port"));
                configuration.auth = (rsLocal.getString("auth"));
                configuration.smtp_starttls = (rsLocal.getString("smtp"));
                configuration.protocol = (rsLocal.getString("protocol"));
                configuration.socketFactoryPort = (rsLocal.getString("socketFactoryPort"));
                configuration.socketFactoryClass = (rsLocal.getString("socketFactoryClass"));
                configuration.socketFactoryFallback = (rsLocal.getString("socketFactoryFallback"));
                configuration.debug = (rsLocal.getString("debug"));
                configuration.quitwait = (rsLocal.getString("quitwait"));
                configuration.userName = (rsLocal.getString("username"));
                configuration.password = (rsLocal.getString("Pwd"));

                session = configuration.configure();
            }
            if(rsLocal != null) {
                rsLocal.close();
            }
            if(pstLocal != null) {
                pstLocal.close();
            }
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void updateTextFileArea() {
        if(files.size() > 0) {
            AttachFileDesc.setVisible(true);
        }
        AttachFileDesc.setText("");
        for(int i=0; i < files.size(); i++) {
            File[] f1 = (File[])files.elementAt(i);
            for(int j=0; j < f1.length; j++) {
                File tempFile = f1[j];
                AttachFileDesc.setText(AttachFileDesc.getText()+tempFile.getName().toString()+" ; ");
            }
        }
    }

    @Override
    public void dispose() {
        try {
            DeskFrame.removeFromScreen(DeskFrame.tabbedPane.getSelectedIndex());
            super.dispose();
        } catch (Exception ex) {
            lb.printToLogFile("Exception at dispose In Email", ex);
        }
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jbtnAttachFiles = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        AttachFileDesc = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        jtxtBody = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jbtnSend = new javax.swing.JButton();
        jbtnCancel = new javax.swing.JButton();
        jtxtTo = new javax.swing.JTextField();
        jtxtSub = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setClosable(true);

        jbtnAttachFiles.setBackground(new java.awt.Color(204, 255, 204));
        jbtnAttachFiles.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jbtnAttachFiles.setMnemonic('A');
        jbtnAttachFiles.setText("ATTACH FILES");
        jbtnAttachFiles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnAttachFilesActionPerformed(evt);
            }
        });

        AttachFileDesc.setColumns(20);
        AttachFileDesc.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        AttachFileDesc.setLineWrap(true);
        AttachFileDesc.setRows(5);
        jScrollPane3.setViewportView(AttachFileDesc);

        jtxtBody.setColumns(20);
        jtxtBody.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtBody.setRows(5);
        jScrollPane4.setViewportView(jtxtBody);

        jPanel1.setBackground(new java.awt.Color(253, 243, 243));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 3, 3, new java.awt.Color(235, 35, 35)), "E-mail", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 2, 16), new java.awt.Color(0, 0, 0))); // NOI18N

        jbtnSend.setBackground(new java.awt.Color(204, 255, 204));
        jbtnSend.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jbtnSend.setMnemonic('S');
        jbtnSend.setText("SEND");
        jbtnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnSendActionPerformed(evt);
            }
        });

        jbtnCancel.setBackground(new java.awt.Color(204, 255, 204));
        jbtnCancel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jbtnCancel.setMnemonic('C');
        jbtnCancel.setText("CLOSE");
        jbtnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnCancelActionPerformed(evt);
            }
        });

        jtxtTo.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtTo.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 0, 0)));
        jtxtTo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtToFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtxtToFocusLost(evt);
            }
        });
        jtxtTo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtToKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxtToKeyReleased(evt);
            }
        });

        jtxtSub.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jtxtSub.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jtxtSubFocusGained(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("SUBJECT");

        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 0, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("TO");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jbtnSend, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jtxtSub)
                            .addComponent(jtxtTo, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jtxtSub, jtxtTo});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jbtnCancel, jbtnSend});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel3});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbtnSend, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtxtTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtxtSub, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(7, 7, 7))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel1, jLabel3, jtxtSub, jtxtTo});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 563, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jbtnAttachFiles, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnAttachFiles, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jbtnAttachFilesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnAttachFilesActionPerformed
    final JFileChooser jfc = new JFileChooser();
    jfc.setMultiSelectionEnabled(true);
    jfc.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            if(evt.getActionCommand().equalsIgnoreCase("ApproveSelection")) {
                files.addElement(jfc.getSelectedFiles());
            }
        }
    });

    jfc.showOpenDialog(this);
    updateTextFileArea();
}//GEN-LAST:event_jbtnAttachFilesActionPerformed

private void jbtnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSendActionPerformed
    sendMail();
}//GEN-LAST:event_jbtnSendActionPerformed

private void jbtnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnCancelActionPerformed
    cancelOrClose();
}//GEN-LAST:event_jbtnCancelActionPerformed

    private void jtxtToKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtToKeyReleased
        if (jtxtTo.getText().toUpperCase().trim().length() > 0) {
            if ((evt.getKeyCode() != java.awt.event.KeyEvent.VK_DOWN) && (evt.getKeyCode() != java.awt.event.KeyEvent.VK_UP)
                && (evt.getKeyCode() != java.awt.event.KeyEvent.VK_PAGE_DOWN) && (evt.getKeyCode() != java.awt.event.KeyEvent.VK_PAGE_UP)) {
                if (jtxtTo.getText().length() == 1
                    && evt.getKeyCode() != KeyEvent.VK_BACK_SPACE
                    && evt.getKeyCode() != KeyEvent.VK_ENTER) {
                    setToPickListView(jtxtTo.getText().toUpperCase());
                    int x = evt.getComponent().getX()+10;
                    int y = evt.getComponent().getY() + evt.getComponent().getHeight()+12;
                    getLayeredPane().remove(memberPickListView);
                    memberPickListView.setLocation(x, y);
                    memberPickListView.setVisible(true);
                    memberPickListView.repaint();
                    getLayeredPane().add(memberPickListView);
                    memberPickListView.tableView.setTableColumnWidthDefault(jtxtTo.getWidth());
                } else if (evt.getKeyCode() != KeyEvent.VK_ENTER) {
                    setToPickListView(jtxtTo.getText().toUpperCase());
                    int x = evt.getComponent().getX()+10;
                    int y = evt.getComponent().getY() + evt.getComponent().getHeight()+12;
                    getLayeredPane().remove(memberPickListView);
                    memberPickListView.setLocation(x, y);
                    memberPickListView.setVisible(true);
                    memberPickListView.repaint();
                    getLayeredPane().add(memberPickListView);
                    memberPickListView.tableView.setTableColumnWidthDefault(jtxtTo.getWidth());
                }
            }
        } else {
            To = "";
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            ((JTextField) evt.getSource()).getText().toString().toUpperCase();
            if (memberPickListView.isVisible()) {
                memberPickListView.showOff();
                if (!new Validation().isValidDatabaseValue(dataConnection, jtxtTo.getText(), jtxtTo.getName(), false, "account_master", "name", new JLabel(), "")) {
                    memberPickListView.showOffForAnotherColumn();
                    To += jtxtTo.getText()+" ,";
                    jtxtTo.setText(To);
                } else {
                    To += jtxtTo.getText()+" ,";
                    jtxtTo.setText(To);
                }
            } else {
//                jtxtCategory.requestFocusInWindow();
            }
        }
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
            memberPickListView.setVisible(false);
        }
    }//GEN-LAST:event_jtxtToKeyReleased

    private void jtxtToKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtToKeyPressed
        if ((evt.getKeyCode() == java.awt.event.KeyEvent.VK_DOWN) || (evt.getKeyCode() == java.awt.event.KeyEvent.VK_UP)
            || (evt.getKeyCode() == java.awt.event.KeyEvent.VK_PAGE_DOWN) || (evt.getKeyCode() == java.awt.event.KeyEvent.VK_PAGE_UP)) {
            int rowID = memberPickListView.tableView.getSelectedRow();
            int colID = memberPickListView.tableView.getSelectedColumn();

            if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_DOWN) {
                if (memberPickListView.tableView.getRowCount() > 0) {
                    rowID++;
                }
            }
            if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_UP) {
                if (memberPickListView.tableView.getRowCount() > 0) {
                    rowID--;
                }
            }
            if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_PAGE_DOWN) {
                if (memberPickListView.tableView.getRowCount() > 0) {
                    rowID += 10;
                }
            }
            if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_PAGE_UP) {
                if (memberPickListView.tableView.getRowCount() > 0) {
                    rowID -= 10;
                }
            }
            if (rowID < 0) {
                rowID = memberPickListView.tableView.getRowCount() - 1;
            }
            if (rowID >= memberPickListView.tableView.getRowCount()) {
                rowID = 0;
            }
            if (rowID >= 0 && rowID < memberPickListView.tableView.getRowCount()) {
                memberPickListView.tableView.changeSelection(rowID, colID, false, false);
            }
        }
        memberPickListView.setLocation(jtxtTo.getX() + jPanel1.getX(), jtxtTo.getY() + jtxtTo.getHeight() + jPanel1.getY());
    }//GEN-LAST:event_jtxtToKeyPressed

    private void jtxtToFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtToFocusLost
        memberPickListView.setVisible(false);
    }//GEN-LAST:event_jtxtToFocusLost

    private void jtxtToFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtToFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtToFocusGained

    private void jtxtSubFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtxtSubFocusGained
        lb.selectAll(evt);
    }//GEN-LAST:event_jtxtSubFocusGained

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea AttachFileDesc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JButton jbtnAttachFiles;
    private javax.swing.JButton jbtnCancel;
    private javax.swing.JButton jbtnSend;
    private javax.swing.JTextArea jtxtBody;
    private javax.swing.JTextField jtxtSub;
    private javax.swing.JTextField jtxtTo;
    // End of variables declaration//GEN-END:variables
}