package com.kol.recognition.beans.entities;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
public abstract class HistoryObject {

    protected Integer id;
    protected User editBy;
    protected Date editDate;
    protected User createBy;
    protected Date createDate;

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "edit_date")
    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    @Column(name = "create_date")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @ManyToOne
    @JoinColumn(name = "edit_by")
    public User getEditBy() {
        return editBy;
    }

    public void setEditBy(User editBy) {
        this.editBy = editBy;
    }

    @ManyToOne
    @JoinColumn(name = "create_by")
    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
    }
}