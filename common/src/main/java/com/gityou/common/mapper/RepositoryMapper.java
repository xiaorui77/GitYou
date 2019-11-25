package com.gityou.common.mapper;

import com.gityou.common.pojo.Repository;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface RepositoryMapper extends Mapper<Repository> {

    @Select("select machine from repository where username = #{user} and name = #{repository}")
    Long queryMachine(String user, String repository);

    // 设置 machine flag
    @Update("update repository set machine = machine | #{flag} where id = #{repository}")
    void setMachineFlag(Long repository, Long flag);
}
