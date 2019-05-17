package com.yulongz.ignite.binary;

import com.yulongz.ignite.model.*;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.cache.*;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.cache.query.TextQuery;
import org.apache.ignite.configuration.CacheConfiguration;

import javax.cache.Cache;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * The type Cache client binary query example.
 *
 * @author hadoop
 * @date 17 -12-5
 */
public class CacheClientBinaryQueryExample {
    private static final String ORG_CACHE = CacheClientBinaryQueryExample.class.getSimpleName() + "orgs";
    private static final String EMP_CACHE = CacheClientBinaryQueryExample.class.getSimpleName() + "emps";

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        try(Ignite ignite = Ignition.start("default-config.xml")){
            CacheConfiguration<Integer, Organization> orgCfg = new CacheConfiguration(ORG_CACHE);
            orgCfg.setCacheMode(CacheMode.PARTITIONED);
            orgCfg.setQueryEntities(Arrays.asList(createOrganizationQueryEntity()));

            CacheConfiguration<EmployeeKey, Employee> empCfg = new CacheConfiguration(EMP_CACHE);
            empCfg.setCacheMode(CacheMode.PARTITIONED);
            empCfg.setQueryEntities(Arrays.asList(createEmployeeQueryEntity()));

            empCfg.setKeyConfiguration(new CacheKeyConfiguration(EmployeeKey.class));

            try(IgniteCache<Integer, Organization> orgCache = ignite.getOrCreateCache(orgCfg);
                IgniteCache<EmployeeKey, Employee> empCache = ignite.getOrCreateCache(empCfg)){

                populateCache(orgCache, empCache);

                IgniteCache<BinaryObject, BinaryObject> binaryCache = empCache.withKeepBinary();

                sqlQuery(binaryCache);

                sqlJoinQuery(binaryCache);

                sqlFieldsQuery(binaryCache);

                textQuery(binaryCache);


            }finally {
                ignite.destroyCache(ORG_CACHE);
                ignite.destroyCache(EMP_CACHE);
            }
        }
    }

    private static void sqlQuery(IgniteCache<BinaryObject, BinaryObject> binaryCache){
        SqlQuery qry = new SqlQuery(Employee.class, "zip = ?");

        int zip = 94109;

        QueryCursor<Cache.Entry<BinaryObject, BinaryObject>> cursor = binaryCache.query(qry.setArgs(zip));

        List<Cache.Entry<BinaryObject, BinaryObject>> entries = cursor.getAll();

        for (Cache.Entry<BinaryObject, BinaryObject> entry : entries){
            System.out.println(">>>     " + entry.getValue().deserialize());
//            System.out.println(">>> no deserialize    " + entry.getValue());
        }
    }

    private static void sqlJoinQuery(IgniteCache<BinaryObject, BinaryObject> cache) {
        SqlQuery<BinaryObject, BinaryObject> qry = new SqlQuery<>(Employee.class,
                "from Employee, \"" + ORG_CACHE + "\".Organization as org " +
                        "where Employee.organizationId = org._key and org.name = ?");

        String organizationName = "GridGain";

        QueryCursor<Cache.Entry<BinaryObject, BinaryObject>> employees =
                cache.query(qry.setArgs(organizationName));

        System.out.println();
        System.out.println(">>> Employees working for " + organizationName + ':');

        for (Cache.Entry<BinaryObject, BinaryObject> e : employees.getAll()) {
            System.out.println(">>>     " + e.getValue());
        }
    }

    private static void sqlFieldsQuery(IgniteCache<BinaryObject, BinaryObject> cache) {
        SqlFieldsQuery qry = new SqlFieldsQuery("select name, salary from Employee");

        QueryCursor<List<?>> employees = cache.query(qry);

        System.out.println();
        System.out.println(">>> Employee names and their salaries:");

        for (List<?> row : employees.getAll()) {
            System.out.println(">>>     [Name=" + row.get(0) + ", salary=" + row.get(1) + ']');
        }
    }

    private static void textQuery(IgniteCache<BinaryObject, BinaryObject> cache) {
        TextQuery<BinaryObject, BinaryObject> qry = new TextQuery<>(Employee.class, "TX");

        QueryCursor<Cache.Entry<BinaryObject, BinaryObject>> employees = cache.query(qry);

        System.out.println();
        System.out.println(">>> Employees living in Texas:");

        for (Cache.Entry<BinaryObject, BinaryObject> e : employees.getAll()) {
            System.out.println(">>>     " + e.getValue().deserialize());
        }
    }

    private static QueryEntity createEmployeeQueryEntity() {
        QueryEntity employeeEntity = new QueryEntity();

        employeeEntity.setValueType(Employee.class.getName());
        employeeEntity.setKeyType(EmployeeKey.class.getName());

        LinkedHashMap<String, String> fields = new LinkedHashMap<>();

        fields.put("name", String.class.getName());
        fields.put("salary", Long.class.getName());
        fields.put("addr.zip", Integer.class.getName());
        fields.put("organizationId", Integer.class.getName());
        fields.put("addr.street", Integer.class.getName());

        employeeEntity.setFields(fields);

        employeeEntity.setIndexes(Arrays.asList(
                new QueryIndex("name"),
                new QueryIndex("salary"),
                new QueryIndex("addr.zip"),
                new QueryIndex("organizationId"),
                new QueryIndex("addr.street", QueryIndexType.FULLTEXT)
        ));

        return employeeEntity;
    }

    private static QueryEntity createOrganizationQueryEntity() {
        QueryEntity organizationEntity = new QueryEntity();

        organizationEntity.setValueType(Organization.class.getName());
        organizationEntity.setKeyType(Integer.class.getName());

        LinkedHashMap<String, String> fields = new LinkedHashMap<>();

        fields.put("name", String.class.getName());
        fields.put("address.street", String.class.getName());

        organizationEntity.setFields(fields);

        organizationEntity.setIndexes(Arrays.asList(
                new QueryIndex("name")
        ));

        return organizationEntity;
    }

    private static void populateCache(IgniteCache<Integer, Organization> orgCache,
                                      IgniteCache<EmployeeKey, Employee> employeeCache) {
        orgCache.put(1, new Organization(
                "GridGain",
                new Address("1065 East Hillsdale Blvd, Foster City, CA", 94404),
                OrganizationType.PRIVATE,
                new Timestamp(System.currentTimeMillis())
        ));

        orgCache.put(2, new Organization(
                "Microsoft",
                new Address("1096 Eddy Street, San Francisco, CA", 94109),
                OrganizationType.PRIVATE,
                new Timestamp(System.currentTimeMillis())
        ));

        employeeCache.put(new EmployeeKey(1, 1), new Employee(
                "James Wilson",
                12500,
                new Address("1096 Eddy Street, San Francisco, CA", 94109),
                Arrays.asList("Human Resources", "Customer Service")
        ));

        employeeCache.put(new EmployeeKey(2, 1), new Employee(
                "Daniel Adams",
                11000,
                new Address("184 Fidler Drive, San Antonio, TX", 78130),
                Arrays.asList("Development", "QA")
        ));

        employeeCache.put(new EmployeeKey(3, 1), new Employee(
                "Cristian Moss",
                12500,
                new Address("667 Jerry Dove Drive, Florence, SC", 29501),
                Arrays.asList("Logistics")
        ));

        employeeCache.put(new EmployeeKey(4, 2), new Employee(
                "Allison Mathis",
                25300,
                new Address("2702 Freedom Lane, San Francisco, CA", 94109),
                Arrays.asList("Development")
        ));

        employeeCache.put(new EmployeeKey(5, 2), new Employee(
                "Breana Robbin",
                6500,
                new Address("3960 Sundown Lane, Austin, TX", 78130),
                Arrays.asList("Sales")
        ));

        employeeCache.put(new EmployeeKey(6, 2), new Employee(
                "Philip Horsley",
                19800,
                new Address("2803 Elsie Drive, Sioux Falls, SD", 57104),
                Arrays.asList("Sales")
        ));

        employeeCache.put(new EmployeeKey(7, 2), new Employee(
                "Brian Peters",
                10600,
                new Address("1407 Pearlman Avenue, Boston, MA", 12110),
                Arrays.asList("Development", "QA")
        ));
    }
}
