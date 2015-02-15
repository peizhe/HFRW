package com.kol.dbPlugin.dao;

import com.kol.dbPlugin.beans.ConnectionData;
import com.kol.dbPlugin.beans.dbObjects.mysql.MySQLChange;
import com.kol.dbPlugin.beans.dbObjects.mysql.MySQLFunction;
import com.kol.dbPlugin.beans.dbObjects.mysql.MySQLProcedure;
import com.kol.dbPlugin.beans.dbObjects.mysql.MySQLView;
import com.kol.dbPlugin.jdbc.DatabaseTemplate;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class MySQLDao extends DatabaseTemplate implements DBPluginDAO<MySQLProcedure, MySQLFunction, MySQLView, MySQLChange> {

    public MySQLDao(@NotNull ConnectionData data) {
        super(data);
    }

    @Override
    public void createPluginTableIfNotExists() {

    }

    @Override
    public Collection<MySQLProcedure> getStoredProcedures(@NotNull final String dbName) {
        final String sql =
                "SELECT db, name, param_list, returns, body, definer, created, modified, comment " +
                "FROM mysql.proc WHERE db = ? AND type = 'PROCEDURE'";
        return query(sql, (rs) -> {
            final MySQLProcedure p = new MySQLProcedure();
            p.setDb(rs.getString("db"));
            p.setName(rs.getString("name"));
            p.setBody(rs.getString("body"));
            p.setDefiner(rs.getString("definer"));
            p.setInParams(rs.getString("param_list"));
            p.setReturns(rs.getString("returns"));
            p.setModified(rs.getTimestamp("modified"));
            p.setComment(rs.getString("comment"));
            p.setCreated(rs.getTimestamp("created"));
            return p;
        }, dbName);
    }

    @Override
    public Collection<MySQLFunction> getFunctions(@NotNull final String dbName) {
        return null;
    }

    @Override
    public Collection<MySQLView> getViews(@NotNull final String dbName) {
        return null;
    }

    @Override
    public Collection<MySQLChange> getTableChanges(@NotNull final String dbName) {
        return null;
    }
}
