package com.gityou.user.mapper;

import com.gityou.common.pojo.User;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Set;


public interface UserMapper extends Mapper<User> {

    // user表, email表联合查询
    List<User> queryUsersByEmails(@Param("emails") Set<String> emails);
}
