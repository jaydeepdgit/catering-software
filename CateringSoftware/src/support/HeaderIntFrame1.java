/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.RowFilter;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import master.AccountMaster;
import master.AccountType;
import master.TaxMaster;
import master.UnitMaster;
import cateringsoftware.DeskFrame;
import master.RawMainCategory;
import master.RawSubCategory;
import utility.CheckPrint;

/**
 *
 * @author @JD@
 */
public class HeaderIntFrame1 extends JInternalFrame {
    public ResultSet viewRSHeader = null;
    private DefaultTableModel dtmHeader = null;
    private int[] setcolHeader;
    private ReportTable rptHeader = null;
    private Library lb = new Library();
    private String strHeader = "";
    private Connection dataConnection;
    private String strCode = "";
    private int[] DateColumn = {-1};
    private int selDateColumn = -1;
    private String docCd = "";
    private int iColNo = 1;
    private Object form;
    private KeyListener keyListen;
    private boolean direcMode = false;
    private String returnTitle = "";
    private JTextField jtfFilter = new JTextField();

    /**
     * Creates new form HeaderIntFrame
     */
    String Syspath = System.getProperty("user.dir");

    public HeaderIntFrame1(Connection con, String strCode, String titleName, ReportTable rptHeader, String strHeader, String docCd, int iColNo, Object form, String returnTitle) {
        initComponents();
        setTitle(titleName);
        dataConnection = con;
        this.strHeader = strHeader;
        this.docCd = docCd;
        this.strCode = strCode;
        this.rptHeader = rptHeader;
        this.iColNo = iColNo;
        this.form = form;
        this.returnTitle = returnTitle;
        setIconToPnael();
    }

    private void setIconToPnael() {
        Syspath += File.separator + "Resources" + File.separator + "Images" + File.separator;
        jbtnViewResult.setIcon(new ImageIcon(Syspath + "view.png"));
        jbtnClose.setIcon(new ImageIcon(Syspath + "close.png"));
    }

    private void setValueToVoucher() {
        if (docCd.equalsIgnoreCase(Constants.TAX_MASTER_FORM_ID)) {
            ((TaxMaster) form).setId(strCode);
        } else if (docCd.equalsIgnoreCase(Constants.ACCOUNT_TYPE_FORM_ID)) {
            ((AccountType) form).setID(strCode);
        } else if (docCd.equalsIgnoreCase(Constants.ACCOUNT_MASTER_FORM_ID)) {
            ((AccountMaster) form).setID(strCode);
        } else if (docCd.equalsIgnoreCase(Constants.UNIT_MASTER_FORM_ID)) {
            ((UnitMaster) form).setID(Integer.parseInt(strCode));
        } else if (docCd.equalsIgnoreCase(Constants.CHECK_PRINT_FORM_ID)) {
            ((CheckPrint) form).setID(strCode);
        } else if (docCd.equalsIgnoreCase(Constants.MAIN_CATEGORY_RW_FORM_ID)) {
            ((RawMainCategory) form).setID(strCode);
        } else if (docCd.equalsIgnoreCase(Constants.SUB_CATEGORY_RW_FORM_ID)) {
            ((RawSubCategory) form).setID(strCode);
        }
    }

    @Override
    public void dispose() {
        try {
            DeskFrame.removeFromScreen(DeskFrame.tabbedPane.getSelectedIndex(), returnTitle);
            super.dispose();
        } catch (Exception ex) {
            lb.printToLogFile("Exception at dispose In Header Int Frame1", ex);
        }
    }

    public void makeView() {
        setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));

        SwingWorker swingWorkerForViewProperty = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                try {
                    makeViewRoutine();
                    selectRecord();
                } catch(Exception ex) {
                    lb.printToLogFile("Exception at HeaderIntFrame in makeView", ex);
                }

                setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

                return null;
            }
        };
        swingWorkerForViewProperty.execute();
    }

    private void makeViewRoutine() {
        rptHeader.setEnabled(false);
        jbtnViewResult.setEnabled(false);

        setcolHeader = rptHeader.getColumnValue();
        dtmHeader = (DefaultTableModel) rptHeader.getModel();

        jPanelHeader.removeAll();
        rptHeader.addTable(jPanelHeader);

        searchOnTextFields();
        if (direcMode) {
            fillTableHeaderDirect();
        } else {
            fillTableHeader();
        }

        headerTableListener();
        setLayer(1);

        InputMap im = rptHeader.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "none");

        rptHeader.setEnabled(true);
        jbtnViewResult.setEnabled(true);
    }

    private void fillTableHeader() {
        try {
            PreparedStatement pst = dataConnection.prepareStatement(strHeader, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            viewRSHeader = pst.executeQuery();
            Vector newrow = null;
            lb.RemoveRows(dtmHeader);

            while (viewRSHeader.next()) {
                newrow = new Vector();
                for (int i = 0; i < setcolHeader.length; i++) {
                    if (viewRSHeader.getMetaData().getColumnType(setcolHeader[i]) == Types.DATE) {
                        if (DateColumn[0] == -1) {
                            selDateColumn = i;
                        }
                        newrow.add(lb.ConvertDateFormetForDBForConcurrency(viewRSHeader.getString(setcolHeader[i])));
                    } else if (viewRSHeader.getMetaData().getColumnType(setcolHeader[i]) == Types.DOUBLE) {
                        newrow.add(viewRSHeader.getDouble(setcolHeader[i]));
                    } else {
                        newrow.add(viewRSHeader.getObject(setcolHeader[i]));
                    }
                }
                dtmHeader.addRow(newrow);
            }
            System.gc();
            viewRSHeader.beforeFirst();
        } catch (Exception ex) {
            lb.printToLogFile("Exception at fillTableHeader in HeaderIntFrame1..!!", ex);
        }
    }

    private void fillTableHeaderDirect() {
        try {
            Vector newrow = null;
            lb.RemoveRows(dtmHeader);

            while (viewRSHeader.next()) {
                newrow = new Vector();
                for (int i = 0; i < setcolHeader.length; i++) {
                    if (viewRSHeader.getMetaData().getColumnType(setcolHeader[i]) == Types.DATE) {
                        if (DateColumn[0] == -1) {
                            selDateColumn = i;
                        }
                        newrow.add(viewRSHeader.getDate(setcolHeader[i]));
                    } else if (viewRSHeader.getMetaData().getColumnType(setcolHeader[i]) == Types.DOUBLE) {
                        newrow.add(viewRSHeader.getDouble(setcolHeader[i]));
                    } else {
                        newrow.add(viewRSHeader.getObject(setcolHeader[i]));
                    }
                }
                dtmHeader.addRow(newrow);
            }
            System.gc();
            viewRSHeader.beforeFirst();
        } catch (Exception ex) {
            lb.printToLogFile("Exception at fillTableHeader in HeaderIntFrame1..!!", ex);
        }
    }

    private void searchOnTextFields() {
        final TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(rptHeader.getModel());
        rptHeader.setRowSorter(rowSorter);
        panel.add(new JLabel("Specify a word to match:"), BorderLayout.WEST);
        jtfFilter.setFont(new Font("Cambria", 1, 12));
        panel.add(jtfFilter, BorderLayout.CENTER);

        jtfFilter.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = jtfFilter.getText();

                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = jtfFilter.getText();

                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }

    public void selectRecord() {
        try {
            rptHeader.requestFocusInWindow();
            viewRSHeader.beforeFirst();
            while (viewRSHeader.next()) {
                if (viewRSHeader.getString(iColNo).equalsIgnoreCase(strCode)) {
                    break;
                }
            }
            rptHeader.setRowSelectionInterval(0, 0);
            setViewPort(rptHeader, 0, 0);
        } catch (Exception ex) {
            lb.printToLogFile("Exception at selectRecord in HeaderIntFrame1..!!", ex);
        } finally {
            lb.closeResultSet(viewRSHeader);
        }
    }

    private void setViewPort(JTable tableViewOnViewAndSearchPanel, int iCol, int iTheCol) {
        try {
            tableViewOnViewAndSearchPanel.setRowSelectionAllowed(true);
            tableViewOnViewAndSearchPanel.setRowSelectionInterval(iCol, iCol);
            tableViewOnViewAndSearchPanel.changeSelection(iCol, iTheCol, false, false);
            tableViewOnViewAndSearchPanel.setFont(new Font("Cambria", 1, 12));

            // Get the row at First
            JViewport viewport = (JViewport) tableViewOnViewAndSearchPanel.getParent();
            Rectangle rect = tableViewOnViewAndSearchPanel.getCellRect(iCol, iTheCol, true);
            Rectangle viewRect = viewport.getViewRect();
            rect.setLocation(rect.x - viewRect.x, rect.y - viewRect.y);
            int centerX = (viewRect.width - rect.width);
            int centerY = (viewRect.height - rect.height);
            rect.translate(centerX, centerY);
            viewport.scrollRectToVisible(rect);
        } catch(Exception ex) {
            lb.printToLogFile("Exception at setViewPort in HeaderIntFrame", ex);
        }
    }

    private void setAlias(String strAlias) {
        try {
            strCode = strAlias;
        } catch (Exception ex) {
            lb.printToLogFile("Exception at setAlias in HeaderIntFrame..!!", ex);
        }
    }

    private void setAliasRoutine() {
        if (rptHeader.getSelectedRow() != -1) {
            Object tableValue = rptHeader.getValueAt(rptHeader.getSelectedRow(), (iColNo - 1));
            if (tableValue != null) {
                setAlias(tableValue.toString().trim());
            }
        }
    }

    private void headerTableListener() {
        rptHeader.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                final MouseEvent tempE = e;
                if (tempE.getClickCount() >= 1) {
                    setAlias(rptHeader.getValueAt(rptHeader.getSelectedRow(), (iColNo-1)).toString().trim());
                    setAliasRoutine();
                }
                setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            }
            public void mouseClicked(MouseEvent e) {
                final MouseEvent tempE = e;
                if (tempE.getClickCount() == 2) {
                    jbtnViewResultActionPerformedRoutine();
                }
                setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            }
        });

        keyListen = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                final KeyEvent tempE = e;
                rptHeaderkeyPressedRoutine(tempE);
                setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            }

            private void rptHeaderkeyPressedRoutine(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && e.getID() == KeyEvent.KEY_RELEASED) {
                    jbtnViewResultActionPerformedRoutine();
                }
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN
                        || e.getKeyCode() == KeyEvent.VK_PAGE_UP || e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
                    setAliasRoutine();
                }
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE && e.getID() == KeyEvent.KEY_RELEASED) {
                    jbtnClose.doClick();
                }
            }

            public void keyTyped(KeyEvent e) {
            }
        };
        rptHeader.addKeyListener(keyListen);
    }

    private void jbtnViewResultActionPerformedRoutine() {
        setValueToVoucher();
        rptHeader.removeKeyListener(keyListen);
        this.dispose();
        JInternalFrame inFram = (JInternalFrame) form;
        int index = DeskFrame.checkAlradyOpen(inFram.getTitle());
        if(index == -1) {
            DeskFrame.addOnScreen((JInternalFrame) form, returnTitle);
        } else {
            DeskFrame.tabbedPane.setSelectedIndex(index);
        }
    }

    private void jbtnCloseActionPerformedRoutine() {
        this.dispose();
        DeskFrame.addOnScreen((JInternalFrame) form, returnTitle);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelHeader = new javax.swing.JPanel();
        jbtnViewResult = new javax.swing.JButton();
        jbtnClose = new javax.swing.JButton();
        panel = new javax.swing.JPanel();

        setPreferredSize(new java.awt.Dimension(950, 621));

        jPanelHeader.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(4, 110, 152), 2));
        jPanelHeader.setPreferredSize(new java.awt.Dimension(750, 420));
        jPanelHeader.setLayout(new javax.swing.BoxLayout(jPanelHeader, javax.swing.BoxLayout.LINE_AXIS));

        jbtnViewResult.setBackground(new java.awt.Color(204, 255, 204));
        jbtnViewResult.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jbtnViewResult.setMnemonic('V');
        jbtnViewResult.setText("View Form");
        jbtnViewResult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnViewResultActionPerformed(evt);
            }
        });

        jbtnClose.setBackground(new java.awt.Color(204, 255, 204));
        jbtnClose.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jbtnClose.setMnemonic('C');
        jbtnClose.setText("Back");
        jbtnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnCloseActionPerformed(evt);
            }
        });

        panel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, 914, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jPanelHeader, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 914, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jbtnViewResult)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jbtnClose)))
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(47, 47, 47)
                    .addComponent(jPanelHeader, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jbtnClose)
                        .addComponent(jbtnViewResult))
                    .addContainerGap()))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnViewResultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnViewResultActionPerformed
        jbtnViewResultActionPerformedRoutine();
}//GEN-LAST:event_jbtnViewResultActionPerformed

    private void jbtnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnCloseActionPerformed
        setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        jbtnCloseActionPerformedRoutine();
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
}//GEN-LAST:event_jbtnCloseActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanelHeader;
    private javax.swing.JButton jbtnClose;
    private javax.swing.JButton jbtnViewResult;
    private javax.swing.JPanel panel;
    // End of variables declaration//GEN-END:variables
}