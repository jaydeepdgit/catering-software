/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

import javax.sql.RowSet;
import javax.sql.rowset.FilteredRowSet;
import javax.sql.rowset.Predicate;

/**
 *
 * @author @JD@
 */
public class DataFilterNumber implements Predicate {
    private int lowAge;
    private int highAge;
    private int columnIndex;
    private String columnName;
    private int index;
    Library lb = new Library();

    public DataFilterNumber(int lowAge, int highAge, int columnIndex, String columnName, int index) {
        this.lowAge = lowAge;
        this.highAge = highAge;
        this.columnName = columnName;
        this.columnIndex = columnIndex;
        this.index = index;
    }

    public DataFilterNumber(double lowAge, double highAge, int columnIndex, String columnName, int index) {
        this.lowAge = (int)lowAge;
        this.highAge = (int)highAge;
        this.columnName = columnName;
        this.columnIndex = columnIndex;
        this.index = index;
    }

    public DataFilterNumber(int lowAge, int highAge, int columnIndex) {
        this(lowAge, highAge, columnIndex, "age", 8);
    }

    public boolean evaluate(Object value, String columnName) {
        boolean evaluation = true;
	if (columnName.equalsIgnoreCase(this.columnName)) {
            int columnValue = ((Integer) value).intValue();
            switch(index) {
                case 1:
                    if(columnValue > this.highAge)
                        evaluation = true;
                    else
                        evaluation = false;
                    break;
                case 2:
                    if(columnValue < this.lowAge)
                        evaluation = true;
                    else
                        evaluation = false;
                    break;
                case 3:
                    if(columnValue >= this.highAge)
                        evaluation = true;
                    else
                        evaluation = false;
                    break;
                case 4:
                    if(columnValue <= this.lowAge)
                        evaluation = true;
                    else
                        evaluation = false;
                    break;
                case 5:
                    if ((columnValue > this.lowAge) && (columnValue < this.highAge))
                        evaluation = true;
                    else
                        evaluation = false;
                    break;
                case 6:
                    if ((columnValue > this.lowAge) && (columnValue <= this.highAge))
                        evaluation = true;
                    else
                        evaluation = false;
                    break;
                case 7:
                    if ((columnValue >= this.lowAge) && (columnValue < this.highAge))
                        evaluation = true;
                    else
                        evaluation = false;
                    break;
                case 8:
                    if ((columnValue >= this.lowAge) && (columnValue <= this.highAge))
                        evaluation = true;
                    else
                        evaluation = false;
                    break;
                case 9:
                    if ((columnValue > this.lowAge) || (columnValue < this.highAge))
                        evaluation = true;
                    else
                        evaluation = false;
                    break;
                case 10:
                    if ((columnValue > this.lowAge) || (columnValue <= this.highAge))
                        evaluation = true;
                    else
                        evaluation = false;
                    break;
                case 11:
                    if ((columnValue >= this.lowAge) || (columnValue < this.highAge))
                        evaluation = true;
                    else
                        evaluation = false;
                    break;
                case 12:
                    if ((columnValue >= this.lowAge) || (columnValue <= this.highAge))
                        evaluation = true;
                    else
                        evaluation = false;
                    break;
            }
        }
	return evaluation;
    }

    public boolean evaluate(Object value, int columnNumber) {
        boolean evaluation = true;
	if (columnIndex == columnNumber) {
            int columnValue = ((Integer) value).intValue();
            switch(index) {
                case 1:
                    if(columnValue > this.highAge)
                        evaluation = true;
                    else
                        evaluation = false;
                    break;
                case 2:
                    if(columnValue < this.lowAge)
                        evaluation = true;
                    else
                        evaluation = false;
                    break;
                case 3:
                    if(columnValue >= this.highAge)
                        evaluation = true;
                    else
                        evaluation = false;
                    break;
                case 4:
                    if(columnValue <= this.lowAge)
                        evaluation = true;
                    else
                        evaluation = false;
                    break;
                case 5:
                    if ((columnValue > this.lowAge) && (columnValue < this.highAge)) 
                        evaluation = true;
                    else
                        evaluation = false;
                    break;
                case 6:
                    if ((columnValue > this.lowAge) && (columnValue <= this.highAge))
                        evaluation = true;
                    else
                        evaluation = false;
                    break;
                case 7:
                    if ((columnValue >= this.lowAge) && (columnValue < this.highAge)) 
                        evaluation = true;
                    else
                        evaluation = false;
                    break;
                case 8:
                    if ((columnValue >= this.lowAge) && (columnValue <= this.highAge)) 
                        evaluation = true;
                    else
                        evaluation = false;
                    break;
                case 9:
                    if ((columnValue > this.lowAge) || (columnValue < this.highAge))
                        evaluation = true;
                    else
                        evaluation = false;
                    break;
                case 10:
                    if ((columnValue > this.lowAge) || (columnValue <= this.highAge))
                        evaluation = true;
                    else
                        evaluation = false;
                    break;
                case 11:
                    if ((columnValue >= this.lowAge) || (columnValue < this.highAge))
                        evaluation = true;
                    else
                        evaluation = false;
                    break;
                case 12:
                    if ((columnValue >= this.lowAge) || (columnValue <= this.highAge))
                        evaluation = true;
                    else
                        evaluation = false;
                    break;
            }
        }
	return evaluation;
    }

    public boolean evaluate(RowSet rs) {
        if (rs == null) {
            return false;
        }
        FilteredRowSet frs = (FilteredRowSet) rs;
        boolean evaluation = false;
        try {
            int columnValue = (int)frs.getDouble(this.columnIndex);
            switch(index) {
                case 1:
                    if(columnValue > this.highAge)
                        evaluation = true;
                    break;
                case 2:
                    if(columnValue < this.lowAge)
                        evaluation = true;
                    break;
                case 3:
                    if(columnValue >= this.highAge)
                        evaluation = true;
                    break;
                case 4:
                    if(columnValue <= this.lowAge)
                        evaluation = true;
                    break;
                case 5:
                    if ((columnValue > this.lowAge) && (columnValue < this.highAge)) {
                        evaluation = true;
                    }
                    break;
                case 6:
                    if ((columnValue > this.lowAge) && (columnValue <= this.highAge)) {
                        evaluation = true;
                    }
                    break;
                case 7:
                    if ((columnValue >= this.lowAge) && (columnValue < this.highAge)) {
                        evaluation = true;
                    }
                    break;
                case 8:
                    if ((columnValue >= this.lowAge) && (columnValue <= this.highAge)) {
                        evaluation = true;
                    }
                    break;
                case 9:
                    if ((columnValue > this.lowAge) || (columnValue < this.highAge))
                        evaluation = true;
                    break;
                case 10:
                    if ((columnValue > this.lowAge) || (columnValue <= this.highAge))
                        evaluation = true;
                    break;
                case 11:
                    if ((columnValue >= this.lowAge) || (columnValue < this.highAge))
                        evaluation = true;
                    break;
                case 12:
                    if ((columnValue >= this.lowAge) || (columnValue <= this.highAge))
                        evaluation = true;
                    break;
            }
        } catch (Exception ex) {
            lb.printToLogFile("Exception at evaluate in Data Filter Number", ex);
            return false;
	}
	return evaluation;
    }
}