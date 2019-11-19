package com.gityou.common.mapper;

import com.gityou.common.pojo.Repository;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface RepositoryMapper extends Mapper<Repository> {

    @Select("select machine from repository where username = #{user} and name = #{repository}")
    Long queryMachine(String user, String repository);
}
