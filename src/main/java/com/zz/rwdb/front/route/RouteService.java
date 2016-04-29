package com.zz.rwdb.front.route;

import java.sql.SQLSyntaxErrorException;

import com.zz.rwdb.front.cache.CachePool;
import com.zz.rwdb.front.cache.CacheService;
import com.zz.rwdb.front.route.support.DruidRouteStrategy;

/**
 * 基本原理，是利用druid的sql解析 原始sql获取相关的sql内容，然后进行相应的路由
 * 
 * @author sunff
 *
 */
public class RouteService {

	private CachePool cachePool;
	private RouteStrategy routeStrategy;

	public RouteService(CacheService cacheService) {
		this.cachePool = cacheService.getCachePool();
		this.routeStrategy = new DruidRouteStrategy();
	}

	public RouteResult route(String sqlStmt,String dbType) throws SQLSyntaxErrorException {

		RouteResult rr = (RouteResult) cachePool.get(sqlStmt);

		if (rr != null) {
			return rr;
		}

		RouteResult newRr = new RouteResult();

		newRr.setStmt(sqlStmt);

		String tableName = removeBackquote(routeStrategy.route(sqlStmt,dbType));
		tableName = removeTableDot(tableName);

		newRr.setTartgetHost(getTartgetMycat(tableName));

		cachePool.putIfAbsent(sqlStmt, newRr);

		return newRr;
	}

	private String getTartgetMycat(String tableName)
			throws SQLSyntaxErrorException {

		return tableName;
	}

	/**
	 * 移除`符号
	 * 
	 * @param str
	 * @return
	 */
	public String removeBackquote(String str) {
		// 删除名字中的`tablename`和'value'
		if (str.length() > 0) {
			StringBuilder sb = new StringBuilder(str);
			if (sb.charAt(0) == '`' || sb.charAt(0) == '\'') {
				sb.deleteCharAt(0);
			}
			if (sb.charAt(sb.length() - 1) == '`'
					|| sb.charAt(sb.length() - 1) == '\'') {
				sb.deleteCharAt(sb.length() - 1);
			}

			return sb.toString();
		}
		return "";

	}

	private String removeTableDot(String tableName) {
		int index = tableName.indexOf(".");
		if (tableName.indexOf(".") != -1) {
			return tableName.substring(index + 1);
		}
		return tableName;
	}

}
