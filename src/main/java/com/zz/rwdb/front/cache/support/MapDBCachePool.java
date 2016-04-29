
package com.zz.rwdb.front.cache.support;

import org.mapdb.HTreeMap;

import com.zz.rwdb.front.cache.CachePool;

public class MapDBCachePool implements CachePool {

	private final HTreeMap<Object, Object> htreeMap;
	private final long maxSize;

	public MapDBCachePool(HTreeMap<Object, Object> htreeMap, long maxSize) {
		this.htreeMap = htreeMap;
		this.maxSize = maxSize;
	}

	@Override
	public void putIfAbsent(Object key, Object value) {
		htreeMap.putIfAbsent(key, value);
	}

	@Override
	public Object get(Object key) {
		return htreeMap.get(key);
	}

	@Override
	public void clearCache() {
		htreeMap.clear();
	}

	@Override
	public long getMaxSize() {
		return maxSize;
	}

}