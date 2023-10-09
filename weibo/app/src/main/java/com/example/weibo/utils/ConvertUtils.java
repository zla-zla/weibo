package com.example.weibo.utils;

import com.google.gson.Gson;

public class ConvertUtils {

    //对象转化json
    public static String objectToJson(Object obj, Class cls) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(obj,cls);
        return jsonStr;
    }

    //json转化对象
    public static Object jsonToobject(String objectJson, Class cls) {
        Gson gson = new Gson();
        Object obj = gson.fromJson(objectJson, cls);
        return obj;
    }

//    /**
//     * 对象list转化为json
//     */
//    public static String objectListToJson(ArrayList<PointKMBean> pointList) {
//        Gson gson = new Gson();
//        //要转化的类型
//        //Type导入的是java.lang.reflect.Type的包
//        //TypeToken导入的是 com.google.gson.reflect.TypeToken的包
//        Type type = new TypeToken<ArrayList<PointKMBean>>() {
//        }.getType();
//        String listStr = gson.toJson(pointList, type);
//        return listStr;
//    }
//
//    /**
//     * json转化对象list
//     */
//    public static ArrayList<PointKMBean> jsonToObjectList(String jsonStr) {
//        Gson gson = new Gson();
//        //要转化的类型
//        //Type导入的是java.lang.reflect.Type的包
//        //TypeToken导入的是 com.google.gson.reflect.TypeToken的包
//        Type type = new TypeToken<ArrayList<PointKMBean>>() {
//        }.getType();
//        ArrayList<PointKMBean> pointList = gson.fromJson(jsonStr, type);
//        return pointList;
//    }
}
