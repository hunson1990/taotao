package com.taotao.manager.service;


import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.pojo.TbItem;
import com.taotao.common.pojo.TbItemDesc;

/**
 * 商品相关的接口
 */
public interface ItemService {
    /**
     * 根据当前的页码和每页的数量，进行分页查询
     * @param page
     * @param rows
     * @return
     */
    public EasyUIDataGridResult getItemList(Integer page, Integer rows);
    public TbItem getItemById(Long itemId);
    public TbItemDesc getItemDescById(Long itemId);
    public TaotaoResult createTbItem(TbItem item, String desc);
}
