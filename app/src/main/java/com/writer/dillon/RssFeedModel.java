package com.writer.dillon;

public class RssFeedModel {

    public String title;
    public String link;
    public String description;

    public RssFeedModel(String title, String link, String description) {
        this.title = title;
        this.link = link;
        this.description = description;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void formatPost(){
        this.description = description.replace("<p>","").replace("</p>", ". ").replace("&#8217;", "'").replace("[&#8230;]", "[click to read more]");
        int ridofpos = description.indexOf("The post");
        this.description = description.substring(0,ridofpos);
    }
}