package com.z.rwdb.front.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheService {
	private static final Logger logger = LoggerFactory.getLogger(CacheService.class);

	private CachePool cachePool;

	public CacheService() {
		//cachePool = CacheFactory.createCachePool();
	}

	/**
	 * get cache pool by name ,caller should cache result
	 * 
	 * @param poolName
	 * @return CachePool
	 */
	public CachePool getCachePool() {
		CachePool pool = this.cachePool;
		if (pool == null) {
			throw new IllegalArgumentException("can't find cache pool:");
		} else {
			return pool;
		}
	}

	public void clearCache() {
		logger.info("clear all cache pool ");
		cachePool.clearCache();
	}

}
