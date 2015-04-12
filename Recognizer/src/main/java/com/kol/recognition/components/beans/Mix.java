package com.kol.recognition.components.beans;

public final class Mix implements Comparable<Mix> {
    private Integer index;
    private Double value;

    public Mix(Integer i, Double v) {
        index = i;
        value = v;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public int compareTo(Mix o) {
        return o.getValue().compareTo(value);
    }
}