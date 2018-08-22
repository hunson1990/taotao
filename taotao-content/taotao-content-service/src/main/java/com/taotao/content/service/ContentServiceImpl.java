package com.taotao.content.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.pojo.TbContent;
import com.taotao.common.pojo.TbContentExample;
import com.taotao.common.utils.JsonUtils;
import com.taotao.content.jedis.JedisClient;
import com.taotao.mapper.TbContentMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;
    @Autowired
    private JedisClient client;
    @Value("${CONTENT_KEY}")
    private String CONTENT_KEY;



    /**
     * 插入数据
     * @param tbContent
     * @return
     */
    @Override
    public TaotaoResult insertContent(TbContent tbContent) {
        //1. 补全其他属性
        tbContent.setCreated(new Date());
        tbContent.setUpdated(tbContent.getCreated());
        //2. 插入数据
        contentMapper.insertSelective(tbContent);

        //处理缓存--- 当添加内容的时候， 需要清空此内容所属分类下面的内容
        try {
            client.hdel(CONTENT_KEY,tbContent.getCategoryId().toString());
            System.out.println("当插入内容时，清空缓存.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return TaotaoResult.ok();
    }

    /**
     * 获取数据内容
     * @param categoryId
     * @param page
     * @param rows
     * @return
     */
    @Override
    public EasyUIDataGridResult getContent(Long categoryId, Integer page, Integer rows) {
        //1.设置分页的信息，使用pageHelp
        if(page == null)page = 1;
        if(rows == null)rows = 30;
        PageHelper.startPage(page, rows);

        //1. 注入mapper
        //2. 查询对应categoryId的数据
        //创建一个example
        TbContentExample contentExample = new TbContentExample();
        TbContentExample.Criteria criteria = contentExample.createCriteria();
        if(categoryId != 0) {
            criteria.andCategoryIdEqualTo(categoryId);
        }
        List<TbContent> tbContents = contentMapper.selectByExample(contentExample);
        //System.out.println("contentList: " + tbContents);
        //3. 分页
        //生成分页
        PageInfo<TbContent> pageInfo = new PageInfo<>(tbContents);
        //封装EasyUIDataGridResult
        EasyUIDataGridResult easyUIDataGridResult = new EasyUIDataGridResult();
        easyUIDataGridResult.setTotal((int)pageInfo.getTotal());
        easyUIDataGridResult.setRows(pageInfo.getList());

        //4. 返回数据
        return easyUIDataGridResult;
    }

    @Override
    public TaotaoResult updateContent(TbContent content) {
        content.setCreated(new Date());
        content.setUpdated(content.getCreated());
        contentMapper.updateByPrimaryKeySelective(content);
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult deleteContent(List<Long> idList) {
        for(Long id : idList){
            contentMapper.deleteByPrimaryKey(id);
        }
        return TaotaoResult.ok();
    }

    @Override
    public List<TbContent> getContentListByCatId(Long categoryId) {
        // 判断redis中是否有数据。如果没有则创建。有则返回
        try {
            String jsonstr = client.hget(CONTENT_KEY,categoryId.toString());
            // 如果存在说明有缓存。
            if(jsonstr != null && jsonstr != ""){
                System.out.println("有缓存");
                return JsonUtils.jsonToList(jsonstr,TbContent.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //----正常业务开始-----
        TbContentExample contentExample = new TbContentExample();
        TbContentExample.Criteria criteria = contentExample.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        List<TbContent> tbContents = contentMapper.selectByExample(contentExample);
        //----结束----

        // 写到缓存（redis）中
        //1 注入redisClient
        //2 调用方法写入redis
        try {
            System.out.println("写缓存啦");
            client.hset(CONTENT_KEY, categoryId.toString(), JsonUtils.objectToJson(tbContents));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tbContents;

    }


}
