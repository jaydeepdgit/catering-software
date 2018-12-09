/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cateringsoftware;

import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author JD
 */
public class ValidateLicence {
    int status = -1;
    private int day = 6;
    public int checkValidateLicence() throws SQLException {
        status = 0;
        Date duedate = null;
        MainClass.mainApp = new LicenceConfig();
        MainClass.mainApp.loadDate();
        if (MainClass.mainApp.getActivationDate() != null) {
            Date today = new Date();
            duedate = MainClass.mainApp.getDueDate();
            today = new Date(today.getYear(), today.getMonth(), today.getDate());
            if ((MainClass.mainApp.getLastStartDate().after(today))) {
                status = 5;
            }
            int diff = getDifference(today, duedate);
            if (status != 5) {
                if (diff < 0) {
                    status = 3;
                } else {
                    if (diff < day) {
                        if (MainClass.mainApp.getStatus() == -1) {
                            status = 2;
                        } else {
                            status = 1;
                        }
                    } else {
                        status = 1;
                    }
                }
                if (status != 5) {
                    updateServerDababase(today, duedate);
                }
            }
        }
        return status;
    }

    private int getDifference(Date from, Date to) {
        long day = to.getTime() - from.getTime();
        day = day/86400000;

        return (int)day;
    }

    private void updateServerDababase(Date current_date, Date due_date) throws SQLException {
        current_date = new Date(current_date.getYear(),current_date.getMonth(),current_date.getDate());
        due_date = new Date(due_date.getYear(),due_date.getMonth(),due_date.getDate());

        MainClass.mainApp.setLastStartDate(current_date);
        MainClass.mainApp.setDueDate(due_date);
        MainClass.mainApp.saveData();
    }
}