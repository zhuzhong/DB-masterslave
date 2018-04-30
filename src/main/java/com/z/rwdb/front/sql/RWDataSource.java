package com.z.rwdb.front.sql;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.slf4j.LoggerFactory;

import com.z.rwdb.BaseService;
import com.z.rwdb.front.keypool.BackendPool;
import com.z.rwdb.front.keypool.PhysicalDatasource;
import com.z.rwdb.util.Constant;

public class RWDataSource implements DataSource {

    private PrintWriter logWriter;
    private int loginTimeout = 3;

    private static org.slf4j.Logger log = LoggerFactory.getLogger(RWDataSource.class);

    /**
     * 一主一从 对于多从，可以采用haproxy进行负载均衡的架构， 而对于多主，可以采用lvs(keepalive)形式进行主备
     * 
     * @param dbType
     * @param masterDataSource
     * @param slaveDataSource
     */
    public RWDataSource(String dbType, DataSource masterDataSource, DataSource slaveDataSource) {

       
        if (dbType == null) {
            throw new RuntimeException("dbTypes null ,please set it...");
        }
        log.info("data source init ,dbtype={}",dbType);
        if (dbType.equalsIgnoreCase("mysql")) {
            dbType = "MySQL";
        }
        BaseService.setDbType(dbType);
        putPhysicalDataSource(Constant.RW.WRITE.name(), masterDataSource);
        putPhysicalDataSource(Constant.RW.READ.name(), slaveDataSource);

    }

    // private List<String> specialWriteSql;

    // 设置特殊的写sql，比如select user_seq.nextval from dual等
    public void setSpecialWriteSql(List<String> specialWriteSql) {
        // this.specialWriteSql = specialWriteSql;
        if (specialWriteSql != null && specialWriteSql.size() > 0) {
            BaseService.setSpecialWriteSql(specialWriteSql);
        }
    }

    public void setRealTime(String isTrue) { //是否需要实时读取
        if ("true".equals(isTrue)) {
            Connection c = null;
            ResultSet rs = null;
            try {
                c = BackendPool.getInstance().getDataSouce(Constant.RW.WRITE.name()).getConnection();
                // 获取全部的数据库表
                rs = c.getMetaData().getTables(null, "%", "%", new String[] { "TABLE" });
                while (rs.next()) {
                    BaseService.addWritedbTables(rs.getString("TABLE_NAME").trim()); // 将写库的全部表都装载进来
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (rs != null)
                        rs.close();
                    if (c != null)
                        c.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (log.isDebugEnabled()) {
                if (BaseService.getAllWritedbTables() != null) {
                    StringBuffer sb = new StringBuffer("[");
                    for (String tableName : BaseService.getAllWritedbTables()) {
                        sb.append(tableName);
                        sb.append(",");
                    }
                    sb.append("]");
                    log.debug("all tableNames {}", sb.toString());
                }
            }

            BaseService.setRealTime(true);
        }
    }

    private void putPhysicalDataSource(String name, DataSource dataSource) {
        PhysicalDatasource physicalDatasource = new PhysicalDatasource(name, dataSource);
        BackendPool.getInstance().putDataSouce(name, physicalDatasource);
    }

    /*
     * private void putPhysicalDataSource(MycatHostConfig config) {
     * PhysicalDatasource physicalDatasource = new PhysicalDatasource(config);
     * BackendPool.getInstance().putDataSouce(config.getName(),
     * physicalDatasource); }
     */

    // public static ThreadLocal<Connection> localRWConnection=new
    // ThreadLocal<Connection>();
    @Override
    public Connection getConnection() throws SQLException {
        /*
         * Connection conn=null;
         * 
         * if(localRWConnection.get()!=null){ conn= localRWConnection.get();
         * }else{ localRWConnection.set(new RWConnection()); conn=
         * localRWConnection.get(); } if(log.isDebugEnabled()){
         * log.debug("Thread id={},invoke rwDataSource$getConnection method,conn={}"
         * ,Thread.currentThread().getId(),conn.toString()); } return conn;
         */
        return new RWConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        // throw new SQLException("only support getConnection()");
        return getConnection();
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return this.logWriter;
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return this.loginTimeout;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        this.logWriter = out;
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        this.loginTimeout = seconds;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new SQLException("not support isWrapperFor()");
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLException("not support unwrap()");
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException("not support getParentLogger()");
    }

}
