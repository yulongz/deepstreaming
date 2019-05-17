package com.yulongz.ignite.datagrid;

import com.yulongz.ignite.model.Organization;
import com.yulongz.ignite.model.Person;
import com.yulongz.ignite.sqlgrid.udf.MyFunctions;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.cache.query.TextQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.lang.IgniteBiPredicate;

import javax.cache.Cache;
import java.util.List;

/**
 * Created by hadoop on 17-10-19.
 *
 * @author Tiny
 */
public class QueryDemo {
    public static void main(String[] args) {
        try(Ignite ignite = Ignition.start("default-config.xml")){
            try{
                CacheConfiguration<Long, Person> cacheCfg = new CacheConfiguration<>("queryCache");
                //
                cacheCfg.setIndexedTypes(Long.class, Person.class);
                cacheCfg.setSqlFunctionClasses(MyFunctions.class);
                ignite.getOrCreateCache(cacheCfg);

                initialize();

//                scanQuery();

//                textQuery();

                sqlFieldQuery();

            }finally {
                ignite.destroyCache("queryCache");
            }
        }
    }

    private static void initialize(){
        IgniteCache<Long, Person> cache = Ignition.ignite().cache("queryCache");

        Organization org1 = new Organization("ApacheIgnite");
        Organization org2 = new Organization("Other");

        Person p1 = new Person(org1, "John", "Doe", 2000, "John Doe has Master Degree.");
        Person p2 = new Person(org1, "Jane", "Doe", 1000, "Jane Doe has Bachelor Degree.");
        Person p3 = new Person(org2, "John", "Smith", 1000, "John Smith has Bachelor Degree.");
        Person p4 = new Person(org2, "Jane", "Smith", 2000, "Jane Smith has Master Degree.");

        cache.put(p1.id, p1);
        cache.put(p2.id, p2);
        cache.put(p3.id, p3);
        cache.put(p4.id, p4);
    }

    private static void scanQuery(){
        IgniteCache<Long, Person> cache = Ignition.ignite().cache("queryCache");

        IgniteBiPredicate<Long, Person> filter = new IgniteBiPredicate<Long, Person>() {
            @Override
            public boolean apply(Long l, Person person) {
                return person.getSalary() > 1000;
            }
        };

        try(QueryCursor<Person> cursor = cache.query(new ScanQuery(filter))) {
//                for (Person p : cursor){
            System.out.println("---" + cursor.getAll());
//                }
        }
    }

    private static void textQuery(){
        IgniteCache<Long, Person> cache = Ignition.ignite().cache("queryCache");
        TextQuery txt = new TextQuery(Person.class, "Master");
        try(QueryCursor<Cache.Entry<Long,Person>> masters = cache.query(txt)){
            for (Cache.Entry<Long,Person> e : masters){
                System.out.println(e.getValue().toString());
            }
            masters.close();
        }
    }

    private static void sqlFieldQuery(){
        IgniteCache<Long, Person> cache = Ignition.ignite().cache("queryCache");

        // Execute query to get names of all employees.
//        QueryCursor<List<?>> cursor = cache.query(new SqlFieldsQuery(
//                "select concat(firstName, ' ', lastName) from Person"));

        QueryCursor<List<?>> cursor = cache.query(new SqlFieldsQuery("select salary from Person where sqr(salary) > 2000"));

        // In this particular case each row will have one element with full name of an employees.
        List<List<?>> res = cursor.getAll();

        // Print names.
        print("Names of all employees:", res);
    }

    /**
     * Prints message and query results.
     *
     * @param msg Message to print before all objects are printed.
     * @param col Query results.
     */
    private static void print(String msg, Iterable<?> col) {
        print(msg);
        print(col);
    }

    /**
     * Prints message.
     *
     * @param msg Message to print before all objects are printed.
     */
    private static void print(String msg) {
        System.out.println();
        System.out.println(">>> " + msg);
    }

    /**
     * Prints query results.
     *
     * @param col Query results.
     */
    private static void print(Iterable<?> col) {
        for (Object next : col) {
            System.out.println(">>>     " + next);
        }

    }
}
