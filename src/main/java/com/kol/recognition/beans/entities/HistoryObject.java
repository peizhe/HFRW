package com.kol.recognition.beans.entities;

import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public abstract class HistoryObject {

    @Id
    @Column(name = "id", unique = true)
    protected String id;

    @LastModifiedBy
    @Column(name = "edit_by")
    protected User editBy;

    @LastModifiedDate
    @Column(name = "edit_date")
    protected DateTime editDate;

    @CreatedBy
    @Column(name = "create_by")
    protected User createBy;

    @CreatedDate
    @Column(name = "create_date")
    protected DateTime createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getEditBy() {
        return editBy;
    }

    public void setEditBy(User editBy) {
        this.editBy = editBy;
    }

    public DateTime getEditDate() {
        return editDate;
    }

    public void setEditDate(DateTime editDate) {
        this.editDate = editDate;
    }

    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
    }

    public DateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(DateTime createDate) {
        this.createDate = createDate;
    }
}