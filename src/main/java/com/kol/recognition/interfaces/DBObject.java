package com.kol.recognition.interfaces;

import org.springframework.jdbc.core.RowMapper;

import java.util.Map;

public interface DBObject<T, I> {

    RowMapper<T> getRowMapper();

    Map<String, Object> toSimpleJdbcInsertParams();

    String getTableName();

    String getIdentityFieldName();

    void setId(I id);

    I getId();
}
