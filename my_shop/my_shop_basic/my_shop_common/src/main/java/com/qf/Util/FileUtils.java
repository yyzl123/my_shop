package com.qf.Util;

/**
 * @ClassName FileUtils
 * @Description TODO
 * @Author 86139
 * @Data 2020/5/13 20:54
 * @Version 1.0
 **/
public class FileUtils {


    public static String crtRedisKey(String pre,String content){
        return new  StringBuilder().append(pre).append(content).toString();
    }

    public static String getRedisKey(String pram1,String pram2,String pram3,String pram4){
        return new StringBuilder().append(pram1).append(pram2).append(pram3).append(pram4).toString();
    }


    public static  String getExtName(String originalName){

        int index =originalName.lastIndexOf(".");
        return originalName.substring(index+1);
    }

    public static  String getFileExtNameWithPoint(String fileName){

        int index = fileName.lastIndexOf(".");

        return fileName.substring(index);
    }

}
