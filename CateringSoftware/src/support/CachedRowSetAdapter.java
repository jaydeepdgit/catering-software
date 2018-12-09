/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package support;

import com.sun.rowset.CachedRowSetImpl;
import com.sun.rowset.FilteredRowSetImpl;
import com.sun.rowset.JoinRowSetImpl;
import java.sql.ResultSet;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.FilteredRowSet;
import javax.sql.rowset.JoinRowSet;

/**
 *
 * @author @JD@
 */
public class CachedRowSetAdapter {
    CachedRowSet cachedViewRs = null;
    JoinRowSet joinViewRs = null;
    FilteredRowSet filterViewRs = null;

    private Library bLib = new Library();
    public CachedRowSetAdapter() {
    }

    public CachedRowSet getCachedResultSet(ResultSet viewRS) {
        try {
            cachedViewRs = new CachedRowSetImpl();
            cachedViewRs.populate(viewRS);
        } catch (Exception ex) {
            bLib.printToLogFile("Exception at getCachedResultSet in CachedRowSetAdapter..",ex);
        }
        return cachedViewRs;
    }

    public JoinRowSet getJoinResultSet() {
        try {
            joinViewRs = new JoinRowSetImpl();
        } catch (Exception ex) {
            bLib.printToLogFile("Exception at getJoinResultSet in CachedRowSetAdapter..",ex);
        }
        return joinViewRs;
    }

    public FilteredRowSet getFilteredResultSet(ResultSet viewRS) {
        try {
            filterViewRs = new FilteredRowSetImpl();
            filterViewRs.populate(viewRS);
        } catch (Exception ex) {
            bLib.printToLogFile("Exception at getFilteredResultSet in CachedRowSetAdapter..",ex);
        }
        return filterViewRs;
    }
}