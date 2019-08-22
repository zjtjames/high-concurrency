/**
 * created by Zheng Jiateng on 2019/8/2.
 */

package com.jmall.high.dao;

import com.jmall.high.pojo.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

	@Select("select * from miaosha_user where id = #{id}")
	MiaoshaUser getById(@Param("id") long id);

	@Update("update miaosha_user set password = #{password} where id = #{id}")
    void update(MiaoshaUser toBeUpdated);
}
