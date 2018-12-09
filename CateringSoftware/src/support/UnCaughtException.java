/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author @JD@
 */
public class UnCaughtException extends PrintStream {
    private static FileOutputStream logClear = null;
    public static Library lb = new Library();

    public UnCaughtException(FileOutputStream fops) {
        super(fops);
        try {
            logClear = new FileOutputStream(new File("LOG/Clear Locks_"+new SimpleDateFormat("ddMMyyyy_hh_mm_ss aaa").format(new Date())+".ini"));
        } catch(Exception ex) {
            lb.printToLogFile("Error in UncaughtException ", ex);
        }
    }
}