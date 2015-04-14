package com.kol.recognition.beans.entities;

import com.kol.recognition.components.ImageManager;
import com.kol.recognition.general.Image;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.beans.Transient;

@Entity
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
    @Column(name = "image_content")
    private String content;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    @Transient
    public byte[] getByteContent() {
        return ImageManager.stringToByte(content);
    }

    public void setContentFromBytes(final byte[] bytes) {
        content = ImageManager.byteToString(bytes);
    }

    public Image toImage() {
        final Image im = new Image();
        im.setClazz(clazz);
        im.setContent(getByteContent());
        im.setFormat(format);
        im.setHeight(height);
        im.setSize(size);
        im.setWidth(width);
        im.setId(id);
        return im;
    }
}
