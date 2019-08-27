/**
 * created by Zheng Jiateng on 2019/8/15.
 */

package com.jmall.high.rabbitmq;

import com.jmall.high.pojo.MiaoshaUser;

public class MiaoshaMessage {
	private MiaoshaUser user; //用户
	private long goodsId; // 商品id

    public MiaoshaUser getUser() {
        return user;
    }

    public void setUser(MiaoshaUser user) {
        this.user = user;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }
}
