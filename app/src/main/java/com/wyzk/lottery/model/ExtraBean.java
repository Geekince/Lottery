package com.wyzk.lottery.model;

import java.io.Serializable;

public class ExtraBean<T> implements Serializable {
    private T data;
    private int id;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ExtraBean{" +
                "data=" + data +
                ", id=" + id +
                '}';
    }
}
