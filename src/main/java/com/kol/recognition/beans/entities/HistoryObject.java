package com.kol.recognition.beans.entities;

import javax.persistence.Column;
import java.util.Date;

public abstract class HistoryObject {

    @Column(name = "id", unique = true)
    protected String id;
    @Column(name = "edit_by")
    protected String editBy;
    @Column(name = "edit_date")
    protected Date editDate;
    @Column(name = "create_by")
    protected String createBy;
    @Column(name = "create_date")
    protected Date createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEditBy() {
        return editBy;
    }

    public void setEditBy(String editBy) {
        this.editBy = editBy;
    }

    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}