package com.taotao.content.service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.pojo.TbContentCategory;
import com.taotao.common.pojo.TbContentCategoryExample;
import com.taotao.mapper.TbContentCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 内容分类
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private TbContentCategoryMapper categoryMapper;

    @Override
    public List<EasyUITreeNode> getContentCategoryList(long parentId) {
        //1 注入mapper
        //2 创建一个example
        TbContentCategoryExample categoryExample = new TbContentCategoryExample();
        //3 设置条件
        TbContentCategoryExample.Criteria criteria = categoryExample.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        //4 执行查询
        List<TbContentCategory> tbContentCategories = categoryMapper.selectByExample(categoryExample);
        //5 转成EasyUITreeNode 列表
        List<EasyUITreeNode> nodes = new ArrayList<>();
        for(TbContentCategory contentCategory : tbContentCategories){
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(contentCategory.getId());
            node.setState(contentCategory.getIsParent()?"closed":"open");
            node.setText(contentCategory.getName()); //分类名称
            nodes.add(node);
        }
        //6 返回
        return nodes;
    }

    @Override
    public TaotaoResult createContentCategory(Long parentId, String name) {
        //1.构建插入对象
        TbContentCategory contentCategory = new TbContentCategory();
        contentCategory.setCreated(new Date());
        contentCategory.setIsParent(false); // 都是新增的节点，都是叶子节点
        contentCategory.setName(name);
        contentCategory.setParentId(parentId);
        contentCategory.setSortOrder(1);
        contentCategory.setStatus(1);
        contentCategory.setUpdated(contentCategory.getCreated());
        //2.插入数据
        categoryMapper.insert(contentCategory);
        //3.返回taotaoresult 包含内容分类的id  需要返回主键


        //判断如果要添加的节点的父节点，本身是叶子节点， 需要更新其为父节点
        TbContentCategory parentCategory = categoryMapper.selectByPrimaryKey(parentId);
        if(parentCategory.getIsParent()==false){
            parentCategory.setIsParent(true);
            categoryMapper.updateByPrimaryKeySelective(parentCategory);
        }
        return TaotaoResult.ok(contentCategory);
    }

    @Override
    public TaotaoResult updateContentCategory(Long id, String name) {
        TbContentCategory contentCategory = categoryMapper.selectByPrimaryKey(id);
        contentCategory.setName(name);
        categoryMapper.updateByPrimaryKeySelective(contentCategory);
        //返回一个 TaotaoResult
        //个人认为此处还需要加一个修改是否成功的状态返回；
        //System.out.println(i);
        return TaotaoResult.ok(contentCategory);
    }

    @Override
    public TaotaoResult deldeteContentCategory(Long id) {
        TbContentCategory contentCategory = categoryMapper.selectByPrimaryKey(id);
        if(contentCategory.getIsParent() == false){
            //不是父节点：
            //1. 删除选中的节点
            categoryMapper.deleteByPrimaryKey(id);
            //2. 查找此节点的父节点
            TbContentCategory parentCategory = categoryMapper.selectByPrimaryKey(contentCategory.getParentId());
            //3. 如果父节点没有子节点，把父节点的isParent修改为false

            TbContentCategoryExample categoryExample = new TbContentCategoryExample();
            TbContentCategoryExample.Criteria criteria = categoryExample.createCriteria();
            criteria.andParentIdEqualTo(parentCategory.getId());
            List<TbContentCategory> contentCategories = categoryMapper.selectByExample(categoryExample);
            System.out.println(contentCategories);
            if(contentCategories.size() == 0){
                //如果父节点没有子节点了
                parentCategory.setIsParent(false);
                categoryMapper.updateByPrimaryKeySelective(parentCategory);
            }
            return TaotaoResult.ok();
        }else{
            return TaotaoResult.build(500,"父节点不能直接删除！");
        }

    }




}
