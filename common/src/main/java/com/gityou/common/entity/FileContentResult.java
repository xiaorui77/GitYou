package com.gityou.common.entity;

public class FileContentResult {
    private String name;
    private String content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = new String(content);
    }
}
