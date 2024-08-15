package com.wu1015.sbfilemanager.filemanager.utils;

public class MyFile {
    private String name;
    private String path;
    private long large;
    private String date;
    private boolean isFolder;

    public MyFile() {
    }

    public MyFile(String name, String path, long large, String date, boolean isFolder) {
        this.name = name;
        this.path = path;
        this.large = large;
        this.date = date;
        this.isFolder = isFolder;
    }

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

    public long getLarge() {
        return large;
    }
//    public String getLarge() {
//        return String q =new String(String.valueOf(large));
//    }

    public void setLarge(long large) {
        this.large = large;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public void setFolder(boolean folder) {
        isFolder = folder;
    }
}
