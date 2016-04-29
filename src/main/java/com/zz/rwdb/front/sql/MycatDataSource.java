package com.zz.rwdb.front.sql;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.zz.rwdb.BaseService;
import com.zz.rwdb.config.model.MycatHostConfig;
import com.zz.rwdb.front.keypool.BackendPool;
import com.zz.rwdb.front.keypool.PhysicalDatasource;
import com.zz.rwdb.util.Constant;

public class MycatDataSource implements DataSource {

    private PrintWriter logWriter;
    private int loginTimeout = 3;

    /**
     * 一主一从
     * 
     * @param dbType
     * @param writeDataSource
     * @param readDataSource
     */
    public MycatDataSource(String dbType, DataSource writeDataSource, DataSource readDataSource) {

        BaseService.getInstance().setDbType(dbType);
        MycatHostConfig mconfig = new MycatHostConfig(Constant.getDataSourceKey(Constant.RW.WRITE.name(), 0),
                writeDataSource);
        putPhysicalDataSource(mconfig);
        mconfig = new MycatHostConfig(Constant.getDataSourceKey(Constant.RW.READ.name(), 0), readDataSource);
        putPhysicalDataSource(mconfig);

    }

    /**
     * 一主多从
     * 
     * @param dbType
     * @param writeDataSource
     * @param readDataSources
     */
    public MycatDataSource(String dbType, DataSource writeDataSource, List<DataSource> readDataSources) {
        BaseService.getInstance().setDbType(dbType);
        MycatHostConfig mconfig;

        mconfig = new MycatHostConfig(Constant.getDataSourceKey(Constant.RW.WRITE.name(), 0), writeDataSource);
        putPhysicalDataSource(mconfig);
        int size;
        size = readDataSources.size();
        for (int index = 0; index < size; index++) {
            mconfig = new MycatHostConfig(Constant.getDataSourceKey(Constant.RW.READ.name(), index),
                    readDataSources.get(index));
            putPhysicalDataSource(mconfig);
        }
    }

    /**
     * 多主多从
     * 
     * @param dbType
     * @param writeDataSources
     * @param readDataSources
     */
    public MycatDataSource(String dbType, List<DataSource> writeDataSources, List<DataSource> readDataSources) {
        BaseService.getInstance().setDbType(dbType);
        MycatHostConfig mconfig;
        int size = writeDataSources.size();
        for (int index = 0; index < size; index++) {
            mconfig = new MycatHostConfig(Constant.getDataSourceKey(Constant.RW.WRITE.name(), index),
                    readDataSources.get(index));
            putPhysicalDataSource(mconfig);
        }

        size = readDataSources.size();
        for (int index = 0; index < size; index++) {
            mconfig = new MycatHostConfig(Constant.getDataSourceKey(Constant.RW.READ.name(), index),
                    readDataSources.get(index));
            putPhysicalDataSource(mconfig);
        }
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
