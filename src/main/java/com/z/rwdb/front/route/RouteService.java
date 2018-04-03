package com.z.rwdb.front.route;

import java.sql.SQLSyntaxErrorException;

import com.z.rwdb.front.cache.CachePool;
import com.z.rwdb.front.cache.CacheService;
import com.z.rwdb.front.route.support.DruidRouteStrategy;

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
		this.cachePool = (cacheService == null ? null : cacheService
				.getCachePool());
		this.routeStrategy = new DruidRouteStrategy();
	}

	public RouteResult route(RouteCondition condition)
			throws SQLSyntaxErrorException {

		/*
		 * RouteResult rr = (RouteResult) cachePool.get(condition.getSql());
		 * 
		 * if (rr != null) { return rr; }
		 * 
		 * RouteResult newRr = new RouteResult();
		 * 
		 * newRr.setStmt(condition.getSql());
		 * 
		 * String dbKey = removeBackquote(routeStrategy.route(condition)); dbKey
		 * = removeTableDot(dbKey);
		 * 
		 * newRr.setTartgetHost(getTartgetMycat(dbKey));
		 * newRr.setTartgetHost(dbKey);
		 * cachePool.putIfAbsent(condition.getSql(), newRr);
		 * 
		 * return newRr;
		 */

		// 去掉缓存机制，因为在现有项目中缓存的需要并不是很强烈
		RouteResult newRr = new RouteResult();
		newRr.setStmt(condition.getSql());
		String dbKey = removeBackquote(routeStrategy.route(condition));
		newRr.setTartgetHost(dbKey);
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
