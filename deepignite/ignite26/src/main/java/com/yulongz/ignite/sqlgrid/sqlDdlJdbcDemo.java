package com.yulongz.ignite.sqlgrid;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Created by hadoop on 17-10-20.
 */
public class sqlDdlJdbcDemo {
    public static void main(String[] args) throws Exception{
        System.out.println("Start create table  -----------");

        Class.forName("org.apache.ignite.IgniteJdbcDriver");
        Connection conn = DriverManager.getConnection("jdbc:ignite:thin://127.0.0.1/");

        Statement stat = conn.createStatement();
        // Create table based on REPLICATED template
        stat.executeUpdate("CREATE TABLE City (" +
                "id LONG PRIMARY KEY, name VARCHAR) " +
                "WITH \"template=replicated\"");
        // Create table based on PARTITIONED template with one backup
        stat.executeUpdate("CREATE TABLE Person ( " +
                "id LONG, name VARCHAR, city_id LONG, PRIMARY KEY (id, city_id)) " +
                "WITH \"backups=1, affinityKey=city_id\"");

        stat.executeUpdate("CREATE INDEX idx_city_name ON City (name)");
        stat.executeUpdate("CREATE INDEX idx_person_name ON Person (name)");
    }
}
