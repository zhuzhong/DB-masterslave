package com.zz.rwdb.front.route.support;

import java.sql.SQLSyntaxErrorException;
import java.util.Random;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.zz.rwdb.front.keypool.BackendPool;
import com.zz.rwdb.front.route.RouteCondition;
import com.zz.rwdb.front.route.RouteStrategy;
import com.zz.rwdb.util.Constant;

public class DruidRouteStrategy implements RouteStrategy {

    @Override
    public String route(RouteCondition condition) throws SQLSyntaxErrorException {
        SQLStatementParser parser = null;
        /*
         * if (Constant.DBTYPE.MYSQL.name().equalsIgnoreCase(dbType)) { parser =
         * new MySqlStatementParser(sqlStmt);
         * 
         * } else if (Constant.DBTYPE.ORACLE.name().equalsIgnoreCase(dbType)) {
         * parser = new OracleStatementParser(sqlStmt); }
         */

        parser = SQLParserUtils.createSQLStatementParser(condition.getSql(), condition.getDbType());

        SQLStatement statement = parser.parseStatement();

        if (statement instanceof SQLSelectStatement) {
            // 对于多个从库则随机取
            int randomNum = random.get().nextInt(condition.getReadDBSize());
            return Constant.getDataSourceKey(Constant.RW.READ.name(), randomNum);

        } else {
            int randomNum = random.get().nextInt(condition.getWriteDBSize());
            return Constant.getDataSourceKey(Constant.RW.WRITE.name(), randomNum);

        }

    }

    private ThreadLocal<Random> random;

    public DruidRouteStrategy() {
        this.random = new ThreadLocal<Random>() {
            @Override
            protected Random initialValue() {
                return new Random();
            }
        };
    }

}
