/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JLabel;

public class Validation {
    SystemValidation systemValidation = null;
    private java.text.SimpleDateFormat formatDateUtil;

    private Library lb = new Library();

    public Validation() {
        systemValidation = new SystemValidation();
        formatDateUtil = new java.text.SimpleDateFormat("dd/MM/yyyy");
        formatDateUtil.setLenient(false);
    }

    public boolean isValidAlpha(String strTxt, String strName, Boolean bZero, Boolean bEmpty, Boolean bNegative, Boolean bRange,JLabel jlblMessageText) {
        boolean validateAlpha = true;
        Pattern pc = Pattern.compile("[0-9]*");
        Matcher m = pc.matcher(strTxt);
        if(!bEmpty) {
            if (strTxt.isEmpty()) {
                jlblMessageText.setText(strName +" should not Empty");
                validateAlpha = false;
                return validateAlpha;
            }
            if (m.matches()) {
                jlblMessageText.setText(strName +" should not Number");
                validateAlpha=false;
                return validateAlpha;
            }
        } else {
            if (!strTxt.isEmpty()) {
                jlblMessageText.setText(strName +" should Empty");
                validateAlpha = false;
                return validateAlpha;
            }
        }
        jlblMessageText.setText("");
        return validateAlpha;
    }

    public boolean isValidAlphaNumber(String strTxt, String strName, Boolean bEmpty, JLabel jlblMessageText) {
        boolean validateAlphaNumber = true;
        if (!bEmpty) {
            if (strTxt.isEmpty()) {
                jlblMessageText.setText(strName +" should not Empty");
                validateAlphaNumber = false;
                return validateAlphaNumber;
            }
        } else {
            if (!strTxt.isEmpty()) {
                jlblMessageText.setText(strName +" should Empty");
                validateAlphaNumber = false;
                return validateAlphaNumber;
            }
        }
        jlblMessageText.setText("");
        return validateAlphaNumber;
    }

    public boolean isValidNumber(String strTxt, String strName, Boolean bZero, Boolean bEmpty, Boolean bNegative, Boolean bRange,JLabel jlblMessageText) {  //Change by JD 30/06/2009
        boolean validateNumber = true;
       // Pattern pc = Pattern.compile("[a-zA-Z]*");
        Pattern pc=Pattern.compile("^[-+]?[0-9]*\\.?[0-9]*");
        Matcher m = pc.matcher(strTxt);

        if (!bEmpty) {
            if (m.matches()) { // if given text is Number that operation perform
                if(!bNegative) {
                    double val = Double.parseDouble(strTxt);
                    if( val < 0) {
                        jlblMessageText.setText(strName +" should not be negative");
                        validateNumber = false;
                        return validateNumber;
                    }
                }
                if (!bZero) {
                    double val = Double.parseDouble(strTxt);
                    if (val == 0 ) {
                        jlblMessageText.setText(strName +" should not be zero ");
                        validateNumber = false;
                        return validateNumber;
                    }
                }
                if (!bRange) {
                    double val = Double.parseDouble(strTxt);
                    if (val == 0) {
                        jlblMessageText.setText(strName +" should not Zero");
                        validateNumber = false;
                        return validateNumber;
                    }
                }
            } else {
                jlblMessageText.setText(strName +" should not Character");
                validateNumber = false;
                return validateNumber;
            }
        }
        jlblMessageText.setText("");
        return validateNumber;
    }

    public boolean isValidDatabaseValue(Connection dataConnection, String strText,String strName,boolean bEmpty,String strtable,String strcolumn,JLabel jlblMessageText,String strWhere) {
        strText = strText.toUpperCase();
        if(!bEmpty) {
            if(strText.isEmpty()) {
                jlblMessageText.setText(strName +" should note empty");
                return false;
            } else {
                String str = null;
                try {
                    String sql = "SELECT "+ strcolumn +" FROM "+ strtable +" "+ strWhere +" ";
                    PreparedStatement pst = dataConnection.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                    ResultSet rs = pst.executeQuery();
                    while(rs.next()) {
                       str = rs.getString(1);
                       if(str.equalsIgnoreCase(strText)) {
                           jlblMessageText.setText(strName +" is valid in database");
                           return true;
                       } else {
                           jlblMessageText.setText(strName +" not in database");
                       }
                    }
                    pst.close();
                    rs.close();
                } catch(Exception ex) {
                    lb.printToLogFile("",ex);
                }
                return false;
            }
        }
        return true;
    }

    public boolean isValidDate(String strDate,String strName,JLabel jlblMessageText) {
        boolean validateDate = true ;
        java.util.Date toDt = null;
        try {
            if (CommonValidation.isValidDate(strDate)) {
                toDt = formatDateUtil.parse(strDate);
                if (!systemValidation.isDateInACPeriod(toDt)) {
                    jlblMessageText.setText("Date is not is current A/c period ");
                    validateDate = false;
                    return validateDate;
                }
            } else {
                jlblMessageText.setText(strName +" Invalid Format");
                validateDate = false;
                return validateDate;
            }
            if (validateDate) {
                jlblMessageText.setText("");
                return validateDate;
            }
        } catch(Exception ex) {
            lb.printToLogFile("",ex);
            validateDate = false;
            return validateDate ;
        } finally {

        }
        jlblMessageText.setText("");
        return validateDate ;
    }

    public boolean isNotValidDbValue(Connection dataConnection, String strText,String strName,boolean bEmpty,String strtable,String strcolumn,JLabel jlblMessageText,String strWhere) {
        if(!bEmpty) {
            if(strText.isEmpty()) {
                jlblMessageText.setText(strName +" should note empty");
                return false;
            } else {
                String str=null;
                try {
                    String sql="SELECT "+ strcolumn +" FROM "+ strtable +" "+ strWhere +" ";
                    PreparedStatement stmt = dataConnection.prepareStatement(sql);
                    ResultSet rs = stmt.executeQuery();
                    while(rs.next()) {
                       str = rs.getString(1);
                       if(str.equalsIgnoreCase(strText)) {
                           jlblMessageText.setText(strName+" is already exit in database");
                           return false;
                       }
                    }
                    stmt.close();
                    rs.close();
                } catch(Exception ex) {
                    lb.printToLogFile("",ex);
                }
                return true;
            }
        }
        return false;
    }
}