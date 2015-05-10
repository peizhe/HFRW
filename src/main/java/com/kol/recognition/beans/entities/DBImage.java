package com.kol.recognition.beans.entities;

import com.kol.recognition.general.Image;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.Table;

@Table(name = "recognition_data")
public class DBImage extends HistoryObject {

    @Column(name = "class_code")
    private String clazz;
    @Column(name = "image_size")
    private Integer size;
    @Column(name = "image_format")
    private String format;
    @Column(name = "image_width")
    private Integer width;
    @Column(name = "image_height")
    private Integer height;
    @Lob
    @Column(name = "image_content")
    private byte[] content;
    @Column(name = "parent_image_id")
    private String parent;

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

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

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public Image toImage() {
        final Image im = new Image();
        im.setClazz(clazz);
        im.setContent(getContent());
        im.setFormat(format);
        im.setHeight(height);
        im.setSize(size);
        im.setWidth(width);
        im.setId(id);
        return im;
    }
}
