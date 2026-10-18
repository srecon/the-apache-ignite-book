package com.blu.imdg.jdbc;

import com.blu.imdg.jdbc.model.Post;
import org.apache.ignite.IgniteException;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.lifecycle.LifecycleAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class PostgresDBStore extends CacheStoreAdapter<String, Post> implements LifecycleAware {

    private static final Logger log = LoggerFactory.getLogger(PostgresDBStore.class);

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Post load(String key) throws CacheLoaderException {
        var inputParam = Map.of("id", key);
        return jdbcTemplate.queryForObject("SELECT * FROM POSTS WHERE id=?", inputParam, this::mapRow);
    }

    private Post mapRow(ResultSet rs, int i) throws SQLException {
        return new Post(
            rs.getString(1),
            rs.getString(2),
            rs.getString(3),
            rs.getDate(4).toLocalDate(),
            rs.getString(5)
        );
    }

    @Override
    public void write(Cache.Entry<? extends String, ? extends Post> entry) throws CacheWriterException {
        var post = entry.getValue();
        var parameterMap = Map.of(
            "ID", post.id(),
            "TITLE", post.title(),
            "DESCRIPTION", post.description(),
            "CREATIONDATE", post.creationDate(),
            "AUTHOR", post.author()
        );
        jdbcTemplate.update("""
            INSERT INTO POSTS(ID, TITLE, DESCRIPTION, CREATIONDATE, AUTHOR)
            VALUES (:ID, :TITLE, :DESCRIPTION, :CREATIONDATE, :AUTHOR)""", parameterMap);
    }

    @Override
    public void delete(Object key) throws CacheWriterException {
        jdbcTemplate.update("DELETE FROM POSTS WHERE ID=?", Map.of("ID", (String) key));
    }

    @Override
    public void start() throws IgniteException {
        var context = new ClassPathXmlApplicationContext("postgres-context.xml");
        jdbcTemplate = context.getBean(NamedParameterJdbcTemplate.class);
        log.info("PostgresDBStore started with NamedParameterJdbcTemplate: {}", jdbcTemplate);
    }

    @Override
    public void stop() throws IgniteException {
        // No-op
    }
}
