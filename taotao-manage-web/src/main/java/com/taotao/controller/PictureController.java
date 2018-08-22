package com.taotao.controller;

import com.taotao.common.utils.FtpUtil;
import com.taotao.common.utils.IDUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 上传图片处理
 *
 */



// Fail to decode request due to: RpcInvocation
// 传输的参数中是否包含了不能序列化的属性，例如ImmutableList、Joda DateTime等。
// 如果存在第二行事情，会出现第一行错误


@Controller
public class PictureController {

    @Value("${FTP_ADDRESS}")
    private String FTP_ADDRESS;
    @Value("${FTP_PORT}")
    private int FTP_PORT;
    @Value("${FTP_USERNAME}")
    private String FTP_USERNAME;
    @Value("${FTP_PASSWORD}")
    private String FTP_PASSWORD;
    @Value("${FTP_BASEPATH}")
    private String FTP_BASEPATH;
    @Value("${IMAGE_BASE_URL}")
    private String IMAGE_BASE_URL;

    @RequestMapping(value = "/pic/upload")
    @ResponseBody
    public Map pictureUpload(MultipartFile uploadFile) {

        Map resultMap = new HashMap();
        // 组织上传文件需要的数据
        String oldName = uploadFile.getOriginalFilename();
        System.out.println(oldName);
        String newName = IDUtils.getImageName();
        newName = newName + oldName.substring(oldName.lastIndexOf("."));
        // org.joda.time.DateTime;这个包只能放在使用它的模块中，不能用maven继承的方式，否则提示没有这个包
        // 不知道是为什么？
        //filePath图片存放的日期形式文件夹
        String filePath =  new DateTime().toString("yyyMMdd");
        InputStream inputStream = null;
        try {
            inputStream = uploadFile.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //开始上传文件
        FtpUtil ftpUtil = new FtpUtil();
        boolean result = ftpUtil.uploadFile(FTP_ADDRESS,FTP_PORT, FTP_USERNAME, FTP_PASSWORD, filePath, newName, inputStream);
        if(!result){
            resultMap.put("error", 1);
            resultMap.put("message", "文件上传失败");
            return resultMap;
        }
        resultMap.put("error", 0);
        resultMap.put("url", IMAGE_BASE_URL + "/" +filePath + "/" +newName);
        return resultMap;
    }




}
