package com.yulongz.ignite.sqlgrid;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.configuration.SqlConnectorConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by hadoop on 17-10-24.
 */
public class JdbcConfigureDemo {
    public static void main(String[] args) throws Exception{

        // Register JDBC driver.
        Class.forName("org.apache.ignite.IgniteJdbcThinDriver");
        // Open the JDBC connection.
        // jdbc:ignite:thin://host[:port][?<params>]
        // port default 10800
        // jdbc:ignite:thin://myHost:11900?distributedJoins=true&autoCloseServerCursor=true
        Connection conn = DriverManager.getConnection("jdbc:ignite:thin://192.168.0.50");

        SqlConnectorConfiguration sqlCfg = new SqlConnectorConfiguration();

        IgniteConfiguration cfg = new IgniteConfiguration().setSqlConnectorConfiguration(sqlCfg);

    }
}
