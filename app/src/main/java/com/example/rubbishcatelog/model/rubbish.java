package com.example.rubbishcatelog.model;
//垃圾模型
public class rubbish {
    public int getClassID() {
        return classID;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public rubbish(int classID, String name) {
        this.classID = classID;
        this.name = name;
    }

    int classID;
    String name;
}
