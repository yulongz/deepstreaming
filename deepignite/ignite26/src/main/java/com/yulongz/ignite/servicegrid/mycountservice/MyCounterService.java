package com.yulongz.ignite.servicegrid.mycountservice;

import javax.cache.CacheException;

/**
 * @author hadoop
 * @date 17-11-28
 */
public interface MyCounterService {

    int increment() throws CacheException;

    int get() throws CacheException;

}
