/**
 * created by Zheng Jiateng on 2019/8/6.
 */

package com.jmall.high.vo;

import com.jmall.high.pojo.Goods;

import java.util.Date;

/**
 * view object 即model 需要前端展示的数据
 * 此VO要展示goods表的字段加miaosha_goods表的字段
 */
public class GoodsVo extends Goods{
    // miaosha_goods表的字段
	private Double miaoshaPrice;
	private Integer stockCount;
	private Date startDate;
	private Date endDate;
	public Integer getStockCount() {
		return stockCount;
	}
	public void setStockCount(Integer stockCount) {
		this.stockCount = stockCount;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Double getMiaoshaPrice() {
		return miaoshaPrice;
	}
	public void setMiaoshaPrice(Double miaoshaPrice) {
		this.miaoshaPrice = miaoshaPrice;
	}
}
