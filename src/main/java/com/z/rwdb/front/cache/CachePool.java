
package com.z.rwdb.front.cache;


public interface CachePool {

	public void putIfAbsent(Object key, Object value);

	public Object get(Object key);

	public void clearCache();

	public long getMaxSize();
}