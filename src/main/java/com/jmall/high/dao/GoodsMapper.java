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
	// 联合查询 返回miaosha_goods表的4个字段和goods表中所有字段，组成GoodsVo
	@Select("select g.*,mg.stock_count, mg.start_date, mg.end_date, mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id = g.id")
	List<GoodsVo> listGoodsVo();

	@Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id = g.id where g.id = #{goodsId}")
	GoodsVo getGoodsVoByGoodsId(@Param("goodsId") long goodsId);

	@Update("update miaosha_goods set stock_count = stock_count - 1 where goods_id = #{goodsId}")
	int reduceStock(MiaoshaGoods g);
	
}
