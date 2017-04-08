package com.zz.rwdb;

import java.util.List;

import com.zz.rwdb.front.cache.CacheService;
import com.zz.rwdb.front.route.RouteService;

public class BaseService {

    // private static CacheService cacheService = new CacheService();
    // private static RouteService routeService = new
    // RouteService(cacheService);

    private static RouteService routeService = new RouteService(null);// 去掉缓存

    /*
     * private static BaseService instance = new BaseService(); 
     * public static BaseService getInstance() { return instance; }
     */

    private BaseService() {
    }

    private static List<String> specialWriteSql;

    public static List<String> getSpecialWriteSql() {
        return specialWriteSql;
    }

    public static void setSpecialWriteSql(List<String> specialWriteSql) {
        BaseService.specialWriteSql = specialWriteSql;
    }

    public static RouteService getRouteService() {
        return routeService;
    }

    private static String dbType;

    public static void setDbType(String dbType) {
        BaseService.dbType = dbType;
    }

    public static String getDbType() {
        return BaseService.dbType;
    }

}
