package com.gityou.common.entity;

import java.util.List;

public class PageResult<T> {
    private long total;
    private int pageTotal;
    private List<T> list;

    public PageResult() {
    }

    public PageResult(long total, int pageTotal, List<T> list) {
        this.total = total;
        this.pageTotal = pageTotal;
        this.list = list;
    }

    public int getTotal() {
        return (int) total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
