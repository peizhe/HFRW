package com.kol.recognition.beans;

import com.kol.recognition.interfaces.DBObject;
import org.springframework.jdbc.core.RowMapper;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class DBImage implements DBObject<DBImage> {

    private Integer id;
    private String clazz;
    private Integer size;
    private String format;
    private Integer width;
    private Integer height;
    private byte[] content;
    private Integer parentId;

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    @Override
    public RowMapper<DBImage> getRowMapper() {
        return (rs, i) -> {
            final DBImage image = new DBImage();
            final String content = rs.getString("image_content");
            image.setContent(Base64.getDecoder().decode(content.getBytes()));
            image.setSize(rs.getInt("image_size"));
            image.setWidth(rs.getInt("image_width"));
            image.setHeight(rs.getInt("image_height"));
            image.setFormat(rs.getString("image_format"));
            image.setId(rs.getInt(getIdentityFieldName()));
            return image;
        };
    }

    @Override
    public Map<String, Object> toSimpleJdbcInsertParams() {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("class_code", getClazz());
        parameters.put("image_format", getFormat());
        parameters.put("image_width", getWidth());
        parameters.put("image_height", getHeight());
        parameters.put("image_size", getSize());
        parameters.put("parent_image_id", getParentId());
        parameters.put("image_content", new String(Base64.getEncoder().encode(getContent())));
        return parameters;
    }

    @Override
    public String getTableName() {
        return "recognition_data";
    }

    @Override
    public String getIdentityFieldName() {
        return "id";
    }

    @Override
    public void setIdentifier(int id) {
        this.id = id;
    }
}
