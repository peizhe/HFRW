package com.kol.recognition.beans.entities;

import com.kol.recognition.interfaces.ByteData;

import javax.persistence.*;
import java.util.Base64;

@Entity
@Table(name = "recognition_data")
public class DBImage extends HistoryObject implements ByteData {

    private RecognitionDataClass clazz;
    private Integer size;
    private String format;
    private Integer width;
    private Integer height;
    private String content;
    private DBImage parent;

    @Column(name = "image_size")
    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Column(name = "image_format")
    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Column(name = "image_width")
    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    @Column(name = "image_height")
    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    @Column(name = "image_content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @ManyToOne
    @JoinColumn(name = "class_code")
    public RecognitionDataClass getClazz() {
        return clazz;
    }

    public void setClazz(RecognitionDataClass clazz) {
        this.clazz = clazz;
    }

    @ManyToOne
    @JoinColumn(name = "parent_image_id")
    public DBImage getParent() {
        return parent;
    }

    public void setParent(DBImage parent) {
        this.parent = parent;
    }

    @Override
    @Transient
    public byte[] getByteContent() {
        return Base64.getDecoder().decode(content.getBytes());
    }

    @Override
    public void setContentFromBytes(final byte[] bytes) {
        content = new String(Base64.getEncoder().encode(bytes));
    }
}
