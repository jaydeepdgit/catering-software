/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cateringsoftware;

import com.jtattoo.plaf.acryl.AcrylLookAndFeel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Properties;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JWindow;
import static cateringsoftware.DeskFrame.backUpSql;
import static cateringsoftware.DeskFrame.connMpMain;
import static cateringsoftware.DeskFrame.dbConproperty;
import static cateringsoftware.DeskFrame.db_name;
import static cateringsoftware.DeskFrame.driver;
import static cateringsoftware.DeskFrame.getConnection;
import static cateringsoftware.DeskFrame.ip;
import static cateringsoftware.DeskFrame.password;
import static cateringsoftware.DeskFrame.port;
import static cateringsoftware.DeskFrame.sqlBinPath;
import static cateringsoftware.DeskFrame.username;
import support.AppConfig;
import support.EmailConfigBean;

/**
 *
 * @author @JD@
 */
class SplashScreen extends JWindow {
    private int duration;

    public SplashScreen(int d) {
        duration = d;
        ImageIcon icon = null;
        JPanel content = (JPanel) getContentPane();
        content.setBackground(Color.WHITE);
        icon = new javax.swing.ImageIcon(getClass().getResource("/cateringsoftware/logo.jpg"));
        int width = icon.getIconWidth();
        int height = icon.getIconHeight();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - width) / 2;
        int y = (screen.height - height) / 2;
        setBounds(x, y, width, height);

        content.add(new JLabel(icon), BorderLayout.CENTER);
        Color oraRed = new Color(156, 20, 20, 255);
        content.setBorder(BorderFactory.createLineBorder(oraRed, 1));

        setVisible(true);
        try {
            File logDir = new File(System.getProperty("user.dir") +"/Log");
            if(logDir.isDirectory()) {
                for(File temp: logDir.listFiles()) {
                    if(temp.length() <= 0) {
                        temp.delete();
                    }
                }
            }
            Thread.sleep(duration);
        } catch (Exception e) {
            System.out.println("Error at log file delete : " + e.getMessage());
        }
        setVisible(false);
        try {
            ValidateLicence vl = new ValidateLicence();
            int valid = vl.checkValidateLicence();
            operationState(valid);
        } catch(SQLException ex) {
            System.out.println("Exception at "+ ex);
        }
    }

    private void operationState(int state) {
        switch (state) {
            case 0: {
                JOptionPane.showMessageDialog(null, "Unregistered User", DeskFrame.TITLE, JOptionPane.ERROR_MESSAGE);
                System.exit(0);
                break;
            }
            case 1: {
                this.dispose();
                break;
            }
            case 2: {
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                String msg = "Your Due Date is " + sdf.format(MainClass.mainApp.getDueDate()) + "\nKindly request you to renew the Software\nYour License No is " + MainClass.mainApp.getLicense();
                JOptionPane.showMessageDialog(null, msg, DeskFrame.TITLE + " Renew Notification", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                break;
            }
            case 3: {
                JOptionPane.showMessageDialog(null, "Your license has expire. Please renew it\nYour License No is " + MainClass.mainApp.getLicense(), DeskFrame.TITLE + " Renew Notification", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
                break;
            }
            case 4: {
                this.dispose();
                break;
            }
            case 5: {
                JOptionPane.showMessageDialog(null, "Invalide System Date.\nPlease check your system date", DeskFrame.TITLE, JOptionPane.ERROR_MESSAGE);
                System.exit(0);
                break;
            }
            case 7: {
                JOptionPane.showMessageDialog(null, "Invalide System to run this software.", DeskFrame.TITLE, JOptionPane.ERROR_MESSAGE);
                System.exit(0);
                break;
            }
        }
    }

    public void dispose() {
        DeskFrame.getMainConnection();
    }
}

public class MainClass {
    /**
     * @param args the command line arguments
     */
    public static DeskFrame df;
    public static AppConfig appConfig = new AppConfig();
    public static EmailConfigBean ecBean = new EmailConfigBean();
    public static LicenceConfig mainApp = null;

    public static void main(String[] args) {
        try {
            PreparedStatement psLocal = null;
            ResultSet rsLocal = null;
            Properties property = new Properties();
            property.load(new FileReader(new File(dbConproperty)));
            ip = property.getProperty("ip");
            port = property.getProperty("port");
            db_name = property.getProperty("db_name");
            driver = property.getProperty("driverClassName");
            username = property.getProperty("db_username");
            password = property.getProperty("db_password");
            backUpSql = property.getProperty("backUpSql");
            sqlBinPath = property.getProperty("sqlBinPath");
            connMpMain = getConnection(db_name);
            String theme = "com.jtattoo.plaf.acryl.AcrylLookAndFeel";
            if (connMpMain != null) {
                appConfig.loadDate();
                psLocal = connMpMain.prepareStatement("SELECT * FROM change_themes WHERE id = "+ DeskFrame.theme_cd);
                rsLocal = psLocal.executeQuery();
                if (rsLocal.next()) {
                    theme = rsLocal.getString("theme_link");
                }
            }
            Properties property1 = new Properties();
            property1.put("logoString", "");
            AcrylLookAndFeel.setTheme(property1);
            javax.swing.UIManager.setLookAndFeel(theme);
            SplashScreen ss = new SplashScreen(5000);
        } catch (Exception e) {
            e.getMessage();
        }
    }
}