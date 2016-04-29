package com.zz.rwdb.front.sql;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.zz.rwdb.BaseService;
import com.zz.rwdb.backend.BackendPool;
import com.zz.rwdb.backend.core.PhysicalDatasource;
import com.zz.rwdb.config.model.MycatHostConfig;
import com.zz.rwdb.util.Constant;

public class MycatDataSource implements DataSource {

    private PrintWriter logWriter;
    private int loginTimeout = 3;

    public MycatDataSource(String dbType, DataSource writeDataSource, DataSource readDataSource) {

        BaseService.getInstance().setDbType(dbType);
        MycatHostConfig mconfig = new MycatHostConfig(Constant.RW.WRITE.name(), writeDataSource);
        putPhysicalDataSource(mconfig);
        mconfig = new MycatHostConfig(Constant.RW.READ.name(), readDataSource);
        putPhysicalDataSource(mconfig);

    }

    private void putPhysicalDataSource(MycatHostConfig config) {
        PhysicalDatasource physicalDatasource = new PhysicalDatasource(config);
        BackendPool.getInstance().putDataSouce(config.getName(), physicalDatasource);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return new MycatConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        //throw new SQLException("only support getConnection()");
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
