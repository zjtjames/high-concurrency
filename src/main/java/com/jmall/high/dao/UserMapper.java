/**
 * created by Zheng Jiateng on 2019/8/7.
 */
package com.jmall.high.dao;

import com.jmall.high.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    // 不用xml 直接把sql写在注解里的方式
    @Select("select * from user where id = #{id}")
    User getById(@Param("id") int id);

    @Insert("insert into user(id, name) values (#{id}, #{name})")
    int insert(User user);
}
