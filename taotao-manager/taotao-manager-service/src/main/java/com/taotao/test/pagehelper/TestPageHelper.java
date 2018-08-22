package com.taotao.test.pagehelper;



import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbItemMapper;
import com.taotao.common.pojo.TbItem;
import com.taotao.common.pojo.TbItemExample;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class TestPageHelper {



    @Test
    public void testHelper(){
        //1.设置分页信息
        //2.初始化spring容器
        //3.获取mapper的代理对象
        //4.调用mapper的方法查询数据
        //5.遍历结果集
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
        TbItemMapper itemMapper = context.getBean(TbItemMapper.class);

        TbItemExample tbItemExample = new TbItemExample();

        PageHelper.startPage(1,3);//3行，对紧跟着的第1个查询有效
        List<TbItem> tbItems_list = itemMapper.selectByExample(tbItemExample);//相当于：select * from tbItemExample;
        List<TbItem> tbItems_list2 = itemMapper.selectByExample(tbItemExample);//相当于：select * from tbItemExample;

        //取分页信息
        PageInfo<TbItem> info = new PageInfo<>(tbItems_list);

        System.out.println("第一个分页的长度：" + tbItems_list.size());
        System.out.println("第二个分页的长度：" + tbItems_list2.size());

        System.out.println("查询的总记录数据：" + info.getTotal());

        for(TbItem tbItem :tbItems_list){
            System.out.println(tbItem.getId() + tbItem.getTitle());
        }


    }
}
