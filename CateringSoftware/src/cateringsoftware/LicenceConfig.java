/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cateringsoftware;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author @JD@
 */
public class LicenceConfig implements Serializable {
    private String clientID;
    private String license_no;
    private String act_cd;
    private String email;
    private String server;
    private String user;
    private String pass;
    private String ipAddr;
    private String hwAddr;
    private int status;
    private int month;
    private int day;
    private int appLimit = 0;
    private int currentLimit = 0;
    private int mode = 0;
    private Date issue_date;
    private Date act_date;
    private Date due_date;
    private Date cur_date;
    private String logDEF;
    private Properties property = null;

    public static final int BASIC = 0;
    public static final int STANDARD = 1;
    public static final int PREMIUM = 2;

    public static final int DEMO = 0;
    public static final int ACTIVE = 1;
    public static final int INACTIVE = -1;

    public LicenceConfig() {
        getDabaseInfo();
    }
    /* SETTER METHOD */

    private void getDabaseInfo() {
        File f = new File(System.getProperty("user.dir") + File.separator +"dbConnection.properties");
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(f);
            property = new Properties();
            property.load(fis);
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            if(fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    Logger.getLogger(LicenceConfig.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private Connection getConnection() {
        Connection main = null;
        try {
            Class.forName(property.getProperty("driverClassName")).newInstance();
            main = DriverManager.getConnection(property.getProperty("connectionURL"), property.getProperty("db_username"), property.getProperty("db_password"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return main;
    }

    public void setProperty(Properties property) {
        this.property = property;
    }

    public void setClientID(String client) {
        this.clientID = client;
    }

    public void setLicense(String license) {
        this.license_no = license;
    }

    public void setActivationCode(String accode) {
        this.act_cd = accode;
    }

    public void setEmail(String mail) {
        this.email = mail;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String pass) {
        this.pass = pass;
    }

    public void setIssueDate(Date date) {
        this.issue_date = date;
    }

    public void setActivationDate(Date date) {
        this.act_date = date;
    }

    public void setDueDate(Date date) {
        this.due_date = date;
    }

    public void setLastStartDate(Date date) {
        this.cur_date = date;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getLogDEF() {
        return logDEF;
    }

    public void setLogDEF(String logDEF) {
        this.logDEF = logDEF;
    }

    public int getAppLimit() {
        return appLimit;
    }

    public void setAppLimit(int appLimit) {
        this.appLimit = appLimit;
    }

    public int getCurrentLimit() {
        return currentLimit;
    }

    public void setCurrentLimit(int currentLimit) {
        this.currentLimit = currentLimit;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getHwAddr() {
        return hwAddr;
    }

    public void setHwAddr(String hwAddr) {
        this.hwAddr = hwAddr;
    }

    public void updateLimitCount() throws SQLException {
        PreparedStatement psLocal = null;
        Connection main = getConnection();
        try {
            if (main != null) {
                psLocal = main.prepareStatement("UPDATE appconfig SET cur_limit = cur_limit + 1");
                currentLimit += psLocal.executeUpdate();
            }
            if (psLocal != null) {
                psLocal.close();
            }
            shutDownDatabase(property.getProperty("connectionURL"), main);
            if (main != null) {
                main.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void loadDate() throws SQLException {
        PreparedStatement psLocal = null;
        ResultSet rsLocal = null;
        Connection main = getConnection();
        if (main != null) {
            psLocal = main.prepareStatement("SELECT * FROM appconfig");
            rsLocal = psLocal.executeQuery();

            if (rsLocal.next()) {
                clientID = rsLocal.getString("client_id");
                status = rsLocal.getInt("status");
                month = rsLocal.getInt("months");
                day = rsLocal.getInt("days");
                appLimit = rsLocal.getInt("app_limit");
                currentLimit = rsLocal.getInt("cur_limit");
                mode = rsLocal.getInt("tmode");
                issue_date = rsLocal.getDate("issue_date");
                act_date = rsLocal.getDate("act_date");
                due_date = rsLocal.getDate("due_date");
                act_cd = rsLocal.getString("act_cd");
                license_no = rsLocal.getString("license_no");
                email = rsLocal.getString("email");
                server = rsLocal.getString("server");
                user = rsLocal.getString("user_nm");
                pass = rsLocal.getString("pass");
                cur_date = rsLocal.getDate("laststart");
                logDEF = rsLocal.getString("def");
                ipAddr = rsLocal.getString("iaddr");
                hwAddr = rsLocal.getString("haddr");
            }
        }

        if (rsLocal != null) {
            rsLocal.close();
        }
        if (psLocal != null) {
            psLocal.close();
        }
        if (main != null) {
            main.close();
        }
    }

    public void shutDownDatabase(String path, Connection con) {
        try {
            Class.forName(property.getProperty("driverClassName"));
            con.close();
            DriverManager.getConnection(path+";shutdown=true");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void saveData() throws SQLException {
        PreparedStatement psLocal = null;
        ResultSet rsLocal = null;
        Connection main = getConnection();

        if (main != null) {
            main.setAutoCommit(false);;
            psLocal = main.prepareStatement("UPDATE appconfig SET client_id=?, issue_date=?, act_date=?, due_date=?, act_cd=?, license_no=?, email=?, server=?, user_nm=?, pass=?, status=?, months=?, days=?, laststart=?, def=?, app_limit=?, cur_limit=?, tmode=?, iaddr=?, haddr=?");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            psLocal.setString(1, clientID);
            psLocal.setString(2, sdf2.format(issue_date));
            psLocal.setString(3, sdf2.format(act_date));
            psLocal.setString(4, sdf2.format(due_date));
            psLocal.setString(5, act_cd);
            psLocal.setString(6, license_no);
            psLocal.setString(7, email);
            psLocal.setString(8, server);
            psLocal.setString(9, user);
            psLocal.setString(10, pass);
            psLocal.setInt(11, status);
            psLocal.setInt(12, month);
            psLocal.setInt(13, day);
            psLocal.setString(14, sdf2.format(cur_date));
            psLocal.setString(15, logDEF);
            psLocal.setInt(16, appLimit);
            psLocal.setInt(17, currentLimit);
            psLocal.setInt(18, mode);
            psLocal.setString(19, ipAddr);
            psLocal.setString(20, hwAddr);
            psLocal.executeUpdate();
        }
        main.commit();
        main.setAutoCommit(true);
        if (rsLocal != null) {
            rsLocal.close();
        }
        if (psLocal != null) {
            psLocal.close();
        }
        if (main != null) {
            main.close();
        }
    }

    /* GETTER METHOD */
    public String getClient() {
        return this.clientID;
    }

    public String getLicense() {
        return this.license_no;
    }

    public String getActivationCode() {
        return this.act_cd;
    }

    public String getEmail() {
        return this.email;
    }

    public String getServer() {
        return this.server;
    }

    public String getUser() {
        return this.user;
    }

    public String getPassword() {
        return this.pass;
    }

    public Date getIssueDate() {
        return this.issue_date;
    }

    public Date getActivationDate() {
        return this.act_date;
    }

    public Date getDueDate() {
        return this.due_date;
    }

    public Date getLastStartDate() {
        return this.cur_date;
    }

    public int getStatus() {
        return this.status;
    }

    public int getMonth() {
        return this.month;
    }

    public int getDay() {
        return this.day;
    }

    public Properties getProperty(){
        return this.property;
    }
}