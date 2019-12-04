package com.aprilhealth.utils;
/*
 *@author April0ne
 *@date 2019/11/21 16:53
 *序列化工具
 */


import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

public class SerializationUtil {
    /*
     * 序列化
     * */
    public static byte[] serialization(Object object){
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(baos != null){
                    baos.close();
                }
                if (oos != null) {
                    oos.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return null;
    }

}
