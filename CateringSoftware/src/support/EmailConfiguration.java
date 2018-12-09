/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

/**
 *
 * @author @JD@
 */
public class EmailConfiguration {
    public String email;
    public String host;
    public String port;
    public String auth;
    public String smtp_starttls;
    public String protocol;
    public String socketFactoryPort;
    public String socketFactoryClass;
    public String socketFactoryFallback;
    public String debug;
    public String quitwait;

    public String userName;
    public String password;

    public Session configure() {
        Properties props = new Properties();

        props.put("mail.smtp.user", email);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", smtp_starttls);
        props.put("mail.transport.protocol", protocol);
        props.put("mail.smtp.socketFactory.port", socketFactoryPort);
        props.put("mail.smtp.socketFactory.class", socketFactoryClass);
        props.put("mail.smtp.socketFactory.fallback", socketFactoryFallback);
        props.put("mail.debug", debug);
        props.put("mail.smtp.quitwait", quitwait);

        Session session = Session.getDefaultInstance(props, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        });
        return session;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getDebug() {
        return debug;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getQuitwait() {
        return quitwait;
    }

    public void setQuitwait(String quitwait) {
        this.quitwait = quitwait;
    }

    public String getSmtp_starttls() {
        return smtp_starttls;
    }

    public void setSmtp_starttls(String smtp_starttls) {
        this.smtp_starttls = smtp_starttls;
    }

    public String getSocketFactoryClass() {
        return socketFactoryClass;
    }

    public void setSocketFactoryClass(String socketFactoryClass) {
        this.socketFactoryClass = socketFactoryClass;
    }

    public String getSocketFactoryFallback() {
        return socketFactoryFallback;
    }

    public void setSocketFactoryFallback(String socketFactoryFallback) {
        this.socketFactoryFallback = socketFactoryFallback;
    }

    public String getSocketFactoryPort() {
        return socketFactoryPort;
    }

    public void setSocketFactoryPort(String socketFactoryPort) {
        this.socketFactoryPort = socketFactoryPort;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}