/**
 * created by Zheng Jiateng on 2019/8/6.
 */

package com.jmall.high.dao;

import com.jmall.high.pojo.MiaoshaGoods;
import com.jmall.high.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface GoodsMapper {
	// 获取商品列表 联合查询 返回miaosha_goods表的4个字段和goods表中所有字段，组成GoodsVo
	@Select("select g.*,mg.stock_count, mg.start_date, mg.end_date, mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id = g.id")
	List<GoodsVo> listGoodsVo();

	// 获取商品详情
	@Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id = g.id where g.id = #{goodsId}")
	GoodsVo getGoodsVoByGoodsId(@Param("goodsId") long goodsId);

	// where条件增加 and stock_count > 0 解决超卖问题 因为innodb默认隔离级别为repeatable read 一个事务在读的时候其他事务不能写
	@Update("update miaosha_goods set stock_count = stock_count - 1 where goods_id = #{goodsId} and stock_count > 0")
	int reduceStock(MiaoshaGoods g);
	
}
