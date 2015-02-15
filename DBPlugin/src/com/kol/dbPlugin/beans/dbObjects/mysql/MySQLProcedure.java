package com.kol.dbPlugin.beans.dbObjects.mysql;

import com.google.common.collect.Lists;
import com.kol.dbPlugin.Util;
import com.kol.dbPlugin.beans.dbObjects.Procedure;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

public class MySQLProcedure implements Procedure {

    private String db;
    private String name;
    private String definer;
    private Date created;
    private Date modified;
    private String comment;
    private String body;
    private String inParams;
    private String returns;

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefiner() {
        return definer;
    }

    public void setDefiner(String definer) {
        this.definer = definer;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getInParams() {
        return inParams;
    }

    public void setInParams(String inParams) {
        this.inParams = inParams;
    }

    public String getReturns() {
        return returns;
    }

    public void setReturns(String returns) {
        this.returns = returns;
    }

    @NotNull
    @Override
    public String makeSQL() {
        final Collection<String> params = Lists.newArrayList();
        if(Util.Str.isNonEmpty(inParams)) {
            params.add(inParams);
        }
        if(Util.Str.isNonEmpty(returns)) {
            params.add(returns);
        }
        final Collection<String> lines = new ArrayList<>();
        if(Util.Str.isNonEmpty(comment)) {
            lines.add("/*" + comment + "*/");
        }
        lines.add("DROP procedure IF EXISTS `" + name + "`;");
        lines.add("CREATE PROCEDURE `" + name + "` " + params.stream().collect(Collectors.joining(", ", "(", ")")));
        lines.add(body + ";");
        return lines.stream().collect(Collectors.joining("\n"));
    }
}
