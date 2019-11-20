package com.gityou.repository.mapper;

import com.gityou.common.pojo.Issue;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface IssueMapper extends Mapper<Issue> {
    @Select("select id from issue where repository = #{repository} AND number = #{number}")
    Long queryId(Long repository, Integer number);

    // 递增 递减
    @Update("update issue set `${field}` = `${field}` + #{value} where id = #{issue}")
    Integer increase(@Param("issue") Long issue, @Param("field") String field, @Param("value") Integer value);
}
