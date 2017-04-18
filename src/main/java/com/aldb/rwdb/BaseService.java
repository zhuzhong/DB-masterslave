package com.aldb.rwdb;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.aldb.rwdb.front.route.RouteService;

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
    private static Set<String> allWritedbTables=new HashSet<String>();
    public static void addWritedbTables(String tableName){
        allWritedbTables.add(tableName);
    }
    public static Set<String> getAllWritedbTables(){
        return allWritedbTables;
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

    
    private static boolean realTime;

    public static boolean isRealTime() {
        return realTime;
    }
    public static void setRealTime(boolean realTime) {
        BaseService.realTime = realTime;
    }

   
    
}
