/*
 * PartyMasterData.java
 *
 * Created on August 20, 2001, 1:01 M
 */

package support;

import com.sun.rowset.JoinRowSetImpl;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.FilteredRowSet;
import javax.sql.rowset.JoinRowSet;

public class PartyMasterData extends ViewData {
    public final static int INTEGER    = java.sql.Types.INTEGER;
    public final static int BIGINT     = java.sql.Types.BIGINT;
    public final static int FLOAT      = java.sql.Types.FLOAT;
    public final static int DOUBLE     = java.sql.Types.DOUBLE;
    public final static int DECIMAL    = java.sql.Types.DECIMAL;
    public final static int VARCHAR    = java.sql.Types.VARCHAR;
    public final static int DATE       = java.sql.Types.DATE;
    public final static int TIME       = java.sql.Types.TIME;
    public final static int TIMESTAMP  = java.sql.Types.TIMESTAMP;
    public final static int OTHER      = java.sql.Types.OTHER;

    private String[] fieldList ;
    public  String masterView = "prtymst";
    private java.util.Vector data = new java.util.Vector();
    private String customClause = "";
    private int[] fieldWidth ;
    private String OtherClause=" ";

    private CachedRowSetAdapter crsa = new CachedRowSetAdapter();

    private Library lb = new Library();

    public PartyMasterData() {
    }

    public PartyMasterData(String[][] dataAry) {
        java.util.Vector rowData = null;

        this.fieldList =  null;
        this.dataConnection = null;
        data = new java.util.Vector();

        if (dataAry.length > 0) {
            for(int row = 0; row < dataAry.length; row++) {
                rowData = null;
                rowData = new java.util.Vector();
                for(int col = 0; col < dataAry[row].length; col++) {
                    rowData.addElement(dataAry[row][col]);
                }
                data.addElement(rowData);
            }
            int fieldWidth[] = new int[dataAry[0].length ];
            for(int i = 0; i < fieldWidth.length; i++) {
                fieldWidth[i]=100;
            }
            this.fieldWidth = fieldWidth;
        }
    }

    public PartyMasterData(String[] fieldList, java.sql.Connection dataConnection) {
        super();
        this.fieldList = fieldList ;
        this.dataConnection = dataConnection;
        try {
            //viewStatement = (oracle.jdbc.driver.OracleStatement) dataConnection.createStatement(java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE, java.sql.ResultSet.CONCUR_READ_ONLY);
            //Commented as it was a statement
            //viewStatement = dataConnection.createStatement(java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE, java.sql.ResultSet.CONCUR_READ_ONLY);
        } catch(Exception ex) {
            lb.printToLogFile("Error in PartyMasterData constructor : ",ex);
        }
    }

    public java.util.Vector changeOrder(int currentLocation, int newLocation) {
        if (dataConnection != null) {
            String stringToChange = fieldList [currentLocation];
            String stringToMove   = fieldList [newLocation];
            fieldList [currentLocation] = stringToMove ;
            fieldList [newLocation] = stringToChange;
            stringToChange = null;
            stringToMove   = null;
            return fetchData();
        } else {
            java.util.Vector rowData = null;
            String swapVal = null;
            this.fieldList =  null;
            this.dataConnection = null;

            for(int row = 0; row < data.size(); row++) {
                rowData = null;
                rowData = (java.util.Vector)data.elementAt(row);

                swapVal = (String)rowData.elementAt(newLocation);
                rowData.setElementAt((String)rowData.elementAt(currentLocation), newLocation );
                rowData.setElementAt(swapVal,currentLocation ); 

                data.setElementAt(rowData, row);
            }
            return fetchData();
        }
    }

    // fetchData from JD
    public java.util.Vector fetchData(String query) {
        if (dataConnection == null) {
            return data;
        }

        Library lb = new Library();

        java.util.Vector record = new java.util.Vector();
        String strQuery = null;
        ResultSetMetaData oraRSMetadata = null;
        int fieldWidth[] = new int[fieldList.length];
        try {
            if (dataConnection == null) {
                lb.printToLogFile("no connection available",null);
            }
            strQuery = query;
            viewStatement = dataConnection.prepareStatement(strQuery,java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE, java.sql.ResultSet.CONCUR_READ_ONLY);

            if (viewStatement != null) {
                viewRS = viewStatement.executeQuery();
                oraRSMetadata = viewRS.getMetaData();

                while(viewRS.next()) {
                    record = null;
                    record = new java.util.Vector();
                    for(int fieldNo = 1; fieldNo <= oraRSMetadata.getColumnCount(); fieldNo++) {
                        fieldWidth[fieldNo-1] = oraRSMetadata.getColumnDisplaySize(fieldNo);
                        if(oraRSMetadata.getColumnType(fieldNo) == Types.DECIMAL) {
                            record.addElement(viewRS.getDouble(fieldNo));
                        } else{
                            record.addElement(viewRS.getString(fieldNo));
                        }
                    }
                    data.addElement(record);
                }
                this.fieldWidth = fieldWidth;
            } else {
                for(int fieldNo = 0 ; fieldNo < fieldList.length; fieldNo ++) {
                    record.addElement("No_Data_Found");
                }
                data.addElement(record);
            }
        } catch(Exception ex) {
            lb.printToLogFile("Error at fetchData in PartyMasterData ",ex);
            for(int fieldNo = 0 ; fieldNo < fieldList.length; fieldNo ++) {
                record.addElement("No_Data_Found");
            }
            data.addElement(record);
        } finally {
            record = null;
            strQuery = null;
            oraRSMetadata = null;
            fieldWidth = null;
            try {
                viewRS.close();
                viewStatement.close();
            } catch(Exception ex) {
                lb.printToLogFile("Error at fetchData1 in PartyMasterData ",ex);
            } finally {
                viewStatement = null;
                viewRS = null;
            }
        }
        return data;
    }

    public java.util.Vector fetchData() {
        if (dataConnection == null) {
            return data;
        }

        data = new java.util.Vector();
        java.util.Vector record = new java.util.Vector();
        String strFieldList = null;
        Object o = null;
        String strQuery = null;
        ResultSetMetaData oraRSMetadata = null;
        int fieldWidth[] = new int[fieldList.length];
        try {
            strFieldList = "";
            for(int i=0; i < fieldList.length; i++) {
                strFieldList += fieldList[i];
                if (i >= 0 && (i != (fieldList.length - 1))) {
                    strFieldList += ", ";
                }
            }
            if (dataConnection == null) {
                lb.printToLogFile("no connection available",null);
            }

            /* Temporary change by JD */
            if(OtherClause.equals(" ")) {
                strQuery = "Select DISTINCT " + strFieldList + " from " + masterView + " " + customClause + " order by " + strFieldList  ;
            } else {
                strQuery = "Select  " + strFieldList + " from " + masterView + " " + customClause + " order by " + OtherClause;
            }
            viewStatement = dataConnection.prepareStatement(strQuery);//,java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE, java.sql.ResultSet.CONCUR_READ_ONLY);
            if (viewStatement != null) {
                viewRS = viewStatement.executeQuery();
                oraRSMetadata = viewRS.getMetaData();
                while(viewRS.next()) {
                    record = null;
                    record = new java.util.Vector();
                    for(int fieldNo = 1 ; fieldNo <= fieldList.length; fieldNo ++) {
                        fieldWidth[fieldNo-1] = oraRSMetadata.getColumnDisplaySize(fieldNo);
                        record.addElement(viewRS.getString(fieldNo));
                    }
                    data.addElement(record);
                }
                this.fieldWidth = fieldWidth;
            } else {
                for(int fieldNo = 0 ; fieldNo < fieldList.length; fieldNo ++) {
                    record.addElement("No_Data_Found");
                }
                data.addElement(record);
            }
        } catch(Exception ex) {
            lb.printToLogFile("Error at fetchData in PartyMasterData ",ex);
            for(int fieldNo = 0 ; fieldNo < fieldList.length; fieldNo ++) {
                record.addElement("No_Data_Found");
            }
            data.addElement(record);
        } finally {
            record = null;
            strFieldList = null;
            o = null;
            strQuery = null;
            oraRSMetadata = null;
            fieldWidth = null;
            try {
                viewRS.close();
                viewStatement.close();
            } catch(Exception ex) {
                lb.printToLogFile("Error at fetchData1 in PartyMasterData ",ex);
            } finally {
                viewStatement = null;
                viewRS = null;
            }
        }
        return data;
    }

    public java.util.Vector joinfetchData(ResultSet RS) {
        if (dataConnection == null) {
            return data;
        }

        data = new java.util.Vector();
        java.util.Vector record = new java.util.Vector();
        ResultSetMetaData oraRSMetadata = null;
        this.viewRS = RS;
        int fieldWidth[] = new int[fieldList.length];
        try {
            if (viewStatement != null) {
                oraRSMetadata = viewRS.getMetaData();

                if (viewRS.isBeforeFirst() != viewRS.isAfterLast()) {
                    while(viewRS.next()) {
                        record = null;
                        record = new java.util.Vector();
                        for(int fieldNo = 1 ; fieldNo <= fieldList.length; fieldNo ++) {
                            fieldWidth[fieldNo-1] = oraRSMetadata.getColumnDisplaySize(fieldNo);
                            record.addElement(viewRS.getString(fieldNo));
                        }
                        data.addElement(record);
                    }
                }
                this.fieldWidth = fieldWidth;
            } else {
                for(int fieldNo = 0 ; fieldNo < fieldList.length; fieldNo ++) {
                    record.addElement("No_Data_Found");
                }
                data.addElement(record);
            }
        } catch(Exception ex) {
            lb.printToLogFile("Exception at joinfetchData In Party Master Data", ex);
            for(int fieldNo = 0 ; fieldNo < fieldList.length; fieldNo ++) {
                record.addElement("No_Data_Found");
            }
            data.addElement(record);
        } finally {
            record = null;
            oraRSMetadata = null;
            fieldWidth = null;
            try {
                viewRS.close();
                viewStatement.close();
            } catch(Exception ex) {
                lb.printToLogFile("Exception at joinfetchData In Party Master Data",ex);
            } finally {
                viewStatement = null;
                viewRS = null;
            }
        }
        return data;
    }

    public java.util.Vector fetchDataForStock(String str1, String str2, int matchColumn) {
        if (dataConnection == null) {
            return data;
        }

        data = new java.util.Vector();
        java.util.Vector record = new java.util.Vector();
        ResultSetMetaData oraRSMetadata = null;
        JoinRowSet joinViewDataRS = null;
        int fieldWidth[] = new int[fieldList.length];
        try {
            if (dataConnection == null) {
                lb.printToLogFile("no connection available",null);
            }

            joinViewDataRS = new JoinRowSetImpl();
            viewStatement = dataConnection.prepareStatement(str1,java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE, java.sql.ResultSet.CONCUR_READ_ONLY);
            ResultSet rs1 = viewStatement.executeQuery();
            rs1.last();
            int iRow=rs1.getRow();
            rs1.beforeFirst();
            CachedRowSet data1 = crsa.getCachedResultSet(rs1);
            data1.setMatchColumn(1);
            if(iRow > 0) {
                joinViewDataRS.addRowSet(data1);

                ResultSet rs2 = viewStatement.executeQuery();
                CachedRowSet data2 = crsa.getCachedResultSet(rs2);
                data2.setMatchColumn(1);
                joinViewDataRS.addRowSet(data2);
            }

            if (viewStatement != null) {
                oraRSMetadata = joinViewDataRS.getMetaData();

                if (joinViewDataRS.isBeforeFirst() != joinViewDataRS.isAfterLast()) {
                    while(joinViewDataRS.next()) {
                        record = null;
                        record = new java.util.Vector();
                        for(int fieldNo = 1 ; fieldNo <= fieldList.length; fieldNo ++) {
                            fieldWidth[fieldNo-1] = oraRSMetadata.getColumnDisplaySize(fieldNo);
                            record.addElement(joinViewDataRS.getString(fieldNo));
                        }
                        data.addElement(record);
                    }
                }
                this.fieldWidth = fieldWidth;
            } else {
                for(int fieldNo = 0 ; fieldNo < fieldList.length; fieldNo ++) {
                    record.addElement(" ");
                }
                data.addElement(record);
            }
        } catch(Exception ex) {
            lb.printToLogFile("",ex);
            for(int fieldNo = 0 ; fieldNo < fieldList.length; fieldNo ++) {
                record.addElement(" ");
            }
            data.addElement(record);
        } finally {
            record = null;
            oraRSMetadata = null;
            fieldWidth = null;

            try {
                joinViewDataRS.close();
                viewStatement.close();
            } catch(Exception ex) {
                lb.printToLogFile("Exception at fetchDataForStock in Party Master Data", ex);
            } finally{
                viewStatement = null;
                joinViewDataRS = null;
            }
        }
        return data;
    }

    public void setCustomClause(String customClause) {
        this.customClause = customClause;
    }

    public void setOtherClause(String OtherClause) {
        this.OtherClause = OtherClause;
    }

    public int getFieldWidth(int field) {
        int returnValue = 1;
        if (fieldWidth.length >= field) {
            returnValue = fieldWidth[field];
        }
        return returnValue;
    }

    public int search(String searchValue, int searchColumn) {
        int currentRecord = 0;
        java.util.Vector record = new java.util.Vector();

        String tmpData = null;
        for(currentRecord = 0; currentRecord < data.size(); currentRecord++) {
            record = null;
            record = (java.util.Vector)(data.elementAt(currentRecord));
            tmpData= null;
            tmpData = (String)record.elementAt(searchColumn);
            if (tmpData.toUpperCase().startsWith(searchValue)) {
                return currentRecord;
            }
        }
        record = null;
        tmpData = null;
        return 1;
    }

    public java.util.Vector fetchDataForStock(String strType) {
        if (dataConnection == null) {
            return data;
        }
        data = new java.util.Vector();
        java.util.Vector record = new java.util.Vector();
        String strFieldList = null;
        String strQuery = null;
        ResultSetMetaData oraRSMetadata = null;
        int fieldWidthLocal[] = new int[fieldList.length];

        try {
            strFieldList = "";
            for(int i=0; i < fieldList.length; i++) {
                strFieldList += fieldList[i];
                if (i >= 0 && (i != (fieldList.length - 1))) {
                    strFieldList += ", ";
                }
            }
            if (dataConnection == null){
                lb.printToLogFile("no connection available",null);
            }

            /* Temporary change by JD */
            if(OtherClause.equals(" ")) {
                strQuery = "SELECT DISTINCT "+ strFieldList +" FROM "+ masterView +" "+ customClause +" ORDER BY "+ strFieldList  ;
            } else {
                strQuery = "SELECT " + strFieldList + " FROM " + masterView + " " + customClause + " ORDER BY "+ OtherClause;
            }

            viewStatement = dataConnection.prepareStatement(strQuery,java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE, java.sql.ResultSet.CONCUR_READ_ONLY);

            viewRS = viewStatement.executeQuery();
            FilteredRowSet filterViewRS = crsa.getFilteredResultSet(viewRS);
            if(strType.equalsIgnoreCase("Charge")) {
                DataFilterNumber df = new DataFilterNumber(0, 0, 7, "ORDQTY", 1);
                filterViewRS.setFilter(df);
                filterViewRS.beforeFirst();
            } else if(strType.equalsIgnoreCase("Free")) {
                DataFilterNumber df = new DataFilterNumber(0, 0, 8, "ORDQTY", 1);
                filterViewRS.setFilter(df);
                filterViewRS.beforeFirst();
            }

            if (viewStatement != null) {
                oraRSMetadata = filterViewRS.getMetaData();

                if (filterViewRS.isBeforeFirst() != filterViewRS.isAfterLast()) {
                    while(filterViewRS.next()) {
                        record = null;
                        record = new java.util.Vector();
                        for(int fieldNo = 1 ; fieldNo <= fieldList.length; fieldNo ++) {
                            fieldWidthLocal[fieldNo-1] = oraRSMetadata.getColumnDisplaySize(fieldNo);
                            record.addElement(filterViewRS.getString(fieldNo));
                        }
                        data.addElement(record);
                    }
                }
                this.fieldWidth = fieldWidthLocal;
            } else {
                for(int fieldNo = 0 ; fieldNo < fieldList.length; fieldNo ++) {
                    record.addElement("No_Data_Found");
                }
                data.addElement(record);
            }
        } catch(Exception ex) {
            lb.printToLogFile("",ex);
            for(int fieldNo = 0 ; fieldNo < fieldList.length; fieldNo ++) {
                record.addElement("No_Data_Found");
            }
            data.addElement(record);
        } finally {
            record = null;
            strFieldList = null;
            strQuery = null;
            oraRSMetadata = null;
            fieldWidthLocal = null;
            try {
                viewRS.close();
                viewStatement.close();
            } catch(Exception ex) {
                lb.printToLogFile("Exception at fetchDataForStock in Party Master Data", ex);
            } finally {
                viewStatement = null;
                viewRS = null;
            }
        }
        return data;
    }
}