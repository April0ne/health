package com.aprilhealth.controller;
/*
 *@author April0ne
 *@date 2019/11/11 9:17
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.aprilhealth.constant.MessageConstant;
import com.aprilhealth.constant.RedisConstant;
import com.aprilhealth.entity.PageResult;
import com.aprilhealth.entity.QueryPageBean;
import com.aprilhealth.entity.Result;
import com.aprilhealth.pojo.CheckGroup;
import com.aprilhealth.pojo.Setmeal;
import com.aprilhealth.service.SetmealService;
import com.aprilhealth.utils.QiNiuStoreUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;//保证只有一个池子,保证线程安全

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @PostMapping("/findPageFromSetmeal")
    @PreAuthorize("hasAnyAuthority('SETMEAL_QUERY')")
    public Result findPageFromSetmeal(@RequestBody QueryPageBean queryPageBean) throws RuntimeException{
        Result result = null;
//        int i = 1/0;
        PageResult pageFromSetmeal = setmealService.findPageFromSetmeal(queryPageBean);
        result = new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,pageFromSetmeal);
        return result;
    }

    /**
     * 查询检查组
     * @return
     */
    @GetMapping("/findAllCheckGroup")
    public Result findAllCheckGroup()throws RuntimeException{
        Result result = null;
        List<CheckGroup> allCheckGroup = setmealService.findAllCheckGroup();
        result = new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,allCheckGroup);
        return result;
    }

    /**
     * 新增套餐
     * @param setmeal
     * @param checkgroupIds
     * @return
     */
    @PostMapping("/addSetmeal")
    @PreAuthorize("hasAnyAuthority('SETMEAL_ADD')")
    public Result method(@RequestBody Setmeal setmeal, Integer[] checkgroupIds)throws RuntimeException{
        Result result = null;
        boolean b = setmealService.addSetmeal(setmeal, checkgroupIds);
        if (b) {
            result = new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
//            //数据库新增套餐成功,将图片名称存储至redis==操作数据库应该放在service中操作.
//            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
        }else {
            result = new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
        return result;
    }

    //上传套餐图片
    @RequestMapping("/upload")
    public Result uploadImage(@RequestParam("imgFile") MultipartFile multipartFile)throws RuntimeException{//这个前端怎么传的? imgFile值name属性的值,如果不加@RequestParam,可直接将入参名,写作imgFile
        Result result = null;
        try {
            //获取文件名
            String originalFilename = multipartFile.getOriginalFilename();
            //获取文件后缀
            String filenameExtension = StringUtils.getFilenameExtension(originalFilename);
            //创建新文件名
            String fileNewName = UUID.randomUUID().toString();
            //上传文件
            QiNiuStoreUtils.upload2Qiniu(multipartFile.getBytes(),fileNewName);//为什么是转换成字节数组,上传工具要的不是路径吗?
            //上传结果
            result = new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS,fileNewName);

            //图片上传成功,将图片名称存储至redis ,这里有个问题呀,如果这里出错了,也会捕获,最终返回给前端的信息是保存失败,但是因为上面代码执行了,且保存成功了,redis存储的操作放在这边会不会有问题呀?
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileNewName);
        } catch (IOException e) {
            e.printStackTrace();
            result = new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }

        return result;
    }


}
