package com.zz.rwdb;

import com.zz.rwdb.front.cache.CacheService;
import com.zz.rwdb.front.route.RouteService;

public class BaseService {

	private static CacheService cacheService = new CacheService();
	private static RouteService routeService = new RouteService(cacheService);
	private static BaseService instance = new BaseService();

	private BaseService() {

	}

	public static BaseService getInstance() {
		return instance;
	}

	public RouteService getRouteService() {
		return routeService;
	}

	private String dbType;

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getDbType() {
		return dbType;
	}


	

}
