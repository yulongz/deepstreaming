package com.yulongz.ignite.sqlgrid;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.configuration.CacheConfiguration;

import java.util.List;

/**
 * Created by hadoop on 17-10-21.
 */
public class sqlDdlApiDemo {

    private static String CACHE_NAME = "person";

    public static void main(String[] args) {
        try(Ignite ignite = Ignition.start("default-config.xml")){

            // Create dummy cache to act as an entry point for SQL queries (new SQL API which do not require this
            // will appear in future versions, JDBC and ODBC drivers do not require it already).
            CacheConfiguration cCfg = new CacheConfiguration(CACHE_NAME).setSqlSchema("PUBLIC");
            try(IgniteCache cache = ignite.getOrCreateCache(cCfg)){
                // Create reference City table based on REPLICATED template.
                cache.query(new SqlFieldsQuery(
                        "CREATE TABLE city (id LONG PRIMARY KEY, name VARCHAR) WITH \"template=replicated\"")).getAll();

                // Create table based on PARTITIONED template with one backup.
                cache.query(new SqlFieldsQuery(
                        "CREATE TABLE person (id LONG, name VARCHAR, city_id LONG, PRIMARY KEY (id, city_id)) " +
                                "WITH \"backups=1, affinityKey=city_id\"")).getAll();

                // Create an index.
                cache.query(new SqlFieldsQuery("CREATE INDEX on Person (city_id)")).getAll();

                SqlFieldsQuery insertData = new SqlFieldsQuery("INSERT INTO city (id, name) VALUES (?, ?)");
                cache.query(insertData.setArgs(1L, "Forest Hill")).getAll();
                cache.query(insertData.setArgs(2L, "Denver")).getAll();
                cache.query(insertData.setArgs(3L, "St. Petersburg")).getAll();

                insertData = new SqlFieldsQuery("INSERT INTO person (id, name, city_id) values (?, ?, ?)");
                cache.query(insertData.setArgs(1L, "John Doe", 3L)).getAll();
                cache.query(insertData.setArgs(2L, "Jane Roe", 2L)).getAll();
                cache.query(insertData.setArgs(3L, "Mary Major", 1L)).getAll();
                cache.query(insertData.setArgs(4L, "Richard Miles", 2L)).getAll();

                SqlFieldsQuery queryData = new SqlFieldsQuery("SELECT p.name, c.name FROM Person p INNER JOIN City c on c.id = p.city_id");
                List<List<?>> res = cache.query(queryData).getAll();

                for (List<?> next : res){
                    System.out.println(">>>    " + next);
                }


                cache.query(new SqlFieldsQuery("drop table Person")).getAll();
                cache.query(new SqlFieldsQuery("drop table City")).getAll();
            } finally {
                ignite.destroyCache(CACHE_NAME);
            }


        }
    }
}
