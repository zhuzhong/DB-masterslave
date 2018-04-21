package com.z.rwdb.front.keypool;

import java.util.concurrent.ConcurrentHashMap;

public class BackendPool {

   // private ThreadLocal<Random> random;

    private BackendPool() {
        super();
        dbMap = new ConcurrentHashMap<String, PhysicalDatasource>();
       /* this.random = new ThreadLocal<Random>() {
            @Override
            protected Random initialValue() {
                return new Random();
            }
        };*/
    }

    private ConcurrentHashMap<String, PhysicalDatasource> dbMap;

    private static BackendPool instance = new BackendPool();

    public static BackendPool getInstance() {
        return instance;
    }

    public PhysicalDatasource getDataSouce(String dsName) {
        return dbMap.get(dsName);
    }

    public void putDataSouce(String dsName, PhysicalDatasource ds) {
        dbMap.putIfAbsent(dsName, ds);
       /* if (dsName.startsWith(Constant.RW.READ.name())) {
            readDataSourceSize.incrementAndGet();
        } else {
            writeDataSourceSize.incrementAndGet();
        }*/
    }

    public void removeDataSouce(String dsName) {
        dbMap.remove(dsName);
        /*if (dsName.startsWith(Constant.RW.READ.name())) {
            readDataSourceSize.decrementAndGet();
        } else {
            writeDataSourceSize.decrementAndGet();
        }*/
    }

    public boolean isAlive(String dsName) {
        return dbMap.containsKey(dsName);
    }

    public ConcurrentHashMap<String, PhysicalDatasource> getDbMap() {
        return dbMap;
    }

/*    private AtomicInteger readDataSourceSize = new AtomicInteger(0);

    private AtomicInteger writeDataSourceSize = new AtomicInteger(0);

    public int getWriteDataSourceSize() {
        return writeDataSourceSize.get();
    }

    public int getReadDataSourceSize() {
        return readDataSourceSize.get();
    }
*/
 /*   public PhysicalDatasource getAlivePhysicalDatasource() {
        Collection<PhysicalDatasource> collection = BackendPool.getInstance().getDbMap().values();
        PhysicalDatasource[] newArray = new PhysicalDatasource[collection.size()];
        collection.toArray(newArray);
        int randomNum = random.get().nextInt(collection.size());

        return newArray[randomNum];
    }*/

}
