package com.kol.recognition.beans;

import org.springframework.jdbc.core.RowMapper;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Map;

@Entity(name = "users")
public class User extends HistoryObject<User, String, String> {

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "username", unique = true)
    private String userName;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "password")
    private String password;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public RowMapper<User> getRowMapper() {
        return null;
    }

    @Override
    public Map<String, Object> toSimpleJdbcInsertParams() {
        return null;
    }

    @Override
    public String getTableName() {
        return "user";
    }

    @Override
    public String getIdentityFieldName() {
        return "userName";
    }

    @Override
    public void setId(String id) {
        this.userName = id;
    }

    @Override
    public String getId() {
        return userName;
    }
}
