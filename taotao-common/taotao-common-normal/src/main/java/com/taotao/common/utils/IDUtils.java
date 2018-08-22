package com.taotao.common.utils;

import java.util.Random;

public class IDUtils{

    /**
     * 图片名字
     * @return
     */
    public static String getImageName(){
        //取当前之间，包含毫秒
        long mills = System.currentTimeMillis();
        //加上三位随机数
        Random random = new Random();
        int end3 = random.nextInt(999);
        //如果不足三位，前面加0
        String str = mills + String.format("%03d",end3);
        return str;
    }


    /**
     * 商品id生成
     */
    public static long genItemId() {
        //取当前时间的长整形值包含毫秒
        long millis = System.currentTimeMillis();
        //long millis = System.nanoTime();
        //加上两位随机数
        Random random = new Random();
        int end2 = random.nextInt(99);
        //如果不足两位前面补0
        String str = millis + String.format("%02d", end2);
        long id = new Long(str);
        return id;
    }

    /**
     * 订单编号id生成
     * @return
     */
    public static String getOrderId(){
        int r1=(int)(Math.random()*(10));//产生2个0-9的随机数
        int r2=(int)(Math.random()*(10));
        long millis = System.currentTimeMillis();//一个13位的时间戳
        return String.valueOf(r1)+String.valueOf(r2)+String.valueOf(millis);// 订单ID
    }



}
