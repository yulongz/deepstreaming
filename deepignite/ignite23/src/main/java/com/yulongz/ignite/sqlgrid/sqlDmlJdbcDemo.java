package com.yulongz.ignite.sqlgrid;

import java.sql.*;

/**
 * Created by hadoop on 17-10-20.
 */
public class sqlDmlJdbcDemo {
    public static void main(String[] args) throws Exception{

        Class.forName("org.apache.ignite.IgniteJdbcDriver");
        Connection conn = DriverManager.getConnection("jdbc:ignite:thin://127.0.0.1/");

//        insertDate(conn);

//        updateDate(conn);

        deleteData(conn);

        queryData(conn);
    }

    private static void insertDate(Connection conn) throws Exception{
        //Populate City table
        try(PreparedStatement stmt = conn.prepareStatement("INSERT INTO City (id, name) VALUES (?, ?)")){
            stmt.setLong(1, 1L);
            stmt.setString(2, "Forest Hill");
            stmt.executeUpdate();

            stmt.setLong(1, 2L);
            stmt.setString(2, "Denver");
            stmt.executeUpdate();

            stmt.setLong(1, 3L);
            stmt.setString(2, "St. Petersburg");
            stmt.executeUpdate();
        }

        //Populate Person table
        try(PreparedStatement stmt = conn.prepareStatement("INSERT INTO Person (id, name, city_id) VALUES (?, ?, ?)")){
            stmt.setLong(1, 1L);
            stmt.setString(2, "John Doe");
            stmt.setLong(3, 3L);
            stmt.executeUpdate();

            stmt.setLong(1, 2L);
            stmt.setString(2, "Jane Roe");
            stmt.setLong(3, 2L);
            stmt.executeUpdate();

            stmt.setLong(1, 3L);
            stmt.setString(2, "Mary Major");
            stmt.setLong(3, 1L);
            stmt.executeUpdate();

            stmt.setLong(1, 4L);
            stmt.setString(2, "Richard Miles");
            stmt.setLong(3, 2L);
            stmt.executeUpdate();
        }
    }

    public static void queryData(Connection conn) {
        try(Statement stat = conn.createStatement()){
            try (ResultSet rs = stat.executeQuery("SELECT p.name, c.name " +
                    "FROM Person p, City c WHERE p.city_id = c.id")){
                System.out.println("Query results:");
                while (rs.next()) {
                    System.out.println(">>>    " +
                            rs.getString(1) +
                            ", " +
                            rs.getString(2));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateDate(Connection conn){
        try (Statement stmt = conn.createStatement()) {
            // Update City
            stmt.executeUpdate("UPDATE City SET name = 'Foster City' WHERE id = 2");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void deleteData(Connection conn){
        try (Statement stmt = conn.createStatement()) {

            // Delete from Person
            stmt.executeUpdate("DELETE FROM Person WHERE name = 'John Doe'");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
