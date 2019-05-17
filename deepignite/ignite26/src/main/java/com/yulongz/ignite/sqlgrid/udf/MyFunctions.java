package com.yulongz.ignite.sqlgrid.udf;

import org.apache.ignite.cache.query.annotations.QuerySqlFunction;

/**
 * Created by hadoop on 17-10-31.
 *
 * cacheConfig.setSqlFunctionClasses(MyFunctions.class);
 */
public class MyFunctions {
    @QuerySqlFunction
    public static double sqr(double x) {

        return x + x;
    }
}
