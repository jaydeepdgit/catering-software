package support;

import java.text.SimpleDateFormat;

public class SystemValidation {
    private int acYear = Integer.parseInt("2012");
    private String systemDateFormat = "dd/MM/yyyy";
    private java.text.SimpleDateFormat SDF =new SimpleDateFormat(systemDateFormat) ;
    private java.util.Date startACDate;
    private java.util.Date endACDate;
    private java.util.Vector vectorRights = new java.util.Vector();
    private java.util.Vector vectorOptions = new java.util.Vector();
    private String strHostName = "";
    private String strHostAddr = "";
    private java.util.Vector vctrUserActionLog = null;

    private Library lb = new Library();

    public SystemValidation() {
        initSysDates();
    }

    public int getAcYear() {
        return acYear;
    }

    public String getHostName() {
        return strHostName; 
    }

    public String getHostAddr() {
        return strHostAddr; 
    }

    public String getOptionID(String searchClass) {
        String optionID = "";
        try{
            for(int i = 0; i < vectorOptions.size(); i++) {
                if(((String)((java.util.Vector)vectorOptions.elementAt(i)).elementAt(1)).equalsIgnoreCase(searchClass.toUpperCase().trim())){
                    optionID = (String)((java.util.Vector)vectorOptions.elementAt(i)).elementAt(0);
                    break;
                }
            }
        } catch(Exception ex){
            optionID = "";
            lb.printToLogFile("Exception at getOptionID In SystemValidation", ex);
        }
        return optionID;
    }

    final int VIEW_ACTION   = 1;
    final int PRINT_ACTION  = 2;
    final int NEW_ACTION    = 4;
    final int EDIT_ACTION   = 8;
    final int DELETE_ACTION = 16;

    public String getPasswordPrompt(String msg) {        
        String strPassword = null;

        javax.swing.JPasswordField textPassword  = new javax.swing.JPasswordField(10);
        javax.swing.JPanel panelPassword = new javax.swing.JPanel(new java.awt.GridLayout(4,1));
        javax.swing.JLabel lblMesg1 = new javax.swing.JLabel(msg);
        javax.swing.JLabel lblMesg2 = new javax.swing.JLabel("Please Enter Password :" );
        javax.swing.JLabel lblMesg3 = new javax.swing.JLabel("(Press Esc, to cancel)");
        panelPassword.add(lblMesg1);
        panelPassword.add(lblMesg2);
        panelPassword.add(lblMesg3);
        panelPassword.add(textPassword);
        textPassword.requestFocus();
        javax.swing.JOptionPane.showMessageDialog(null,panelPassword,"Password Verification.", javax.swing.JOptionPane.INFORMATION_MESSAGE);

        try {
            strPassword = textPassword.getText();
        } catch(Exception ex) {
            lb.printToLogFile("Exception at getPasswordPrompt in System Validation", ex);
            strPassword = "";
        } finally {
        }
        return strPassword;
    }

    public String getPasswordPrompt_1(String msg) {        
        String strPassword = null;

        javax.swing.JPasswordField textPassword  = new javax.swing.JPasswordField(10);
        javax.swing.JPanel panelPassword = new javax.swing.JPanel(new java.awt.GridLayout(4,1));
        javax.swing.JLabel lblMesg1 = new javax.swing.JLabel(msg);
        javax.swing.JLabel lblMesg2 = new javax.swing.JLabel("Please Enter Password :" );
        javax.swing.JLabel lblMesg3 = new javax.swing.JLabel("(Press Esc, to cancel)");
        panelPassword.add(lblMesg1);
        panelPassword.add(lblMesg2);
        panelPassword.add(lblMesg3);
        panelPassword.add(textPassword);
        javax.swing.JOptionPane.showOptionDialog(null, panelPassword, "Password Verification.", javax.swing.JOptionPane.DEFAULT_OPTION, javax.swing.JOptionPane.INFORMATION_MESSAGE, null, null, null);

        try {
            strPassword = textPassword.getText();
        } catch(Exception ex){
            strPassword = "";
        } finally{
        }
        return strPassword;
    }

    public boolean isRightsAvailable(String searchClass, int actionType, boolean withPrompt, boolean withPassword) {
        String optionID = getOptionID(searchClass.toUpperCase());
        String mesg = "Are you sure ?" ;        
        boolean hasAccess = false;
        int intConf = 0;
        int accessToken = 0, remVal = 0;
        int remViewAction = 0, remPrintAction = 0, remNewAction = 0, remEditAction = 0, remDeleteAction = 0;
        if (optionID == null || optionID.equals("")) {
            hasAccess = false;
        } else {
            hasAccess = true;
        }

        accessToken = getOptionRightsValue(optionID);
        if (accessToken <= 0) {
            hasAccess = false;
        } else{
            hasAccess = true;
        }

        try {
            if (withPrompt) {
                intConf = -1;
                hasAccess = false;
                Object[] options = { "NO","YES"};

                if(actionType == DELETE_ACTION) {
                    mesg = "Want to delete the record ?";
                } else if(actionType == EDIT_ACTION) {
                    mesg = "Want to edit the record ?";
                }
                hasAccess = true;
            }
            if (withPassword) {
                if(hasAccess) {
                    String usrPassword = "";
                    usrPassword = getPasswordPrompt(mesg);
                    CommonValidation.showDebugMsg("PWD : " + usrPassword);
                } else {
                    hasAccess = false;
                }
                CommonValidation.showDebugMsg("has access : " + hasAccess);
            }
            if (hasAccess){
                hasAccess = false;
                remVal = accessToken ;
                remDeleteAction = remVal / DELETE_ACTION;
                if (remDeleteAction >= 1) {
                    remVal = remVal - DELETE_ACTION;
                }

                remEditAction   = remVal / EDIT_ACTION;
                if (remEditAction >= 1) {
                    remVal = remVal - EDIT_ACTION;
                }

                remNewAction    = remVal / NEW_ACTION;
                if (remNewAction >= 1) {
                    remVal = remVal - NEW_ACTION;
                }

                remPrintAction  = remVal / PRINT_ACTION;
                if (remPrintAction >= 1) {
                    remVal = remVal - PRINT_ACTION;
                }

                remViewAction   = remVal / VIEW_ACTION;
                if (remViewAction >= 1) {
                    remVal = remVal - VIEW_ACTION;
                }

                if (remDeleteAction >= 1 && actionType == DELETE_ACTION) {
                    hasAccess = true;
                }
                else if (remEditAction >= 1 && actionType == EDIT_ACTION) {
                    hasAccess = true;
                }
                else if (remNewAction >= 1 && actionType == NEW_ACTION) {
                    hasAccess = true;
                }
                else if (remPrintAction >= 1 && actionType == PRINT_ACTION) {
                    hasAccess = true;
                }
                else if (remViewAction >= 1 && actionType == VIEW_ACTION) {
                    hasAccess = true;
                }
            }
        } catch(Exception ex) {
            lb.printToLogFile("",ex);
            hasAccess = false;
        } finally {
            if (!hasAccess && intConf >= 1) {
                javax.swing.JOptionPane.showMessageDialog(null,"Unauthorised User Action Fired.", "Security Alert", javax.swing.JOptionPane.OK_OPTION);
            }
        }
        return hasAccess;
    }

    private void initSysDates() {
        String d = null;
        try {
            d = "01/04/" + acYear ;
            startACDate = SDF.parse(d);
            d = "31/03/" + (acYear + 1) ;
            endACDate   = SDF.parse(d);
        } catch(Exception ex) {
            lb.printToLogFile("",ex);
        } finally {
            d = null;
        }
    }

    public void setUserRights(java.util.Vector vectorRights) {
        this.vectorRights = vectorRights ;
    }

    public boolean isDateInACPeriod(String stringDate) {
        boolean isValidated = false ;
        java.util.Date date = null;
        try {
            date = SDF.parse(stringDate);
            isValidated = isDateInACPeriod(date);
        } catch(Exception ex) {
            lb.printToLogFile("Error in  isDateInACPeriond(String) ",ex);
        } finally {
            date = null;
        }
        return isValidated;
    }

    public boolean isAccessible(String optionID) {
        boolean haveAccess =false;
        try {
            if(getOptionRightsValue(optionID) > 0 ){
                haveAccess = true;
            }
        } catch(Exception ex) {
            haveAccess = false;
            lb.printToLogFile("",ex);
        } finally {
            if (!haveAccess) {
                javax.swing.JOptionPane.showMessageDialog(null,"Unauthorised User Access.", "Security Alert", javax.swing.JOptionPane.OK_OPTION);
            }
        }
        return haveAccess;
    }

    public int getOptionRightsValue(String optionID) {
        int optRights = 0;
        try {
            for(int i =0;i<vectorRights.size();i++) {
                if(((String)((java.util.Vector)vectorRights.elementAt(i)).elementAt(0)).equals(optionID)) {
                    if (((java.util.Vector)vectorRights.elementAt(i)).elementAt(1) != null) {
                        optRights = ((java.math.BigDecimal)((java.util.Vector)vectorRights.elementAt(i)).elementAt(1)).intValue();
                    }
                }
            }
        } catch(Exception ex) {
            optRights = 0;
            lb.printToLogFile("Exception at getOptionRightsValue In SystemValidation", ex);
        }
        return optRights;
    }

    public boolean isDateInACPeriod(java.util.Date date) {
        boolean isValidated = false ;
        try {
            if ((date.after(startACDate) && date.before(endACDate)) || date.equals(startACDate) ||  date.equals(endACDate)) {
                isValidated = true;
            }
        } catch(Exception ex) {
            lb.printToLogFile("Error in  isDateInACPeriond(Date) ",ex);
        } finally {
        }
        return isValidated;
    }

    public void finalize() {
        try {
            super.finalize();
            systemDateFormat = "dd/MM/yyyy";
            SDF = null;
            startACDate= null;
            endACDate = null;
        } catch(java.lang.Throwable throwable){
            lb.printToLogFile("Exception at finalize In SystemValidation", null);
        } finally{
        }
    }

    public void addUserAction(String strModule,String strAction,java.util.Date date) {
        java.util.Hashtable htAction = new java.util.Hashtable(1);
        java.util.GregorianCalendar gc = new java.util.GregorianCalendar();
        htAction.put("module", strModule);
        htAction.put("action", strAction);
        htAction.put("frm_time",gc);
        htAction.put("to_time",gc);
        vctrUserActionLog.addElement(htAction);
    }
}