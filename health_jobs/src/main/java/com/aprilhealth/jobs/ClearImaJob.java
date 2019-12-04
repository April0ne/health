package com.aprilhealth.jobs;
/*
 *@author April0ne
 *@date 2019/11/19 11:06
 *定时清理图片任务
 */

import com.aprilhealth.constant.RedisConstant;
import com.aprilhealth.utils.QiNiuStoreUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import java.util.Set;

@Component
public class ClearImaJob {

    @Autowired
    private JedisPool jedisPool;

    //清理图片
    public void clearImg(){
        Jedis jedis = jedisPool.getResource();
        //sdiff方法参数位置 : 被比较的key,是第一个;参考key是第二个;翻译就是,从RedisConstant.SETMEAL_PIC_RESOURCES中找edisConstant.SETMEAL_PIC_DB_RESOURCES中没有的值.如果返了就找不到了
        Set<String> sdiff = jedis.sdiff(RedisConstant.SETMEAL_PIC_RESOURCES,RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        //讲义代码
//        Iterator<String> iterator = sdiff.iterator();
//        while(iterator.hasNext()){
//            String imgName = iterator.next();
//            System.out.println("删除图片的名称是："+imgName);
//            //从七牛云上删除该图片
//            QiNiuStoreUtils.deleteFileFromQiniu(imgName);
//            //从redis中删除该图片
//            jedis.srem(RedisConstant.SETMEAL_PIC_RESOURCES,imgName);
//            Set<String> smembers = jedis.smembers(RedisConstant.SETMEAL_PIC_RESOURCES);
//            for (String smember : smembers) {
//                System.out.println("SETMEAL_PIC_RESOURCES：" + smember);
//            }
//        }


        //自己
        for (String imgName : sdiff) {
            System.out.println("删除图片的名称是："+imgName);
                //从七牛云上删除该图片
                QiNiuStoreUtils.deleteFileFromQiniu(imgName);
                //从redis中删除该图片
                jedis.srem(RedisConstant.SETMEAL_PIC_RESOURCES,imgName);
                Set<String> smembers = jedis.smembers(RedisConstant.SETMEAL_PIC_RESOURCES);
                for (String smember : smembers) {
                System.out.println("SETMEAL_PIC_RESOURCES：" + smember);
            }

        }
    }
}
