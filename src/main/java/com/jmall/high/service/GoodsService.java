/**
 * created by Zheng Jiateng on 2019/8/9.
 */

package com.jmall.high.service;

import com.jmall.high.dao.GoodsMapper;
import com.jmall.high.pojo.MiaoshaGoods;
import com.jmall.high.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {
	
	@Autowired
	GoodsMapper goodsMapper;
	
	public List<GoodsVo> listGoodsVo(){
		return goodsMapper.listGoodsVo();
	}

	public GoodsVo getGoodsVoByGoodsId(long goodsId) {
		return goodsMapper.getGoodsVoByGoodsId(goodsId);
	}

	public void reduceStock(GoodsVo goods) {
		MiaoshaGoods g = new MiaoshaGoods();
		g.setGoodsId(goods.getId());
		goodsMapper.reduceStock(g);
	}
}
