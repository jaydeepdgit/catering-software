/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JLabel;

/**
 *
 * @author @JD@
 */
public class ShowCurrentTime extends Thread {
    JLabel jc = null;
    boolean chk = true;

    public ShowCurrentTime(JLabel component) {
        jc = component;
        this.start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss aaa");
                jc.setText(sdf.format(cal.getTime()));
                ShowCurrentTime.sleep(1000);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}