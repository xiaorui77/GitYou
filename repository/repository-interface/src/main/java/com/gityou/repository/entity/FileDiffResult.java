package com.gityou.repository.entity;

/*
 * 文件差异
 *
 * 增加/删除函数统计改为前端进行
 *
 * */
public class FileDiffResult {
    private String name;
    private String oldPath;
    private String path;
    private String type;

    private String statistics;  // 统计信息
    private String diff;

    // 构造

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getOldPath() {
        return oldPath;
    }

    public void setOldPath(String oldPath) {
        this.oldPath = oldPath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatistics() {
        return statistics;
    }

    public void setStatistics(String statistics) {
        this.statistics = statistics;
    }

    public String getDiff() {
        return diff;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }
}
