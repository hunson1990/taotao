package com.taotao.manager.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.activemq.TopicSender;
import com.taotao.common.pojo.*;
import com.taotao.common.utils.IDUtils;
import com.taotao.manager.service.ItemService;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper tbItemMapper;

    @Autowired
    private TbItemDescMapper tbItemDescMapper;

    @Autowired
    private TopicSender topicSender;

    @Override
    public TaotaoResult createTbItem(TbItem item, String desc) {
        //item补全
        //生成商品ID
        final Long itemId = IDUtils.genItemId();
        item.setId(itemId);
        //商品状态： 1正常 2下架 3删除
        item.setStatus((byte)1);
        item.setCreated(new Date());
        item.setUpdated(item.getCreated());
        //构造一个itemDesc
        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setItemId(itemId);
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(itemDesc.getCreated());
        //插入到数据库
            //item的基本信息插入
        tbItemMapper.insert(item);
            //item的描述信息插入
        tbItemDescMapper.insert(itemDesc);
        //添加发送消息的业务逻辑(消息队列，用于更新索引)
        //ApplicationContext context = ClassPathXmlApplicationContext("");
        topicSender.sendMessage(itemId + "");

        return TaotaoResult.ok();
    }

    @Override
    public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
        //1.设置分页的信息，使用pageHelp
        if(page == null)page = 1;
        if(rows == null)rows = 30;
        PageHelper.startPage(page, rows);

        //2.注入mapper

        //3.创建一个example 对象
        TbItemExample tbItemExample = new TbItemExample();

        //4.根据mapper调用查询数据的方法
        List<TbItem> tbItemList = tbItemMapper.selectByExample(tbItemExample);

        //5 获取分页信息
        PageInfo<TbItem> pageInfo = new PageInfo<>(tbItemList);

        //6 封装到EasyUIDataGridResult
        EasyUIDataGridResult easyUIDataGridResult = new EasyUIDataGridResult();
        easyUIDataGridResult.setTotal((int)pageInfo.getTotal());
        easyUIDataGridResult.setRows(pageInfo.getList());

        //7 返回
        return easyUIDataGridResult;
    }

    @Override
    public TbItem getItemById(Long itemId) {
        TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
        return tbItem;
    }

    @Override
    public TbItemDesc getItemDescById(Long itemId) {
        TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
        return tbItemDesc;
    }
}
