package com.yulongz.ignite.persistentstore.jdbcstore;

import com.yulongz.ignite.model.Person;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.cache.store.CacheStoreSession;
import org.apache.ignite.lang.IgniteBiInClosure;
import org.apache.ignite.resources.CacheStoreSessionResource;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import java.sql.*;

/**
 * Created by hadoop on 17-10-31.
 */
public class CacheJdbcPersonStoreTx extends CacheStoreAdapter<Long, Person>{

    @CacheStoreSessionResource
    private CacheStoreSession session;

    @Override
    public void sessionEnd(boolean commit){
        try(Connection conn = session.attachment()){
            if (conn != null && session.isWithinTransaction()){
                if (commit){
                    conn.commit();
                }else {
                    conn.rollback();
                }
            }
        } catch (SQLException e) {
            throw new CacheWriterException("Failed to end cache session : "+e);
        }
    }

    @Override
    public Person load(Long key) throws CacheLoaderException {
        try(Connection conn = connection()){
            try (PreparedStatement st = conn.prepareStatement("select * from PERSONS where id=?")) {
                st.setLong(1, key);
                ResultSet rs = st.executeQuery();
                return rs.next() ? new Person(rs.getLong(1), rs.getString(2), rs.getString(3)) : null;
            }
        } catch (SQLException e){
            throw  new CacheLoaderException("Failed to load : " + key, e);
        }
    }

    @Override
    public void write(Cache.Entry<? extends Long, ? extends Person> entry){
        Long key = entry.getKey();
        Person val = entry.getValue();
        try (Connection conn = connection()) {
            // Syntax of MERGE statement is database specific and should be adopted for your database.
            // If your database does not support MERGE statement then use sequentially update, insert statements.
            try (PreparedStatement st = conn.prepareStatement(
                    "merge into PERSONS (id, firstName, lastName) key (id) VALUES (?, ?, ?)")) {
                st.setLong(1, key);
                st.setString(2, val.firstName);
                st.setString(3, val.lastName);
                st.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new CacheWriterException("Failed to write [key=" + key + ", val=" + val + ']', e);
        }

    }

    @Override
    public void delete(Object key){
        try (Connection conn = connection()) {
            try (PreparedStatement st = conn.prepareStatement("delete from PERSONS where id=?")) {
                st.setLong(1, (Long)key);
                st.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new CacheWriterException("Failed to delete: " + key, e);
        }
    }

    @Override
    public void loadCache(IgniteBiInClosure<Long, Person> clo, Object... args) {
        if (args == null || args.length == 0 || args[0] == null)
            throw new CacheLoaderException("Expected entry count parameter is not provided.");
        final int entryCnt = (Integer)args[0];
        try (Connection conn = connection()) {
            try (PreparedStatement st = conn.prepareStatement("select * from PERSONS")) {
                try (ResultSet rs = st.executeQuery()) {
                    int cnt = 0;
                    while (cnt < entryCnt && rs.next()) {
                        Person person = new Person(rs.getLong(1), rs.getString(2), rs.getString(3));
                        clo.apply(person.id, person);
                        cnt++;
                    }
                }
            }
        }
        catch (SQLException e) {
            throw new CacheLoaderException("Failed to load values from cache store.", e);
        }
    }

    // Opens JDBC connection and attaches it to the ongoing
    // session if within a transaction.
    private Connection connection() throws SQLException  {
        if (session.isWithinTransaction()) {
            Connection conn = session.attachment();
            if (conn == null) {
                conn = openConnection(false);
                // Store connection in the session, so it can be accessed
                // for other operations within the same transaction.
                session.attach(conn);
            }
            return conn;
        }
        // Transaction can be null in case of simple load or put operation.
        else
            return openConnection(true);
    }
    // Opens JDBC connection.
    private Connection openConnection(boolean autocommit) throws SQLException {
        // Open connection to your RDBMS systems (Oracle, MySQL, Postgres, DB2, Microsoft SQL, etc.)
        // In this example we use H2 Database for simplification.
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:example;DB_CLOSE_DELAY=-1");
        conn.setAutoCommit(autocommit);
        return conn;
    }
}
