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
public class CacheJdbcPersonStoreNonTx extends CacheStoreAdapter<Long, Person>{

    @CacheStoreSessionResource
    private CacheStoreSession session;

    @Override
    public Person load(Long key) throws CacheLoaderException {
        try(Connection conn = connection();
        PreparedStatement st = conn.prepareStatement("select * from PERSON where id = ?")){
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                return new Person(rs.getLong(1), rs.getString(2), rs.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void write(Cache.Entry<? extends Long, ? extends Person> entry){
        Long key = entry.getKey();
        Person val = entry.getValue();

        try {


            int updated;

            // Try update first. If it does not work, then try insert.
            // Some databases would allow these to be done in one 'upsert' operation.
            try (Connection conn = connection()){
                PreparedStatement st = conn.prepareStatement(
                        "update PERSON set first_name = ?, last_name = ? where id = ?");
                st.setString(1, val.firstName);
                st.setString(2, val.lastName);
                st.setLong(3, val.id);

                updated = st.executeUpdate();
            }

            // If update failed, try to insert.
            if (updated == 0) {
                try(Connection conn = connection();
                    PreparedStatement st = conn.prepareStatement(
                            "insert into PERSON (id, first_name, last_name) values (?, ?, ?)")){
                    st.setLong(1, val.id);
                    st.setString(2, val.firstName);
                    st.setString(3, val.lastName);

                    st.executeUpdate();
                }
            }
        }
        catch (SQLException e) {
            throw new CacheWriterException("Failed to write object [key=" + key + ", val=" + val + ']', e);
        }
    }

    @Override
    public void delete(Object key) throws CacheWriterException {
        Connection conn = session.attachment();

        try (PreparedStatement st = conn.prepareStatement("delete from PERSON where id=?")) {
            st.setLong(1, (Long)key);

            st.executeUpdate();
        }
        catch (SQLException e) {
            throw new CacheWriterException("Failed to delete object [key=" + key + ']', e);
        }
    }

    @Override
    // bulk loading
    public void loadCache(IgniteBiInClosure<Long, Person> closure, Object... args){
        if (args == null || args.length == 0 || args[0] == null)
            throw new CacheLoaderException("Expected entry count parameter is not provided.");

        final int entryCnt = (Integer)args[0];

        Connection conn = session.attachment();

        try (PreparedStatement stmt = conn.prepareStatement("select * from PERSON limit ?")) {
            stmt.setInt(1, entryCnt);

            ResultSet rs = stmt.executeQuery();

            int cnt = 0;

            while (cnt<entryCnt && rs.next()) {
                Person person = new Person(rs.getLong(1), rs.getString(2), rs.getString(3));

                closure.apply(person.id, person);

                cnt++;
            }

            System.out.println(">>> Loaded " + cnt + " values into cache.");
        }
        catch (SQLException e) {
            throw new CacheLoaderException("Failed to load values from cache store.", e);
        }
    }

    private Connection connection() throws SQLException  {
        // Open connection to your RDBMS systems (Oracle, MySQL, Postgres, DB2, Microsoft SQL, etc.)
        // In this example we use H2 Database for simplification.
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:example;DB_CLOSE_DELAY=-1");
        conn.setAutoCommit(true);
        return conn;
    }
}
