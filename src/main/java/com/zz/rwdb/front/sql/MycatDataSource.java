package com.zz.rwdb.front.sql;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.zz.rwdb.BaseService;
import com.zz.rwdb.front.keypool.BackendPool;
import com.zz.rwdb.front.keypool.PhysicalDatasource;
import com.zz.rwdb.util.Constant;

public class MycatDataSource implements DataSource {

    private PrintWriter logWriter;
    private int loginTimeout = 3;

    /**
     * 一主一从 对于多从，可以采用haproxy进行负载均衡的架构， 而对于多主，可以采用lvs(keepalive)形式进行主备
     * 
     * @param dbType
     * @param masterDataSource
     * @param slaveDataSource
     */
    public MycatDataSource(String dbType, DataSource masterDataSource, DataSource slaveDataSource) {

        BaseService.setDbType(dbType);
        // MycatHostConfig mconfig = new MycatHostConfig(Constant.RW.WRITE.name(), masterDataSource);
        // putPhysicalDataSource(mconfig);
        //mconfig = new MycatHostConfig(Constant.RW.READ.name(), slaveDataSource);
        //putPhysicalDataSource(mconfig);
        
        putPhysicalDataSource(Constant.RW.WRITE.name(), masterDataSource);
        putPhysicalDataSource(Constant.RW.READ.name(), slaveDataSource);
        if(masterDataSource!=null){
            try {
                Connection c=masterDataSource.getConnection();
                //获取全部的数据库表
                ResultSet rs = c.getMetaData().getTables(null, "%", "%", new String[] { "TABLE" });
                while (rs.next()) {
                    BaseService.addWritedbTables(rs.getString("TABLE_NAME").trim()); //将写库的全部表都装载进来
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
    }

    // private List<String> specialWriteSql;

    // 设置特殊的写sql，比如select user_seq.nextval from dual等
    public void setSpecialWriteSql(List<String> specialWriteSql) {
        // this.specialWriteSql = specialWriteSql;
        if (specialWriteSql != null && specialWriteSql.size() > 0) {
            BaseService.setSpecialWriteSql(specialWriteSql);
        }
    }

    
    private void putPhysicalDataSource(String name,DataSource dataSource) {
        PhysicalDatasource physicalDatasource = new PhysicalDatasource(name,dataSource);
        BackendPool.getInstance().putDataSouce(name, physicalDatasource);
    }
    
    
    /*private void putPhysicalDataSource(MycatHostConfig config) {
        PhysicalDatasource physicalDatasource = new PhysicalDatasource(config);
        BackendPool.getInstance().putDataSouce(config.getName(), physicalDatasource);
    }*/

    @Override
    public Connection getConnection() throws SQLException {
        return new MycatConnection();
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
        throw new SQLException("only support isWrapperFor()");
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLException("only support unwrap()");
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException("only support getParentLogger()");
    }

}
