package com.z.rwdb.front.keypool;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class PhysicalDatasource {

    private DataSource dataSource;

    private String name;

    public PhysicalDatasource(String pyhsicalName, DataSource dataSource) {
        this.name = pyhsicalName;
        this.dataSource = dataSource;
    }

    public String getName() {
        return this.name;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
