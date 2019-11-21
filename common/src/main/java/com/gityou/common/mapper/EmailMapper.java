package com.gityou.common.mapper;

import com.gityou.common.pojo.Email;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface EmailMapper extends Mapper<Email> {

    // 根据 Type获取用户
    @Select("select * FROM email WHERE user = #{user} AND type & ${type}")
    Email queryByType(Integer user, Integer type);

    // type 移除
    @Update("update email set type = type & (~${type}) where id = #{id} AND user = #{user}")
    Integer removeType(Long id, Integer user, Integer type);

    // type字段 设置
    @Update("update email set type = type | ${type} where id = #{id} AND user = #{user}")
    Integer addType(Long id, Integer user, Integer type);

    // 获取某用户接受消息的邮箱
    @Select("select email from email where user = #{user} and type & 0x10 = 0x10")
    String queryEmail(Integer user);
}
