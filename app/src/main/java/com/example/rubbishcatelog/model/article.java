package com.example.rubbishcatelog.model;
//文章模型
public class article {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    String title;
    String content;
    String url;
//标题内容url
    public String getImgname() {
        return Imgname;
    }

    public void setImgname(String imgname) {
        Imgname = imgname;
    }
//图
    String Imgname;
    public  boolean isWork() {
        return work;
    }

    public void setWork(boolean work) {
        this.work = work;
    }

    boolean work=false;
//是否被加载过
}
