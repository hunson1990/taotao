package com.taotao.manager.service.impl;

import com.taotao.common.pojo.EUTreeNode;
import com.taotao.common.pojo.TbItemCat;
import com.taotao.common.pojo.TbItemCatExample;
import com.taotao.manager.service.ItemCatService;
import com.taotao.mapper.TbItemCatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Override
    public List<EUTreeNode> getEUTreeNodes(Long id) {
        if(id == null)id = new Long(0);
        System.out.println("查询的商品分类的id ： " + id);
        TbItemCatExample itemCatExample = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = itemCatExample.createCriteria();
        criteria.andParentIdEqualTo(id);
        List<TbItemCat> itemCats = itemCatMapper.selectByExample(itemCatExample);
        System.out.println(itemCats);
        //把列表转换成treeNodeList
        List<EUTreeNode> resultList = new ArrayList<>();
        for(TbItemCat itemCat : itemCats){
            EUTreeNode treeNode = new EUTreeNode();
            treeNode.setId(itemCat.getId());
            treeNode.setText(itemCat.getName());

            treeNode.setState(itemCat.getIsParent()?"closed":"open");
            resultList.add(treeNode);
        }
        return resultList;
    }

}
