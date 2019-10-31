package com.gityou.repository.mapper;

import com.gityou.repository.pojo.Issue;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface IssueMapper extends Mapper<Issue> {
    @Select("select id from issue where repository = #{repository} AND number = #{number}")
    Long queryId(Long repository, Integer number);
}
