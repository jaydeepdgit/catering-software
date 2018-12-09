/*
 * Validation.java
 *
 * Created on September 21, 2001, 10:47 AM
 */

package support;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Date;

/**
 *
 * @author  @JD@
 * @version
 */
public class CommonValidation {
    private static String dateFormat = "dd/MM/yyyy";
    private static java.text.SimpleDateFormat formatDateUtil;
    private static java.text.SimpleDateFormat formatDate;
    private static int intDebugMode = 0;
    private static String isSysCdBase = "N";
    private static BufferedWriter bwLog = null;
    private static String fromDt = "01/04/1970";
    private static String toDt = "31/03/2100";
    private static Library lb = new Library();

    /** Creates new Validation */
    public CommonValidation() {
        formatDate = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.UK);
    }

    // method checks para data is valid date or not
    //"dd/MM/yyyy"
    public static java.sql.Date getFormatedSqlDate(String date) {
        //java.text.SimpleDateFormat formatSqlDate = new java.text.SimpleDateFormat("yyyy-MM-dd"); //, java.util.Locale.UK);
        java.text.SimpleDateFormat formatSqlDate = new java.text.SimpleDateFormat("dd/MM/yyyy");
        java.sql.Date returnDate = null;
        try {
            formatDateUtil = new java.text.SimpleDateFormat(dateFormat, java.util.Locale.UK);
            formatDateUtil.setLenient(false);

            if (!isDateEmpty(date)) {
                returnDate = java.sql.Date.valueOf(formatSqlDate.format(formatDateUtil.parse(date)));
            }
        } catch(Exception ex){
            CommonValidation.showDebugMsg("Error in format date..."+ex);
        } finally{
            formatSqlDate = null;
            return returnDate;
        }
    }

    public static boolean isDateEmpty(String stringDate) {
        String checkValue = "--";
        if (stringDate == null){
            return true;
        }
        char[] c = stringDate.toCharArray();
        stringDate = "";
        for(int i = 0; i < c.length; i++) {
            if (c[i] != ' ') {
                stringDate += c[i] ;
            }
        }

        if (checkValue.equals(stringDate)) {
            checkValue = null;
            return true;
        } else {
            checkValue = null;
            return false;
        }
    }

    public static boolean isValidDate(String stringDate, String dateFormat) {
        if (isDateEmpty(stringDate)) {
            return false;
        }

        boolean isValidated = false ;
        stringDate = stringDate.trim();
        formatDateUtil = new java.text.SimpleDateFormat(dateFormat, java.util.Locale.UK);
        formatDateUtil.setLenient(false);
        try {
            if (stringDate != null && !stringDate.equals("")  && !stringDate.trim().startsWith("/")  && !stringDate.trim().contains(" ")) {
                java.util.Date d = formatDateUtil.parse(stringDate);
                formatDateUtil.format(d);
                Date fromDate;
                Date toDate;
                formatDate = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.UK);
                fromDate = formatDate.parse(fromDt);
                toDate  = formatDate.parse(toDt);
                if(fromDate.after(d) || toDate.before(d)) {
                    isValidated = false;
                } else
                isValidated = true;
            }
        } catch(Exception ex) {
            CommonValidation.showDebugMsg(ex);
        } finally {
            formatDateUtil = null;
            return isValidated;
        }
    }

    public static boolean tempIsValidDate(String stringDate, String dateFormat) throws Exception {
        if (isDateEmpty(stringDate)) {
            return false;
        }
        boolean isValidated = false ;
        stringDate = stringDate.trim();
        java.text.SimpleDateFormat formatDateUtil = new java.text.SimpleDateFormat(dateFormat, java.util.Locale.UK);
        formatDateUtil.setLenient(false);
        if (stringDate != null && !stringDate.equals("") && !stringDate.trim().startsWith("/") && !stringDate.trim().contains(" ")) {
            java.util.Date d = formatDateUtil.parse(stringDate);
            formatDateUtil.format(d);
            Date fromDate;
            Date toDate;
            formatDate = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.UK);
            fromDate = formatDate.parse(fromDt);
            toDate  = formatDate.parse(toDt);
            if(fromDate.after(d) || toDate.before(d)) {
                isValidated = false;
            } else
            isValidated = true;
        }
        formatDateUtil = null;
        return isValidated;
    }

    public static String getValidDate(String stringDate) {
        return getValidDate(stringDate, dateFormat);
    }

    public static String getValidDate(String stringDate, String dateFormat) {
        String validDate = "--";
        stringDate = stringDate.trim();
        formatDateUtil = new java.text.SimpleDateFormat(dateFormat, java.util.Locale.UK);
        formatDateUtil.setLenient(false);
        try {
            if(stringDate != null && !stringDate.equals("")) {
                validDate = formatDateUtil.format(formatDateUtil.parse(stringDate));
            }
        } catch(Exception ex) {
            lb.printToLogFile("Exception at getValidDate in Common Validation", ex);
            validDate = "    -  -  ";
        } finally {
            formatDateUtil = null;
            return validDate ;
        }
    }

    public static boolean isValidDate(String stringDate) {
        return isValidDate(stringDate, dateFormat);
    }

    public static boolean isChar(char c) {
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
            return true;
        }
        else
        return false;
    }

    public static boolean isNum(char c) {
        if (c >= '0' && c <= '9') {
            return true;
        }
        else
        return false;
    }

    public static String padToLeft(String targetString, char padChar, int length) {
        if (targetString == null) {
            targetString = "";
        }
        String padString = "";
        for(int i = 0; i < length - targetString.length(); i++) {
            padString += padChar ;
        }
        return (padString + targetString);
    }

    public static String padToLeft(String targetString, int length) {
        return padToLeft(targetString,' ', length);
    }

    public static String padToRight(String targetString, int length) {
        return padToRight(targetString,' ', length);
    }

    public static String padToRight(String targetString, char padChar, int length) {
        String padString = "";
        if (targetString == null){
            targetString = "";
        }
        for(int i = 0; i < length - targetString.length(); i++) {
            padString += padChar ;
        }
        return (targetString + padString);
    }

    public static boolean isValidTime(String time) {
        boolean isValid = false;
        String strHours = null, strMinutes = null;
        int intHours = 0, intMinutes = 0;
        try {
            strHours = time.substring(0,2);
            strMinutes = time.substring(3, 5);
            try {
                intHours = Integer.parseInt(strHours.trim());
            } catch(Exception ex) {
                lb.printToLogFile("Exception at isValidTime in Common Validation", ex);
                intHours = 0;
            }
            try {
                intMinutes = Integer.parseInt(strMinutes.trim());
            } catch(Exception ex) {
                lb.printToLogFile("Exception at isValidTime1 in Common Validation", ex);
                intMinutes = 0;
            }
            if (intHours >= 0  && intHours <= 23 && intMinutes >= 0 && intMinutes <= 59) {
                isValid = true;
            }
        } catch(Exception ex) {
            lb.printToLogFile("Exception at isValidTime2 in Common Validation", ex);
            isValid = false;
        } finally {
            return isValid ;
        }
    }

    public static String getValidTime(String time) {
        String stringReturnVal = "";
        String strHours = null, strMinutes = null;
        int intHours = 0, intMinutes = 0;
        try {
            strHours = time.substring(0,time.indexOf("."));
            strMinutes = time.substring(time.indexOf(".") + 1, 5);
            try {
                intHours = Integer.parseInt(strHours.trim());
            } catch(Exception ex) {
                lb.printToLogFile("Exception at getValidTime in Common Validation", ex);
                intHours = 0;
            }
            try {
                intMinutes = Integer.parseInt(strMinutes.trim());
            } catch(Exception ex) {
                lb.printToLogFile("Exception at getValidTime1 in Common Validation", ex);
                intMinutes = 0;
            }
            if (intHours >= 0  && intHours <= 23) {
            } else {
                intHours = 0;
            }
            if(intMinutes >= 0 && intMinutes <= 59) {
            } else{
                intMinutes = 0;
            }
        } catch(Exception ex) {
            lb.printToLogFile("Exception at getValidTime2 in Common Validation", ex);
        } finally {
            strHours = padToLeft(intHours+"",'0',2);
            strMinutes = padToLeft(intMinutes+"",'0',2);
            strHours+= "." + strMinutes;

            stringReturnVal += strHours;

            return stringReturnVal ;
        }
    }
    /* ************************************
     * Num to words program for rupees/paisa only...
     */

    private static String baseNumber[] = {"", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine",  "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen" };
    private static String baseTen[]    = {"", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};
    private static String suffix[]     = {"Hundred", "Thousand", "Lakh", "Crore"};

    public static String num2Word(String s){
        return num2Word(s, false);
    }

    public static String num2Word(String s, boolean isPaisa){
        String afterDeci = "", returnString = "";
        String deciValue = "";
        int length = 0 ;

        int indexOfDeci = s.indexOf("."); // if does not exist value will be -1

        try {
            if (indexOfDeci >= 0) {
                deciValue = s.substring(indexOfDeci+1).trim() ;
                if (deciValue.length() > 0 &&  Integer.parseInt(deciValue) > 0) {
                    afterDeci = num2Word(s.substring(indexOfDeci+1), true) + "paisa " ;
                    if (s.indexOf(".") > 0) {
                        afterDeci = " and "  + afterDeci ;
                    } else {
                        return afterDeci;
                    }
                }
                s = s.substring(0, indexOfDeci);
            }
        } catch(Exception ex) {
            afterDeci = "";
            CommonValidation.showDebugMsg(ex);
        }

        s = s.trim();
        length = s.length() ;

        if (s.length() >= 8) {
            returnString += convert( s.substring(0, length-7), 0) ;
        }
        if (s.length() >= 6) {
            if (s.length() >= 7) {
                //Lakh
                returnString += convert( s.substring(length-7, length-5), 7) ;
            } else {
                //Lakh
                returnString += convert( s.substring(length-6, length-5), 7) ;
            }
        }
        if (s.length() >= 4) {
            if (s.length() >= 5) {
                //Sahastra
                returnString += convert( s.substring(length-5, length-3), 5) ;
            } else {
                //Sahastra
                returnString += convert( s.substring(length-4, length-3), 5) ;
            }
        }
        if (s.length() > 2) {
            //Shatak
            returnString += convert( s.substring(length-3, length-2), 3) ;
        }
        if (s.length() > 0) {
            if (s.length() >= 2) {
                //Dashak
                returnString += convert( s.substring(length-2, length), 2) ;
                if (!isPaisa) {
                    returnString += "Rupees";
                }
            } else {
                //Ekam
                returnString += convert( s.substring(length-1, length), 2) ;
                if (!isPaisa) {
                    returnString += "Rupees";
                }
            }
        }
        return returnString + afterDeci;
    }

    private static String convert(String s, int location) {
        String returnString = "";
        String tmpString = "";
        int iPart = 0;

        s = s.trim();
        try {
            switch(location) {
                case 0 :
                    // Crore
                    returnString = num2Word(s);

                    if (returnString.trim().length() > 0) {
                        returnString += suffix[3] + " ";
                    }
                    break;
                case 2 :
                    iPart = Integer.parseInt(s);
                    if (iPart <= 19) {
                        returnString = baseNumber[iPart] + " ";
                    } else {
                        tmpString = Long.toString(iPart);
                        returnString = baseTen[Integer.parseInt(tmpString.substring(0,1))] + " " + baseNumber[Integer.parseInt(tmpString.substring(1,2))]  + " " ;
                    }
                    break;
                case 3 :
                    iPart = Integer.parseInt(s);
                    returnString = baseNumber[iPart] + " " ;
                    if (returnString.trim().length() > 0) {
                        returnString += suffix[0] + " ";
                    }
                    break;
                case 5 :
                    iPart = Integer.parseInt(s);
                    returnString = convert(Integer.toString(iPart), 2);
                    if (returnString.trim().length() > 0) {
                        returnString += suffix[1] + " ";
                    }
                    break;
                case 7 :
                    iPart = Integer.parseInt(s);
                    returnString = convert(Integer.toString(iPart), 2);
                    if (returnString.trim().length() > 0) {
                        returnString += suffix[2] + " ";
                    }
                    break;
            }
        } catch(Exception ex) {
           lb.printToLogFile("Exception at convert in Common Validation", ex);
        } finally {
            return returnString ;
        }
    }

    public static boolean isNull (Object o) {
        if (o == null) {
            return true;
        }
        return false;
    }

    public static String getCurrentMonth(boolean shouldBeName, boolean shouldBeLongName) {
        String stringCurrentMonth = null;
        java.text.SimpleDateFormat formatSqlDate = null;

        try {
            if (shouldBeName) {
                if (shouldBeLongName) {
                    formatSqlDate = new java.text.SimpleDateFormat("MMMM", java.util.Locale.UK);
                } else {
                    formatSqlDate = new java.text.SimpleDateFormat("MMM", java.util.Locale.UK);
                }
            } else {
                formatSqlDate = new java.text.SimpleDateFormat("MM", java.util.Locale.UK);
            }
            formatSqlDate.setLenient(false);
            stringCurrentMonth = formatSqlDate.format(new java.util.Date());
        } catch(Exception ex) {
            CommonValidation.showDebugMsg("Error in format date..."+ ex);
        } finally{
            formatSqlDate = null;
            return stringCurrentMonth ;
        }
    }

    public static void showDebugMsg(Object obj) {
        showDebugMsg(obj.toString());
    }

    public static void showDebugMsg(Exception ex) {
        showDebugMsg(ex.getMessage());
    }

    public static void showDebugMsg(String strMsg) {
        switch (getDebugMode())  {
            case 0 :
                break;
            case 1 :
                System.out.println(strMsg);
                break;
            case 2 :
                if (writeToLog(strMsg) < 0) {
                    setDebugMode(1); 
                }  
                break;
        }
    }    

    public static void setDebugMode(int intMode) {
        intDebugMode = intMode;
        if (intDebugMode == 2) {
            if (bwLog == null) {
                if (initLogWriter() < 0) {
                    setDebugMode(1); 
                }
            }    
        }
    }

    public static int getDebugMode() {
        return intDebugMode;
    }

    public static String isSysCdBase() {
        return isSysCdBase;
    }

    private static int initLogWriter() {
        int intRetVal = 0;
        if(bwLog == null) {
            try {
                bwLog = new BufferedWriter(new FileWriter("goclubs.log",true));
                bwLog.write("Goclubs log for : " + new java.util.Date());
                bwLog.newLine();
                intRetVal = 0;
            } catch (Exception ex) {
                lb.printToLogFile("Exception at initLogWriter in Common Validation", ex);
                intRetVal = -1;
            }
        }
        return intRetVal;
    }

    public static int writeToLog(String strMsg) {
        int intRetVal = 0;
        try {
           bwLog.write(strMsg);
           bwLog.newLine();
           intRetVal = 0;
        } catch (Exception ex) {
            lb.printToLogFile("Exception at writeToLog in Common Validation", ex);
            intRetVal = -1;
        }
        return intRetVal;
    }

    public static int closeLogWriter() {
        int intRetVal = 0;
        if (bwLog != null) {
            try {
                bwLog.close();
                intRetVal = 0;
            } catch (Exception ex) {
                lb.printToLogFile("Exception at closeLogWriter in Common Validation", ex);
                intRetVal = -1;
            }
        }  
        return intRetVal;
    }

    public static void selectText(java.awt.event.FocusEvent evt) {
        javax.swing.JTextField jt = null;
        try {
            jt = (javax.swing.JTextField )evt.getComponent();

            jt.setSelectionStart(0);
            jt.setSelectionEnd(jt.getText().length());
        } catch(Exception ex){
            lb.printToLogFile("Exception at selectText in Common Validation", ex);
        } finally{
            jt = null;
        }
    }
}