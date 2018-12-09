/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cateringsoftware;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import static cateringsoftware.DeskFrame.checkAlradyOpen;
import static cateringsoftware.DeskFrame.tabbedPane;
import support.Constants;
import support.Library;

/**
 *
 * @author @JD@
 */
public class HomePage extends javax.swing.JInternalFrame {
    Library lb = DeskFrame.lb;
    Connection dataConnection = null;
    public static String dbYear = "", month = "";
    DefaultTableModel salesBill = null, purBill = null, lastPaymnt = null;
    DeskFrame jhome = null;
    DefaultMutableTreeNode root;
    TreePath changePath;
    public static JLabel jlblDate = new JLabel();
    String Syspath = System.getProperty("user.dir");
    private JMenuItem newcmpny = null;

    /**
     * Creates new form HomePage
     */
    public HomePage(DeskFrame jwHome) {
        initComponents();
        salesBill = (DefaultTableModel) jTableTaxInvoice.getModel();
        purBill = (DefaultTableModel) jTablePurchaseBill.getModel();
        lastPaymnt = (DefaultTableModel) jTableLastPayment.getModel();
        this.dataConnection = DeskFrame.connMpAdmin;
        jTableTaxInvoice.setBackground(new Color(253, 243, 243));
        jTablePurchaseBill.setBackground(new Color(253, 243, 243));
        jTableLastPayment.setBackground(new Color(253, 243, 243));
        setIconToPnael();
        this.jhome = jwHome;
        sideBarMenu();
        setShortcut();
        setData();
        jlblSoftware.setText(Constants.SOFTWARE_NAME);
        setTitle(Constants.HOME_PAGE);
    }

    private void setData() {

    }

    private void setShortcut(){
        newcmpny = new javax.swing.JMenuItem();
        newcmpny.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        newcmpny.setText("Authentication");
        newcmpny.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createNewCmpny();
            }
        });
        newcmpny.setVisible(true);
        this.add(newcmpny);
    }

    private void createNewCmpny() {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Enter a Password");
        final JPasswordField pass = new JPasswordField(10);

        KeyListener key = new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if(evt.getKeyCode()== KeyEvent.VK_ENTER) {
                    evt.consume();
                    if(evt.getSource() == pass) {
                        lb.keyPress(KeyEvent.VK_TAB);
                    }
                }
            }
        };

        pass.addKeyListener(key);
        panel.add(label);
        panel.add(pass);
        String[] options = new String[]{"OK", "Cancel"};
        int option = JOptionPane.showOptionDialog(null, panel, "Authentication",
            JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, pass);
        if (option == 0) { // pressing OK button
            char[] password = pass.getPassword();

            if(new String(password).equals("india123")) {
                int index = checkAlradyOpen(Constants.NEW_COMPANY_FORM_NAME);
                if(index == -1) {
//                    NewCompany nc = new NewCompany(MainClass.df, true);
//                    nc.show();
                } else {
                    tabbedPane.setSelectedIndex(index);
                }
            }
        }
        pass.requestFocusInWindow();
    }

    private void sideBarMenu() {
        try {
            root = new DefaultMutableTreeNode("PROGRAM EXPLORER", true);
            jTree1.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
            jTree1.setModel(new DefaultTreeModel(root));
            // MASTER
            DefaultMutableTreeNode child1 = new DefaultMutableTreeNode(Constants.MASTER, true);
            root.add(child1);
            // SUB MASTER
            DefaultMutableTreeNode parent1 = (DefaultMutableTreeNode) child1;
            if (MainClass.df.hasPermission(Constants.TAX_MASTER_FORM_ID)) {
                DefaultMutableTreeNode child11 = new DefaultMutableTreeNode(Constants.TAX_MASTER_FORM_NAME, true);
                parent1.add(child11);
            }
            if (MainClass.df.hasPermission(Constants.ACCOUNT_TYPE_FORM_ID)) {
                DefaultMutableTreeNode child12 = new DefaultMutableTreeNode(Constants.ACCOUNT_TYPE_FORM_NAME, true);
                parent1.add(child12);
            }
            if (MainClass.df.hasPermission(Constants.ACCOUNT_MASTER_FORM_ID)) {
                DefaultMutableTreeNode child13 = new DefaultMutableTreeNode(Constants.ACCOUNT_MASTER_FORM_NAME, true);
                parent1.add(child13);
            }
            if (MainClass.df.hasPermission(Constants.UNIT_MASTER_FORM_ID)) {
                DefaultMutableTreeNode child14 = new DefaultMutableTreeNode(Constants.UNIT_MASTER_FORM_NAME, true);
                parent1.add(child14);
            }
            if (MainClass.df.hasPermission(Constants.BANK_MASTER_FORM_ID)) {
                DefaultMutableTreeNode child17 = new DefaultMutableTreeNode(Constants.BANK_MASTER_FORM_NAME, true);
                parent1.add(child17);
            }

            // REPORTS
            DefaultMutableTreeNode child4 = new DefaultMutableTreeNode(Constants.REPORTS, true);
            root.add(child4);
            // SUB REPORTS
            DefaultMutableTreeNode parent4 = (DefaultMutableTreeNode) child4;
//            if (MainClass.df.hasPermission(Constants.TAX_INVOICE_REPORT_FORM_ID)) {
//                DefaultMutableTreeNode child41 = new DefaultMutableTreeNode(Constants.TAX_INVOICE_REPORT_FORM_NAME, true);
//                parent4.add(child41);
//            }

            // UTILITY
            DefaultMutableTreeNode child6 = new DefaultMutableTreeNode(Constants.UTILITY, true);
            root.add(child6);
            DefaultMutableTreeNode parent6 = (DefaultMutableTreeNode) child6;
            if (MainClass.df.hasPermission(Constants.COMPANY_SETTING_FORM_ID)) {
                DefaultMutableTreeNode child61 = new DefaultMutableTreeNode(Constants.COMPANY_SETTING_FORM_NAME, true);
                parent6.add(child61);
            }
            if (MainClass.df.hasPermission(Constants.MANAGE_USER_FORM_ID)) {
                DefaultMutableTreeNode child62 = new DefaultMutableTreeNode(Constants.MANAGE_USER_FORM_NAME, true);
                parent6.add(child62);
            }
            if (MainClass.df.hasPermission(Constants.USER_RIGHTS_FORM_ID)) {
                DefaultMutableTreeNode child63 = new DefaultMutableTreeNode(Constants.USER_RIGHTS_FORM_NAME, true);
                parent6.add(child63);
            }
            if (MainClass.df.hasPermission(Constants.MANAGE_EMAIL_FORM_ID)) {
                DefaultMutableTreeNode child64 = new DefaultMutableTreeNode(Constants.MANAGE_EMAIL_FORM_NAME, true);
                parent6.add(child64);
            }
            if (MainClass.df.hasPermission(Constants.CHANGE_PASSWORD_FORM_ID)) {
                DefaultMutableTreeNode child65 = new DefaultMutableTreeNode(Constants.CHANGE_PASSWORD_FORM_NAME, true);
                parent6.add(child65);
            }
            DefaultMutableTreeNode child66 = new DefaultMutableTreeNode(Constants.CHANGE_DATE_FORM_NAME, true);
            parent6.add(child66);
            if (MainClass.df.hasPermission(Constants.QUICK_OPEN_FORM_ID)) {
                DefaultMutableTreeNode child67 = new DefaultMutableTreeNode(Constants.QUICK_OPEN_FORM_NAME, true);
                parent6.add(child67);
            }
            if (MainClass.df.hasPermission(Constants.BACK_UP_FORM_ID)) {
                DefaultMutableTreeNode child68 = new DefaultMutableTreeNode(Constants.BACK_UP_FORM_NAME, true);
                parent6.add(child68);
            }
            if (MainClass.df.hasPermission(Constants.RESET_FORM_ID)) {
                DefaultMutableTreeNode child69 = new DefaultMutableTreeNode(Constants.RESET_FORM_NAME, true);
                parent6.add(child69);
            }
            if (MainClass.df.hasPermission(Constants.EMAIL_FORM_ID)) {
                DefaultMutableTreeNode child70 = new DefaultMutableTreeNode(Constants.EMAIL_FORM_NAME, true);
                parent6.add(child70);
            }
            if (MainClass.df.hasPermission(Constants.CHECK_PRINT_FORM_ID)) {
                DefaultMutableTreeNode child612 = new DefaultMutableTreeNode(Constants.CHECK_PRINT_FORM_NAME, true);
                parent6.add(child612);
            }
            if (MainClass.df.hasPermission(Constants.NEW_YEAR_FORM_ID)) {
                DefaultMutableTreeNode child612 = new DefaultMutableTreeNode(Constants.NEW_YEAR_FORM_NAME, true);
                parent6.add(child612);
            }
        } catch (Exception ex) {
            lb.printToLogFile("Error at sideBarMenu In Home Page", ex);
        }
    }

    public void setToolBar(String user_id, String year) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        jlblDate.setText(sdf.format(cal.getTime()));
    }

    private void setIconToPnael() {
        Syspath += File.separator + "Resources" + File.separator + "Images" + File.separator;
        jlblLogo.setIcon(new ImageIcon(Syspath + "pwr_logo.png"));
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel6 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableTaxInvoice = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jlblSoftware = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jlblLogo = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTablePurchaseBill = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableLastPayment = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jbtnItemStock = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();

        setBackground(new java.awt.Color(201, 212, 216));
        setPreferredSize(new java.awt.Dimension(972, 623));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jPanel6.setBackground(new java.awt.Color(174, 203, 228));
        jPanel6.setBorder(javax.swing.BorderFactory.createMatteBorder(3, 3, 3, 3, new java.awt.Color(235, 35, 35)));

        jPanel4.setBackground(new java.awt.Color(174, 203, 228));
        jPanel4.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(235, 35, 35)));

        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 0, new java.awt.Color(235, 35, 35)));

        jTableTaxInvoice.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jTableTaxInvoice.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SR. NO.", "INVOICE NO", "INVOICE DATE", "CUSTOMER NAME", "NET AMOUNT"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableTaxInvoice.setRowHeight(23);
        jTableTaxInvoice.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableTaxInvoiceMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTableTaxInvoice);
        jTableTaxInvoice.getColumnModel().getColumn(0).setMinWidth(70);
        jTableTaxInvoice.getColumnModel().getColumn(0).setPreferredWidth(70);
        jTableTaxInvoice.getColumnModel().getColumn(0).setMaxWidth(70);
        jTableTaxInvoice.getColumnModel().getColumn(1).setMinWidth(80);
        jTableTaxInvoice.getColumnModel().getColumn(1).setPreferredWidth(80);
        jTableTaxInvoice.getColumnModel().getColumn(1).setMaxWidth(80);
        jTableTaxInvoice.getColumnModel().getColumn(2).setMinWidth(90);
        jTableTaxInvoice.getColumnModel().getColumn(2).setPreferredWidth(90);
        jTableTaxInvoice.getColumnModel().getColumn(2).setMaxWidth(90);
        jTableTaxInvoice.getColumnModel().getColumn(4).setMinWidth(110);
        jTableTaxInvoice.getColumnModel().getColumn(4).setPreferredWidth(110);
        jTableTaxInvoice.getColumnModel().getColumn(4).setMaxWidth(110);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(235, 35, 35));
        jLabel1.setText(" TAX / RETAIL INVOICE");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(204, 102, 255));
        jLabel2.setText("Note : First Select Invoice No and Click on Print Bill");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 698, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 3, 1, new java.awt.Color(153, 153, 153)));

        jlblSoftware.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jlblSoftware.setForeground(new java.awt.Color(235, 35, 35));
        jlblSoftware.setText("CATERING MANAGEMENT");
        jlblSoftware.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 3, new java.awt.Color(153, 153, 153)));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(235, 35, 35));
        jLabel4.setText("Computerized Billing System");
        jLabel4.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(153, 153, 153)));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 153, 0));
        jLabel5.setText("<html><b style='color:red;'>&#174;</b>Version : <br />&nbsp;&nbsp;&nbsp;&nbsp; 1.0.0</html>");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jlblSoftware, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlblSoftware, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createMatteBorder(3, 0, 0, 0, new java.awt.Color(235, 35, 35)));

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("<html>&copy; 2018 CATERING MANAGEMENT All Rights Reserved.</html>");

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setText("<html><body><span>Developed By </span></body></html>");

        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel45.setText("<html><b>HELP LINE NO. : <strong style = 'font-size : 14px'>+919727397009 / +917405116442</strong></b><br /><b style='color:black;'>E-mail : </b><b style='color:red;'>dhameliya.jaydeep@gmail.com / info@powertechitsolution.net</b></html>");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel45, javax.swing.GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jlblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 0, new java.awt.Color(235, 35, 35)));

        jTablePurchaseBill.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jTablePurchaseBill.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SR. NO.", "INVOICE NO", "INVOICE DATE", "CUSTOMER NAME", "NET AMOUNT"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTablePurchaseBill.setRowHeight(23);
        jTablePurchaseBill.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTablePurchaseBillMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTablePurchaseBill);
        jTablePurchaseBill.getColumnModel().getColumn(0).setMinWidth(70);
        jTablePurchaseBill.getColumnModel().getColumn(0).setPreferredWidth(70);
        jTablePurchaseBill.getColumnModel().getColumn(0).setMaxWidth(70);
        jTablePurchaseBill.getColumnModel().getColumn(1).setMinWidth(80);
        jTablePurchaseBill.getColumnModel().getColumn(1).setPreferredWidth(80);
        jTablePurchaseBill.getColumnModel().getColumn(1).setMaxWidth(80);
        jTablePurchaseBill.getColumnModel().getColumn(2).setMinWidth(90);
        jTablePurchaseBill.getColumnModel().getColumn(2).setPreferredWidth(90);
        jTablePurchaseBill.getColumnModel().getColumn(2).setMaxWidth(90);
        jTablePurchaseBill.getColumnModel().getColumn(4).setMinWidth(110);
        jTablePurchaseBill.getColumnModel().getColumn(4).setPreferredWidth(110);
        jTablePurchaseBill.getColumnModel().getColumn(4).setMaxWidth(110);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(235, 35, 35));
        jLabel6.setText(" PURCHASE BILL");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(204, 102, 255));
        jLabel7.setText("Note : First Select Invoice No and Click on Print Bill");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 698, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel8.setBackground(new java.awt.Color(255, 204, 204));
        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(235, 35, 35));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("TODAY'S BILLING");
        jLabel8.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 0, new java.awt.Color(235, 35, 35)));

        jPanel7.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 0, new java.awt.Color(235, 35, 35)));

        jTableLastPayment.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        jTableLastPayment.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SR. NO.", "INVOICE NO", "INVOICE DATE", "LAST PAYMENT DATE", "CUSTOMER NAME", "NET AMOUNT"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableLastPayment.setRowHeight(23);
        jTableLastPayment.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableLastPaymentMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTableLastPayment);
        jTableLastPayment.getColumnModel().getColumn(0).setMinWidth(70);
        jTableLastPayment.getColumnModel().getColumn(0).setPreferredWidth(70);
        jTableLastPayment.getColumnModel().getColumn(0).setMaxWidth(70);
        jTableLastPayment.getColumnModel().getColumn(1).setMinWidth(80);
        jTableLastPayment.getColumnModel().getColumn(1).setPreferredWidth(80);
        jTableLastPayment.getColumnModel().getColumn(1).setMaxWidth(80);
        jTableLastPayment.getColumnModel().getColumn(2).setMinWidth(90);
        jTableLastPayment.getColumnModel().getColumn(2).setPreferredWidth(90);
        jTableLastPayment.getColumnModel().getColumn(2).setMaxWidth(90);
        jTableLastPayment.getColumnModel().getColumn(3).setMinWidth(130);
        jTableLastPayment.getColumnModel().getColumn(3).setPreferredWidth(130);
        jTableLastPayment.getColumnModel().getColumn(3).setMaxWidth(130);
        jTableLastPayment.getColumnModel().getColumn(5).setMinWidth(110);
        jTableLastPayment.getColumnModel().getColumn(5).setPreferredWidth(110);
        jTableLastPayment.getColumnModel().getColumn(5).setMaxWidth(110);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(235, 35, 35));
        jLabel9.setText(" LAST PAYMENT");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(204, 102, 255));
        jLabel10.setText("Note : First Select Invoice No and Click on Print Bill");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 698, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(0, 0, 0)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jbtnItemStock.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jbtnItemStock.setText("ITEM STOCK");
        jbtnItemStock.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 3, 1, new java.awt.Color(235, 35, 35)));
        jbtnItemStock.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jbtnItemStockMouseClicked(evt);
            }
        });
        jbtnItemStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnItemStockActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 568, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jbtnItemStock, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnItemStock))
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel8, jbtnItemStock});

        jScrollPane2.setBackground(new java.awt.Color(253, 243, 243));

        jTree1.setBackground(new java.awt.Color(253, 243, 243));
        jTree1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 3, new java.awt.Color(235, 35, 35)));
        jTree1.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("System");
        jTree1.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTree1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTree1MouseClicked(evt);
            }
        });
        jTree1.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTree1ValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jTree1);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTree1ValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jTree1ValueChanged
        changePath = evt.getPath();
    }//GEN-LAST:event_jTree1ValueChanged

    private void jTree1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree1MouseClicked
        if(evt.getClickCount() == 2) {
            DefaultMutableTreeNode selectedNode = new DefaultMutableTreeNode(changePath.getLastPathComponent());

            if (selectedNode.isLeaf()) {
                lb.openForm(selectedNode.toString());
            }
        }
        if(evt.getClickCount() == 1) {
            setData();
        }
    }//GEN-LAST:event_jTree1MouseClicked

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        if(evt.getClickCount() == 1) {
            setData();
        }
    }//GEN-LAST:event_formMouseClicked

    private void jTableTaxInvoiceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableTaxInvoiceMouseClicked
        if(evt.getClickCount() == 2) {
            int row = jTableTaxInvoice.getSelectedRow();
            lb.quickOpen(jTableTaxInvoice.getValueAt(row, 1).toString());
        }
    }//GEN-LAST:event_jTableTaxInvoiceMouseClicked

    private void jTablePurchaseBillMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTablePurchaseBillMouseClicked
        if(evt.getClickCount() == 2) {
            int row = jTablePurchaseBill.getSelectedRow();
            lb.quickOpen(jTablePurchaseBill.getValueAt(row, 1).toString());
        }
    }//GEN-LAST:event_jTablePurchaseBillMouseClicked

    private void jTableLastPaymentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableLastPaymentMouseClicked
        if(evt.getClickCount() == 2) {
            int row = jTableLastPayment.getSelectedRow();
            lb.quickOpen(jTableLastPayment.getValueAt(row, 1).toString());
        }
    }//GEN-LAST:event_jTableLastPaymentMouseClicked

    private void jbtnItemStockMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbtnItemStockMouseClicked
        if(evt.getClickCount() == 1) {
            jbtnItemStock.doClick();
        }
    }//GEN-LAST:event_jbtnItemStockMouseClicked

    private void jbtnItemStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnItemStockActionPerformed
//        OpenItemStock glp = new OpenItemStock(null, true);
//        glp.getData("", this);
//        glp.jTable1.requestFocusInWindow();
//        glp.setLocation(jlblSoftware.getX(), jlblSoftware.getY() + jlblSoftware.getHeight());
//
//        glp.show();
    }//GEN-LAST:event_jbtnItemStockActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTableLastPayment;
    private javax.swing.JTable jTablePurchaseBill;
    private javax.swing.JTable jTableTaxInvoice;
    private javax.swing.JTree jTree1;
    private javax.swing.JButton jbtnItemStock;
    private javax.swing.JLabel jlblLogo;
    private javax.swing.JLabel jlblSoftware;
    // End of variables declaration//GEN-END:variables
}