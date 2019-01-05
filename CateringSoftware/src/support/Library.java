/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

import cateringsoftware.CursorGlassPane;
import com.sun.rowset.CachedRowSetImpl;
import cateringsoftware.DeskFrame;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.sql.rowset.CachedRowSet;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRootPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.JTextComponent;
import master.AccountMaster;
import master.BankMaster;
import master.AccountType;
import master.TaxMaster;
import master.UnitMaster;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.swing.JRViewer;
import static cateringsoftware.DeskFrame.DELETE_PWD;
import static cateringsoftware.DeskFrame.addOnScreen;
import static cateringsoftware.DeskFrame.checkAlradyOpen;
import static cateringsoftware.DeskFrame.tabbedPane;
import cateringsoftware.MainClass;
import javax.swing.JComboBox;
import utility.BackUp;
import utility.ChangePassword;
import utility.ChangeThemes;
import utility.CheckPrint;
import utility.CompanySetting;
import utility.DateSetting;
import utility.Email;
import utility.ManageEmail;
import utility.ManageUserView;
import utility.QuickOpen;
import utility.Reset;
import utility.UserRights;

/**
 *
 * @author @JD@
 */
public class Library {
    Connection dataConnection = null;
    public ResultSet viewDataRs = null;
    private Robot robotVar = null;
    public String f, m, l;
    BodyPart htmlPart2 = new MimeBodyPart();
    BodyPart htmlPart3 = new MimeBodyPart();
    String message1 = null;
    String msg2;
    public SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
    public SimpleDateFormat userFormat = new SimpleDateFormat("dd/MM/yyyy");
    public SimpleDateFormat timestamp = new SimpleDateFormat("dd-MM-yyyy hh:mm:SS a");
    public Robot robot;
    public boolean type;
    CursorGlassPane glassPane = new CursorGlassPane();
    Component oldGlass = null;
    private int[] DateColumn = {-1};
    private int selDateColumn = -1;

    {
        try {
            robotVar = new Robot();
        } catch (Exception ex) {
            printToLogFile("Exception at initial block in Library", ex);
        }
    }

    public Library() {
        dataConnection = DeskFrame.connMpAdmin;
    }

    public void addGlassPane(SmallNavigation1 navLoad) {
        Component c = navLoad.getParent();
        while (!(c instanceof JInternalFrame)) {
            c = c.getParent();
        }
        if (!(((JInternalFrame) c).getGlassPane() instanceof CursorGlassPane)) {
            // If antother Glass Pane is set for this form then save it to restore it after saving..
            oldGlass = ((JInternalFrame) c).getGlassPane();
            ((JInternalFrame) c).setGlassPane(glassPane);
        }
        ((JInternalFrame) c).getGlassPane().setVisible(true);
        ((JInternalFrame) c).getGlassPane().requestFocusInWindow();

        navLoad.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
    }

    public void removeGlassPane(SmallNavigation1 navLoad) {
        Component c = navLoad.getParent();
        while (!(c instanceof JInternalFrame)) {
            c = c.getParent();
        }
        navLoad.requestFocusInWindow();
        ((JInternalFrame) c).getGlassPane().setVisible(false);
        if (oldGlass != null) {
            ((JInternalFrame) c).setGlassPane(oldGlass);
        }
        navLoad.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }

    public void selectRowShortcut(JInternalFrame form, final JTable table, final JComponent comp) {
        KeyStroke closeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_MASK, false);
        Action closeKeyAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getRowCount();
                int selRow = table.getSelectedRow();
                if (row != -1 && selRow == -1) {
                    table.clearSelection();
                    table.requestFocusInWindow();
                    table.setRowSelectionInterval(0, 0);
                } else {
                    table.clearSelection();
                    comp.requestFocusInWindow();
                }
            }
        };
        form.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(closeKeyStroke, "Table");
        form.getActionMap().put("Table", closeKeyAction);
    }

    public void setDateChooserPropertyInit(datechooser.beans.DateChooserCombo jcmbDate) {
        jcmbDate.setDateFormat(getDateFormat(new SimpleDateFormat("dd/MM/yyyy")));
        Font font = new Font("Cambria", 1, 12);
        jcmbDate.setFont(font);
    }

    public void keyPress(int code) {
        robotVar.keyPress(code);
    }

    public void toUniform(java.awt.event.FocusEvent evt) {
        if (evt.getSource() instanceof JTextField) {
            JTextField txt = ((JTextField) evt.getSource());
            toUniform(txt);
        }
    }

    public boolean validateUser(String strUser, String strPwd) {
        boolean flag = false;
        try {
            PreparedStatement pst = dataConnection.prepareStatement("SELECT * FROM user_master WHERE username = '"+ strUser +"' "
                    + "AND password='"+ strPwd +"' AND fk_status_id = 1");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                DeskFrame.user_id = rs.getInt("id");
                flag = true;
            } else {
                flag = false;
            }
            closeResultSet(rs);
            closeStatement(pst);
        } catch (Exception ex) {
            printToLogFile("Exception at validateUser In Library", ex);
        }
        return flag;
    }

    public String getAccountCode(String name, String id) {
        String ans = "";
        if (name == null) {
            return "";
        }
        try {
            String sql = "SELECT * FROM account_master WHERE name = '"+ name +"' AND id <> '"+ id +"'";
            if (id.equalsIgnoreCase("N")) {
                sql = "SELECT name FROM account_master WHERE id = ?";
            } else if (id.equalsIgnoreCase("C")) {
                sql = "SELECT id FROM account_master WHERE name = ?";
            } else if (id.equalsIgnoreCase("CG")) {
                sql = "SELECT fk_account_type_id FROM account_master WHERE id = ?";
            } else if (id.equalsIgnoreCase("DT")) {
                sql = "SELECT lock_date FROM account_master WHERE id = ?";
            }

            PreparedStatement pstLocal = dataConnection.prepareStatement(sql);
            pstLocal.setString(1, name);
            ResultSet rsLocal = pstLocal.executeQuery();
            if (rsLocal.next()) {
                ans = rsLocal.getString(1);
            }
            closeResultSet(rsLocal);
            closeStatement(pstLocal);
        } catch (Exception ex) {
            printToLogFile("Exception at getAccountCode In Library", ex);
        }
        return ans;
    }

    public String getChequePartyName(String name, String id) {
        String ans = "";
        if (name == null) {
            return "";
        }
        try {
            String sql = "";
            if (id.equalsIgnoreCase("N")) {
                sql = "SELECT party_name FROM cheque_print WHERE id = ?";
            } else if (id.equalsIgnoreCase("C")) {
                sql = "SELECT id FROM cheque_print WHERE party_name = ?";
            }

            PreparedStatement pstLocal = dataConnection.prepareStatement(sql);
            pstLocal.setString(1, name);
            ResultSet rsLocal = pstLocal.executeQuery();
            if (rsLocal.next()) {
                ans = rsLocal.getString(1);
            }
            closeResultSet(rsLocal);
            closeStatement(pstLocal);
        } catch (Exception ex) {
            printToLogFile("Exception at getChequePartyName In Library", ex);
        }
        return ans;
    }

    public String getTaxCode(String val, String tag) {
        String returnVal = "";
        try {
            String sql = "";
            if (tag.equalsIgnoreCase("N")) {
                sql = "SELECT name FROM tax_master WHERE id = '"+ val +"'";
            } else if (tag.equalsIgnoreCase("C")) {
                sql = "SELECT id FROM tax_master WHERE name = '"+ val +"'";
            } else if (tag.equalsIgnoreCase("RC")) {
                sql = "SELECT id FROM tax_master WHERE tax = '"+ val +"'";
            } else if (tag.equalsIgnoreCase("CR")) {
                sql = "SELECT tax FROM tax_master WHERE id = '"+ val +"'";
            } else if (tag.equalsIgnoreCase("CN")) {
                sql = "SELECT tax FROM tax_master WHERE name = '"+ val +"'";
            } else if (tag.equalsIgnoreCase("TAC")){
                sql = "SELECT tax_ac_cd FROM tax_master WHERE id = '"+ val +"'";
            }
            PreparedStatement pstLocal = dataConnection.prepareStatement(sql);
            ResultSet rsLocal = pstLocal.executeQuery();
            if (rsLocal.next()) {
                returnVal = rsLocal.getString(1);
            }
            closeResultSet(rsLocal);
            closeStatement(pstLocal);
        } catch (Exception ex) {
            printToLogFile("Exception at getTaxCode In Library", ex);
        }
        return returnVal;
    }

    public CachedRowSet getBlankCachedRowSet(String[] columnName, int[] datatype) {
        CachedRowSet crsBlank = null;
        String query = "SELECT ";
        try {
            for (int i = 0; i < columnName.length; i++) {
                if (datatype[i] == 0) {
                    query = query + "'0' AS " + columnName[i];
                } else if (datatype[i] == 1) {
                    query = query + "0 AS " + columnName[i];
                } else if (datatype[i] == 2) {
                    query = query + "0.00 AS " + columnName[i];
                } else if (datatype[i] == 3) {
                    query = query + "CAST (NULL AS BLOB) AS " + columnName[i];
                } else if (datatype[i] == 4) {
                    query = query + "'' AS " + columnName[i];
                } else if (datatype[i] == 5) {
                    query = query + "CURRENT_DATE AS " + columnName[i];
                }
                query += ", ";
            }
            query = query.substring(0, query.length() - 2);
            query += " FROM DUAL";
            PreparedStatement pstBlank = dataConnection.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ResultSet rsBlank = pstBlank.executeQuery();

            crsBlank = new CachedRowSetImpl();
            crsBlank.populate(rsBlank);

            rsBlank.close();
            pstBlank.close();

            crsBlank.beforeFirst();
            crsBlank.beforeFirst();

            while (crsBlank.next()) {
                crsBlank.deleteRow();
            }
        } catch (Exception e) {
            printToLogFile("Error at getBlankCachedRowSet In Library", e);
        }
        return crsBlank;
    }

    public String ConvertDateFormetForDB(String strOrgDate) {
        // Changed
        String strConvDate = "";
        try {
            strOrgDate = strOrgDate.trim();
            if (!strOrgDate.startsWith("/")) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                java.util.Date dt = sdf.parse(strOrgDate);
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                strConvDate = sdf2.format(dt);
            }
        } catch (Exception ex) {
            printToLogFile("Error at ConvertDateFormetForDB In Library", ex);
        }
        return strConvDate;
    }

    public JasperPrint reportGenerator(String fileName, HashMap params, ResultSet viewDataRs, JPanel panelReport) {
        JRResultSetDataSource dataSource = new JRResultSetDataSource(viewDataRs);
        JasperPrint print = null;
        try {
            print = JasperFillManager.fillReport(System.getProperty("user.dir") + File.separatorChar + "Reports" + File.separatorChar + fileName, params, dataSource);
            panelReport.removeAll();
            JRViewer jrViewer = new JRViewer(print);
            jrViewer.setSize(panelReport.getWidth(), panelReport.getHeight());
            panelReport.add(jrViewer);
            SwingUtilities.updateComponentTreeUI(panelReport);
            panelReport.requestFocusInWindow();
        } catch (Exception ex) {
            printToLogFile("Exception at reportGenerator In Library", ex);
        }
        return print;
    }

    public boolean reportGeneratorInPDF(String fileName, String pdfName, String email, HashMap params, ResultSet viewDataRs, JPanel panelReport) {
        JRResultSetDataSource dataSource = new JRResultSetDataSource(viewDataRs);
        JasperPrint print = null;
        try {
            print = JasperFillManager.fillReport(System.getProperty("user.dir") + File.separatorChar + "Reports" + File.separatorChar +  fileName, params, dataSource);

            JRPdfExporter expoterPDF = new JRPdfExporter();	
            expoterPDF.setParameter(JRExporterParameter.JASPER_PRINT, print);
            expoterPDF.setParameter(JRExporterParameter.OUTPUT_STREAM, new FileOutputStream("pdfFolder"+ File.separatorChar + pdfName + ".pdf"));
            expoterPDF.exportReport();

            if(sendEmail(pdfName, email)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            printToLogFile("Exception at reportGeneratorInPDF In Library", ex);
            return false;
        }
    }

    public boolean reportGeneratorInPDFLD(String fileName, String pdfName, String email, HashMap params, JRResultSetDataSource dataSource, JPanel panelReport) {
        JasperPrint print = null;
        try {
            print = JasperFillManager.fillReport(System.getProperty("user.dir") + File.separatorChar + "Reports" + File.separatorChar + fileName, params, dataSource);

            JRPdfExporter expoterPDF = new JRPdfExporter();	
            expoterPDF.setParameter(JRExporterParameter.JASPER_PRINT, print);
            expoterPDF.setParameter(JRExporterParameter.OUTPUT_STREAM, new FileOutputStream("pdfFolder\\"+pdfName + ".pdf"));
            expoterPDF.exportReport();

            if(sendEmail(pdfName, email)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            printToLogFile("Exception at reportGeneratorInPDF In Library", ex);
            return false;
        }
    }

    public boolean sendEmail(String pdfName, String emailTo) {
        final String Email1 = MainClass.ecBean.getManage_email();
        final String password1 = MainClass.ecBean.getManage_pwd();
        final String port = MainClass.ecBean.getManage_port();
        final String host = MainClass.ecBean.getManage_host();

        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", Integer.parseInt(port));

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(Email1, password1);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Email1));

            InternetAddress[] address = { new InternetAddress(emailTo)};
            message.setRecipients(Message.RecipientType.TO, address);
            message.setSubject("INVOICE COPY");

            Multipart multipart = new MimeMultipart();

            BodyPart htmlpart = new MimeBodyPart();

            String header = "";
            header = (new StringBuilder(String.valueOf(header)))
                .append("<br /><br />")
                .append("Dear Sir,")
                .append("<br />")
                .append("\nPlease Check This Invoice Copy...")
                .append("<br />")
                .append("\nPlease Contact me On <b>"+ DeskFrame.clSysEnv.getCONTACT_PERSON() +"</b>("+ DeskFrame.clSysEnv.getMOB_NO() +").")
                .append("<br /><br />")
                .toString();
            htmlPart2.setContent(header,"text/html");

            Multipart mp = new MimeMultipart();

            FileDataSource source = null;

            msg2 = System.getProperty("user.dir") + File.separatorChar + "pdfFolder\\"+ pdfName + ".pdf";

            htmlpart = null;
            htmlpart = new MimeBodyPart();

            if(!pdfName.equalsIgnoreCase("")) {
                source = new FileDataSource(msg2);
                htmlpart.setDataHandler(new DataHandler(source));
                htmlpart.setFileName(pdfName+".pdf");
                mp.addBodyPart(htmlpart);
            }

            message1 = new StringBuilder()
                .append("<br /><br />")
                .append("\n\n From : "+ DeskFrame.clSysEnv.getCMPN_NAME() +"")
                .append("<br /> MOB. NO. : "+ DeskFrame.clSysEnv.getMOB_NO() +" / "+ DeskFrame.clSysEnv.getPHONE_NO() +"")
                .append("<br /> EMAIL ID : "+ DeskFrame.clSysEnv.getEMAIL() +"")
                .append("<br /><br />")
                .toString();
            htmlPart3.setContent(message1,"text/html");

            mp.addBodyPart(htmlPart2);
            mp.addBodyPart(htmlPart3);

            message.setContent(mp);

            if(!pdfName.equalsIgnoreCase("")) {
                multipart.addBodyPart(htmlpart);
            }
            Transport.send(message);
            System.out.println("Message Success.....");
            return true;
        } catch(Exception ex){
            printToLogFile("Exception at Send Message Using While Email In Library", ex);
            return false;
        }
    }

    public boolean sendEmailBackUp(String path, String file_name) {
        final String Email1 = MainClass.ecBean.getManage_email();
        final String password1 = MainClass.ecBean.getManage_pwd();
        final String port = MainClass.ecBean.getManage_port();
        final String host = MainClass.ecBean.getManage_host();

        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(Email1, password1);
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Email1));

            InternetAddress[] address = { new InternetAddress(Email1)};
            message.setRecipients(Message.RecipientType.TO, address);
            message.setSubject(Constants.BACK_UP_FORM_NAME +" FILE");

            Multipart multipart = new MimeMultipart();

            BodyPart htmlpart = new MimeBodyPart();

            String header = "";
            header = (new StringBuilder(String.valueOf(header)))
                .append("<br /><br />")
                .append("Dear Sir,")
                .append("\n")
                .append("<br />")
                .append("Attached ")
                .append(Constants.BACK_UP_FORM_NAME)
                .append(" FILE, Please Check...")
                .append("<br /><br />")
                .toString();
            htmlPart2.setContent(header,"text/html");

            Multipart mp = new MimeMultipart();

            FileDataSource source = null;

            msg2 = path;

            htmlpart = null;
            htmlpart = new MimeBodyPart();

            source = new FileDataSource(msg2);
            htmlpart.setDataHandler(new DataHandler(source));
            htmlpart.setFileName(file_name);
            mp.addBodyPart(htmlpart);

            message1 = new StringBuilder()
                .append("<br /><br />")
                .append("\n\n From : "+ DeskFrame.clSysEnv.getCMPN_NAME() +"")
                .append("<br /><br />")
                .toString();
            htmlPart3.setContent(message1,"text/html");

            mp.addBodyPart(htmlPart2);
            mp.addBodyPart(htmlPart3);

            message.setContent(mp);

            multipart.addBodyPart(htmlpart);
            Transport.send(message);
            System.out.println("Message Success.....");
            return true;
        } catch(Exception ex) {
            printToLogFile("Exception at send Email BackUp Using While Email In Library", ex);
            return false;
        }
    }

    public Image resizeImage(Image image, int width, int height, boolean max) {
        if (width < 0 && height > 0) {
            return resizeImageBy(image, width, height, false);
        } else if (width > 0 && height < 0) {
            return resizeImageBy(image, width, height, true);
        } else if (width < 0 && height < 0) {
            System.err.println("Setting the image size to (width, height) of: ("
                    + width + ", " + height + ") effectively means \"do nothing\"... Returning original image");
            return image;
        }
        return resizeImageBy(image, width, height, true);
    }

    public Image resizeImageBy(Image image, int width, int height, boolean setWidth) {
        return image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    public ResultSet fetchData(String s) {
        try {
            viewDataRs = null;
            PreparedStatement ps = dataConnection.prepareStatement(s);
            viewDataRs = ps.executeQuery();
        } catch (Exception e) {
            printToLogFile("Exception at FetchData In Library", e);
        }
        return viewDataRs;
    }

    public void closeResultSet(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (Exception ex) {
            printToLogFile("Error at closeResultSet In Library", ex);
        }
    }

    public void closeStatement(PreparedStatement pst) {
        try {
            if (pst != null) {
                pst.close();
            }
        } catch (Exception ex) {
            printToLogFile("Error at closeStatement In Library", ex);
        }
    }

    public void closeConnection(Connection con) {
        try {
            if (con != null) {
                con.close();
            }
        } catch (Exception ex) {
            printToLogFile("Error at closeConnection In Library", ex);
        }
    }

    public void setCloseShortcut(JInternalFrame form, final JButton button) {
        KeyStroke closeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        Action closeKeyAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!PickList.isVisible) {
                    button.doClick();
                }
            }
        };
        form.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(closeKeyStroke, "Close");
        form.getActionMap().put("Close", closeKeyAction);
    }

    public void setUserRightsToButton(JButton button, String FormID, String rights) {
        button.setEnabled(getRight(FormID, rights));
    }

    public boolean getRight(String form_id, String right) {
        boolean flag = false;
        try {
            String query = "SELECT "+ right +" FROM user_rights WHERE user_cd  = "+ DeskFrame.user_id +" AND form_cd = "+ form_id;
            PreparedStatement pstLocal = dataConnection.prepareStatement(query);
            ResultSet rsLocal = pstLocal.executeQuery();
            if (rsLocal.next()) {
                if (rsLocal.getInt(1) == 1) {
                    flag = true;
                }
            }
            closeResultSet(rsLocal);
            closeStatement(pstLocal);
        } catch (Exception ex) {
            printToLogFile("Exception at getRight In Library", ex);
        }
        return flag;
    }

    public String[] getUserName() {
        String[] h1 = null;
        try {
            PreparedStatement pst = dataConnection.prepareStatement("SELECT * FROM user_master ORDER BY id", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = pst.executeQuery();
            rs.last();
            h1 = new String[rs.getRow()];
            rs.beforeFirst();
            int i = 0;
            while (rs.next()) {
                h1[i] = rs.getString(2);
                i++;
            }
            closeResultSet(rs);
            closeStatement(pst);
        } catch (Exception ex) {
            printToLogFile("Exception at getUserName In Library", ex);
        }
        return h1;
    }

    public String getUserName(String strVal, String tag) {
        String userName = "";
        try {
            PreparedStatement pstLocal = null;
            if (tag.equalsIgnoreCase("N")) {
                pstLocal = dataConnection.prepareStatement("SELECT username FROM user_master WHERE id = ?");
            } else if (tag.equalsIgnoreCase("C")) {
                pstLocal = dataConnection.prepareStatement("SELECT id FROM user_master WHERE username = ?");
            }
            pstLocal.setString(1, strVal);
            ResultSet rsLocal = pstLocal.executeQuery();
            if (rsLocal.next()) {
                userName = rsLocal.getString(1);
            }
            closeResultSet(rsLocal);
            closeStatement(pstLocal);
        } catch (Exception ex) {
            printToLogFile("Exception at getUserName In Library", ex);
        }
        return userName;
    }

    public String getData(String column, String table, String where, String whereData,Connection dataConnection) {
        String data = "";
        try {
            String sql = "";
            sql = "SELECT "+ column +" FROM "+ table +" WHERE "+ where +"=?";

            PreparedStatement pstLocal = dataConnection.prepareStatement(sql);
            pstLocal.setString(1, whereData);
            ResultSet rsLocal = pstLocal.executeQuery();
            if (rsLocal.next()) {
                data = rsLocal.getString(1);
            }
            if (data == null) {
                data = "";
            }
            closeResultSet(rsLocal);
            closeStatement(pstLocal);
        } catch (Exception ex) {
            printToLogFile("Exception at getData In Library", ex);
        }
        return data;
    }

    public String getData(String column, String table, String where, String whereData) {
        String data = "";
        try {
            String sql = "";
            sql = "SELECT "+ column +" FROM "+ table +" WHERE "+ where +"=?";
            PreparedStatement pstLocal = dataConnection.prepareStatement(sql);
            pstLocal.setString(1, whereData);
            ResultSet rsLocal = pstLocal.executeQuery();
            if (rsLocal.next()) {
                data = rsLocal.getString(1);
            }
            if (data == null) {
                data = "";
            }
            closeResultSet(rsLocal);
            closeStatement(pstLocal);
        } catch (Exception ex) {
            printToLogFile("Exception at getData In Library", ex);
        }
        return data;
    }

    public boolean checkAccountType(String id) {
        boolean flag = false;
        try {
            String sql = "SELECT head FROM account_type WHERE id = ?";
            PreparedStatement pstLocal = dataConnection.prepareStatement(sql);
            pstLocal.setString(1, id);
            ResultSet rsLocal = pstLocal.executeQuery();
            if (rsLocal.next()) {
                if (rsLocal.getInt(1) == 0) {
                    flag = false;
                } else {
                    flag = true;
                }
            }
        } catch (Exception ex) {
            printToLogFile("Exception at checkAccountType In Library", ex);
        }
        return flag;
    }

    public String getcustomFormat(double value) { // By JD
        String pattern = "##,##,##,##0.00";
        DecimalFormat df = new DecimalFormat(pattern);
        String formatvalue = df.format(value);

        if (formatvalue.equalsIgnoreCase(null) || String.valueOf(value).equalsIgnoreCase("NaN")) {
            formatvalue = "0.00";
        }
        return formatvalue;
    }

    public void setStaticImage(String photo, JLabel jImg, JLabel jImg2) {
        ImageIcon img = null;
        Dimension d = jImg.getPreferredSize();
        if(photo != null) {
            if (!photo.isEmpty()) {
                if(isValidImageFile(photo)) {
                    File f = new File(DeskFrame.currentDirectory + File.separatorChar +"Resources"+ File.separatorChar +"Images"+ File.separatorChar + photo);
                    if (f.exists()) {
                        img = new ImageIcon(resizeImage(Toolkit.getDefaultToolkit().createImage(f.getAbsolutePath()), d.width, d.height, true));
                        jImg.setIcon(img);
                        jImg.setText("");
                    } else {
                        jImg.setIcon(null);
                        jImg.setText("No Image");
                    }
                } else {
                    jImg.setIcon(null);
                    jImg.setText("No Image");
                }
            } else {
                jImg.setIcon(null);
                jImg.setText("No Image");
            }
        } else{
            jImg.setIcon(null);
            jImg.setText("No Image");
        }
        if(jImg2 != null) {
            jImg2.setText(photo);
        }
    }

    public String getAccountType(String strVal, String tag) {
        PreparedStatement pstLocal = null;
        ResultSet rsLocal = null;
        String returnVal = "";
        String sql = "";
        if (strVal.trim().equalsIgnoreCase("") && tag.equalsIgnoreCase("C")) {
            return "";
        }
        try {
            if (tag.equalsIgnoreCase("C")) {
                sql = "SELECT id FROM account_type WHERE name='"+ strVal +"'";
            } else if (tag.equalsIgnoreCase("N")) {
                sql = "SELECT name FROM account_type WHERE id='"+ strVal +"'";
            }

            if (sql != null) {
                pstLocal = dataConnection.prepareStatement(sql);
                rsLocal = pstLocal.executeQuery();
                while (rsLocal.next()) {
                    returnVal = rsLocal.getString(1);
                }
                closeResultSet(rsLocal);
                closeStatement(pstLocal);
            }
        } catch (Exception ex) {
            printToLogFile("Exception at getAccountType In Library", ex);
        }
        return returnVal;
    }

    // Return main category and sub category name or id
    public String getRawMainCategory(String strVal, String tag, String category) {
        PreparedStatement pstLocal = null;
        ResultSet rsLocal = null;
        String returnVal = "";
        String sql = "";
        if (strVal.trim().equalsIgnoreCase("") && tag.equalsIgnoreCase("C")) {
            return "";
        }
        try {
            if(category.equalsIgnoreCase("main")) {
                if (tag.equalsIgnoreCase("C")) {
                    sql = "SELECT id FROM raw_material_main WHERE name_en='"+ strVal +"'";
                } else if (tag.equalsIgnoreCase("N")) {
                    sql = "SELECT name_en FROM raw_material_main WHERE id='"+ strVal +"'";
                }
            } else if (category.equalsIgnoreCase("sub")) {
                if (tag.equalsIgnoreCase("C")) {
                    sql = "SELECT id FROM raw_material_sub WHERE name_en='"+ strVal +"'";
                } else if (tag.equalsIgnoreCase("N")) {
                    sql = "SELECT name_en FROM raw_material_sub WHERE id='"+ strVal +"'";
                }
            }
            if (sql != null) {
                pstLocal = dataConnection.prepareStatement(sql);
                rsLocal = pstLocal.executeQuery();
                while (rsLocal.next()) {
                    returnVal = rsLocal.getString(1);
                }
                closeResultSet(rsLocal);
                closeStatement(pstLocal);
            }
        } catch (Exception ex) {
            printToLogFile("Exception at getRawMainCategory In Library", ex);
        }
        return returnVal;
    }
    
    // Return raw material ID or Name
    public String getRawMaterialname(String strVal, String tag) {
        PreparedStatement pstLocal = null;
        ResultSet rsLocal = null;
        String returnVal = "";
        String sql = "";
        if (strVal.trim().equalsIgnoreCase("") && tag.equalsIgnoreCase("C")) {
            return "0";
        }
        try {
            if (tag.equalsIgnoreCase("C")) {
                sql = "SELECT id FROM raw_material WHERE name_en = '"+ strVal +"'";
            } else if (tag.equalsIgnoreCase("N")) {
                sql = "SELECT name_en FROM raw_material WHERE id = '"+ strVal +"'";
            }
            if (sql != null) {
                pstLocal = dataConnection.prepareStatement(sql);
                rsLocal = pstLocal.executeQuery();
                while (rsLocal.next()) {
                    returnVal = rsLocal.getString(1);
                }
                closeResultSet(rsLocal);
                closeStatement(pstLocal);
            }
        } catch (Exception ex) {
            printToLogFile("Exception at get raw material In Library", ex);
        }
        return returnVal;
    }
    
    public String getData(String query) {
        String data = "";
        PreparedStatement pstLocal = null;
        ResultSet rsLocal = null;
        try {
            pstLocal = dataConnection.prepareStatement(query);
            rsLocal = pstLocal.executeQuery();
            if (rsLocal.next()) {
                data = rsLocal.getString(1);
            }
        } catch (Exception ex) {
            printToLogFile("Exception at getData In Library", ex);
        } finally {
            closeResultSet(rsLocal);
            closeStatement(pstLocal);
        }
        return data;
    }

    public boolean isExist(String table, String column, String data) {
        boolean flag = false;
        PreparedStatement psLocal = null;
        ResultSet rsLocal = null;
        try {
            psLocal = DeskFrame.connMpAdmin.prepareStatement("SELECT "+ column +" FROM "+ table +" WHERE UPPER("+ column +") = ?");
            psLocal.setString(1, data.toUpperCase());
            rsLocal = psLocal.executeQuery();
            flag = rsLocal.next();
        } catch (Exception ex) {
            printToLogFile("Error at isExist In Library", ex);
        } finally {
            closeResultSet(rsLocal);
            closeStatement(psLocal);
        }
        return flag;
    }

    public boolean isExistForEdit(String table, String column, String data, String primaryCol, String primaryVal, Connection con) {
        boolean flag = false;
        try {
            PreparedStatement psLocal = con.prepareStatement("SELECT "+ column +" FROM "+ table +" WHERE UPPER("+ column +")=? AND "+ primaryCol +"<>?");
            psLocal.setString(1, data.toUpperCase());
            psLocal.setString(2, primaryVal.toUpperCase());
            ResultSet rsLocal = psLocal.executeQuery();
            flag = rsLocal.next();
            closeResultSet(rsLocal);
            closeStatement(psLocal);
        } catch (Exception ex) {
            printToLogFile("Error at isExistForEdit In Library", ex);
        }
        return flag;
    }

    public String getData(String column, String table, String where, String whereData, int type) {
        String data = "";
        PreparedStatement pstLocal = null;
        ResultSet rsLocal = null;
        try {
            String sql = "";
            if (type == 0) {
                sql = "SELECT "+ column +" FROM "+ table +" WHERE "+ where +"='"+ whereData +"'";
            } else {
                sql = "SELECT "+ column +" FROM "+ table +" WHERE "+ where +"="+ whereData +"";
            }
            pstLocal = dataConnection.prepareStatement(sql);
            rsLocal = pstLocal.executeQuery();
            if (rsLocal.next()) {
                data = rsLocal.getString(1);
            }
        } catch (Exception ex) {
            printToLogFile("Exception at getData In Library", ex);
        } finally {
            closeResultSet(rsLocal);
            closeStatement(pstLocal);
        }
        return data;
    }

    public String getField(String table, String column, String whField, String whValue) {
        String result = null;
        PreparedStatement psLocal = null;
        String sql = null;
        try {
            if (whField == null && whValue == null) {
                sql = "SELECT "+ column +" FROM "+ table;
                psLocal = dataConnection.prepareStatement(sql);
            } else {
                sql = "SELECT "+ column +" FROM "+ table +" WHERE "+ whField +"=?";
                psLocal = dataConnection.prepareStatement(sql);
                psLocal.setString(1, whValue);
            }
            ResultSet rsLocal = psLocal.executeQuery();
            if (rsLocal.next()) {
                result = rsLocal.getString(1);
            }
        } catch (Exception ex) {
            printToLogFile("Error at getField In Library", ex);
        }
        return result;
    }

    public void printToLogFile(String strMsg, Exception exType) {
        try {
            HashSet<String> hsFileName = new HashSet<String>();
            if (exType != null) {
                StackTraceElement str[] = exType.getStackTrace();
                int iIndex = 0;
                if (str.length > 10) {
                    iIndex = 10;
                } else {
                    iIndex = str.length;
                }
                for (int i = 0; i < iIndex; i++) {
                    if (str[i].getFileName() != null) {
                        hsFileName.add(str[i].getFileName());
                    }
                }
                if (!hsFileName.contains("DataFilterColumnArray.java") && !hsFileName.contains("DataFilterDate.java")
                        && !hsFileName.contains("DataFilterNumber.java") && !hsFileName.contains("DataFilterString.java")) {
                    DeskFrame.logFile.write("Time : "+ getCurrentDBServerTime());
                    DeskFrame.logFile.newLine();
                    DeskFrame.logFile.write("Exception From : "+ strMsg.toString());
                    DeskFrame.logFile.newLine();
                    DeskFrame.logFile.write("Main Exception :"+ exType.toString());
                    DeskFrame.logFile.newLine();

                    for (int i = 0; i < iIndex; i++) {
                        DeskFrame.logFile.write("          ======================           ");
                        DeskFrame.logFile.newLine();
                        DeskFrame.logFile.write("Class Name  :"+ str[i].getClassName());
                        DeskFrame.logFile.newLine();
                        DeskFrame.logFile.write("File Name   :"+ str[i].getFileName());
                        DeskFrame.logFile.newLine();
                        DeskFrame.logFile.write("Method Name :"+ str[i].getMethodName());
                        DeskFrame.logFile.newLine();
                        DeskFrame.logFile.write("Line Number :"+ str[i].getLineNumber());
                        DeskFrame.logFile.newLine();
                    }
                    DeskFrame.logFile.write("==================================================");
                    DeskFrame.logFile.newLine();
                    DeskFrame.logFile.write("==================================================");
                    DeskFrame.logFile.newLine();
                }
            } else {
                DeskFrame.logFile.write("Time : "+ getCurrentDBServerTime());
                DeskFrame.logFile.newLine();
                DeskFrame.logFile.write("Message(For Information) : "+ strMsg.toString());
                DeskFrame.logFile.newLine();
                DeskFrame.logFile.write("==================================================");
                DeskFrame.logFile.newLine();
            }
            DeskFrame.logFile.flush();
            if (exType instanceof java.sql.SQLNonTransientConnectionException) {
                JButton exit = new JButton(Constants.EXIT_FORM_NAME);
                JButton[] button = {exit};
                exit.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                });
                JOptionPane.showOptionDialog(null, "Please Restart the Application and\n Check the Database Connection", "Connection Error",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, button, exit);
                return;
            }
        } catch (Exception ex) {
            printToLogFile(strMsg +" (And Error in PrintToLogFile : "+ ex +")", exType, true);
        }
    }

    public String getCurrentDBServerTime() {
        String strTime = "";
        try {
            if (dataConnection != null) {
                PreparedStatement pst = dataConnection.prepareStatement("SELECT NOW() AS CUR_DATE FROM DUAL", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    strTime = rs.getTimestamp(1).toString();
                }
                closeResultSet(rs);
                closeStatement(pst);
            }
        } catch (Exception ex) {
            printToLogFile("Exception at getCurrentDBServerTime In Library..!!: ", ex, true);
        }
        return strTime;
    }

    public void printToLogFile(String strMsg, Exception exType, boolean withoutTiming) {
        try {
            HashSet<String> hsFileName = new HashSet<String>();
            if (exType != null) {
                StackTraceElement str[] = exType.getStackTrace();
                int iIndex = 0;
                if (str.length > 10) {
                    iIndex = 10;
                } else {
                    iIndex = str.length;
                }

                for (int i = 0; i < iIndex; i++) {
                    if (str[i].getFileName() != null) {
                        hsFileName.add(str[i].getFileName());
                    }
                }

                if (!hsFileName.contains("DataFilterColumnArray.java")
                        && !hsFileName.contains("DataFilterDate.java")
                        && !hsFileName.contains("DataFilterNumber.java")
                        && !hsFileName.contains("DataFilterString.java")) {
                    DeskFrame.logFile.write("Exception From : "+ strMsg.toString());
                    DeskFrame.logFile.newLine();
                    DeskFrame.logFile.write("Main Exception :"+ exType.toString());
                    DeskFrame.logFile.newLine();

                    for (int i = 0; i < iIndex; i++) {
                        DeskFrame.logFile.write("          ======================           ");
                        DeskFrame.logFile.newLine();
                        DeskFrame.logFile.write("Class Name  :"+ str[i].getClassName());
                        DeskFrame.logFile.newLine();
                        DeskFrame.logFile.write("File Name   :"+ str[i].getFileName());
                        DeskFrame.logFile.newLine();
                        DeskFrame.logFile.write("Method Name :"+ str[i].getMethodName());
                        DeskFrame.logFile.newLine();
                        DeskFrame.logFile.write("Line Number :"+ str[i].getLineNumber());
                        DeskFrame.logFile.newLine();
                    }
                    DeskFrame.logFile.write("==================================================");
                    DeskFrame.logFile.newLine();
                    DeskFrame.logFile.write("==================================================");
                    DeskFrame.logFile.newLine();
                }
                if (exType instanceof java.sql.SQLNonTransientConnectionException) {
                    DeskFrame.logFile.flush();
                    JButton exit = new JButton(Constants.EXIT_FORM_NAME);
                    JButton[] button = {exit};
                    exit.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            System.exit(0);
                        }
                    });
                    JOptionPane.showOptionDialog(null, "Please Restart the Application and\n Check the Database Connection", "Connection Error",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, null, button, exit);
                    return;
                }
            } else {
                DeskFrame.logFile.write("Main Exception :" + strMsg.toString());
                DeskFrame.logFile.newLine();
            }
            DeskFrame.logFile.flush();
        } catch (Exception ex) {
            System.out.println("Exception at printToLogFile_withoutTiming In Library..!!" + ex);
        }
    }

    public boolean checkOldPwd(int id, String Password) {
        boolean flag = false;
        try {
            String sql = "SELECT password FROM user_master WHERE id="+ id;
            PreparedStatement pstLocal = dataConnection.prepareStatement(sql);
            ResultSet rsLocal = pstLocal.executeQuery();
            if (rsLocal.next()) {
                if (rsLocal.getString(1).equalsIgnoreCase(Password)) {
                    flag = true;
                }
            }
            closeResultSet(rsLocal);
            closeStatement(pstLocal);
        } catch (Exception ex) {
            printToLogFile("exception at ckeckOldPwd In Library", ex);
        }
        return flag;
    }

    public ResultSet fetchData(String query, Connection con) {
        ResultSet rsLocal = null;
        try {
            PreparedStatement psLocal = con.prepareStatement(query);
            rsLocal = psLocal.executeQuery();
        } catch (Exception ex) {
            printToLogFile("Error at fetchData In Library", ex);
        }
        return rsLocal;
    }

    public boolean changePwd(int id, String pwd) {
        boolean flag = false;
        try {
            String sql = "UPDATE user_master SET password = ? WHERE id = ?";
            PreparedStatement pstLocal = dataConnection.prepareStatement(sql);
            pstLocal.setString(1, pwd); // PASSWORD
            pstLocal.setInt(2, id); // USER CD
            if (pstLocal.executeUpdate() > 0) {
                flag = true;
            }
        } catch (Exception ex) {
            printToLogFile("exception at changePwd In Library", ex);
        }
        return flag;
    }

    public void toUniform(JTextField txt) {
        StringTokenizer strToken = new StringTokenizer(txt.getText().trim().toLowerCase(), " ");
        String finalStr = "";
        while (strToken.hasMoreTokens()) {
            String word = strToken.nextToken();
            String first = "";
            String remain = "";
            if (!word.trim().equalsIgnoreCase("")) {
                first = word.substring(0, 1);
                remain = word.substring(1);
            }
            finalStr += (first.toUpperCase() + remain +" ");
        }
        txt.setText(finalStr.trim());
    }

    public void toRupee(java.awt.event.FocusEvent evt) {
        JTextField txt = ((JTextField) evt.getSource());
        txt.setText(Convert2DecFmt(Math.abs(isNumber(txt))));
    }

    public void setPreviewShortcut(JInternalFrame form, final JButton button) {
        KeyStroke closeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_MASK, false);
        Action closeKeyAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button.doClick();
            }
        };
        form.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(closeKeyStroke, "Preview");
        form.getActionMap().put("Preview", closeKeyAction);
    }

    public void setViewShortcut(JInternalFrame form, final JButton button) {
        KeyStroke closeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.ALT_MASK, false);
        Action closeKeyAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button.doClick();
            }
        };
        form.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(closeKeyStroke, "View");
        form.getActionMap().put("View", closeKeyAction);
    }

    public void sendKeys(int keyCode) {
        try {
            robotVar.keyPress(keyCode);
            robotVar.keyRelease(keyCode);
        } catch (Exception ex) {
            printToLogFile("Exception at sendKeys in Library", ex);
        }
    }

    public boolean checkDate2(JTextField jtxtDate) {
        boolean flag = true;
        try {
            String[] date = new String[3];
            StringTokenizer stToken = new StringTokenizer(jtxtDate.getText(), "/");
            int i = 0;
            while (stToken.hasMoreElements()) {
                String token = stToken.nextToken().trim();
                if (!token.equalsIgnoreCase("")) {
                    date[i] = token;
                    i++;
                }
            }
            int day = 0, month = 0, year = 0;
            if (i == 3) {
                day = (int) replaceAll(date[0]);
                month = (int) replaceAll(date[1]);
                year = (int) replaceAll(date[2]);

                if (day < 0 || day > 31) {
                    flag = false;
                }
                if (month < 1 || month > 12) {
                    flag = false;
                }

                if ((year + "").length() == 4) {
                } else if ((year + "").length() == 2) {
                    year += 2000;
                } else {
                    flag = false;
                }
                if (year < 1900 || year > 2099) {
                    flag = false;
                }
            } else {
                flag = false;
            }
            Date d = null;
            if (flag) {
                Calendar cal = Calendar.getInstance();
                cal.set(year, month - 1, day);
                d = cal.getTime();
                jtxtDate.setText(userFormat.format(d));
            }
        } catch (Exception ex) {
            printToLogFile("Exception at checkDate2 in Library", ex);
            flag = false;
            jtxtDate.requestFocusInWindow();
        }
        if (!flag) {
            jtxtDate.setText(userFormat.format(new Date()));
        }
        return flag;
    }

    public boolean checkFinancialDate(JTextField jtxtDate) {
        boolean flag = checkDate2(jtxtDate);
        if (flag) {
            try {
                userFormat.parse(jtxtDate.getText());
            } catch (Exception ex) {
                printToLogFile("Exception at checkFinancialDate in Library", ex);
                flag = false;
            }
        }
        return flag;
    }

    public void toDouble(java.awt.event.FocusEvent evt) {
        ((JTextField) evt.getSource()).setText(replaceAll(((JTextField) evt.getSource()).getText()) + "");
    }

    public boolean checkDate(JTextField jtxtDate) {
        boolean flag = true;
        try {
            if (jtxtDate.getText().contains("/")) {
                jtxtDate.setText(jtxtDate.getText().replace("/", ""));
            }
            if (jtxtDate.getText().length() == 8) {
                String temp = jtxtDate.getText();
                String setDate = (temp.substring(0, 2)).replace(temp.substring(0, 2), temp.substring(0, 2) + "/") + (temp.substring(2, 4)).replace(temp.substring(2, 4), temp.substring(2, 4) + "/") + temp.substring(4, temp.length());
                jtxtDate.setText(setDate);
            }
        } catch (Exception ex) {
            printToLogFile("Exception at checkDate in Library", ex);
            flag = false;
            jtxtDate.requestFocusInWindow();
        }
        return flag;
    }

    public String getUnitName(String strVal, String tag) {
        PreparedStatement pstLocal = null;
        ResultSet rsLocal = null;
        String returnVal = "";
        String sql = "";
        if (strVal.trim().equalsIgnoreCase("") && tag.equalsIgnoreCase("C")) {
            return "0";
        }
        try {
            if (tag.equalsIgnoreCase("C")) {
                sql = "SELECT id FROM unit_master WHERE name_en = '"+ strVal +"'";
            } else if (tag.equalsIgnoreCase("N")) {
                sql = "SELECT name_en FROM unit_master WHERE id = '"+ strVal +"'";
            }
            if (sql != null) {
                pstLocal = dataConnection.prepareStatement(sql);
                rsLocal = pstLocal.executeQuery();
                while (rsLocal.next()) {
                    returnVal = rsLocal.getString(1);
                }
                closeResultSet(rsLocal);
                closeStatement(pstLocal);
            }
        } catch (Exception ex) {
            printToLogFile("Exception at getUnitName In Library", ex);
        }
        return returnVal;
    }

    public String getBankName(String strVal, String tag) {
        PreparedStatement pstLocal = null;
        ResultSet rsLocal = null;
        String returnVal = "";
        String sql = "";
        if (strVal.trim().equalsIgnoreCase("") && tag.equalsIgnoreCase("C")) {
            return "0";
        }
        try {
            if (tag.equalsIgnoreCase("C")) {
                sql = "SELECT id FROM bank_master WHERE name = '"+ strVal +"'";
            } else if (tag.equalsIgnoreCase("N")) {
                sql = "SELECT name FROM bank_master WHERE id = '"+ strVal +"'";
            }

            if (sql != null) {
                pstLocal = dataConnection.prepareStatement(sql);
                rsLocal = pstLocal.executeQuery();
                while (rsLocal.next()) {
                    returnVal = rsLocal.getString(1);
                }
                closeResultSet(rsLocal);
                closeStatement(pstLocal);
            }
        } catch (Exception ex) {
            printToLogFile("Exception at getBankName In Library", ex);
        }
        return returnVal;
    }

    public void confirmDialog(String message) {
        final JButton yes = new JButton("Yes");
        final JButton no = new JButton("No");
        type = false;
        JOptionPane JP = new JOptionPane();

        no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                type = false;
                no.getTopLevelAncestor().setVisible(false);
            }
        });

        yes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                type = true;
                yes.getTopLevelAncestor().setVisible(false);
            }
        });

        Action yesKeyAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                yes.doClick();
            }
        };

        Action noKeyAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                no.doClick();
            }
        };
        yes.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, 0, false), "Click Me Button");
        yes.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_N, 0, false), "Click Me");
        no.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_N, 0, false), "Click Me");
        no.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, 0, false), "Click Me Button");
        yes.getActionMap().put("Click Me Button", yesKeyAction);
        yes.getActionMap().put("Click Me", noKeyAction);
        no.getActionMap().put("Click Me Button", yesKeyAction);
        no.getActionMap().put("Click Me", noKeyAction);
        JButton[] options = {yes, no};
        JP.showOptionDialog(null, message + " \n (Press Y for Yes)  (Press N for No)", Constants.SOFTWARE_NAME, -1, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    }

    public void openForm(String form_name) {
        if(form_name.equalsIgnoreCase(Constants.TAX_MASTER_FORM_NAME)) { // TAX MASTER
            if (MainClass.df.hasPermission(Constants.TAX_MASTER_FORM_ID)) {
                int index = checkAlradyOpen(Constants.TAX_MASTER_FORM_NAME);
                if(index == -1) {
                    TaxMaster tm = new TaxMaster();
                    addOnScreen(tm, Constants.TAX_MASTER_FORM_NAME);
                    tm.setTitle(Constants.TAX_MASTER_FORM_NAME);
                } else {
                    tabbedPane.setSelectedIndex(index);
                }
            } else {
                JOptionPane.showMessageDialog(null, Constants.NO_RIGHTS_TO_VIEW, l, JOptionPane.WARNING_MESSAGE);
            }
        } else if(form_name.equalsIgnoreCase(Constants.ACCOUNT_TYPE_FORM_NAME)) { // GROUP MASTER
            if (MainClass.df.hasPermission(Constants.ACCOUNT_TYPE_FORM_ID)) {
                int index = checkAlradyOpen(Constants.ACCOUNT_TYPE_FORM_NAME);
                if (index == -1) {
                    AccountType gm = new AccountType();
                    addOnScreen(gm, Constants.ACCOUNT_TYPE_FORM_NAME);
                    gm.setTitle(Constants.ACCOUNT_TYPE_FORM_NAME);
                } else {
                    tabbedPane.setSelectedIndex(index);
                }
            } else {
                JOptionPane.showMessageDialog(null, Constants.NO_RIGHTS_TO_VIEW, l, JOptionPane.WARNING_MESSAGE);
            }
        } else if(form_name.equalsIgnoreCase(Constants.ACCOUNT_MASTER_FORM_NAME)) { // ACCOUNT MASTER
            if (MainClass.df.hasPermission(Constants.ACCOUNT_MASTER_FORM_ID)) {
                int index = checkAlradyOpen(Constants.ACCOUNT_MASTER_FORM_NAME);
                if (index == -1) {
                    AccountMaster am = new AccountMaster();
                    addOnScreen(am, Constants.ACCOUNT_MASTER_FORM_NAME);
                    am.setTitle(Constants.ACCOUNT_MASTER_FORM_NAME);
                } else {
                    tabbedPane.setSelectedIndex(index);
                }
            } else {
                JOptionPane.showMessageDialog(null, Constants.NO_RIGHTS_TO_VIEW, l, JOptionPane.WARNING_MESSAGE);
            }
        } else if(form_name.equalsIgnoreCase(Constants.UNIT_MASTER_FORM_NAME)) { // UNIT MASTER
            if (MainClass.df.hasPermission(Constants.UNIT_MASTER_FORM_ID)) {
                int index = checkAlradyOpen(Constants.UNIT_MASTER_FORM_NAME);
                if (index == -1) {
                    UnitMaster um = new UnitMaster();
                    addOnScreen(um, Constants.UNIT_MASTER_FORM_NAME);
                    um.setTitle(Constants.UNIT_MASTER_FORM_NAME);
                } else {
                    tabbedPane.setSelectedIndex(index);
                }
            } else {
                JOptionPane.showMessageDialog(null, Constants.NO_RIGHTS_TO_VIEW, l, JOptionPane.WARNING_MESSAGE);
            }
        } else if(form_name.equalsIgnoreCase(Constants.BANK_MASTER_FORM_NAME)) { // BANK MASTER
            if (MainClass.df.hasPermission(Constants.BANK_MASTER_FORM_ID)) {
                int index = checkAlradyOpen(Constants.BANK_MASTER_FORM_NAME);
                if (index == -1) {
                    BankMaster bm = new BankMaster();
                    addOnScreen(bm, Constants.BANK_MASTER_FORM_NAME);
                    bm.setTitle(Constants.BANK_MASTER_FORM_NAME);
                    bm.setStartupFocus();
                } else {
                    tabbedPane.setSelectedIndex(index);
                }
            } else {
                JOptionPane.showMessageDialog(null, Constants.NO_RIGHTS_TO_VIEW, l, JOptionPane.WARNING_MESSAGE);
            }
        } else if(form_name.equalsIgnoreCase(Constants.COMPANY_SETTING_FORM_NAME)) { // COMPANY SETTING
            if (MainClass.df.hasPermission(Constants.COMPANY_SETTING_FORM_ID)) {
                int index = checkAlradyOpen(Constants.COMPANY_SETTING_FORM_NAME);
                if (index == -1) {
                    CompanySetting cs = new CompanySetting();
                    addOnScreen(cs, Constants.COMPANY_SETTING_FORM_NAME);
                    cs.setTitle(Constants.COMPANY_SETTING_FORM_NAME);
                } else {
                    tabbedPane.setSelectedIndex(index);
                }
            } else {
                JOptionPane.showMessageDialog(null, Constants.NO_RIGHTS_TO_VIEW, l, JOptionPane.WARNING_MESSAGE);
            }
        } else if(form_name.equalsIgnoreCase(Constants.MANAGE_USER_FORM_NAME)) { // MANAGE USER
            if (MainClass.df.hasPermission(Constants.MANAGE_USER_FORM_ID)) {
                int index = checkAlradyOpen(Constants.MANAGE_USER_FORM_NAME);
                if (index == -1) {
                    ManageUserView mu = new ManageUserView();
                    addOnScreen(mu, Constants.MANAGE_USER_FORM_NAME);
                    mu.setTitle(Constants.MANAGE_USER_FORM_NAME);
                } else {
                    tabbedPane.setSelectedIndex(index);
                }
            } else {
                JOptionPane.showMessageDialog(null, Constants.NO_RIGHTS_TO_VIEW, l, JOptionPane.WARNING_MESSAGE);
            }
        } else if(form_name.equalsIgnoreCase(Constants.USER_RIGHTS_FORM_NAME)) { // USER RIGHTS
            if (MainClass.df.hasPermission(Constants.USER_RIGHTS_FORM_ID)) {
                int index = checkAlradyOpen(Constants.USER_RIGHTS_FORM_NAME);
                if (index == -1) {
                    UserRights permission = new UserRights();
                    addOnScreen(permission, Constants.USER_RIGHTS_FORM_NAME);
                    permission.setTitle(Constants.USER_RIGHTS_FORM_NAME);
                } else {
                    tabbedPane.setSelectedIndex(index);
                }
            } else {
                JOptionPane.showMessageDialog(null, Constants.NO_RIGHTS_TO_VIEW, l, JOptionPane.WARNING_MESSAGE);
            }
        } else if(form_name.equalsIgnoreCase(Constants.MANAGE_EMAIL_FORM_NAME)) { // MANAGE EMAIL
            if (MainClass.df.hasPermission(Constants.MANAGE_EMAIL_FORM_ID)) {
                int index = checkAlradyOpen(Constants.MANAGE_EMAIL_FORM_NAME);
                if(index == -1) {
                    ManageEmail tm = new ManageEmail();
                    addOnScreen(tm, Constants.MANAGE_EMAIL_FORM_NAME);
                    tm.setTitle(Constants.MANAGE_EMAIL_FORM_NAME);
                } else {
                    tabbedPane.setSelectedIndex(index);
                }
            } else {
                JOptionPane.showMessageDialog(null, Constants.NO_RIGHTS_TO_VIEW, l, JOptionPane.WARNING_MESSAGE);
            }
        } else if(form_name.equalsIgnoreCase(Constants.CHANGE_PASSWORD_FORM_NAME)) { // CHANGE PASSWORD
            if (MainClass.df.hasPermission(Constants.CHANGE_PASSWORD_FORM_ID)) {
                int index = checkAlradyOpen(Constants.CHANGE_PASSWORD_FORM_NAME);
                if (index == -1) {
                    ChangePassword cp = new ChangePassword();
                    addOnScreen(cp, Constants.CHANGE_PASSWORD_FORM_NAME);
                    cp.setTitle(Constants.CHANGE_PASSWORD_FORM_NAME);
                } else {
                    tabbedPane.setSelectedIndex(index);
                }
            } else {
                JOptionPane.showMessageDialog(null, Constants.NO_RIGHTS_TO_VIEW, l, JOptionPane.WARNING_MESSAGE);
            }
        } else if(form_name.equalsIgnoreCase(Constants.CHANGE_DATE_FORM_NAME)) { // CHANGE DATE
            int index = checkAlradyOpen(Constants.CHANGE_DATE_FORM_NAME);
            if (index == -1) {
                DateSetting ds = new DateSetting();
                addOnScreen(ds, Constants.CHANGE_DATE_FORM_NAME);
                ds.setTitle(Constants.CHANGE_DATE_FORM_NAME);
            } else {
                tabbedPane.setSelectedIndex(index);
            }
        } else if(form_name.equalsIgnoreCase(Constants.QUICK_OPEN_FORM_NAME)) { // QUICK OPEN
            if (MainClass.df.hasPermission(Constants.QUICK_OPEN_FORM_ID)) {
                int index = checkAlradyOpen(Constants.QUICK_OPEN_FORM_NAME);
                if(index == -1){
                    QuickOpen qp = new QuickOpen();
                    addOnScreen(qp, Constants.QUICK_OPEN_FORM_NAME);
                    qp.setFocus();
                } else {
                    tabbedPane.setSelectedIndex(index);
                }
            } else {
                JOptionPane.showMessageDialog(null, Constants.NO_RIGHTS_TO_VIEW, l, JOptionPane.WARNING_MESSAGE);
            }
        } else if(form_name.equalsIgnoreCase(Constants.BACK_UP_FORM_NAME)) { // BACK UP
            if (MainClass.df.hasPermission(Constants.BACK_UP_FORM_ID)) {
                int index = checkAlradyOpen(Constants.BACK_UP_FORM_NAME);
                if(index == -1){
                    BackUp bu = new BackUp(MainClass.df, true);
                    bu.show();
                } else {
                    tabbedPane.setSelectedIndex(index);
                }
            } else {
                JOptionPane.showMessageDialog(null, Constants.NO_RIGHTS_TO_VIEW, l, JOptionPane.WARNING_MESSAGE);
            }
        } else if(form_name.equalsIgnoreCase(Constants.RESET_FORM_NAME)) { // RESET
            int index = checkAlradyOpen(Constants.RESET_FORM_NAME);
            if(index == -1){
                Reset bu = new Reset(MainClass.df, true);
                bu.show();
            } else {
                tabbedPane.setSelectedIndex(index);
            }
        } else if(form_name.equalsIgnoreCase(Constants.EMAIL_FORM_NAME)) { // EMAIL
            if (MainClass.df.hasPermission(Constants.EMAIL_FORM_ID)) {
                int index = checkAlradyOpen(Constants.EMAIL_FORM_NAME);
                if (index == -1) {
                    Email em = new Email();
                    addOnScreen(em, Constants.EMAIL_FORM_NAME);
                    em.setTitle(Constants.EMAIL_FORM_NAME);
                } else {
                    tabbedPane.setSelectedIndex(index);
                }
            } else {
                JOptionPane.showMessageDialog(null, Constants.NO_RIGHTS_TO_VIEW, l, JOptionPane.WARNING_MESSAGE);
            }
        } else if(form_name.equalsIgnoreCase(Constants.CHECK_PRINT_FORM_NAME)) { // CHECK PRINT
            if (MainClass.df.hasPermission(Constants.CHECK_PRINT_FORM_ID)) {
                int index = checkAlradyOpen(Constants.CHECK_PRINT_FORM_NAME);
                if (index == -1) {
                    CheckPrint cp = new CheckPrint();
                    addOnScreen(cp, Constants.CHECK_PRINT_FORM_NAME);
                    cp.setTitle(Constants.CHECK_PRINT_FORM_NAME);
                    cp.setStartupFocus();
                } else {
                    tabbedPane.setSelectedIndex(index);
                }
            } else {
                JOptionPane.showMessageDialog(null, Constants.NO_RIGHTS_TO_VIEW, l, JOptionPane.WARNING_MESSAGE);
            }
        } else if(form_name.equalsIgnoreCase(Constants.NEW_YEAR_FORM_NAME)) { // NEW YEAR
//            if (MainClass.df.hasPermission(Constants.NEW_YEAR_FORM_ID)) {
//                int index = checkAlradyOpen(Constants.NEW_YEAR_FORM_NAME);
//                if (index == -1) {
//                    NewYear ny = new NewYear(MainClass.df, true);
//                    ny.show();
//                } else {
//                    tabbedPane.setSelectedIndex(index);
//                }
//            } else {
//                JOptionPane.showMessageDialog(null, Constants.NO_RIGHTS_TO_VIEW, l, JOptionPane.WARNING_MESSAGE);
//            }
        } else if(form_name.equalsIgnoreCase(Constants.CHANGE_THEMES_FORM_NAME)) { // CHANGE THEMES
            if (MainClass.df.hasPermission(Constants.CHANGE_THEMES_FORM_ID)) {
                int index = checkAlradyOpen(Constants.CHANGE_THEMES_FORM_NAME);
                if (index == -1) {
                    ChangeThemes ct = new ChangeThemes();
                    addOnScreen(ct, Constants.CHANGE_THEMES_FORM_NAME);
                    ct.setTitle(Constants.CHANGE_THEMES_FORM_NAME);
                } else {
                    tabbedPane.setSelectedIndex(index);
                }
            } else {
                JOptionPane.showMessageDialog(null, Constants.NO_RIGHTS_TO_VIEW, l, JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public String getEmailId(String accountName) {
        String emailId = "";
        PreparedStatement pstLocal = null;
        ResultSet rsLocal = null;
        try {
            String sql = "SELECT email_id FROM account_master WHERE name = '"+ accountName +"'";
            pstLocal = dataConnection.prepareStatement(sql);
            rsLocal = pstLocal.executeQuery();
            if (rsLocal.next()) {
                emailId = rsLocal.getString(1);
            }
        } catch (SQLException ex) {
            printToLogFile("Exception at getEmailId in CrmLibrary", ex);
        } finally {
            closeResultSet(rsLocal);
            closeStatement(pstLocal);
        }
        return emailId;
    }

    public String getCustomFormat(String input) {
        String value = "";
        value = input.replace(",", "");
        return value;
    }

    public String Convert2DecFmt1ForRs(double strSource) {
        String str = "0";
        try {
            str = new com.ibm.icu.text.DecimalFormat(" #,##,##0.00").format(strSource);
        } catch (Exception ex) {
            printToLogFile("Exception at Convert2DecFmt1ForRs in Library", ex);
        }
        return str;
    }

    public double replaceAll(String strSource) {
        double str = 0.00;
        try {
            str = isNumber(strSource.replaceAll(",", ""));
        } catch (Exception ex) {
            printToLogFile("Exception at replaceAll in Library", ex);
        }
        return str;
    }

    public void setTable(JPanel jPanel, JTable jTableDet, JComponent[] compHeader, JComponent[] compFooter) {
        int maxHeightHeaderComp = 0;
        int maxHeightFooterComp = 0;
        int x = 0;
        int y = 0;

        if (compHeader != null) {
            for (int i = 0; i < compHeader.length; i++) {
                if (compHeader[i] != null) {
                    if (maxHeightHeaderComp < compHeader[i].getHeight()) {
                        maxHeightHeaderComp = compHeader[i].getHeight();
                    }
                }
            }
            // SETTING HEADER
            x = jPanel.getX();
            y = jPanel.getY() - maxHeightHeaderComp;
            for (int i = 0; i < jTableDet.getColumnCount(); i++) {
                if (compHeader[i] != null) {
                    compHeader[i].setBounds(x, y, jTableDet.getColumn(jTableDet.getColumnName(i).toString()).getWidth() - 1, maxHeightHeaderComp);
                }
                x += jTableDet.getColumn(jTableDet.getColumnName(i).toString()).getWidth();
            }
        }
        if (compFooter != null) {
            for (int i = 0; i < compFooter.length; i++) {
                if (compFooter[i] != null) {
                    Dimension d = compFooter[i].getPreferredSize();
                    if (maxHeightFooterComp < d.height) {
                        maxHeightFooterComp = d.height;
                    }
                }
            }
            // SETTING FOOTER
            x = jPanel.getX();
            y = jPanel.getY() + jPanel.getHeight();
            for (int i = 0; i < jTableDet.getColumnCount(); i++) {
                if (compFooter[i] != null) {
                    compFooter[i].setBounds(x, y, jTableDet.getColumn(jTableDet.getColumnName(i).toString()).getWidth() - 1, maxHeightFooterComp);
                }
                x += jTableDet.getColumn(jTableDet.getColumnName(i).toString()).getWidth();
            }
        }
    }

    public void selectAll(java.awt.event.FocusEvent evt) {
        ((JTextField) evt.getSource()).selectAll();
    }

    public void toUpper(java.awt.event.FocusEvent evt) {
        ((JTextField) evt.getSource()).setText(((JTextField) evt.getSource()).getText().toUpperCase());
    }

    public void toUpper(Component comp) {
        if(comp instanceof JTextComponent){
            JTextField text = (JTextField) comp;
            text.setText(text.getText().toUpperCase());
        }
    }

    public void appendColumnToCacheRowSet(CachedRowSet crs, String[] colunmData, int[] datatype) {
        try {
            crs.moveToInsertRow();
            for (int i = 0; i < colunmData.length; i++) {
                if (datatype[i] == 0) {
                    crs.updateString(i + 1, colunmData[i]);
                } else if (datatype[i] == 1) {
                    crs.updateInt(i + 1, (int) replaceAll(colunmData[i]));
                } else if (datatype[i] == 2) {
                    crs.updateDouble(i + 1, replaceAll(colunmData[i]));
                } else if (datatype[i] == 3) {
                    crs.updateBytes(i + 1, null);
                }
            }
            crs.insertRow();
            crs.moveToCurrentRow();
            crs.last();
        } catch (Exception e) {
            System.out.println("Error at appendColumnToCacheRowSet In Library" + e);
        }
    }

    public DateFormat getDateFormat(final SimpleDateFormat sdf) {
        return new DateFormat() {
            public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
                return new StringBuffer(sdf.format(date));
            }
            @Override
            public Date parse(String source, ParsePosition pos) {
                return new Date();
            }
        };
    }

    public String getTimeStamp(Timestamp time) {
        return timestamp.format(new Date(time.getTime()));
    }

    public void enterFocus(KeyEvent evt, JComponent comp) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            evt.consume();
            ((JComponent) evt.getComponent()).setNextFocusableComponent(comp);
            comp.requestFocusInWindow();
        }
    }

    public void downFocus(KeyEvent evt, JComponent comp) {
        if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            if (evt.getModifiers() == KeyEvent.CTRL_MASK) {
                evt.consume();
                comp.requestFocusInWindow();
            }
        }
    }

    public void enterClick(KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            evt.consume();
            ((JButton) evt.getSource()).doClick();
        }
    }

    public double isNumber(Component comp) {
        double ans = 0.00;
        JTextField text = (JTextField) comp;
        try {
            if (!text.getText().equalsIgnoreCase("")) {
                ans = Double.parseDouble(text.getText());
            }
        } catch (Exception ex) {
            printToLogFile("Error at isNumber in Library", ex);
        }
        return ans;
    }

    public double isNumber(JComponent comp) {
        double ans = 0.00;
        String txt = "";
        if (comp instanceof JTextField) {
            txt = ((JTextField) comp).getText();
        } else if (comp instanceof JLabel) {
            txt = ((JLabel) comp).getText();
        }
        try {
            if(!txt.equalsIgnoreCase("")) {
                ans = Double.parseDouble(txt);
            }
        } catch (Exception ex) {
            printToLogFile("Error at isNumber in Library", ex);
        }
        return ans;
    }

    public void fixLength(KeyEvent event, int len) {
        try {
            if (event.isConsumed()) {
                return;
            }
            JTextComponent source = (JTextComponent) event.getSource();
            if (len == -1) {
                len = source.getText().length() + 1;
            }
            if (source.getText().length() >= len && event.getKeyChar() != event.VK_BACK_SPACE && source.getSelectionStart() == source.getSelectionEnd()) {
                source.getToolkit().beep();
                event.consume();
            }
        } catch (Exception e) {
            printToLogFile("Exception at fixLength In Library", e);
        }
    }

    public void onlyAlpha(KeyEvent event, int len) {
        try {
            int keyCode = event.getKeyChar();
            if (keyCode < 48 || keyCode > 58) {
                if (event.isConsumed()) {
                    return;
                }
                JTextComponent source = (JTextComponent) event.getSource();
                if (len == -1) {
                    len = source.getText().length() + 1;
                }
                if (source.getText().length() >= len && event.getKeyChar() != event.VK_BACK_SPACE && source.getSelectionStart() == source.getSelectionEnd()) {
                    source.getToolkit().beep();
                    event.consume();
                }
            } else {
                event.consume();
            }
        } catch (Exception e) {
            printToLogFile("Exception at OnlyAlpha In Library", e);
        }
    }

    public void onlyInteger(KeyEvent event, int len) {
        try {
            int keyCode = event.getKeyChar();
            JTextComponent source = (JTextComponent) event.getSource();
            if (!(keyCode < 48 || keyCode > 58) || keyCode == 45) {
                if (event.isConsumed()) {
                    return;
                }
                if (source.getText().length() >= len && event.getKeyChar() != event.VK_BACK_SPACE && source.getSelectionStart() == source.getSelectionEnd()) {
                    source.getToolkit().beep();
                    event.consume();
                }
            } else {
                event.consume();
            }
        } catch (Exception e) {
            printToLogFile("Exception at OnlyInteger In Library", e);
        }
    }

    public void onlyNumber(KeyEvent event, int len) {
        try {
            int keyCode = event.getKeyChar();
            JTextComponent source = (JTextComponent) event.getSource();
            if (len == -1) {
                len = source.getText().length() + 1;
            }
            if (!(keyCode < 48 || keyCode > 58) || keyCode == 46 || keyCode == 45) {
                if (event.isConsumed()) {
                    return;
                }
                if (source.getText().length() >= len && event.getKeyChar() != event.VK_BACK_SPACE && source.getSelectionStart() == source.getSelectionEnd()) {
                    source.getToolkit().beep();
                    event.consume();
                }
            } else {
                event.consume();
            }
        } catch (Exception e) {
            printToLogFile("Exception at onlyNumber In Library", e);
        }
    }

    public String generateKey(String table, String column, int length, String prefix) {
        String code = "";
        int no = 0;
        PreparedStatement pstLocal = null;
        ResultSet rsLocal = null;
        try {
            pstLocal = dataConnection.prepareStatement("SELECT MAX("+ column +") FROM "+ table +" WHERE UPPER(" + column + ") LIKE '"+ prefix.toUpperCase() +"%'");
            rsLocal = pstLocal.executeQuery();
            if (rsLocal.next()) {
                if (rsLocal.getString(1) != null) {
                    String sno = rsLocal.getString(1).substring(prefix.length());
                    no = Integer.parseInt(sno);
                    no++;
                    for (int i = (no + "").length(); i < (length - prefix.length()); i++) {
                        code += "0";
                    }
                    code = prefix + code + no;
                } else {
                    code = prefix;
                    for (int i = 1; i < (length - prefix.length()); i++) {
                        code += "0";
                    }
                    code = code + "1";
                }
            } else {
                code = prefix;
                for (int i = 1; i < (length - prefix.length()); i++) {
                    code += "0";
                }
                code = code + "1";
            }
            closeResultSet(rsLocal);
            closeStatement(pstLocal);
        } catch (Exception ex) {
            printToLogFile("Exception at generateKey In Library", ex);
        }
        return code;
    }

    public int generateKey(String table, String column) {
        int no = 0;
        PreparedStatement pstLocal = null;
        ResultSet rsLocal = null;
        try {
            pstLocal = dataConnection.prepareStatement("SELECT MAX("+ column +") FROM "+ table);
            rsLocal = pstLocal.executeQuery();
            if (rsLocal.next()) {
                if (rsLocal.getString(1) != null) {
                    String sno = rsLocal.getString(1);
                    no = Integer.parseInt(sno);
                    no++;
                } else {
                    no = 1;
                }
            }
        } catch (Exception ex) {
            printToLogFile("Exception at generateKey In Library", ex);
        } finally {
            closeResultSet(rsLocal);
            closeStatement(pstLocal);
        }
        return no;
    }

    public boolean isExist(String table, String column, String data, Connection con) {
        boolean flag = false;
        try {
            PreparedStatement psLocal = con.prepareStatement("SELECT "+ column +" FROM "+ table +" WHERE UPPER("+ column +")=?");
            psLocal.setString(1, data.toUpperCase());
            ResultSet rsLocal = psLocal.executeQuery();
            flag = rsLocal.next();
            closeResultSet(rsLocal);
            closeStatement(psLocal);
        } catch (Exception ex) {
            printToLogFile("Error at isExist In Library", ex);
        }
        return flag;
    }

    public boolean isExist(String query, Connection con) {
        boolean flag = false;
        PreparedStatement psLocal = null;
        ResultSet rsLocal = null;
        try {
            psLocal = con.prepareStatement(query);
            rsLocal = psLocal.executeQuery();
            flag = rsLocal.next();
        } catch (Exception ex) {
            printToLogFile("Error at isExist In Library", ex);
        } finally {
            closeResultSet(rsLocal);
            closeStatement(psLocal);
        }
        return flag;
    }

    public boolean isEnter(KeyEvent evt) {
        boolean flag = false;
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            flag = true;
        }
        return flag;
    }

    public String getField(String query, Connection con) {
        String data = "";
        ResultSet rsLocal = null;
        PreparedStatement psLocal = null;
        try {
            psLocal = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rsLocal = psLocal.executeQuery();
            if (rsLocal.next()) {
                data = rsLocal.getString(1);
            }
        } catch (Exception ex) {
            printToLogFile("Error at getField In Library", ex);
        } finally {
            closeResultSet(rsLocal);
            closeStatement(psLocal);
        }
        return data;
    }

    public boolean checkAccountName(String name, String id) {
        boolean flag = false;
        try {
            String sql = "SELECT * FROM account_master WHERE name = '"+ name +"' AND id <> '"+ id +"'";
            PreparedStatement pstLocal = dataConnection.prepareStatement(sql);
            ResultSet rsLocal = pstLocal.executeQuery();
            if (rsLocal.next()) {
                flag = false;
            } else {
                flag = true;
            }
            closeResultSet(rsLocal);
            closeStatement(pstLocal);
        } catch (Exception ex) {
            printToLogFile("Exception at checkAccountName In Library", ex);
        }
        return flag;
    }

    public void quickOpen(String ref_no) {
        if (ref_no.startsWith(Constants.CHECK_PRINT_INITIAL)) { // CHECK PRINT
            if (MainClass.df.hasPermission(Constants.CHECK_PRINT_FORM_ID)) {
                int index = DeskFrame.checkAlradyOpen(Constants.CHECK_PRINT_FORM_NAME);
                if (index == -1) {
                    CheckPrint cp = new CheckPrint(ref_no);
                    addOnScreen(cp, Constants.CHECK_PRINT_FORM_NAME);
                    cp.setTitle(Constants.CHECK_PRINT_FORM_NAME);
                } else {
                    DeskFrame.tabbedPane.setSelectedIndex(index);
                }
            } else {
                JOptionPane.showMessageDialog(null, Constants.NO_RIGHTS_TO_VIEW, l, JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public boolean setFocusComponent(Component comp) {
        return comp.requestFocusInWindow();
    }

    public boolean isNegative(String num) {
        boolean flag = false;
        try {
            double n = Double.parseDouble(num);
            if (n < 0) {
                flag = true;
            }
        } catch (Exception ex) {
            printToLogFile("Exception at isNegative In Library", ex);
        }
        return flag;
    }

    public boolean isValidImageFile(String path) {
        if (path.toLowerCase().endsWith("jpg")
                || path.endsWith("jpeg")
                || path.endsWith("gif")
                || path.endsWith("png")) {
            return true;
        }
        return false;
    }

    public boolean isBlank(Component comp) {
        JTextField jText = (JTextField) comp;
        if (jText.getText().trim().length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void Uppercase(JTextField texfield) {
        texfield.setText(texfield.getText().toUpperCase());
    }

    public double isNumber(String text) {
        double ans = 0.00;
        try {
            if(!text.equalsIgnoreCase("")) {
                ans = Double.parseDouble(text);
            }
        } catch (Exception ex) {
            printToLogFile("Error at isNumber in Library", ex);
        }
        return ans;
    }

    public double isNumber(Object num) {
        try {
            if (((Double) num).isNaN()) {
                return 0.00;
            } else {
                return Double.parseDouble(num.toString());
            }
        } catch (Exception ex) {
            printToLogFile("Exception at isNumber Object In Library", ex);
        }
        return 0.00;
    }

    public String Convert2DecFmt(double strSource) {
        String str = "0";
        try {
            String digit = "";
            for (int i = 1; i <= DeskFrame.digit; i++) {
                digit += "0";
            }
            double newKB = Math.round(strSource*100.0)/100.0;
            DecimalFormat formatter = new DecimalFormat("#0." + digit);
            str = formatter.format(newKB);
        } catch (Exception ex) {
            printToLogFile("Exception at Convert2DecFmt In Library", ex);
        }
        return str;
    }

    public String tempConvertFormatForDBorConcurrency(String strOrgDate) throws ParseException {
        String strConvDate = "";
        strOrgDate = strOrgDate.trim();
        if (!strOrgDate.startsWith("/")) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date dt = sdf.parse(strOrgDate);
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            strConvDate = sdf2.format(dt);
        }
        return strConvDate;
    }

    public Date tempConvertFormatForDBorConcurrencydATE(String strOrgDate) throws ParseException {
        Date strConvDate = null;
        strOrgDate = strOrgDate.trim();
        if (!strOrgDate.startsWith("/")) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date dt = sdf.parse(strOrgDate);
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = sdf2.format(dt);
            strConvDate = sdf2.parse(strDate);
        }
        return strConvDate;
    }

    public String setDay(JTextField jtxtDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEEEEEEE");
            SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
            Calendar cal = Calendar.getInstance();
            java.util.Date dt = sdfDate.parse(jtxtDate.getText());
            cal.setTime(dt);
            return (sdf.format(cal.getTime()));

        } catch (Exception ex) {
            printToLogFile("Exception at setDay In Library", ex);
        }
        return "";
    }

    public void setDateChooserPropertyInit(JTextField jcmbDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        jcmbDate.setText(sdf.format(cal.getTime()));
    }

    public void setDateChooserProperty(JTextField jcmbDate) {
        jcmbDate.setText(DeskFrame.date);
    }

    public String getDigit() {
        String digit = "";
        for(int i = 0; i < DeskFrame.digit; i++) {
            digit += "0";
        }
        return digit;
    }

    public void RemoveRows(DefaultTableModel dtmHeader) {
        dtmHeader.setRowCount(0);
    }

    public String ConvertTimeStampFormetForDisplay(String strOrgDate) {
        //Changed
        String strConvDate = "";
        try {
            strOrgDate = strOrgDate.trim();
            if(!strOrgDate.startsWith("/")) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
                java.util.Date dt = sdf.parse(strOrgDate);
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:SS");
                strConvDate = sdf2.format(dt);
            }
        } catch (Exception ex) {
            printToLogFile("Exception at ConvertTimeStampFormetForDisplay In Library", ex);
        }
        return strConvDate;
    }

    public String ConvertDateFormetForTemp(String strOrgDate) {
        //Changed
        String strConvDate = "";
        try {
            strOrgDate = strOrgDate.trim();
            if(!strOrgDate.startsWith("/")) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                java.util.Date dt = sdf.parse(strOrgDate);
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                strConvDate = sdf2.format(dt);
            }
        } catch (Exception ex) {
            printToLogFile("Exception at ConvertDateFormetForTemp In Library", ex);
        }
        return strConvDate;
    }

    public String getDeCustomFormat(String input) {
        String value = "";
        value = input.replace(",", "");
        return value;
    }

    public String getIndianFormat(double value) {
        String formatvalue = "";
        Format format = com.ibm.icu.text.NumberFormat.getCurrencyInstance(new Locale("en", "in"));
        formatvalue = format.format(value);
        if(value < 0) {
            return "-" + formatvalue.substring(4).trim();
        }
        return formatvalue.substring(4);
    }

    public String ConvertDateFormetForDBForConcurrency(String strOrgDate) throws Exception {
        //Changed
        String strConvDate = "";
        if(!strOrgDate.equalsIgnoreCase("")) {
            strOrgDate = strOrgDate.trim();
            if(!strOrgDate.startsWith("/")) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date dt = sdf.parse(strOrgDate);
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
                strConvDate = sdf2.format(dt);
            }
        } else {
            strConvDate = null;
        }
        return strConvDate;
    }

    public String ConvertDateCalendarFormetForDBDiaryString(String strOrgDate) {
        //Changed
        String strConvDate = "";
        try {
            strOrgDate = strOrgDate.trim();
            SimpleDateFormat parseFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
            Date date = parseFormat.parse(strOrgDate);
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            strConvDate = format.format(date);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return strConvDate;
    }

    public void enterEvent(KeyEvent evt, Component nextFocusComponent) {
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            nextFocusComponent.requestFocusInWindow();
        }
    }

    public String getMonth(String i, String tag) {
        if (tag.equalsIgnoreCase("n")) {
            if (i.equalsIgnoreCase("1")) {
                return "January";
            } else if (i.equalsIgnoreCase("2")) {
                return "February";
            } else if (i.equalsIgnoreCase("3")) {
                return "March";
            } else if (i.equalsIgnoreCase("4")) {
                return "April";
            } else if (i.equalsIgnoreCase("5")) {
                return "May";
            } else if (i.equalsIgnoreCase("6")) {
                return "June";
            } else if (i.equalsIgnoreCase("7")) {
                return "July";
            } else if (i.equalsIgnoreCase("8")) {
                return "August";
            } else if (i.equalsIgnoreCase("9")) {
                return "September";
            } else if (i.equalsIgnoreCase("10")) {
                return "October";
            } else if (i.equalsIgnoreCase("11")) {
                return "November";
            } else if (i.equalsIgnoreCase("12")) {
                return "December";
            }
        } else if (tag.equalsIgnoreCase("c")) {
            if (i.equalsIgnoreCase("January")) {
                return "01";
            } else if (i.equalsIgnoreCase("February")) {
                return "02";
            } else if (i.equalsIgnoreCase("March")) {
                return "03";
            } else if (i.equalsIgnoreCase("April")) {
                return "04";
            } else if (i.equalsIgnoreCase("May")) {
                return "05";
            } else if (i.equalsIgnoreCase("June")) {
                return "06";
            } else if (i.equalsIgnoreCase("July")) {
                return "07";
            } else if (i.equalsIgnoreCase("August")) {
                return "08";
            } else if (i.equalsIgnoreCase("September")) {
                return "09";
            } else if (i.equalsIgnoreCase("October")) {
                return "10";
            } else if (i.equalsIgnoreCase("November")) {
                return "11";
            } else if (i.equalsIgnoreCase("December")) {
                return "12";
            }
        }
        return "";
    }

    public void setTable(JTable jTableDet, JComponent[] compHeader) {
        int maxHeightHeaderComp = 0;
        int x = 0;
        int y = 0;

        if(compHeader != null) {
            for(int i = 0; i < compHeader.length; i++) {
                if(compHeader[i] != null) {
                    if (maxHeightHeaderComp < compHeader[i].getHeight()) {
                        maxHeightHeaderComp = compHeader[i].getHeight();
                    }
                }
            }
            // SETTING HEADER
            x = 0;
            y = 0;
            for(int i = 0; i < jTableDet.getColumnCount(); i++) {
                if(compHeader[i] != null) {
                    compHeader[i].setBounds(x, y, jTableDet.getColumn(jTableDet.getColumnName(i).toString()).getWidth() - 1, maxHeightHeaderComp);
                }
                x += jTableDet.getColumn(jTableDet.getColumnName(i).toString()).getWidth();
            }
        }
    }

    public String generateKey(String table, String column, String prefix, int length) {
        String code = "";
        long no = 0;
        PreparedStatement pstLocal = null;
        ResultSet rsLocal = null;
        try {
            pstLocal = dataConnection.prepareStatement("SELECT MAX("+ column +") FROM "+ table +" WHERE "+ column +" LIKE '"+ prefix +"%'");
            rsLocal = pstLocal.executeQuery();
            if(rsLocal.next()) {
                if(rsLocal.getString(1) != null) {
                    String sno = rsLocal.getString(1).substring(prefix.length());
                    no = Integer.parseInt(sno);
                    no++;
                    for (int i = (no + "").length(); i < (length - prefix.length()); i++) {
                        code += "0";
                    }
                    code = prefix + code + no;
                } else {
                    code = prefix;
                    for (int i = 1; i < (length - prefix.length()); i++) {
                        code += "0";
                    }
                    code = code + "1";
                }
            } else {
                code = prefix;
                for (int i = 1; i < (length - prefix.length()); i++) {
                    code += "0";
                }
                code = code + "1";
            }
        } catch (Exception ex) {
            printToLogFile("Exception at generate key In Library", ex);
        } finally {
            closeResultSet(rsLocal);
            closeStatement(pstLocal);
        }
        return code;
    }

    public void serchOnViewTable(final JTable tableViewOnViewAndSearchPanel, Container getContentPane, int x_panel, int y_panel) {
        final JTextField searchTextFields[] = new JTextField[tableViewOnViewAndSearchPanel.getColumnCount()];
        int x_textfield = x_panel;
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableViewOnViewAndSearchPanel.getModel());
        tableViewOnViewAndSearchPanel.setRowSorter(sorter);
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");

        for (int col = 0; col < tableViewOnViewAndSearchPanel.getColumnCount(); col++) {
            searchTextFields[col] = new JTextField();
            getContentPane.add(searchTextFields[col]);
            int y_textfield = (int) (y_panel - searchTextFields[col].getPreferredSize().getHeight());
            int textfieldwidth = tableViewOnViewAndSearchPanel.getColumn(tableViewOnViewAndSearchPanel.getColumnName(col)).getPreferredWidth();
            searchTextFields[col].setBounds(x_textfield, y_textfield, textfieldwidth, (int) searchTextFields[col].getPreferredSize().getHeight());
            searchTextFields[col].setVisible(true);
            searchTextFields[col].setFont(new Font(searchTextFields[col].getFont().getName(), Font.BOLD, searchTextFields[col].getFont().getSize()));
            searchTextFields[col].setText("");

            final int thecol = col;

            searchTextFields[col].addKeyListener(new java.awt.event.KeyAdapter() {
                List<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
                int lastCol = -1;

                public void keyReleased(java.awt.event.KeyEvent e) {
                    if ((e.getKeyCode() != java.awt.event.KeyEvent.VK_DOWN)
                            && (e.getKeyCode() != java.awt.event.KeyEvent.VK_UP)
                            && (e.getKeyCode() != java.awt.event.KeyEvent.VK_PAGE_DOWN)
                            && (e.getKeyCode() != java.awt.event.KeyEvent.VK_PAGE_UP)) {
                        String str = searchTextFields[thecol].getText();
                        str = str.toUpperCase();
                        searchTextFields[thecol].setText(str);
                        Date dt;

                        // Change By JD...22/08/2015
                        if (searchTextFields[thecol].getText().trim().length() > 0
                                && searchTextFields[thecol].getText().trim().length() < 6) {
                            lastCol = -1;
                        }

                        if(lastCol != thecol) {
                            sortKeys = null;
                            sortKeys = new ArrayList<RowSorter.SortKey>();
                            for(int col = 0; col < tableViewOnViewAndSearchPanel.getColumnCount(); col++) {
                                if(!searchTextFields[col].getText().equalsIgnoreCase("")) {
                                    sortKeys.add(new RowSorter.SortKey(col + (tableViewOnViewAndSearchPanel.getModel().getColumnCount() - tableViewOnViewAndSearchPanel.getColumnCount()), SortOrder.ASCENDING));
                                }
                            }
                            sorter.setSortKeys(sortKeys);
                            sorter.sort();
                            lastCol = thecol;
                        }

                        try {
                            for(int i = 0; i < tableViewOnViewAndSearchPanel.getRowCount(); i++) {
                                if (selDateColumn != thecol) {
                                    if (tableViewOnViewAndSearchPanel.getValueAt(i, thecol).toString().trim().toUpperCase().startsWith(str.toUpperCase())) {
                                        setViewPort(tableViewOnViewAndSearchPanel, i, thecol);
                                        break;
                                    }
                                } else {
                                    if(!tableViewOnViewAndSearchPanel.getValueAt(i, thecol).toString().equalsIgnoreCase("")) {
                                        dt = sdf.parse(tableViewOnViewAndSearchPanel.getValueAt(i, thecol).toString());
                                        if (sdf2.format(dt).trim().startsWith(str)) {
                                            setViewPort(tableViewOnViewAndSearchPanel, i, thecol);
                                            break;
                                        }
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                }

                public void keyPressed(java.awt.event.KeyEvent e) {
                    if((e.getKeyCode() == java.awt.event.KeyEvent.VK_DOWN)
                            || (e.getKeyCode() == java.awt.event.KeyEvent.VK_UP)
                            || (e.getKeyCode() == java.awt.event.KeyEvent.VK_PAGE_DOWN)
                            || (e.getKeyCode() == java.awt.event.KeyEvent.VK_PAGE_UP)) {
                        int rowID = tableViewOnViewAndSearchPanel.getSelectedRow();
                        int colID = tableViewOnViewAndSearchPanel.getSelectedColumn();
                        tableViewOnViewAndSearchPanel.requestFocusInWindow();

                        if(e.getKeyCode() == java.awt.event.KeyEvent.VK_DOWN) {
                            if (tableViewOnViewAndSearchPanel.getRowCount() > 0) {
                                rowID++;
                            }
                        }

                        if(e.getKeyCode() == java.awt.event.KeyEvent.VK_UP) {
                            if (tableViewOnViewAndSearchPanel.getRowCount() > 0) {
                                rowID--;
                            }
                        }

                        if(e.getKeyCode() == java.awt.event.KeyEvent.VK_PAGE_DOWN) {
                            if (tableViewOnViewAndSearchPanel.getRowCount() > 0) {
                                rowID += 10;
                            }
                        }

                        if(e.getKeyCode() == java.awt.event.KeyEvent.VK_PAGE_UP) {
                            if (tableViewOnViewAndSearchPanel.getRowCount() > 0) {
                                rowID -= 10;
                            }
                        }

                        if(rowID < 0) {
                            rowID = tableViewOnViewAndSearchPanel.getRowCount() - 1;
                        }

                        if(rowID >= tableViewOnViewAndSearchPanel.getRowCount()) {
                            rowID = 0;
                        }

                        if(rowID >= 0 && rowID < tableViewOnViewAndSearchPanel.getRowCount()) {
                            tableViewOnViewAndSearchPanel.changeSelection(rowID, colID, false, false);
                        }
                    }
                }
            });

            searchTextFields[col].addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusLost(java.awt.event.FocusEvent evt) {
                    searchTextFields[thecol].setText("");
                }
                public void focusGained(java.awt.event.FocusEvent evt) {
                    checkDateColumn(thecol);
                }
            });
            x_textfield = x_textfield + textfieldwidth;
        }
    }

    private void setViewPort(JTable tableViewOnViewAndSearchPanel, int iCol, int iTheCol) {
        tableViewOnViewAndSearchPanel.setRowSelectionAllowed(true);
        tableViewOnViewAndSearchPanel.setRowSelectionInterval(iCol, iCol);
        tableViewOnViewAndSearchPanel.changeSelection(iCol, iTheCol, false, false);

        // Get the row at First
        JViewport viewport = (JViewport) tableViewOnViewAndSearchPanel.getParent();
        Rectangle rect = tableViewOnViewAndSearchPanel.getCellRect(iCol, iTheCol, true);
        Rectangle viewRect = viewport.getViewRect();
        rect.setLocation(rect.x - viewRect.x, rect.y - viewRect.y);
        int centerX = (viewRect.width - rect.width);
        int centerY = (viewRect.height - rect.height);
        rect.translate(centerX, centerY);
        viewport.scrollRectToVisible(rect);
    }

    private void checkDateColumn(int thecol) {
        try {
            for(int i = 0; i < DateColumn.length; i++) {
                if (DateColumn[i] == thecol) {
                    selDateColumn = thecol;
                    break;
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void searchTableContents(String searchString, JTable jTable1, Vector originalTableModel) {
        DefaultTableModel currtableModel = (DefaultTableModel) jTable1.getModel();
        // To empty the table before search
        currtableModel.setRowCount(0);
        // To search for contents from original table content
        for (Object rows : originalTableModel) {
            Vector rowVector = (Vector) rows;
            for (Object column : rowVector) {
                if (column.toString().contains(searchString)) {
                    // content found so adding to table
                    currtableModel.addRow(rowVector);
                    break;
                }
            }
        }
    }

    public void setPermission(SmallNavigation navLoad, String form_id) {
        UserRights.setUserRightsToPanel(navLoad, form_id);
    }

    public void setPermission(NavigationPanel1 navLoad, String form_id) {
        UserRights.setUserRightsToPanel(navLoad, form_id);
    }

    public void setPermission(NavigationPanel navLoad, String form_id) {
        UserRights.setUserRightsToPanel(navLoad, form_id);
    }

    public class StatusColumnCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            // Cells are by default rendered as a JLabel.
            JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            l.setHorizontalAlignment(SwingConstants.RIGHT);
            // Get the status for the current row.
            TableModel tableModel = (TableModel) table.getModel();
            if (col == 0) {
                if(!isSelected) {
                    l.setForeground(Color.BLUE);
                }
                l.setFont(l.getFont().deriveFont(Font.BOLD));
            } else if (col == 1) {
                if(!isSelected) {
                    l.setForeground(Color.RED);
                }
                l.setFont(l.getFont().deriveFont(Font.BOLD));
            } else if(col == 2) {
                double val=Double.parseDouble(getDeCustomFormat(tableModel.getValueAt(row,col).toString()));
                if(val >= 0) {
                    l.setForeground(Color.BLUE);
                    l.setText(getIndianFormat(val));
                } else {
                    l.setForeground(Color.RED);
                    l.setText(getIndianFormat(val*-1));
                }
                l.setFont(l.getFont().deriveFont(Font.BOLD));
            }
            // Return the JLabel which renders the cell.
            return l;
        }
    }

    public void searchOnTextFields(final JTextField jtxtSearch, final TableRowSorter<TableModel> rowSorter) {
        jtxtSearch.setFont(new Font("Cambria", 1, 14));

        jtxtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = jtxtSearch.getText();

                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = jtxtSearch.getText();

                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools | Templates.
            }
        });
    }

    // get Invoice Number
    public int getInvNo(String table_name, String column_name, String column_value) {
        try {
            String sql = "SELECT MAX(bill_no * 1) AS bill_no FROM "+ table_name +"";
            if(!column_name.equals("")) {
                sql += " WHERE "+ column_name +"='"+ column_value +"'";
            }
            PreparedStatement pstLocal = dataConnection.prepareStatement(sql);
            ResultSet rsLocal = pstLocal.executeQuery();
            int no = 0;
            if(rsLocal.next()){
                no = rsLocal.getInt(1);
            }
            no++;
            return no;
        } catch(Exception ex){
            printToLogFile("Exception at getInvNO In Library.", ex);
        }
        return 0;
    }

    // create password
    public boolean createPassword() {
        boolean flag = false;
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Enter a Password");
        final JPasswordField pass = new JPasswordField(10);

        KeyListener key = new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    evt.consume();
                    if(evt.getSource() == pass){
                        keyPress(KeyEvent.VK_TAB);
                    }
                }
            }
        };

        pass.addKeyListener(key);
        panel.add(label);
        panel.add(pass);
        String[] options = new String[]{"OK", "Cancel"};
        int option = JOptionPane.showOptionDialog(null, panel, "Authentication",
                JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, pass);
        if (option == 0) { // pressing OK button
            char[] password = pass.getPassword();

            if(new String(password).equalsIgnoreCase(DELETE_PWD)){
                int index = checkAlradyOpen("PASSWORD");
                if(index == -1){
                    flag = true;
                } else {
                    flag = false;
                    tabbedPane.setSelectedIndex(index);
                }
            } else {
                flag = false;
                JOptionPane.showMessageDialog(panel, "Password is not valid");
            }
        }
        pass.requestFocusInWindow();
        return flag;
    }

    public void registerShortKeys(JRootPane jrootPane, final JButton jbtnClose, final JButton jbtnEdit, final JButton jbtnSave, final JButton jbtnPrint) {
        if(!jbtnClose.getText().equals("")) {
            KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
            Action escapeAction = new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    if(!PickList.isVisible){
                        jbtnClose.doClick();
                    }
                }
            };
            jrootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
            jrootPane.getActionMap().put("ESCAPE", escapeAction);
        }

        if(!jbtnEdit.getText().equals("")) {
            KeyStroke editKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_MASK, false);
            Action editKeyAction = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    jbtnEdit.doClick();
                }
            };
            jrootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(editKeyStroke, "Edit");
            jrootPane.getActionMap().put("Edit", editKeyAction);
        }

        if(!jbtnSave.getText().equals("")) {
            KeyStroke saveKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK, false);
            Action saveKeyAction = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    jbtnSave.doClick();
                }
            };
            jrootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(saveKeyStroke, "Save");
            jrootPane.getActionMap().put("Save", saveKeyAction);
        }

        if(!jbtnPrint.getText().equals("")) {
            KeyStroke printKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK, false);
            Action printKeyAction = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    jbtnPrint.doClick();
                }
            };
            jrootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(printKeyStroke, "Print");
            jrootPane.getActionMap().put("Print", printKeyAction);
        }
    }

    public void setDateUsingJTextField(JTextField jtxtDate) {
        try {
            if(jtxtDate.getText().contains("/")) {
                jtxtDate.setText(jtxtDate.getText().replace("/", ""));
            }
            if(jtxtDate.getText().length() == 8) {
                String temp = jtxtDate.getText();
                String setDate = (temp.substring(0, 2)).replace(temp.substring(0, 2), temp.substring(0, 2) +"/") + (temp.substring(2, 4)).replace(temp.substring(2, 4), temp.substring(2, 4) +"/") + temp.substring(4, temp.length());
                jtxtDate.setText(setDate);
            }
        } catch (Exception ex) {
            printToLogFile("Exception at setDate for JTextField in Library", ex);
            jtxtDate.requestFocusInWindow();
        }
    }

    public void setDateUsingJTextField(JTextField jtxtDate, NavigationPanel1 navLoad) {
        try {
            if(jtxtDate.getText().contains("/")) {
                jtxtDate.setText(jtxtDate.getText().replace("/", ""));
            }
            if(jtxtDate.getText().length() == 8) {
                String temp = jtxtDate.getText();
                String setDate = (temp.substring(0, 2)).replace(temp.substring(0, 2), temp.substring(0, 2) +"/") + (temp.substring(2, 4)).replace(temp.substring(2, 4), temp.substring(2, 4) +"/") + temp.substring(4, temp.length());
                jtxtDate.setText(setDate);
            }
        } catch (Exception ex) {
            printToLogFile("Exception at setDate for JTextField in Library", ex);
            if(navLoad != null) {
                navLoad.setMessage(Constants.CORRECT_DATE);
            }
            jtxtDate.requestFocusInWindow();
        }
    }

    public void setDateUsingJTextField(JTextField jtxtDate, JButton jbtnFocus) {
        try {
            if(jtxtDate.getText().contains("/")) {
                jtxtDate.setText(jtxtDate.getText().replace("/", ""));
            }
            if(jtxtDate.getText().length() == 8) {
                String temp = jtxtDate.getText();
                String setDate = (temp.substring(0, 2)).replace(temp.substring(0, 2), temp.substring(0, 2) +"/") + (temp.substring(2, 4)).replace(temp.substring(2, 4), temp.substring(2, 4) +"/") + temp.substring(4, temp.length());
                jtxtDate.setText(setDate);
            }
            if ((new SimpleDateFormat("dd/MM/yyyy").format(new Date(jtxtDate.getText().trim()))) != null) {
                jbtnFocus.requestFocusInWindow();
            }
        } catch (Exception ex) {
            printToLogFile("Exception at setDate for JTextField in Library", ex);
            jbtnFocus.requestFocusInWindow();
        }
    }
    public void setStatusComboBox(JComboBox jcmBox) {
        String sql = "SELECT name FROM status_master ORDER BY order_sequence, id";
        viewDataRs = fetchData(sql);
        jcmBox.removeAllItems();
        try {
            while (viewDataRs.next()) {
                jcmBox.addItem(viewDataRs.getObject(1).toString());
            }
        } catch (Exception e) {
            printToLogFile("Exception at setTaxCombo In Library", e);
        }
    }
    public String getStatusData(String status, String tag) {
        String ans = "";
        if (status == null) {
            return "";
        }
        try {
            String sql = "";
            if (tag.equalsIgnoreCase("N")) {
                sql = "SELECT name FROM status_master WHERE id = ?";
            } else if (tag.equalsIgnoreCase("C")) {
                sql = "SELECT id FROM status_master WHERE name = ?";
            }

            PreparedStatement pstLocal = dataConnection.prepareStatement(sql);
            pstLocal.setString(1, status);
            ResultSet rsLocal = pstLocal.executeQuery();
            if (rsLocal.next()) {
                ans = rsLocal.getString(1);
            }
            closeResultSet(rsLocal);
            closeStatement(pstLocal);
        } catch (Exception ex) {
            printToLogFile("Exception at getAccountCode In Library", ex);
        }
        return ans;
    }
    public String getFunctionData(String strVal, String tag) {
        PreparedStatement pstLocal = null;
        ResultSet rsLocal = null;
        String returnVal = "";
        String sql = "";
        if (strVal.trim().equalsIgnoreCase("") && tag.equalsIgnoreCase("C")) {
            return "0";
        }
        try {
            if (tag.equalsIgnoreCase("C")) {
                sql = "SELECT id FROM function_master WHERE name_en = '"+ strVal +"'";
            } else if (tag.equalsIgnoreCase("N")) {
                sql = "SELECT name_en FROM function_master WHERE id = '"+ strVal +"'";
            }
            if (sql != null) {
                pstLocal = dataConnection.prepareStatement(sql);
                rsLocal = pstLocal.executeQuery();
                while (rsLocal.next()) {
                    returnVal = rsLocal.getString(1);
                }
                closeResultSet(rsLocal);
                closeStatement(pstLocal);
            }
        } catch (Exception ex) {
            printToLogFile("Exception at getFunctionData In Library", ex);
        }
        return returnVal;
    }
}