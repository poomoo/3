/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.response;

import java.util.List;

/**
 * 作者: 李苜菲
 * 日期: 2016/7/5 16:00.
 */
public class RCommodityInfoBO {
    public String exchangeCondition;//1.测试1；rn2.测试2；rn3.测试3；rn4.测试4",	--兑换条件，注意格式
    public int exchangedNum;//已经兑换数量
    public String goodsDetails;//"<p>html图文富文本</p>",		--商品详情介绍
    public int goodsId;
    public String goodsName;// --商品名称
    public String jumpUrl;//--兑换跳转地址
    public int price;//--价格
    public boolean isCollect;//-- 是否收藏
    public List<String> picList;//--轮播图集合
}
