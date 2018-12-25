/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cateringsoftware;

import static cateringsoftware.MainClass.df;
import java.awt.Color;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import support.SysEnv;
import utility.ChangePassword;
import utility.IPSetting;
import utility.ManageUserView;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import login.CompanySelect;
import master.AccountMaster;
import master.AccountType;
import master.UnitMaster;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.JMenuItem;
import master.TaxMaster;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import master.BankMaster;
import master.FinishItemCommon;
import master.RawMainCategory;
import master.RawMaterialMaster;
import master.RawSubCategory;
import reports.AccountMasterList;
import reports.CheckPrintList;
import support.Constants;
import utility.BackUp;
import utility.Email;
import support.Library;
import support.UnCaughtException;
import utility.ChangeThemes;
import utility.CheckPrint;
import utility.CompanySetting;
import utility.DateSetting;
import utility.ManageEmail;
import utility.QuickOpen;
import utility.Reset;
import utility.UserRights;

/**
 *
 * @author @JD@
 */
public class DeskFrame extends javax.swing.JFrame {
    public static JTabbedPane tabbedPane = new JTabbedPane();
    public static int user_id = 1;
    public static int digit = 2;
    public static String DELETE_PWD = "";
    public static String theme_cd = "";
    private SystemTray systemTray = SystemTray.getSystemTray();
    private TrayIcon trayIcon = null;
    private Toolkit toolkit = Toolkit.getDefaultToolkit();
    public static Connection connMpAdmin = null;
    public static Connection connMpMain = null;
    public static BufferedWriter logFile = null;
    public static String dbConproperty = "dbConnection.properties";
    public static String currentDirectory = System.getProperty("user.dir").toString();
    public static String dbName = "";
    public static SysEnv clSysEnv = new SysEnv();
    public static String TITLE = Constants.SOFTWARE_NAME +" "+ Constants.VERSION;
    private HashMap<Integer, JMenuItem> hashMenu = null;
    private ArrayList<String> hasPermission = new ArrayList<String>();
    public static String ip;
    public static String port;
    public static String db_name;
    public static String driver;
    public static String username;
    public static String password;
    public static String backUpSql;
    public static String sqlBinPath;
    public static Library lb = new Library();
    FileOutputStream errorFile = null;
    public static String date = "";
    public static String dbYear = "", month = "", year = "";
    public static String forms;

    /**
     * Creates new form DeskFrame
     */
    public DeskFrame() {
        initComponents();
        tabbedPane.setBounds(0, 0, jDesktopPane1.getWidth(), jDesktopPane1.getHeight());
        jDesktopPane1.add(tabbedPane);
        tabbedPane.setVisible(true);
        setTrayIcon();
        setShortcut();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(0, 0, (int) screenSize.getWidth(), (int) screenSize.getHeight());
        this.setLocationRelativeTo(null);

        setExtendedState(MAXIMIZED_BOTH);
        setVisibleSelDesl();
        setMenuText();
    }

    public DeskFrame(String ComName) {
        initComponents();
        setTitle(Constants.SOFTWARE_NAME +" "+ Constants.VERSION +" - "+ ComName);
        tabbedPane.setBounds(0, 0, jDesktopPane1.getWidth(), jDesktopPane1.getHeight());
        jDesktopPane1.add(tabbedPane);
        tabbedPane.setVisible(true);
        setTrayIcon();
        setShortcut();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(0, 0, (int) screenSize.getWidth(), (int) screenSize.getHeight());
        this.setLocationRelativeTo(null);

        setExtendedState(MAXIMIZED_BOTH);

        jmnloginActionPerformedRoutine();
        setVisibleSelDesl();
        setMenuText();
    }

    private void setShortcut() {
        JMenuItem newcmpny = new javax.swing.JMenuItem();
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
                if(evt.getKeyCode() == KeyEvent.VK_ENTER){
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

            if(new String(password).equals("india123")){
                int index = checkAlradyOpen(Constants.BACK_UP_FORM_NAME);
                if(index == -1){
//                    NewCompany nc = new NewCompany(MainClass.df, true);
//                    nc.show();
                } else {
                    tabbedPane.setSelectedIndex(index);
                }
            }
        }
        pass.requestFocusInWindow();
    }

    private void setVisibleSelDesl() {
        jmnReset.setVisible(false);
    }

    private void setMenuText() {
        // LOGIN
        jmnLogin.setText(Constants.LOGIN_FORM_NAME);
        jmnLogout.setText(Constants.LOG_OUT_FORM_NAME);
        jmnExit.setText(Constants.EXIT_FORM_NAME);
        jmnMinimize.setText(Constants.MINIMIZE_FORM_NAME);

        // MASTER
        jmnAccountType.setText(Constants.ACCOUNT_TYPE_FORM_NAME);
        jmnAccountMaster.setText(Constants.ACCOUNT_MASTER_FORM_NAME);
        jmnMainCategoryRM.setText(Constants.MAIN_CATEGORY_FORM_NAME);
        jmnSubCategoryRM.setText(Constants.SUB_CATEGORY_FORM_NAME);
        jmnRawMaterialRM.setText(Constants.RAW_MATERIAL_FORM_NAME);
        jmnMainCategoryFI.setText(Constants.MAIN_CATEGORY_FORM_NAME);
        jmnFoodTypeFI.setText(Constants.FOOD_TYPE_FORM_NAME);
        jmnFinishMaterial.setText(Constants.FINISH_MATERIAL_FORM_NAME);
        jmnTimeMaster.setText(Constants.TIME_MASTER_FORM_NAME);
        jmnUnitMaster.setText(Constants.UNIT_MASTER_FORM_NAME);
        jmnMenuTypeMaster.setText(Constants.MENU_TYPE_MASTER_FORM_NAME);
        jmnDressCodeMaster.setText(Constants.DRESS_CODE_MASTER_FORM_ID);
        jmnBankMaster.setText(Constants.BANK_MASTER_FORM_NAME);
        jmnTaxMaster.setText(Constants.TAX_MASTER_FORM_NAME);

        // EVENT
        jmnFunctionMaster.setText(Constants.FUNCTION_MASTER_FORM_NAME);
        jmnEventPackage.setText(Constants.EVENT_PACKAGE_FORM_NAME);

        // REPORT
        jmnCheckPrintReport.setText(Constants.CHECK_PRINT_REPORT_FORM_NAME);
        jmnAccountList.setText(Constants.ACCOUNT_LIST_FORM_NAME);

        // UTILITY
        jmnCompanySetting.setText(Constants.COMPANY_SETTING_FORM_NAME);
        jmnManageUser.setText(Constants.MANAGE_USER_FORM_NAME);
        jmnUserRights.setText(Constants.USER_RIGHTS_FORM_NAME);
        jmnManageEmail.setText(Constants.MANAGE_EMAIL_FORM_NAME);
        jmnChangePassword.setText(Constants.CHANGE_PASSWORD_FORM_NAME);
        jmnChangeDate.setText(Constants.CHANGE_DATE_FORM_NAME);
        jmnQuickOpen.setText(Constants.QUICK_OPEN_FORM_NAME);
        jmnBackUp.setText(Constants.BACK_UP_FORM_NAME);
        jmnReset.setText(Constants.RESET_FORM_NAME);
        jmnEmail.setText(Constants.EMAIL_FORM_NAME);
        jmnCheckPrint.setText(Constants.CHECK_PRINT_FORM_NAME);
        jmnNewYear.setText(Constants.NEW_YEAR_FORM_NAME);
        jmnChangeThemes.setText(Constants.CHANGE_THEMES_FORM_NAME);
    }

    private void jmnloginActionPerformedRoutine() {
        openLogFile();
        currentDirectory = System.getProperty("user.dir");
        setTrayIcon();
        login.setEnabled(false);
        setEnabledDisabledMenu(false);
        CompanySelect cp = new CompanySelect(this);
        addOnScreen(cp, Constants.COMPANY_SELECT);
        cp.setStartupFocus();
    }

    public void callLogOFF() {
        jmnLogout.doClick();
    }

    private void openLogFile() {
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy_hh_mm_ss aaa");
            File folder = new File("LOG");
            if (!folder.exists()) {
                folder.mkdir();
            }
            File localFile = new File(folder, "logFileCatch" + "_" + sdf.format(cal.getTime()) + ".ini");
            FileWriter fw = new FileWriter(localFile);
            logFile = new BufferedWriter(fw);
            File fileName = new File(folder, "logFileUnCaught" + "_" + sdf.format(cal.getTime()) + ".ini");
            errorFile = new FileOutputStream(fileName, true);
            start();
        } catch (Exception ex) {
            lb.printToLogFile("Exception at makeLogFile In DeskFrame...", ex);
        }
    }

    public void start() {
        // Saving the orginal stream
        PrintStream fileStream = new UnCaughtException(errorFile);
        // Redirecting console output to file
        System.setOut(fileStream);
        // Redirecting runtime exceptions to file
        System.setErr(fileStream);
    }

    private void setTrayIcon() {
        String path = System.getProperty("user.dir") + "/Resources/Images/logo.png";
        try {
            if (systemTray.isSupported()) {
                settrayImage(path);
                trayIcon.displayMessage(Constants.SOFTWARE_NAME +" "+ Constants.VERSION, "", TrayIcon.MessageType.INFO);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void getMainConnection() {
        try {
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
            if (DeskFrame.connMpMain != null) {
                MainClass.appConfig.loadDate();

                df = new DeskFrame("");
                java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
                df.setBounds(0, 0, (int) screenSize.getWidth(), (int) screenSize.getHeight());
                df.setLocationRelativeTo(null);
                df.setContentPane(df.tabbedPane);
                df.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Unable to connect Server. Please check Connection Setting", "Connection", JOptionPane.WARNING_MESSAGE);
                IPSetting conSetting = new IPSetting();
                conSetting.setVisible(true);
                java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
                conSetting.setBounds(screenSize.width / 2 - conSetting.getWidth() / 2, screenSize.height / 2 - conSetting.getHeight() / 2, conSetting.getWidth(), conSetting.getHeight());
            }
        } catch (Exception ex) {
            lb.printToLogFile("Exception at getMainConnection In DeskFrame", ex);
        }
    }

    public static Connection getConnection(String db) {
        Connection tempCon = null;
        try {
            Class.forName(driver);
            // Establish network connection to database
            Connection connection = DriverManager.getConnection("jdbc:mysql://"+ ip +":"+ port +"/"+ db +"?user="+ username +"&password="+ password +"&autoReconnect=true&useUnicode=true&characterEncoding=UTF-8", username, password);
            return(connection);
        } catch (Exception ex) {
            lb.printToLogFile("Exception at getConnection In DeskFrame", ex);
        }
        return tempCon;
    }

    private void settrayImage(String path) {
        try {
            File imageFile = new File(path);
            Image image = toolkit.getImage(imageFile.toURI().toURL());
            setIconImage(image);
            removeTrayIcon();
            trayIcon = null;
            trayIcon = new TrayIcon(image);
            trayIcon.setImageAutoSize(true);
            systemTray.add(trayIcon);

            trayIcon.setToolTip(Constants.SOFTWARE_NAME +" "+ Constants.VERSION +" Running");
        } catch (Exception ex) {
            lb.printToLogFile("Exception at setTrayImage In DeskFrame", ex);
        }
    }

    private void removeTrayIcon() {
        if (trayIcon != null) {
            systemTray.remove(trayIcon);
        }
    }

    private void createMenuList() {
        hashMenu = new HashMap<Integer, JMenuItem>();
        // MASTER
        hashMenu.put(Integer.parseInt(Constants.ACCOUNT_TYPE_FORM_ID), jmnAccountType);
        hashMenu.put(Integer.parseInt(Constants.ACCOUNT_MASTER_FORM_ID), jmnAccountMaster);
        hashMenu.put(Integer.parseInt(Constants.MAIN_CATEGORY_RW_FORM_ID), jmnMainCategoryRM);
        hashMenu.put(Integer.parseInt(Constants.SUB_CATEGORY_RW_FORM_ID), jmnSubCategoryRM);
        hashMenu.put(Integer.parseInt(Constants.RAW_MATERIAL_RW_FORM_ID), jmnRawMaterialRM);
        hashMenu.put(Integer.parseInt(Constants.MAIN_CATEGORY_FI_FORM_ID), jmnMainCategoryFI);
        hashMenu.put(Integer.parseInt(Constants.FOOD_TYPE_FI_FORM_ID), jmnFoodTypeFI);
        hashMenu.put(Integer.parseInt(Constants.FINISH_MATERIAL_FI_FORM_ID), jmnFinishMaterial);
        hashMenu.put(Integer.parseInt(Constants.TIME_MASTER_FORM_ID), jmnTimeMaster);
        hashMenu.put(Integer.parseInt(Constants.UNIT_MASTER_FORM_ID), jmnUnitMaster);
        hashMenu.put(Integer.parseInt(Constants.MENU_TYPE_MASTER_FORM_ID), jmnMenuTypeMaster);
        hashMenu.put(Integer.parseInt(Constants.DRESS_CODE_MASTER_FORM_ID), jmnDressCodeMaster);
        hashMenu.put(Integer.parseInt(Constants.BANK_MASTER_FORM_ID), jmnBankMaster);
        hashMenu.put(Integer.parseInt(Constants.TAX_MASTER_FORM_ID), jmnTaxMaster);

        // EVENT
        hashMenu.put(Integer.parseInt(Constants.FUNCTION_MASTER_FORM_ID), jmnFunctionMaster);
        hashMenu.put(Integer.parseInt(Constants.EVENT_PACKAGE_FORM_ID), jmnEventPackage);

        // MENU ORDER
        hashMenu.put(Integer.parseInt(Constants.MAIN_ORDER_FORM_ID), jmnMainOrder);
        hashMenu.put(Integer.parseInt(Constants.ADD_ORDER_FORM_ID), jmnAddOrder);
        hashMenu.put(Integer.parseInt(Constants.ADD_MULTIPLE_ORDER_FORM_ID), jmnAddMultipleOrder);
        hashMenu.put(Integer.parseInt(Constants.ORDER_LIST_FORM_ID), jmnOrderList);

        // REPORT
        hashMenu.put(Integer.parseInt(Constants.CHECK_PRINT_REPORT_FORM_ID), jmnCheckPrintReport);
        hashMenu.put(Integer.parseInt(Constants.ACCOUNT_LIST_FORM_ID), jmnAccountList);

        // UTILITY
        hashMenu.put(Integer.parseInt(Constants.COMPANY_SETTING_FORM_ID), jmnCompanySetting);
        hashMenu.put(Integer.parseInt(Constants.MANAGE_USER_FORM_ID), jmnManageUser);
        hashMenu.put(Integer.parseInt(Constants.USER_RIGHTS_FORM_ID), jmnUserRights);
        hashMenu.put(Integer.parseInt(Constants.MANAGE_EMAIL_FORM_ID), jmnManageEmail);
        hashMenu.put(Integer.parseInt(Constants.CHANGE_PASSWORD_FORM_ID), jmnChangePassword);
        hashMenu.put(Integer.parseInt(Constants.QUICK_OPEN_FORM_ID), jmnQuickOpen);
        hashMenu.put(Integer.parseInt(Constants.BACK_UP_FORM_ID), jmnBackUp);
//        hashMenu.put(Integer.parseInt(Constants.RESET), jmnReset);
        hashMenu.put(Integer.parseInt(Constants.EMAIL_FORM_ID), jmnEmail);
//        hashMenu.put(Integer.parseInt(Constants.EXPORT_DATA), jmnExportData);
        hashMenu.put(Integer.parseInt(Constants.CHECK_PRINT_FORM_ID), jmnCheckPrint);
        hashMenu.put(Integer.parseInt(Constants.NEW_YEAR_FORM_ID), jmnNewYear);
        hashMenu.put(Integer.parseInt(Constants.CHANGE_THEMES_FORM_ID), jmnChangeThemes);
    }

    public boolean hasPermission(String form) {
        return hasPermission.contains(form);
    }

    public void setPermission() {
        createMenuList();
        PreparedStatement psLocal = null;
        ResultSet rsLocal = null;
        try {
            if (hashMenu != null) {
                Set set = hashMenu.entrySet();
                Iterator i = set.iterator();
                while (i.hasNext()) {
                    Map.Entry me = (Map.Entry) i.next();
                    if (me.getValue() != null) {
                        ((JMenuItem) me.getValue()).setVisible(false);
                    }
                }
                forms = "";
                set = hashMenu.entrySet();
                i = set.iterator();
                while (i.hasNext()) {
                    Map.Entry me = (Map.Entry) i.next();
                    forms += (me.getKey().toString() + ",");
                }
                if (!forms.isEmpty()) {
                    forms = forms.substring(0, forms.length() - 1);
                }
                String sql = "SELECT form_cd, views FROM user_rights WHERE user_cd = ? AND views = 1";
                if (!forms.isEmpty()) {
                    sql += " AND form_cd IN (" + forms + ")";
                }
                psLocal = connMpAdmin.prepareStatement(sql);
                psLocal.setInt(1, user_id);
                rsLocal = psLocal.executeQuery();
                hasPermission.clear();

                while(rsLocal.next()) {
                    hasPermission.add(rsLocal.getString("form_cd"));
                    if (hashMenu.get(rsLocal.getInt("form_cd")) != null) {
                        hashMenu.get(rsLocal.getInt("form_cd")).setVisible(true);
                    }
                }
            }
        } catch (Exception ex) {
            lb.printToLogFile("Error at setPermission In DeskFrame", ex);
        } finally {
            lb.closeResultSet(rsLocal);
            lb.closeStatement(psLocal);
        }
    }

    public void setUserRights(boolean flag) {
        jmnLogout.setEnabled(flag);
        jmnLogin.setEnabled(!flag);
        master.setEnabled(flag);
        report.setEnabled(flag);
        utility.setEnabled(flag);

        setPermission();
    }

    public static void removeFromScreen(int index) {
        tabbedPane.removeTabAt(index);
    }

    public static void removeFromScreen(int index, String name) {
        if (!(index == 0 || name.equalsIgnoreCase(Constants.HOME_PAGE))) {
            tabbedPane.removeTabAt(index);
        }
    }

    public void setEnabledDisabledLogin(boolean flag) {
        jmnLogin.setEnabled(flag);
        jmnLogout.setEnabled(!flag);
    }

    public void setEnabledDisabledMenu(boolean flag) {
        master.setEnabled(flag);
        report.setEnabled(flag);
        utility.setEnabled(flag);
    }

    public static int checkAlradyOpen(String Title) {
        double count = tabbedPane.getTabCount();
        for (int i = 0; i < count; i++) {
            if (tabbedPane.getComponentAt(i).getName().equalsIgnoreCase(Title)) {
                System.out.println("Already Open");
                return i;
            }
        }
        return -1;
    }

    public static void addOnScreen(JInternalFrame inFrame, String title) {
        int index = checkAlradyOpen(title);
        if (index == -1) {
            javax.swing.plaf.InternalFrameUI ifu = inFrame.getUI();
            ((javax.swing.plaf.basic.BasicInternalFrameUI) ifu).setNorthPane(null);
            Border b1 = new LineBorder(Color.darkGray, 5) {
            };
            boolean flag = true;
            if (inFrame instanceof ChangePassword || inFrame instanceof ManageUserView || inFrame instanceof CompanySetting || inFrame instanceof TaxMaster || inFrame instanceof AccountType || inFrame instanceof AccountMaster || inFrame instanceof UnitMaster || inFrame instanceof ManageEmail || inFrame instanceof QuickOpen || inFrame instanceof BankMaster || inFrame instanceof CheckPrint || inFrame instanceof RawMainCategory || inFrame instanceof RawSubCategory ||inFrame instanceof RawMaterialMaster) {
                flag = false;
            }
            if (flag) {
                inFrame.setLocation(0, 0);
                inFrame.setSize(jDesktopPane1.getWidth(), jDesktopPane1.getHeight());
            }
            inFrame.setBorder(b1);
            JPanel jp = new JPanel();
            if (flag) {
                jp.setLayout(new GridLayout());
            }
            jp.add(inFrame);
            jp.setBackground(new Color(201, 212, 216));
            if (flag) {
                jp.setSize(jDesktopPane1.getWidth(), jDesktopPane1.getHeight());
            }
            jp.setName(title);
            tabbedPane.addTab(title, jp);
            tabbedPane.setSelectedComponent(jp);
            inFrame.setVisible(true);
            inFrame.requestFocusInWindow();
            tabbedPane.setVisible(true);
        } else {
            tabbedPane.setSelectedIndex(index);
        }
    }

    private void startup() {
        CompanySelect compSelect = new CompanySelect(this);
        addOnScreen(compSelect, Constants.COMPANY_SELECT);
        compSelect.setStartupFocus();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDesktopPane1 = new javax.swing.JDesktopPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        login = new javax.swing.JMenu();
        jmnLogin = new javax.swing.JMenuItem();
        jmnLogout = new javax.swing.JMenuItem();
        jmnExit = new javax.swing.JMenuItem();
        jmnMinimize = new javax.swing.JMenuItem();
        master = new javax.swing.JMenu();
        jmnAccount = new javax.swing.JMenu();
        jmnAccountType = new javax.swing.JMenuItem();
        jmnAccountMaster = new javax.swing.JMenuItem();
        jmnRawMaterialMain = new javax.swing.JMenu();
        jmnMainCategoryRM = new javax.swing.JMenuItem();
        jmnSubCategoryRM = new javax.swing.JMenuItem();
        jmnRawMaterialRM = new javax.swing.JMenuItem();
        jmnFinishItemMain = new javax.swing.JMenu();
        jmnMainCategoryFI = new javax.swing.JMenuItem();
        jmnFoodTypeFI = new javax.swing.JMenuItem();
        jmnFinishMaterial = new javax.swing.JMenuItem();
        jmnTimeMaster = new javax.swing.JMenuItem();
        jmnUnitMaster = new javax.swing.JMenuItem();
        jmnMenuTypeMaster = new javax.swing.JMenuItem();
        jmnDressCodeMaster = new javax.swing.JMenuItem();
        jmnBankMaster = new javax.swing.JMenuItem();
        jmnTaxMaster = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jmnFunctionMaster = new javax.swing.JMenuItem();
        jmnEventPackage = new javax.swing.JMenuItem();
        menuorder = new javax.swing.JMenu();
        jmnMainOrder = new javax.swing.JMenuItem();
        jmnAddOrder = new javax.swing.JMenuItem();
        jmnAddMultipleOrder = new javax.swing.JMenuItem();
        jmnOrderList = new javax.swing.JMenuItem();
        report = new javax.swing.JMenu();
        jmnCheckPrintReport = new javax.swing.JMenuItem();
        jmnAccountList = new javax.swing.JMenuItem();
        utility = new javax.swing.JMenu();
        jmnCompanySetting = new javax.swing.JMenuItem();
        jmnManageUser = new javax.swing.JMenuItem();
        jmnUserRights = new javax.swing.JMenuItem();
        jmnManageEmail = new javax.swing.JMenuItem();
        jmnChangePassword = new javax.swing.JMenuItem();
        jmnChangeDate = new javax.swing.JMenuItem();
        jmnQuickOpen = new javax.swing.JMenuItem();
        jmnBackUp = new javax.swing.JMenuItem();
        jmnReset = new javax.swing.JMenuItem();
        jmnEmail = new javax.swing.JMenuItem();
        jmnCheckPrint = new javax.swing.JMenuItem();
        jmnNewYear = new javax.swing.JMenuItem();
        jmnChangeThemes = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 491, Short.MAX_VALUE)
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 307, Short.MAX_VALUE)
        );

        login.setMnemonic('L');
        login.setText("LOGIN");
        login.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jmnLogin.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        jmnLogin.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnLogin.setMnemonic('L');
        jmnLogin.setText("LOGIN");
        jmnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnLoginActionPerformed(evt);
            }
        });
        login.add(jmnLogin);

        jmnLogout.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jmnLogout.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnLogout.setMnemonic('O');
        jmnLogout.setText("LOG OUT");
        jmnLogout.setEnabled(false);
        jmnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnLogoutActionPerformed(evt);
            }
        });
        login.add(jmnLogout);

        jmnExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, java.awt.event.InputEvent.SHIFT_MASK));
        jmnExit.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnExit.setMnemonic('E');
        jmnExit.setText("EXIT");
        jmnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnExitActionPerformed(evt);
            }
        });
        login.add(jmnExit);

        jmnMinimize.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnMinimize.setMnemonic('E');
        jmnMinimize.setText("MINIMIZE");
        jmnMinimize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnMinimizeActionPerformed(evt);
            }
        });
        login.add(jmnMinimize);

        jMenuBar1.add(login);

        master.setMnemonic('M');
        master.setText("MASTER");
        master.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jmnAccount.setMnemonic('A');
        jmnAccount.setText("ACCOUNT");
        jmnAccount.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        jmnAccountType.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnAccountType.setMnemonic('A');
        jmnAccountType.setText("ACCOUNT TYPE");
        jmnAccountType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnAccountTypeActionPerformed(evt);
            }
        });
        jmnAccount.add(jmnAccountType);

        jmnAccountMaster.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnAccountMaster.setMnemonic('A');
        jmnAccountMaster.setText("ACCOUNT MASTER");
        jmnAccountMaster.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnAccountMasterActionPerformed(evt);
            }
        });
        jmnAccount.add(jmnAccountMaster);

        master.add(jmnAccount);

        jmnRawMaterialMain.setMnemonic('R');
        jmnRawMaterialMain.setText("RAW MATERIAL");
        jmnRawMaterialMain.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        jmnMainCategoryRM.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnMainCategoryRM.setMnemonic('M');
        jmnMainCategoryRM.setText("MAIN CATEGORY");
        jmnMainCategoryRM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnMainCategoryRMActionPerformed(evt);
            }
        });
        jmnRawMaterialMain.add(jmnMainCategoryRM);

        jmnSubCategoryRM.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnSubCategoryRM.setMnemonic('S');
        jmnSubCategoryRM.setText("SUB CATEGORY");
        jmnSubCategoryRM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnSubCategoryRMActionPerformed(evt);
            }
        });
        jmnRawMaterialMain.add(jmnSubCategoryRM);

        jmnRawMaterialRM.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnRawMaterialRM.setMnemonic('R');
        jmnRawMaterialRM.setText("RAW MATERIAL");
        jmnRawMaterialRM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnRawMaterialRMActionPerformed(evt);
            }
        });
        jmnRawMaterialMain.add(jmnRawMaterialRM);

        master.add(jmnRawMaterialMain);

        jmnFinishItemMain.setMnemonic('F');
        jmnFinishItemMain.setText("FINISH ITEM");
        jmnFinishItemMain.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N

        jmnMainCategoryFI.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnMainCategoryFI.setMnemonic('M');
        jmnMainCategoryFI.setText("MAIN CATEGORY");
        jmnMainCategoryFI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnMainCategoryFIActionPerformed(evt);
            }
        });
        jmnFinishItemMain.add(jmnMainCategoryFI);

        jmnFoodTypeFI.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnFoodTypeFI.setMnemonic('F');
        jmnFoodTypeFI.setText("FOOD TYPE");
        jmnFoodTypeFI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnFoodTypeFIActionPerformed(evt);
            }
        });
        jmnFinishItemMain.add(jmnFoodTypeFI);

        jmnFinishMaterial.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnFinishMaterial.setMnemonic('F');
        jmnFinishMaterial.setText("FINISH MATERIAL");
        jmnFinishMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnFinishMaterialActionPerformed(evt);
            }
        });
        jmnFinishItemMain.add(jmnFinishMaterial);

        master.add(jmnFinishItemMain);

        jmnTimeMaster.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnTimeMaster.setMnemonic('T');
        jmnTimeMaster.setText("TIME MASTER");
        jmnTimeMaster.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnTimeMasterActionPerformed(evt);
            }
        });
        master.add(jmnTimeMaster);

        jmnUnitMaster.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnUnitMaster.setMnemonic('U');
        jmnUnitMaster.setText("UNIT MASTER");
        jmnUnitMaster.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnUnitMasterActionPerformed(evt);
            }
        });
        master.add(jmnUnitMaster);

        jmnMenuTypeMaster.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnMenuTypeMaster.setMnemonic('M');
        jmnMenuTypeMaster.setText("MENU TYPE MASTER");
        jmnMenuTypeMaster.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnMenuTypeMasterActionPerformed(evt);
            }
        });
        master.add(jmnMenuTypeMaster);

        jmnDressCodeMaster.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnDressCodeMaster.setMnemonic('D');
        jmnDressCodeMaster.setText("DRESS CODE MASTER");
        jmnDressCodeMaster.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnDressCodeMasterActionPerformed(evt);
            }
        });
        master.add(jmnDressCodeMaster);

        jmnBankMaster.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnBankMaster.setMnemonic('B');
        jmnBankMaster.setText("BANK MASTER");
        jmnBankMaster.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnBankMasterActionPerformed(evt);
            }
        });
        master.add(jmnBankMaster);

        jmnTaxMaster.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnTaxMaster.setMnemonic('T');
        jmnTaxMaster.setText("TAX MASTER");
        jmnTaxMaster.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnTaxMasterActionPerformed(evt);
            }
        });
        master.add(jmnTaxMaster);

        jMenuBar1.add(master);

        jMenu1.setMnemonic('E');
        jMenu1.setText("EVENT");
        jMenu1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jmnFunctionMaster.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnFunctionMaster.setMnemonic('F');
        jmnFunctionMaster.setText("FUNCTION MASTER");
        jMenu1.add(jmnFunctionMaster);

        jmnEventPackage.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnEventPackage.setMnemonic('E');
        jmnEventPackage.setText("EVENT PACKAGE");
        jMenu1.add(jmnEventPackage);

        jMenuBar1.add(jMenu1);

        menuorder.setMnemonic('M');
        menuorder.setText("MENU ORDER");
        menuorder.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jmnMainOrder.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnMainOrder.setMnemonic('M');
        jmnMainOrder.setText("MAIN ORDER");
        jmnMainOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnMainOrderActionPerformed(evt);
            }
        });
        menuorder.add(jmnMainOrder);

        jmnAddOrder.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnAddOrder.setMnemonic('A');
        jmnAddOrder.setText("ADD ORDER");
        jmnAddOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnAddOrderActionPerformed(evt);
            }
        });
        menuorder.add(jmnAddOrder);

        jmnAddMultipleOrder.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnAddMultipleOrder.setMnemonic('A');
        jmnAddMultipleOrder.setText("ADD MULTIPLE ORDER");
        jmnAddMultipleOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnAddMultipleOrderActionPerformed(evt);
            }
        });
        menuorder.add(jmnAddMultipleOrder);

        jmnOrderList.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnOrderList.setMnemonic('O');
        jmnOrderList.setText("ORDER LIST");
        jmnOrderList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnOrderListActionPerformed(evt);
            }
        });
        menuorder.add(jmnOrderList);

        jMenuBar1.add(menuorder);

        report.setMnemonic('R');
        report.setText("REPORTS");
        report.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jmnCheckPrintReport.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnCheckPrintReport.setMnemonic('C');
        jmnCheckPrintReport.setText("CHECK PRINT REPORT");
        jmnCheckPrintReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnCheckPrintReportActionPerformed(evt);
            }
        });
        report.add(jmnCheckPrintReport);

        jmnAccountList.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnAccountList.setMnemonic('A');
        jmnAccountList.setText("ACCOUNT LIST");
        jmnAccountList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnAccountListActionPerformed(evt);
            }
        });
        report.add(jmnAccountList);

        jMenuBar1.add(report);

        utility.setMnemonic('U');
        utility.setText("UTILITY");
        utility.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jmnCompanySetting.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnCompanySetting.setMnemonic('C');
        jmnCompanySetting.setText("COMPANY SETTING");
        jmnCompanySetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnCompanySettingActionPerformed(evt);
            }
        });
        utility.add(jmnCompanySetting);

        jmnManageUser.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnManageUser.setMnemonic('M');
        jmnManageUser.setText("MANAGE USER");
        jmnManageUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnManageUserActionPerformed(evt);
            }
        });
        utility.add(jmnManageUser);

        jmnUserRights.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnUserRights.setMnemonic('U');
        jmnUserRights.setText("USER RIGHTS");
        jmnUserRights.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnUserRightsActionPerformed(evt);
            }
        });
        utility.add(jmnUserRights);

        jmnManageEmail.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnManageEmail.setMnemonic('M');
        jmnManageEmail.setText("MANAGE EMAIL");
        jmnManageEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnManageEmailActionPerformed(evt);
            }
        });
        utility.add(jmnManageEmail);

        jmnChangePassword.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnChangePassword.setMnemonic('C');
        jmnChangePassword.setText("CHANGE PASSWORD");
        jmnChangePassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnChangePasswordActionPerformed(evt);
            }
        });
        utility.add(jmnChangePassword);

        jmnChangeDate.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.ALT_MASK));
        jmnChangeDate.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnChangeDate.setMnemonic('D');
        jmnChangeDate.setText("CHANGE DATE");
        jmnChangeDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnChangeDateActionPerformed(evt);
            }
        });
        utility.add(jmnChangeDate);

        jmnQuickOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        jmnQuickOpen.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnQuickOpen.setMnemonic('Q');
        jmnQuickOpen.setText("QUICK OPEN");
        jmnQuickOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnQuickOpenActionPerformed(evt);
            }
        });
        utility.add(jmnQuickOpen);

        jmnBackUp.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnBackUp.setMnemonic('B');
        jmnBackUp.setText("BACK UP");
        jmnBackUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnBackUpActionPerformed(evt);
            }
        });
        utility.add(jmnBackUp);

        jmnReset.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnReset.setMnemonic('R');
        jmnReset.setText("RESET");
        jmnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnResetActionPerformed(evt);
            }
        });
        utility.add(jmnReset);

        jmnEmail.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnEmail.setMnemonic('E');
        jmnEmail.setText("EMAIL");
        jmnEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnEmailActionPerformed(evt);
            }
        });
        utility.add(jmnEmail);

        jmnCheckPrint.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnCheckPrint.setMnemonic('C');
        jmnCheckPrint.setText("CHECK PRINT");
        jmnCheckPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnCheckPrintActionPerformed(evt);
            }
        });
        utility.add(jmnCheckPrint);

        jmnNewYear.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnNewYear.setMnemonic('N');
        jmnNewYear.setText("NEW YEAR");
        jmnNewYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnNewYearActionPerformed(evt);
            }
        });
        utility.add(jmnNewYear);

        jmnChangeThemes.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jmnChangeThemes.setMnemonic('C');
        jmnChangeThemes.setText("CHANGE THEMES");
        jmnChangeThemes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmnChangeThemesActionPerformed(evt);
            }
        });
        utility.add(jmnChangeThemes);

        jMenuBar1.add(utility);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jDesktopPane1)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jmnAccountTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnAccountTypeActionPerformed
        int index = checkAlradyOpen(Constants.ACCOUNT_TYPE_FORM_NAME);
        if (index == -1) {
            AccountType gm = new AccountType();
            addOnScreen(gm, Constants.ACCOUNT_TYPE_FORM_NAME);
            gm.setTitle(Constants.ACCOUNT_TYPE_FORM_NAME);
        } else {
            tabbedPane.setSelectedIndex(index);
        }
    }//GEN-LAST:event_jmnAccountTypeActionPerformed

    private void jmnAccountMasterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnAccountMasterActionPerformed
        int index = checkAlradyOpen(Constants.ACCOUNT_MASTER_FORM_NAME);
        if (index == -1) {
            AccountMaster am = new AccountMaster();
            addOnScreen(am, Constants.ACCOUNT_MASTER_FORM_NAME);
            am.setTitle(Constants.ACCOUNT_MASTER_FORM_NAME);
        } else {
            tabbedPane.setSelectedIndex(index);
        }
    }//GEN-LAST:event_jmnAccountMasterActionPerformed

    private void jmnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnExitActionPerformed
        if (JOptionPane.showConfirmDialog(null, "Do you want to exit from system?", "Exit", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_jmnExitActionPerformed

    private void jmnChangePasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnChangePasswordActionPerformed
        int index = checkAlradyOpen(Constants.CHANGE_PASSWORD_FORM_NAME);
        if (index == -1) {
            ChangePassword cp = new ChangePassword();
            addOnScreen(cp, Constants.CHANGE_PASSWORD_FORM_NAME);
            cp.setTitle(Constants.CHANGE_PASSWORD_FORM_NAME);
        } else {
            tabbedPane.setSelectedIndex(index);
        }
    }//GEN-LAST:event_jmnChangePasswordActionPerformed

    private void jmnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnLoginActionPerformed
        startup();
        jmnLogout.setEnabled(true);
        jmnLogin.setEnabled(false);
    }//GEN-LAST:event_jmnLoginActionPerformed

    private void jmnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnLogoutActionPerformed
        tabbedPane.removeAll();
        setEnabledDisabledLogin(true);
        setEnabledDisabledMenu(false);
        setTitle(TITLE);
    }//GEN-LAST:event_jmnLogoutActionPerformed

    private void jmnManageUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnManageUserActionPerformed
        int index = checkAlradyOpen(Constants.MANAGE_USER_FORM_NAME);
        if (index == -1) {
            ManageUserView mu = new ManageUserView();
            addOnScreen(mu, Constants.MANAGE_USER_FORM_NAME);
            mu.setTitle(Constants.MANAGE_USER_FORM_NAME);
        } else {
            tabbedPane.setSelectedIndex(index);
        }
    }//GEN-LAST:event_jmnManageUserActionPerformed

    private void jmnUnitMasterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnUnitMasterActionPerformed
        int index = checkAlradyOpen(Constants.UNIT_MASTER_FORM_NAME);
        if (index == -1) {
            UnitMaster um = new UnitMaster();
            addOnScreen(um, Constants.UNIT_MASTER_FORM_NAME);
            um.setTitle(Constants.UNIT_MASTER_FORM_NAME);
        } else {
            tabbedPane.setSelectedIndex(index);
        }
    }//GEN-LAST:event_jmnUnitMasterActionPerformed

    private void jmnChangeDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnChangeDateActionPerformed
        int index = checkAlradyOpen(Constants.CHANGE_DATE_FORM_NAME);
        if (index == -1) {
            DateSetting ds = new DateSetting();
            addOnScreen(ds, Constants.CHANGE_DATE_FORM_NAME);
            ds.setTitle(Constants.CHANGE_DATE_FORM_NAME);
        } else {
            tabbedPane.setSelectedIndex(index);
        }
    }//GEN-LAST:event_jmnChangeDateActionPerformed

    private void jmnCompanySettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnCompanySettingActionPerformed
        int index = checkAlradyOpen(Constants.COMPANY_SETTING_FORM_NAME);
        if (index == -1) {
            CompanySetting cs = new CompanySetting();
            addOnScreen(cs, Constants.COMPANY_SETTING_FORM_NAME);
            cs.setTitle(Constants.COMPANY_SETTING_FORM_NAME);
        } else {
            tabbedPane.setSelectedIndex(index);
        }
    }//GEN-LAST:event_jmnCompanySettingActionPerformed

    private void jmnTaxMasterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnTaxMasterActionPerformed
        int index = checkAlradyOpen(Constants.TAX_MASTER_FORM_NAME);
        if(index == -1) {
            TaxMaster tm = new TaxMaster();
            addOnScreen(tm, Constants.TAX_MASTER_FORM_NAME);
            tm.setTitle(Constants.TAX_MASTER_FORM_NAME);
        } else {
            tabbedPane.setSelectedIndex(index);
        }
    }//GEN-LAST:event_jmnTaxMasterActionPerformed

    private void jmnManageEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnManageEmailActionPerformed
        int index = checkAlradyOpen(Constants.MANAGE_EMAIL_FORM_NAME);
        if(index == -1) {
            ManageEmail tm = new ManageEmail();
            addOnScreen(tm, Constants.MANAGE_EMAIL_FORM_NAME);
            tm.setTitle(Constants.MANAGE_EMAIL_FORM_NAME);
        } else {
            tabbedPane.setSelectedIndex(index);
        }
    }//GEN-LAST:event_jmnManageEmailActionPerformed

    private void jmnUserRightsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnUserRightsActionPerformed
        int index = checkAlradyOpen(Constants.USER_RIGHTS_FORM_NAME);
        if (index == -1) {
            UserRights permission = new UserRights();
            addOnScreen(permission, Constants.USER_RIGHTS_FORM_NAME);
            permission.setTitle(Constants.USER_RIGHTS_FORM_NAME);
        } else {
            tabbedPane.setSelectedIndex(index);
        }
    }//GEN-LAST:event_jmnUserRightsActionPerformed

    private void jmnQuickOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnQuickOpenActionPerformed
        int index = checkAlradyOpen(Constants.QUICK_OPEN_FORM_NAME);
        if(index == -1){
            QuickOpen qp = new QuickOpen();
            addOnScreen(qp, Constants.QUICK_OPEN_FORM_NAME);
            qp.setFocus();
        } else {
            tabbedPane.setSelectedIndex(index);
        }
    }//GEN-LAST:event_jmnQuickOpenActionPerformed

    private void jmnBackUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnBackUpActionPerformed
        int index = checkAlradyOpen(Constants.BACK_UP_FORM_NAME);
        if(index == -1) {
            BackUp bu = new BackUp(MainClass.df, true);
            bu.show();
        } else {
            tabbedPane.setSelectedIndex(index);
        }
    }//GEN-LAST:event_jmnBackUpActionPerformed

    private void jmnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnResetActionPerformed
        int index = checkAlradyOpen(Constants.RESET_FORM_NAME);
        if(index == -1) {
            Reset bu = new Reset(MainClass.df, true);
            bu.show();
        } else {
            tabbedPane.setSelectedIndex(index);
        }
    }//GEN-LAST:event_jmnResetActionPerformed

    private void jmnEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnEmailActionPerformed
        int index = checkAlradyOpen(Constants.EMAIL_FORM_NAME);
        if (index == -1) {
            Email em = new Email();
            addOnScreen(em, Constants.EMAIL_FORM_NAME);
            em.setTitle(Constants.EMAIL_FORM_NAME);
        } else {
            tabbedPane.setSelectedIndex(index);
        }
    }//GEN-LAST:event_jmnEmailActionPerformed

    private void jmnBankMasterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnBankMasterActionPerformed
        int index = checkAlradyOpen(Constants.BANK_MASTER_FORM_NAME);
        if (index == -1) {
            BankMaster bm = new BankMaster();
            addOnScreen(bm, Constants.BANK_MASTER_FORM_NAME);
            bm.setTitle(Constants.BANK_MASTER_FORM_NAME);
            bm.setStartupFocus();
        } else {
            tabbedPane.setSelectedIndex(index);
        }
    }//GEN-LAST:event_jmnBankMasterActionPerformed

    private void jmnCheckPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnCheckPrintActionPerformed
        int index = checkAlradyOpen(Constants.CHECK_PRINT_FORM_NAME);
        if (index == -1) {
            CheckPrint cp = new CheckPrint();
            addOnScreen(cp, Constants.CHECK_PRINT_FORM_NAME);
            cp.setTitle(Constants.CHECK_PRINT_FORM_NAME);
            cp.setStartupFocus();
        } else {
            tabbedPane.setSelectedIndex(index);
        }
    }//GEN-LAST:event_jmnCheckPrintActionPerformed

    private void jmnNewYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnNewYearActionPerformed
        int index = checkAlradyOpen(Constants.NEW_YEAR_FORM_NAME);
        if(index == -1){
//            NewYear ny = new NewYear(MainClass.df, true);
//            ny.show();
        } else {
            tabbedPane.setSelectedIndex(index);
        }
    }//GEN-LAST:event_jmnNewYearActionPerformed

    private void jmnMinimizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnMinimizeActionPerformed
        df.setState(Frame.ICONIFIED);
    }//GEN-LAST:event_jmnMinimizeActionPerformed

    private void jmnChangeThemesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnChangeThemesActionPerformed
        int index = checkAlradyOpen(Constants.CHANGE_THEMES_FORM_NAME);
        if (index == -1) {
            ChangeThemes ct = new ChangeThemes();
            addOnScreen(ct, Constants.CHANGE_THEMES_FORM_NAME);
            ct.setTitle(Constants.CHANGE_THEMES_FORM_NAME);
        } else {
            tabbedPane.setSelectedIndex(index);
        }
    }//GEN-LAST:event_jmnChangeThemesActionPerformed

    private void jmnTimeMasterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnTimeMasterActionPerformed
        
    }//GEN-LAST:event_jmnTimeMasterActionPerformed

    private void jmnMenuTypeMasterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnMenuTypeMasterActionPerformed
        
    }//GEN-LAST:event_jmnMenuTypeMasterActionPerformed

    private void jmnDressCodeMasterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnDressCodeMasterActionPerformed
        
    }//GEN-LAST:event_jmnDressCodeMasterActionPerformed

    private void jmnCheckPrintReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnCheckPrintReportActionPerformed
        int index = checkAlradyOpen(Constants.CHECK_PRINT_REPORT_FORM_NAME);
        if (index == -1) {
            CheckPrintList pb = new CheckPrintList();
            addOnScreen(pb, Constants.CHECK_PRINT_REPORT_FORM_NAME);
            pb.setTitle(Constants.CHECK_PRINT_REPORT_FORM_NAME);
        } else {
            tabbedPane.setSelectedIndex(index);
        }
    }//GEN-LAST:event_jmnCheckPrintReportActionPerformed

    private void jmnAccountListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnAccountListActionPerformed
        int index = checkAlradyOpen(Constants.ACCOUNT_LIST_FORM_NAME);
        if (index == -1) {
            AccountMasterList am = new AccountMasterList();
            addOnScreen(am, Constants.ACCOUNT_LIST_FORM_NAME);
            am.setTitle(Constants.ACCOUNT_LIST_FORM_NAME);
        } else {
            tabbedPane.setSelectedIndex(index);
        }
    }//GEN-LAST:event_jmnAccountListActionPerformed

    private void jmnMainCategoryRMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnMainCategoryRMActionPerformed
        int index = checkAlradyOpen(Constants.MAIN_CATEGORY_FORM_NAME);
        if (index == -1) {
            RawMainCategory am = new RawMainCategory();
            addOnScreen(am, Constants.MAIN_CATEGORY_FORM_NAME);
            am.setTitle(Constants.MAIN_CATEGORY_FORM_NAME);
        } else {
            tabbedPane.setSelectedIndex(index);
        }        
    }//GEN-LAST:event_jmnMainCategoryRMActionPerformed

    private void jmnSubCategoryRMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnSubCategoryRMActionPerformed
        int index = checkAlradyOpen(Constants.SUB_CATEGORY_FORM_NAME);
        if (index == -1) {
            RawSubCategory am = new RawSubCategory();
            addOnScreen(am, Constants.SUB_CATEGORY_FORM_NAME);
            am.setTitle(Constants.SUB_CATEGORY_FORM_NAME);
        } else {
            tabbedPane.setSelectedIndex(index);
        }
    }//GEN-LAST:event_jmnSubCategoryRMActionPerformed

    private void jmnRawMaterialRMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnRawMaterialRMActionPerformed
        int index = checkAlradyOpen(Constants.RAW_MATERIAL_FORM_NAME);
        if (index == -1) {
            RawMaterialMaster am = new RawMaterialMaster();
            addOnScreen(am, Constants.RAW_MATERIAL_FORM_NAME);
            am.setTitle(Constants.RAW_MATERIAL_FORM_NAME);
        } else {
            tabbedPane.setSelectedIndex(index);
        }
    }//GEN-LAST:event_jmnRawMaterialRMActionPerformed

    private void jmnMainCategoryFIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnMainCategoryFIActionPerformed
        initFinishItemCommonFrame(Constants.Tables.FINISH_ITEM);
    }//GEN-LAST:event_jmnMainCategoryFIActionPerformed

    private void jmnFoodTypeFIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnFoodTypeFIActionPerformed
        initFinishItemCommonFrame(Constants.Tables.FOOD_TYPE);
    }//GEN-LAST:event_jmnFoodTypeFIActionPerformed

    private void jmnFinishMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnFinishMaterialActionPerformed
        
    }//GEN-LAST:event_jmnFinishMaterialActionPerformed

    private void jmnMainOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnMainOrderActionPerformed
        
    }//GEN-LAST:event_jmnMainOrderActionPerformed

    private void jmnAddOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnAddOrderActionPerformed
        
    }//GEN-LAST:event_jmnAddOrderActionPerformed

    private void jmnAddMultipleOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnAddMultipleOrderActionPerformed
        
    }//GEN-LAST:event_jmnAddMultipleOrderActionPerformed

    private void jmnOrderListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmnOrderListActionPerformed
        
    }//GEN-LAST:event_jmnOrderListActionPerformed

    private void initFinishItemCommonFrame(Constants.Tables table) {
        int index = checkAlradyOpen(table.FRAME_TITLE);
        if (index == -1) {
            FinishItemCommon finishItemCommon;
            try {
               finishItemCommon = new FinishItemCommon(table);
               addOnScreen(finishItemCommon, table.FRAME_TITLE);
               finishItemCommon.setTitle(table.FRAME_TITLE);
            } catch (Exception e) {
                lb.printToLogFile("Exception while opening tab " +table.FRAME_TITLE+ " In DeskFrame...", e);
            }
        } else {
            tabbedPane.setSelectedIndex(index);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jmnAccount;
    private javax.swing.JMenuItem jmnAccountList;
    private javax.swing.JMenuItem jmnAccountMaster;
    private javax.swing.JMenuItem jmnAccountType;
    private javax.swing.JMenuItem jmnAddMultipleOrder;
    private javax.swing.JMenuItem jmnAddOrder;
    private javax.swing.JMenuItem jmnBackUp;
    public static javax.swing.JMenuItem jmnBankMaster;
    private javax.swing.JMenuItem jmnChangeDate;
    private javax.swing.JMenuItem jmnChangePassword;
    public static javax.swing.JMenuItem jmnChangeThemes;
    public static javax.swing.JMenuItem jmnCheckPrint;
    private javax.swing.JMenuItem jmnCheckPrintReport;
    private javax.swing.JMenuItem jmnCompanySetting;
    private javax.swing.JMenuItem jmnDressCodeMaster;
    private javax.swing.JMenuItem jmnEmail;
    private javax.swing.JMenuItem jmnEventPackage;
    private javax.swing.JMenuItem jmnExit;
    private javax.swing.JMenu jmnFinishItemMain;
    private javax.swing.JMenuItem jmnFinishMaterial;
    private javax.swing.JMenuItem jmnFoodTypeFI;
    private javax.swing.JMenuItem jmnFunctionMaster;
    private javax.swing.JMenuItem jmnLogin;
    private javax.swing.JMenuItem jmnLogout;
    private javax.swing.JMenuItem jmnMainCategoryFI;
    private javax.swing.JMenuItem jmnMainCategoryRM;
    private javax.swing.JMenuItem jmnMainOrder;
    private javax.swing.JMenuItem jmnManageEmail;
    private javax.swing.JMenuItem jmnManageUser;
    private javax.swing.JMenuItem jmnMenuTypeMaster;
    private javax.swing.JMenuItem jmnMinimize;
    public static javax.swing.JMenuItem jmnNewYear;
    private javax.swing.JMenuItem jmnOrderList;
    private javax.swing.JMenuItem jmnQuickOpen;
    private javax.swing.JMenu jmnRawMaterialMain;
    private javax.swing.JMenuItem jmnRawMaterialRM;
    private javax.swing.JMenuItem jmnReset;
    private javax.swing.JMenuItem jmnSubCategoryRM;
    private javax.swing.JMenuItem jmnTaxMaster;
    private javax.swing.JMenuItem jmnTimeMaster;
    private javax.swing.JMenuItem jmnUnitMaster;
    private javax.swing.JMenuItem jmnUserRights;
    public javax.swing.JMenu login;
    private javax.swing.JMenu master;
    private javax.swing.JMenu menuorder;
    private javax.swing.JMenu report;
    private javax.swing.JMenu utility;
    // End of variables declaration//GEN-END:variables
}