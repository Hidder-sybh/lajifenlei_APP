package com.example.rubbishcatelog.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class CommonData {

    public CommonData() {
        //题目初始化*10
        Random ran = new Random();
        Set<Integer> set = new HashSet<Integer>();
        while(set.size()==10?false:true){
            int num = ran.nextInt(1153)+1;
            set.add(num);
        }
        Iterator<Integer> it = set.iterator();
        while(it.hasNext()){
            Map<String, String> map = new HashMap<String,String>();
            try {
                rubbish rub = (rubbish) CommonData.rubbish.get(it.next());

            String qus=rub.getName()+"是什么垃圾？";
            Double ClassId=Double.valueOf(rub.getClassID());
            String ans="";
            if(ClassId==1.0)
                ans="可回收物";
            if(ClassId==2.0)
                ans="干垃圾";
            if(ClassId==3.0)
                ans="有害垃圾";
            if(ClassId==4.0)
                ans="可回收物";
            map.put("问题",qus);
            map.put("答案",ans);
            mapList.add(map);
            }catch (Exception e)
            {

            }
        }


    }
    public static int points=0;
    public static int  work=1;
    public static ArrayList arrayList=new ArrayList();
    public   static List<Wrongtask> wrongList=new ArrayList<>();//错题集
    private  static List<Map<String,String>> mapList=new ArrayList<>();//题目集
    public static List<Map<String, String>> getMapList() {
        return mapList;
    }
    public static List rubbish=new ArrayList();//垃圾类
    public static List<article> articles=new ArrayList<>();//文章集
    public static String sentense;//每日一句
}
