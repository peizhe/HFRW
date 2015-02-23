package com.kol.recognition.beans;

import com.kol.recognition.interfaces.DBObject;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public abstract class HistoryObject<T, I, IP> implements DBObject<T, I> {

    @Column(name = "edit_by")
    protected IP editBy;
    @Column(name = "edit_date")
    protected Date editDate;
    @Column(name = "create_by")
    protected IP createBy;
    @Column(name = "create_date")
    protected Date createDate;

    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public IP getEditBy() {
        return editBy;
    }

    public void setEditBy(IP editBy) {
        this.editBy = editBy;
    }

    public IP getCreateBy() {
        return createBy;
    }

    public void setCreateBy(IP createBy) {
        this.createBy = createBy;
    }
}
