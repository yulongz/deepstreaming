package com.yulongz.ignite.sqlgrid;

import com.yulongz.ignite.model.Organization;
import com.yulongz.ignite.model.Person;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.configuration.CacheConfiguration;

import java.util.List;

/**
 * Created by hadoop on 17-10-21.
 */
public class sqlDmlApiDemo {
    private static final String ORG_CACHE = sqlDmlApiDemo.class.getSimpleName() + "Organizations";
    private static final String PERSON_CACHE = sqlDmlApiDemo.class.getSimpleName() + "Persons";

    public static void main(String[] args) {
        try(Ignite ignite = Ignition.start("default-config.xml")){
            CacheConfiguration<Long, Organization> orgCacheCfg = new CacheConfiguration<>(ORG_CACHE);
            orgCacheCfg.setIndexedTypes(Long.class, Organization.class);
//            orgCacheCfg.setSqlSchema("PUBLIC");

            CacheConfiguration<Long, Person> personCacheCfg = new CacheConfiguration<>(PERSON_CACHE);
            personCacheCfg.setIndexedTypes(Long.class, Person.class);
//            personCacheCfg.setSqlSchema("PUBLIC");


            try {
                ignite.getOrCreateCache(orgCacheCfg);
                ignite.getOrCreateCache(personCacheCfg);


                insertData();
                selectData("Insert data");

                update();
                selectData("Update salary for Master degrees");

                delete();
                selectData("Delete non-Apache employees");


            } finally {
                    ignite.destroyCache(ORG_CACHE);
                    ignite.destroyCache(PERSON_CACHE);
            }
        }
    }

    public static void insertData(){
        IgniteCache<Long, Organization> orgCache = Ignition.ignite().cache(ORG_CACHE);
        IgniteCache<Long, Person> personCache = Ignition.ignite().cache(PERSON_CACHE);

        // Clear cache before running the example.
        orgCache.clear();

        personCache.clear();

        SqlFieldsQuery qry = new SqlFieldsQuery("insert into Organization (_key, id, name) values (?, ?, ?)");

        orgCache.query(qry.setArgs(1L, 1L, "ASF"));
        orgCache.query(qry.setArgs(2L, 2L, "Eclipse"));

        qry = new SqlFieldsQuery(
                "insert into Person (_key, id, orgId, firstName, lastName, salary, resume) values (?, ?, ?, ?, ?, ?, ?)");

        personCache.query(qry.setArgs(1L, 1L, 1L, "John", "Doe", 4000, "Master"));
        personCache.query(qry.setArgs(2L, 2L, 1L, "Jane", "Roe", 2000, "Bachelor"));
        personCache.query(qry.setArgs(3L, 3L, 2L, "Mary", "Major", 5000, "Master"));
        personCache.query(qry.setArgs(4L, 4L, 2L, "Richard", "Miles", 3000, "Bachelor"));
    }

    private static void update() {

        IgniteCache<Long, Person> personCache = Ignition.ignite().cache(PERSON_CACHE);
        String sql =
                "update Person set salary = salary * 1.1 " +
                        "where resume = ?";

        personCache.query(new SqlFieldsQuery(sql).setArgs("Master"));
    }

    private static void delete() {

        IgniteCache<Long, Person> personCache = Ignition.ignite().cache(PERSON_CACHE);
        String sql =
                "delete from Person " +
                        "where id in (" +
                        "select p.id " +
                        "from Person p,  \"" + ORG_CACHE + "\".Organization as o " +
                        "where o.name != ? and p.orgId = o.id" +
                        ")";

        personCache.query(new SqlFieldsQuery(sql).setArgs("ASF")).getAll();
    }

    public static void selectData(String msg) {
        IgniteCache<Long, Person> cache = Ignition.ignite().cache(PERSON_CACHE);

        String sql =
                "select p.id, concat(p.firstName, ' ', p.lastName), o.name, p.resume, p.salary " +
                        "from Person as p, \"" + ORG_CACHE + "\".Organization as o " +
                        "where p.orgId = o.id";

        List<List<?>> res = cache.query(new SqlFieldsQuery(sql).setDistributedJoins(true)).getAll();

        print(msg);

        for (Object next : res) {
            System.out.println(">>>     " + next);
        }
    }

    private static void print(String msg) {
        System.out.println();
        System.out.println(">>> " + msg);
    }
}
