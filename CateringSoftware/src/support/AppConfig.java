/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

import cateringsoftware.DeskFrame;
import cateringsoftware.MainClass;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
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
public class AppConfig implements Serializable {
    private String clientID;
    private String license_no;
    private String act_cd;
    private String email;
    private String server;
    private String user;
    private String pass;
    private String LICENSE_NO;
    private String ACT1;
    private String ACT2;
    private String ACT3;
    private String ACT4;
    private int status;
    private int month;
    private int day;
    private String HADDR;
    private String DEF;
    private int appLimit = 0;
    private int currentLimit = 0;
    private int mode = 0;
    private int PROJ_MODE = 0;
    private Date issue_date;
    private Date act_date;
    private Date due_date;
    private Date cur_date;
    private String logDEF;
    private Properties property = null;

    public static final int BASIC = 0;
    public static final int STANDARD = 1;
    public static final int PREMIUM = 2;

    public static final int DEMO = -1;
    public static final int ACTIVE = 1;
    public static final int INACTIVE = 0;

    public AppConfig() {
        getDabaseInfo();
    }
    /* SETTER METHOD */

    private void getDabaseInfo() {
        File f = new File(System.getProperty("user.dir") + File.separator + "dbConnection.properties");
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
                    Logger.getLogger(AppConfig.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
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

    public String getLICENSE_NO() {
        return LICENSE_NO;
    }

    public void setLICENSE_NO(String LICENSE_NO) {
        this.LICENSE_NO = LICENSE_NO;
    }

    public String getACT1() {
        return ACT1;
    }

    public void setACT1(String ACT1) {
        this.ACT1 = ACT1;
    }

    public String getACT2() {
        return ACT2;
    }

    public void setACT2(String ACT2) {
        this.ACT2 = ACT2;
    }

    public String getACT3() {
        return ACT3;
    }

    public void setACT3(String ACT3) {
        this.ACT3 = ACT3;
    }

    public String getACT4() {
        return ACT4;
    }

    public void setACT4(String ACT4) {
        this.ACT4 = ACT4;
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

    public String getHADDR() {
        return HADDR;
    }

    public void setHADDR(String HADDR) {
        this.HADDR = HADDR;
    }

    public String getDEF() {
        return DEF;
    }

    public void setDEF(String DEF) {
        this.DEF = DEF;
    }

    public int getPROJ_MODE() {
        return PROJ_MODE;
    }

    public void setPROJ_MODE(int PROJ_MODE) {
        this.PROJ_MODE = PROJ_MODE;
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

    public void updateLimitCount() throws SQLException {
        if (MainClass.appConfig.getStatus() == AppConfig.DEMO) {
            PreparedStatement psLocal = null;
            if (DeskFrame.connMpMain != null) {
                psLocal = DeskFrame.connMpMain.prepareStatement("UPDATE appconfig SET cur_limit = cur_limit + 1");
                currentLimit += psLocal.executeUpdate();
            }
            if (psLocal != null) {
                psLocal.close();
            }
        }
    }

    public void loadDate() throws SQLException {
        PreparedStatement psLocal = null;
        ResultSet rsLocal = null;
        if (DeskFrame.connMpMain != null) {
            psLocal = DeskFrame.connMpMain.prepareStatement("SELECT * FROM appconfig");
            rsLocal = psLocal.executeQuery();
            if (rsLocal.next()) {
                clientID = rsLocal.getString("client_id");
                status = rsLocal.getInt("status");
                issue_date = rsLocal.getDate("issue_date");
                act_date = rsLocal.getDate("act_date");
                due_date = rsLocal.getDate("due_date");
                license_no = rsLocal.getString("license_no");
                cur_date = rsLocal.getDate("laststart");
                DeskFrame.theme_cd = rsLocal.getString("theme_cd");
                setLICENSE_NO(rsLocal.getString("license_no"));
                setACT1(rsLocal.getString("act1"));
                setACT2(rsLocal.getString("act2"));
                setACT3(rsLocal.getString("act3"));
                setACT4(rsLocal.getString("act4"));
            }
        }
        if (rsLocal != null) {
            rsLocal.close();
        }
        if (psLocal != null) {
            psLocal.close();
        }
    }

    public void saveData() throws SQLException {
        PreparedStatement psLocal = null;
        ResultSet rsLocal = null;

        if (DeskFrame.connMpMain != null) {
            psLocal = DeskFrame.connMpMain.prepareStatement("UPDATE appconfig SET client_id = ?, issue_date = ?, act_date = ?, due_date = ?, license_no = ?, status = ?, laststart = ?");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            psLocal.setString(1, MainClass.appConfig.getClient());
            psLocal.setString(2, sdf2.format(issue_date));
            psLocal.setString(3, sdf2.format(act_date));
            psLocal.setString(4, sdf2.format(due_date));
            psLocal.setString(5, license_no);
            psLocal.setInt(6, status);
            psLocal.setString(7, sdf2.format(cur_date));
            psLocal.executeUpdate();
        }
        if (rsLocal != null) {
            rsLocal.close();
        }
        if (psLocal != null) {
            psLocal.close();
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