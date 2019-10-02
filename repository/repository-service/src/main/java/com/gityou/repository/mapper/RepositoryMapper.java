package com.gityou.repository.mapper;

import com.gityou.repository.pojo.Repository;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;


public interface RepositoryMapper extends Mapper<Repository> {

    @Select("select name from repository where name = #{name}")
    public String hasName(String name);
}
