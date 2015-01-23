package com.kol.dbTool;

import org.springframework.jdbc.core.JdbcOperations;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
        final DBProperties p = new DBProperties();
        p.setDriver("com.mysql.jdbc.Driver");
        p.setPassword("pass");
        p.setUser("dbUser");
        p.setUrl("jdbc:mysql://localhost:3306/hfr");

        final JdbcOperations jdbc = DBConnector.getJdbc(p);

        final Date date = jdbc.queryForObject("SELECT NOW()", Date.class);
        System.out.println(date);
    }
}
