/**
 * created by Zheng Jiateng on 2019/8/9.
 */

package com.jmall.high.service;

import com.jmall.high.dao.GoodsMapper;
import com.jmall.high.pojo.MiaoshaGoods;
import com.jmall.high.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	// 减少miaosha_goods表中的库存
    @Transactional
	public boolean reduceStock(GoodsVo goods) {
		MiaoshaGoods g = new MiaoshaGoods();
		g.setGoodsId(goods.getId());
		int result = goodsMapper.reduceStock(g);
		return result > 0;
	}
}
