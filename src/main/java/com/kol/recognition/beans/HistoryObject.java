package com.kol.recognition.beans;

import com.kol.recognition.interfaces.DBObject;

import java.util.Date;

public abstract class HistoryObject<T, I, IP> implements DBObject<T, I> {

    protected IP editBy;
    protected Date editDate;
    protected IP createBy;
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
