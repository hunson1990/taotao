package com.taotao.manager.service.impl;

import com.taotao.common.utils.IDUtils;
import com.taotao.manager.service.PictureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

//import org.joda.time.DateTime;


@Service
public class PictureServiceImpl implements PictureService {

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


    @Override
    public Map uploadPicture(String oldName, byte[] bytes){
        //由于文件流用dubbo不好传输，所以暂时不在service层做图片上传服务
        Map resultMap = new HashMap();

        System.out.println(bytes);
        //生成一个新的文件名
        //UUID.randomUUID();
        String newName = IDUtils.getImageName();
        //String newDir =  "/" + new DateTime().toString("/yyy/MM/dd");
        String filePath =  "/20180623";
        newName = newName + oldName.substring(oldName.lastIndexOf("."));
//        InputStream inputStream = new ByteArrayInputStream(bytes);
        //图片上传


//        boolean result = FtpUtil2.uploadFile(FTP_ADDRESS, FTP_PORT, FTP_USERNAME, FTP_PASSWORD,
//                FTP_BASEPATH, newName, inputStream);
//        if(!result){
//            resultMap.put("error", 1);
//            resultMap.put("message", "文件上传失败");
//            return resultMap;
//        }
//        resultMap.put("error", 0);
//        resultMap.put("url", IMAGE_BASE_URL + filePath + "/" +newName);
        return resultMap;

    }


}
