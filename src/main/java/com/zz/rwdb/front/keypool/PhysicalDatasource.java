package com.zz.rwdb.front.keypool;

import java.sql.Connection;
import java.sql.SQLException;

import com.zz.rwdb.config.model.MycatHostConfig;

public class PhysicalDatasource {

	private MycatHostConfig config;

	public PhysicalDatasource(MycatHostConfig config) {

		this.config = config;
	}

	public String getName() {
		return config.getName();
	}

	public Connection getConnection() throws SQLException {
		return config.getDataSource().getConnection();
	}

}
