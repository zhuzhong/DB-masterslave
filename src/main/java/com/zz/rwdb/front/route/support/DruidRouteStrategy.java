package com.zz.rwdb.front.route.support;

import java.sql.SQLSyntaxErrorException;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.zz.rwdb.front.route.RouteStrategy;
import com.zz.rwdb.util.Constant;

public class DruidRouteStrategy implements RouteStrategy {

    @Override
    public String route(String sqlStmt, String dbType) throws SQLSyntaxErrorException {
        SQLStatementParser parser = null;
        /*
         * if (Constant.DBTYPE.MYSQL.name().equalsIgnoreCase(dbType)) { parser =
         * new MySqlStatementParser(sqlStmt);
         * 
         * } else if (Constant.DBTYPE.ORACLE.name().equalsIgnoreCase(dbType)) {
         * parser = new OracleStatementParser(sqlStmt); }
         */

        parser = SQLParserUtils.createSQLStatementParser(sqlStmt, dbType.toLowerCase());

        SQLStatement statement = parser.parseStatement();

        if (statement instanceof SQLSelectStatement) {
            return Constant.RW.READ.name();
        } else {
            return Constant.RW.WRITE.name();
        }

    }

}
