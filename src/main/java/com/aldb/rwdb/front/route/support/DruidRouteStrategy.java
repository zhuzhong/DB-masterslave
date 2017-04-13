package com.aldb.rwdb.front.route.support;

import java.sql.SQLSyntaxErrorException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aldb.rwdb.BaseService;
import com.aldb.rwdb.front.route.RouteCondition;
import com.aldb.rwdb.front.route.RouteStrategy;
import com.aldb.rwdb.util.Constant;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.stat.TableStat.Name;

public class DruidRouteStrategy implements RouteStrategy {

    private static final Logger log = LoggerFactory.getLogger(DruidRouteStrategy.class);

    @Override
    public String route(RouteCondition condition) throws SQLSyntaxErrorException {

        String sql = condition.getSql();
        log.info("init_need_route_sql={}", sql);
        if (BaseService.getSpecialWriteSql() != null) {
            for (String sepecialSql : BaseService.getSpecialWriteSql()) {
                if (sql.indexOf(sepecialSql) > -1) {
                    return Constant.RW.WRITE.name();
                }
            }
        }
        String dbName = null;
        try {
            SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, condition.getDbType().toLowerCase());
            SQLStatement statement = parser.parseStatement();

            if (statement instanceof SQLSelectStatement) {      
                dbName = Constant.RW.READ.name();
                boolean change = true;
                // 对于读取操作，如果读取的全部是写库的表，则可以将其路由到写库，以解决数据同步的问题
                if (condition.getDbType().toLowerCase().equals("mysql")) {
                    MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
                    statement.accept(visitor);
                    Set<Name> tableNames = visitor.getTables().keySet();
                    logReadTables(tableNames);
                    for (Name name : tableNames) {
                        if (!BaseService.getAllWritedbTables().contains(name.getName().trim())) {
                            change = false;
                            break;
                        }

                    }
                }
                if (change) {
                    dbName = Constant.RW.WRITE.name();
                }
            } else {
                dbName = Constant.RW.WRITE.name();
            }
        } catch (Exception e) {
            log.error("durid parse sql error,sql={},e={}", sql,e);
            if (sql != null && sql.trim() != null && sql.length() >= 6) {
                String subSql = sql.substring(0, 6);
                if (subSql.equalsIgnoreCase("select")) {
                    dbName = Constant.RW.READ.name();
                } else {
                    dbName = Constant.RW.WRITE.name();
                }
            }
            
        }
        log.info("need_route_sql={},result={}", sql, dbName);
        return dbName;
    }

    /*
     * private ThreadLocal<Random> random;
     * 
     * public DruidRouteStrategy() { this.random = new ThreadLocal<Random>() {
     * 
     * @Override protected Random initialValue() { return new Random(); } }; }
     */
    private void logReadTables(Set<Name> tableNames) {
        StringBuffer sb = new StringBuffer();
        for (Name name : tableNames) {
            sb.append(name.getName());
            sb.append(",");
        }
        log.info("read all table name is {}", sb.toString());
    }
}
