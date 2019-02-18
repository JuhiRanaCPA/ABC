package Supporting;

import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class TablePrinter {

    private static final int DEFAULT_MAX_TEXT_COL_WIDTH = 150;
    public static final int CATEGORY_STRING = 1;
    public static final int CATEGORY_INTEGER = 2;
    public static final int CATEGORY_DOUBLE = 3;
    public static final int CATEGORY_DATETIME = 4;
    public static final int CATEGORY_BOOLEAN = 5;
    public static final int CATEGORY_OTHER = 0;
    private static class Column {

        private String label;
        private int type;
        private String typeName;
        private int width = 0;
        @SuppressWarnings({ "unchecked", "rawtypes" })
  private List<String> values = new ArrayList();
        private String justifyFlag = "";
        private int typeCategory = 0;

        public Column (String label, int type, String typeName) {
            this.label = label;
            this.type = type;
            this.typeName = typeName;
        }

        public String getLabel() {
            return label;
        }

        public int getType() {
            return type;
        }

        public String getTypeName() {
            return typeName;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public void addValue(String value) {
            values.add(value);
        }

        public String getValue(int i) {
            return values.get(i);
        }

        public String getJustifyFlag() {
            return justifyFlag;
        }

        public void justifyLeft() {
            this.justifyFlag = "-";
        }

        public int getTypeCategory() {
            return typeCategory;
        }

        public void setTypeCategory(int typeCategory) {
            this.typeCategory = typeCategory;
        }
    }


    public static Hashtable<String, String> printResultSet(String sTestName, ResultSet rs) {
        return printResultSet(sTestName, rs, DEFAULT_MAX_TEXT_COL_WIDTH);
    }

    public static Hashtable<String, String> printResultSet(String sTestName, ResultSet rs, int maxStringColWidth) {
     
     Hashtable<String, String> hm = new Hashtable<String, String>();
        try {
            if (rs == null) {
                System.err.println("DBTablePrinter Error: Result set is null!");
                return null;
            }
            if (rs.isClosed()) {
                System.err.println("DBTablePrinter Error: Result Set is closed!");
                return null;
            }
            if (maxStringColWidth < 1) {
                System.err.println("DBTablePrinter Info: Invalid max. varchar column width. Using default!");
                maxStringColWidth = DEFAULT_MAX_TEXT_COL_WIDTH;
            }

            // Get the meta data object of this ResultSet.
            ResultSetMetaData rsmd;
            rsmd = rs.getMetaData();

            // Total number of columns in this ResultSet
            int columnCount = rsmd.getColumnCount();

            // List of Column objects to store each columns of the ResultSet
            // and the String representation of their values.
            @SuppressWarnings({ "unchecked", "rawtypes" })
   List<Column> columns = new ArrayList(columnCount);

            // List of table names. Can be more than one if it is a joined
            // table query
            @SuppressWarnings({ "unchecked", "rawtypes" })
   List<String> tableNames = new ArrayList(columnCount);

            // Get the columns and their meta data.
            // NOTE: columnIndex for rsmd.getXXX methods STARTS AT 1 NOT 0
            for (int i = 1; i <= columnCount; i++) {
                Column c = new Column(rsmd.getColumnLabel(i),
                        rsmd.getColumnType(i), rsmd.getColumnTypeName(i));
                c.setWidth(c.getLabel().length());
                c.setTypeCategory(whichCategory(c.getType()));
                columns.add(c);

                if (!tableNames.contains(rsmd.getTableName(i))) {
                    tableNames.add(rsmd.getTableName(i));
                }
            }

            // Go through each row, get values of each column and adjust
            // column widths.
            int rowCount = 0;
            while (rs.next()) {

                // NOTE: columnIndex for rs.getXXX methods STARTS AT 1 NOT 0
                for (int i = 0; i < columnCount; i++) {
                    Column c = columns.get(i);
                    String value;
                    int category = c.getTypeCategory();

                    if (category == CATEGORY_OTHER) {

                        value = "(" + c.getTypeName() + ")";

                    } else {
                        value = rs.getString(i+1) == null ? "NULL" : rs.getString(i+1);
                    }
                    switch (category) {
                        case CATEGORY_DOUBLE:

                            if (!value.equals("NULL")) {
                                Double dValue = rs.getDouble(i+1);
                                value = String.format("%.3f", dValue);
                            }
                            break;

                        case CATEGORY_STRING:

                            // Left justify the text columns
                            c.justifyLeft();

                            // and apply the width limit
                            if (value.length() > maxStringColWidth) {
                                value = value.substring(0, maxStringColWidth - 3) + "...";
                            }
                            break;
                    }

                    // Adjust the column width
                    c.setWidth(value.length() > c.getWidth() ? value.length() : c.getWidth());
                    c.addValue(value);
                    hm.put(c.getLabel(), value);
                } // END of for loop columnCount
                rowCount++;

            } // END of while (rs.next)

            // For the fun of it, I will use StringBuilder
            StringBuilder strToPrint = new StringBuilder();
            StringBuilder rowSeparator = new StringBuilder();

            // Iterate over columns
            for (Column c : columns) {
                int width = c.getWidth();

              // Center the column label
                String toPrint;
                String name = c.getLabel();
                int diff = width - name.length();

                if ((diff%2) == 1) {
                    width++;
                    diff++;
                    c.setWidth(width);
                }

                int paddingSize = diff/2; // InteliJ says casting to int is redundant.

                String padding = new String(new char[paddingSize]).replace("\0", " ");

                toPrint = "| " + padding + name + padding + " ";
              // END centering the column label

                strToPrint.append(toPrint);

                rowSeparator.append("+");
                rowSeparator.append(new String(new char[width + 2]).replace("\0", "-"));
            }

            String lineSeparator = System.getProperty("line.separator");

            // Is this really necessary ??
            lineSeparator = lineSeparator == null ? "\n" : lineSeparator;

            rowSeparator.append("+").append(lineSeparator);

            strToPrint.append("|").append(lineSeparator);
            strToPrint.insert(0, rowSeparator);
            strToPrint.append(rowSeparator);
            
            String sj = "";
            //StringJoiner sj = new StringJoiner(", "); 
            for (String name : tableNames) {
             sj = ", " + name;
                //sj.add(name);
            }

            String info = "Printing " + rowCount;
            info += rowCount > 1 ? " rows from " : " row from ";
            info += tableNames.size() > 1 ? "tables " : "table ";
            info += sj;
            
            String outVal="Result->\n";     
   outVal = info+"\n";

            // Print out the formatted column labels
            outVal+= strToPrint.toString();

            String format;

            // Print out the rows
            for (int i = 0; i < rowCount; i++) {
                for (Column c : columns) {

                    // This should form a format string like: "%-60s"
                    format = String.format("| %%%s%ds ", c.getJustifyFlag(), c.getWidth());
                    outVal+= String.format(format, c.getValue(i));
                }

                outVal+= "\n|";
                outVal+= rowSeparator;
            }

            outVal+="\n";
            IO.PrintLog(sTestName, outVal);
        } catch (SQLException e) {
            System.err.println("SQL exception in DBTablePrinter. Message:");
            System.err.println(e.getMessage());
        }
        return hm;
    }

    private static int whichCategory(int type) {
        switch (type) {
            case Types.BIGINT:
            case Types.TINYINT:
            case Types.SMALLINT:
            case Types.INTEGER:
            case Types.REAL:
            case Types.DOUBLE:
            case Types.DECIMAL:
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
            case Types.BOOLEAN:
            case Types.VARCHAR:
            case Types.NVARCHAR:
            case Types.LONGVARCHAR:
            case Types.LONGNVARCHAR:
            case Types.CHAR:
            case Types.NCHAR:
            case Types.NUMERIC:
                return CATEGORY_STRING;

            default:
                return CATEGORY_OTHER;
        }
    }
}