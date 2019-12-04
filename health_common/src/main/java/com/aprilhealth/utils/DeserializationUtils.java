package com.aprilhealth.utils;
/*
 *@author April0ne
 *@date 2019/11/21 16:59
 */


import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class DeserializationUtils {
    /*
     * 反序列化
     * */
    public static Object deserialize(byte[] bytes){
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try{
            bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            return ois.readObject();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (ois != null) {
                    ois.close();
                }

                if (bais != null) {
                    bais.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return null;
    }

}
