package com.zz.rwdb.front.route.support;

import java.sql.SQLSyntaxErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.zz.rwdb.BaseService;
import com.zz.rwdb.front.route.RouteCondition;
import com.zz.rwdb.front.route.RouteStrategy;
import com.zz.rwdb.util.Constant;

public class DruidRouteStrategy implements RouteStrategy {

    private static final Logger log = LoggerFactory.getLogger(DruidRouteStrategy.class);

    @Override
    public String route(RouteCondition condition) throws SQLSyntaxErrorException {
        SQLStatementParser parser = null;

        String sql = condition.getSql();
        log.info("need parse full sql={}", sql);
        if (BaseService.getSpecialWriteSql() != null) {
            for (String sepecialSql : BaseService.getSpecialWriteSql()) {
                if (sql.indexOf(sepecialSql) > -1) {
                    return Constant.RW.WRITE.name();
                }
            }
        }
        parser = SQLParserUtils.createSQLStatementParser(sql, condition.getDbType());

        SQLStatement statement = parser.parseStatement();

        if (statement instanceof SQLSelectStatement) {
            return Constant.RW.READ.name();

        } else {
            return Constant.RW.WRITE.name();

        }

    }

    /*
     * private ThreadLocal<Random> random;
     * 
     * public DruidRouteStrategy() { this.random = new ThreadLocal<Random>() {
     * 
     * @Override protected Random initialValue() { return new Random(); } }; }
     */
}
