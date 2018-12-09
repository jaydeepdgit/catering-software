/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import cateringsoftware.DeskFrame;

/**
 *
 * @author @JD@
 */
public class EmailConfigBean {
    private String manage_id;
    private String manage_email;
    private String manage_pwd;
    private String manage_port;
    private String manage_host;
    private String manage_mobno;

    public String getManage_id() {
        return manage_id;
    }

    public void setManage_id(String manage_id) {
        this.manage_id = manage_id;
    }

    public String getManage_email() {
        return manage_email;
    }

    public void setManage_email(String manage_email) {
        this.manage_email = manage_email;
    }

    public String getManage_pwd() {
        return manage_pwd;
    }

    public void setManage_pwd(String manage_pwd) {
        this.manage_pwd = manage_pwd;
    }

    public String getManage_port() {
        return manage_port;
    }

    public void setManage_port(String manage_port) {
        this.manage_port = manage_port;
    }

    public String getManage_host() {
        return manage_host;
    }

    public void setManage_host(String manage_host) {
        this.manage_host = manage_host;
    }

    public String getManage_mobno() {
        return manage_mobno;
    }

    public void setManage_mobno(String manage_mobno) {
        this.manage_mobno = manage_mobno;
    }

    public void getEmailConfigDtls() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM manage_email";
        try {
            ps = DeskFrame.connMpAdmin.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next()){
                setManage_id(rs.getString("manage_id"));
                setManage_email(rs.getString("manage_email"));
                setManage_pwd(rs.getString("manage_pwd"));
                setManage_port(rs.getString("manage_port"));
                setManage_host(rs.getString("manage_host"));
                setManage_mobno(rs.getString("manage_mobno"));
            }
        } catch(Exception ex){
            ex.getMessage();
            ex.printStackTrace();
            System.out.println(ex);
        } finally{
            try {
                if(rs != null) {
                    rs.close();
                }
                if(ps != null) {
                    ps.close();
                }
            } catch(Exception ex) {
                ex.getMessage();
                ex.printStackTrace();
                System.out.println(ex);
            }
        }
    }
}