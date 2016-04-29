package com.zz.rwdb.config.model;

import javax.sql.DataSource;

public class MycatHostConfig {

    private final String name;
    private final DataSource dataSource;

    public MycatHostConfig(String name, DataSource dataSource) {
        super();
        this.name = name;
        this.dataSource = dataSource;
    }

    public String getName() {
        return name;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

}