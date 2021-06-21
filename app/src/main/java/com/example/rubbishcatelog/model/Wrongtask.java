package com.example.rubbishcatelog.model;
//错题模型
public class Wrongtask {
    String qus;
    String ans;

    public String getQus() {
        return qus;
    }

    public Wrongtask(String qus, String ans) {
        this.qus = qus;
        this.ans = ans;
    }
    public Wrongtask(){}

    public void setQus(String qus) {
        this.qus = qus;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }
}
