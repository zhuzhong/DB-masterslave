package com.zz.rwdb.front.route;

import java.sql.SQLSyntaxErrorException;

public interface RouteStrategy {

	String route(String sqlStmt,String dbType) throws SQLSyntaxErrorException;
}
