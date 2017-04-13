/**
 * 
 */
package com.aldb.rwdb.front.route;

/**
 * @author Administrator
 *
 */
public class RouteCondition {

    private final String sql;
    private final String dbType;

  /*  private final int writeDBSize;

    private final int readDBSize;*/

    public RouteCondition(String sql, String dbType//, int writeDBSize, int readDBSize
            ) {
        super();
        this.sql = sql;
        this.dbType = dbType.toLowerCase();
   /*     this.writeDBSize = writeDBSize;
        this.readDBSize = readDBSize;*/
    }

    public String getSql() {
        return sql;
    }

    public String getDbType() {
        return dbType;
    }

 /*   public int getWriteDBSize() {
        return writeDBSize;
    }

    public int getReadDBSize() {
        return readDBSize;
    }*/

}
