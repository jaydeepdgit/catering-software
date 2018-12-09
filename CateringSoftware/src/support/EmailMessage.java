/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JOptionPane;
import cateringsoftware.MainClass;

/**
 *
 * @author @JD@
 */
public class EmailMessage {
    private String to;
    private String displayName;
    private String from;
    private String cc = "";
    private String bcc = "";
    private String files[] = new String[5];
    private String subject = "";
    private String messageText = "";
    Library lb = new Library();

    public EmailMessage(Session session){
        
    }
    private boolean success = false;

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String[] getFiles() {
        return files;
    }

    public void setFiles(String[] files) {
        this.files = files;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean sendMessage() {
        try {
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
                protected PasswordAuthentication getPasswordAuthentication(){
                    return new PasswordAuthentication(Email1, password1);
                }
            });

            Message msg = new MimeMessage(session);

            msg.setFrom(new InternetAddress(Email1));

            InternetAddress internetAddress_to[] = getAddreesses(to);

            msg.setRecipients(Message.RecipientType.TO, internetAddress_to);

            if (!cc.equals("")) {
                InternetAddress internetAddress_cc[] = getAddreesses(cc);
                msg.setRecipients(Message.RecipientType.CC, internetAddress_cc);
            }
            if (!bcc.equals("")) {
                msg.setRecipient(Message.RecipientType.BCC, new InternetAddress(bcc));
            }

            msg.setSubject(subject);

            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setText(messageText);

            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp1);

            for (int i = 0; i < files.length; i++) {
                if (files[i] != null) {
                    MimeBodyPart mbp2 = new MimeBodyPart();

                    FileDataSource fds = new FileDataSource(files[i]);
                    mbp2.setDataHandler(new DataHandler(fds));
                    mbp2.setFileName(fds.getName());

                    mp.addBodyPart(mbp2);
                }
            }

            msg.setContent(mp);

            Transport.send(msg);
            success = true;

            JOptionPane.showMessageDialog(null, "Sending Successed to " + this.to, "Sucessfully E-mail", JOptionPane.INFORMATION_MESSAGE);
        } catch (AddressException ex) {
            lb.printToLogFile("Exception at sendMessage in Email Message", ex);
            success = false;

            JOptionPane.showMessageDialog(null, "Sending failed to " + this.to, "Invalid Email Address", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            lb.printToLogFile("Exception at sendMessage1 in Email Message", ex);
            success = false;

            JOptionPane.showMessageDialog(null, "Sending failed to " + this.to, "Could not send to '" + displayName + "'", JOptionPane.ERROR_MESSAGE);
        }
        return success;
    }

    private InternetAddress[] getAddreesses(String addresses) throws Exception {
        InternetAddress internetAddress [] = null;
        String multiple_to [] = addresses.split(";");
        internetAddress = new InternetAddress[multiple_to.length];

        for(int addCount = 0; addCount < multiple_to.length; addCount++) {
            String eID = multiple_to[addCount].trim();
            internetAddress[addCount] = new InternetAddress(eID);
        }
        return internetAddress;
    }
}