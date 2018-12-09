/*
 * ViewData.java
 *
 * Created on October 20, 2001, 4:39 PM
 */

package support;

import java.sql.ResultSet;

/**
 *
 * @author  @JD@
 * @version
 */
public abstract class ViewData {
    protected java.sql.PreparedStatement viewStatement ;
    protected java.sql.ResultSet viewRS  ;
    protected java.sql.Connection dataConnection ;
    protected String[] fieldList ;

    /** Creates new ViewData */
    protected ViewData() {
    }

    //return all thd data from the table
    public abstract java.util.Vector fetchData() ;
    public abstract java.util.Vector joinfetchData(ResultSet RS) ;
    public abstract java.util.Vector fetchData(String query) ;
    public abstract java.util.Vector fetchDataForStock(String strType) ;
    public abstract java.util.Vector fetchDataForStock(String str1,String str2,int matchColumn) ;

    //return all thd data from the table which satisfy the para condion
    /*********************************************************
    No designed this is for table detail view
    when one voucher will be selected voucher view class call the method for the detail table
    and detail table will be refreshed;
    --> public Vector fetchData(String[] field);
     ********************************************************* /
    /*
    public Vector fetchData(String[] field);
     */
}