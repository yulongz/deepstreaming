package com.yulongz.ignite.sqlgrid;

import com.yulongz.ignite.model.Organization;
import com.yulongz.ignite.model.Person;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.affinity.AffinityKey;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.configuration.CacheConfiguration;

import java.util.List;

/**
 * Created by hadoop on 17-10-24.
 *
 * @author hadoop
 * @date 2017/10/24
 */
public class sqlQueryDemo {
    /** Organizations cache name. */
    private static final String ORG_CACHE = sqlQueryDemo.class.getSimpleName() + "Organizations";

    /** Persons collocated with Organizations cache name. */
    private static final String COLLOCATED_PERSON_CACHE = sqlQueryDemo.class.getSimpleName() + "CollocatedPersons";

    /** Persons cache name. */
    private static final String PERSON_CACHE = sqlQueryDemo.class.getSimpleName() + "Persons";

    public static void main(String[] args) {
        try(Ignite ignite = Ignition.start("default-config.xml")) {

            CacheConfiguration<Long, Organization> orgCacheCfg = new CacheConfiguration<>(ORG_CACHE);
            orgCacheCfg.setCacheMode(CacheMode.PARTITIONED);
            orgCacheCfg.setIndexedTypes(Long.class, Organization.class);

            CacheConfiguration<AffinityKey<Long>, Person> colPersonCacheCfg =
                    new CacheConfiguration<>(COLLOCATED_PERSON_CACHE);
            colPersonCacheCfg.setCacheMode(CacheMode.PARTITIONED);
            colPersonCacheCfg.setIndexedTypes(AffinityKey.class, Person.class);

            CacheConfiguration<Long, Person> personCacheCfg = new CacheConfiguration<>(PERSON_CACHE);
            personCacheCfg.setCacheMode(CacheMode.PARTITIONED);
            personCacheCfg.setIndexedTypes(Long.class, Person.class);

            try{
                ignite.getOrCreateCache(orgCacheCfg);
                ignite.getOrCreateCache(colPersonCacheCfg);
                ignite.getOrCreateCache(personCacheCfg);

                initialize();

                sqlQuery();

                sqlQueryWithJoin();

                sqlQueryWithDistributedJoin();

                sqlQueryWithAggregation();

                sqlFieldsQuery();

                sqlFieldsQueryWithJoin();

            } finally {
                ignite.destroyCache(ORG_CACHE);
                ignite.destroyCache(COLLOCATED_PERSON_CACHE);
                ignite.destroyCache(PERSON_CACHE);
            }
        }
    }

    public static void sqlQuery(){
        IgniteCache<Long, Person> cache = Ignition.ignite().cache(PERSON_CACHE);

        // SQL clause which selects salaries based on range.
        String sql = "salary > ? and salary <= ?";

        // Execute queries for salary ranges.
        print("People with salaries between 0 and 1000 (queried with SQL query): ",
                cache.query(new SqlQuery<AffinityKey<Long>, Person>(Person.class, sql).
                        setArgs(0, 1000)).getAll());

        print("People with salaries between 1000 and 2000 (queried with SQL query): ",
                cache.query(new SqlQuery<AffinityKey<Long>, Person>(Person.class, sql).
                        setArgs(1000, 2000)).getAll());
    }

    private static void sqlQueryWithJoin() {
        IgniteCache<AffinityKey<Long>, Person> cache = Ignition.ignite().cache(COLLOCATED_PERSON_CACHE);

        // SQL clause query which joins on 2 types to select people for a specific organization.
        String joinSql =
                "from Person, \"" + ORG_CACHE + "\".Organization as org " +
                        "where Person.orgId = org.id " +
                        "and lower(org.name) = lower(?)";

        // Execute queries for find employees for different organizations.
        print("Following people are 'ApacheIgnite' employees: ",
                cache.query(new SqlQuery<AffinityKey<Long>, Person>(Person.class, joinSql).
                        setArgs("ApacheIgnite")).getAll());

        print("Following people are 'Other' employees: ",
                cache.query(new SqlQuery<AffinityKey<Long>, Person>(Person.class, joinSql).
                        setArgs("Other")).getAll());
    }

    private static void sqlQueryWithDistributedJoin() {
        IgniteCache<Long, Person> cache = Ignition.ignite().cache(PERSON_CACHE);

        // SQL clause query which joins on 2 types to select people for a specific organization.
        String joinSql =
                "from Person, \"" + ORG_CACHE + "\".Organization as org " +
                        "where Person.orgId = org.id " +
                        "and lower(org.name) = lower(?)";

        SqlQuery qry = new SqlQuery<Long, Person>(Person.class, joinSql).
                setArgs("ApacheIgnite");

        // Enable distributed joins for query.
        qry.setDistributedJoins(true);

        // Execute queries for find employees for different organizations.
        print("Following people are 'ApacheIgnite' employees (distributed join): ", cache.query(qry).getAll());

        qry.setArgs("Other");

        print("Following people are 'Other' employees (distributed join): ", cache.query(qry).getAll());
    }

    private static void sqlQueryWithAggregation() {
        IgniteCache<AffinityKey<Long>, Person> cache = Ignition.ignite().cache(COLLOCATED_PERSON_CACHE);

        // Calculate average of salary of all persons in ApacheIgnite.
        // Note that we also join on Organization cache as well.
        String sql =
                "select avg(salary) " +
                        "from Person, \"" + ORG_CACHE + "\".Organization as org " +
                        "where Person.orgId = org.id " +
                        "and lower(org.name) = lower(?)";

        QueryCursor<List<?>> cursor = cache.query(new SqlFieldsQuery(sql).setArgs("ApacheIgnite"));

        // Calculate average salary for a specific organization.
        print("Average salary for 'ApacheIgnite' employees: ", cursor.getAll());
    }

    private static void sqlFieldsQuery() {
        IgniteCache<Long, Person> cache = Ignition.ignite().cache(PERSON_CACHE);

        // Execute query to get names of all employees.
        QueryCursor<List<?>> cursor = cache.query(new SqlFieldsQuery(
                "select concat(firstName, ' ', lastName) from Person"));

        // In this particular case each row will have one element with full name of an employees.
        List<List<?>> res = cursor.getAll();

        // Print names.
        print("Names of all employees:", res);
    }


    private static void sqlFieldsQueryWithJoin() {
        IgniteCache<AffinityKey<Long>, Person> cache = Ignition.ignite().cache(COLLOCATED_PERSON_CACHE);

        // Execute query to get names of all employees.
        String sql =
                "select concat(firstName, ' ', lastName), org.name " +
                        "from Person, \"" + ORG_CACHE + "\".Organization as org " +
                        "where Person.orgId = org.id";

        QueryCursor<List<?>> cursor = cache.query(new SqlFieldsQuery(sql));

        // In this particular case each row will have one element with full name of an employees.
        List<List<?>> res = cursor.getAll();

        // Print persons' names and organizations' names.
        print("Names of all employees and organizations they belong to: ", res);
    }

    public static void initialize(){
        IgniteCache<Long, Organization> orgCache = Ignition.ignite().cache(ORG_CACHE);
        IgniteCache<AffinityKey<Long>, Person> colCache = Ignition.ignite().cache(COLLOCATED_PERSON_CACHE);
        IgniteCache<Long, Person> personCache = Ignition.ignite().cache(PERSON_CACHE);

        orgCache.clear();
        colCache.clear();
        personCache.clear();

        Organization o1 = new Organization("ApacheIgnite");
        Organization o2 = new Organization("Other");

        Person p1 = new Person(o1, "John", "Doe", 2000, "John Doe has Master Degree.");
        Person p2 = new Person(o1, "Jane", "Doe", 1000, "Jane Doe has Bachelor Degree.");
        Person p3 = new Person(o2, "John", "Smith", 1000, "John Smith has Bachelor Degree.");
        Person p4 = new Person(o2, "Jane", "Smith", 2000, "Jane Smith has Master Degree.");

        orgCache.put(o1.id(), o1);
        orgCache.put(o2.id(), o2);

        colCache.put(p1.key(), p1);
        colCache.put(p2.key(), p2);
        colCache.put(p3.key(), p3);
        colCache.put(p4.key(), p4);

        personCache.put(p1.id, p1);
        personCache.put(p2.id, p2);
        personCache.put(p3.id, p3);
        personCache.put(p4.id, p4);
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
        for (Object next : col)
            System.out.println(">>>     " + next);
    }
}
