package com.writer.dillon;


public class Item {

    private String name;
    private String path;

    public Item(String n, String p) {
        name = n;
        path = p;
    }
    public String getName() {
        return name;
    }
    public String getPath() {
        return path;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setPath(String path){
        this.path = path;
    }
}