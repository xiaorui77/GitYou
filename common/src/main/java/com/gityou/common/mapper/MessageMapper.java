package com.gityou.common.mapper;

import com.gityou.common.pojo.Message;
import com.gityou.common.pojo.MessageRepository;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface MessageMapper extends Mapper<Message> {

    @Select("select message.repository as repositoryId, repository.name as repositoryName, user.username, user.avatar, count(message.repository) as number " +
            "from message, repository, user " +
            "where message.user = #{userId} and message.status & 0x4 = 0x4 and message.repository = repository.id and repository.user_id = user.id " +
            "group by message.repository")
    List<MessageRepository> queryRepositories(Integer userId);
}
