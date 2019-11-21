package com.gityou.message.mapper;

import com.gityou.common.pojo.SubscriptionIssue;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface SubscriptionIssueMapper extends Mapper<SubscriptionIssue> {

    @Select("select repository from issue where id = #{issue}")
    Long queryRepository(Long issue);
}
