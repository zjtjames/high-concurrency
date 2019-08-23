/**
 * created by Zheng Jiateng on 2019/8/6.
 */

package com.jmall.high.dao;

import com.jmall.high.pojo.MiaoshaOrder;
import com.jmall.high.pojo.OrderInfo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OrderMapper {
	
	@Select("select * from miaosha_order where user_id=#{userId} and goods_id=#{goodsId}")
	MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);

	// 在order_info表插入订单 返回订单id
	@Insert("insert into order_info(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date)values("
			+ "#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel},#{status},#{createDate} )")
	@SelectKey(keyColumn="id", keyProperty="id", resultType=long.class, before=false, statement="select last_insert_id()")
	long insert(OrderInfo orderInfo);

	// 在miaosha_order中插入秒杀订单
	@Insert("insert into miaosha_order (user_id, goods_id, order_id)values(#{userId}, #{goodsId}, #{orderId})")
	int insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);

	// 根据订单号获取订单详情
    @Select("select * from order_info where id = #{orderId}")
    OrderInfo getOrderById(@Param("orderId")long orderId);
}
