package com.kol.dbTool;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DBConnector {

    private DriverManagerDataSource dataSource;

    private DBConnector(){
        dataSource = new DriverManagerDataSource();
    }

    public static DBConnector start() {
        return new DBConnector();
    }

    /**
     * @param driver name of connection driver (com.mysql.Driver)
     */
    public DBConnector driver(final String driver) {
        dataSource.setDriverClassName(driver);
        return this;
    }

    /**
     * @param url - url to database (localhost:3306/database)
     */
    public DBConnector url(final String url) {
        dataSource.setUrl(url);
        return this;
    }

    /**
     * @param username - username for database connection (root)
     */
    public DBConnector user(final String username) {
        dataSource.setUsername(username);
        return this;
    }

    /**
     * @param password - password of your user (pass)
     */
    public DBConnector password(final String password) {
        dataSource.setPassword(password);
        return this;
    }

    /**
     * @return new JdbcOperations (JdbcTemplate) object
     */
    public JdbcTemplate jdbc() {
        return new JdbcTemplate(dataSource);
    }

    /**
     * required properties:
     *  driver - name of connection driver (com.mysql.Driver)
     *  url - url to database (localhost:3306/database)
     *  user - username for database connection (root)
     *  password - password of your user (pass)
     *
     * @param properties - all properties for creating connection to database
     * @return new {@code org.springframework.jdbc.core.JdbcTemplate}
     */
    public JdbcTemplate jdbcByProperties(final DBProperties properties) {
        return driver(properties.getDriver())
                .url(properties.getUrl())
                .user(properties.getUser())
                .password(properties.getPassword())
                .jdbc();
    }

    /**
     * @param properties - database properties
     * @return new JdbcOperations object
     */
    public static JdbcOperations getJdbc(final DBProperties properties) {
        return start().jdbcByProperties(properties);
    }
}
