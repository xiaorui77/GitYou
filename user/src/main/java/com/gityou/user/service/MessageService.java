package com.gityou.user.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gityou.common.entity.PageResult;
import com.gityou.common.entity.UserInfo;
import com.gityou.common.mapper.MessageMapper;
import com.gityou.common.pojo.Message;
import com.gityou.user.util.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageMapper messageMapper;

    // 消息所涉及的repository
    public List messageRepository(Integer userId) {
        UserInfo loginUser = LoginInterceptor.getLoginUser();
        if (loginUser == null || !loginUser.getId().equals(userId))
            return null;

        return messageMapper.queryRepositories(userId);
    }

    // 分页查询
    public PageResult messagePage(Integer userId, Integer page, Integer status, Integer type) {
        UserInfo loginUser = LoginInterceptor.getLoginUser();
        if (loginUser == null || !loginUser.getId().equals(userId))
            return null;

        Example example = new Example(Message.class);
        Example.Criteria criteria = example.createCriteria();
        example.setOrderByClause("status desc");
        criteria.andEqualTo("user", userId);
        // 过滤type
        if (type != 0)
            criteria.andEqualTo("type", type);
        // 过滤status
        if (status == 0x0)
            criteria.andCondition("(status & 0x4 = 0x4)");   // 0x4为未做
        else if (status == 0x10)
            criteria.andCondition("(status & 0x14 = 0x14)");    // 未读
        else if (status == 0x1)
            criteria.andCondition("(status & 0x1 = 0x1)");  // 收藏
        else if (status == 0x4)
            criteria.andCondition("(status | 0x1 = 0x1)");    // 已做

        // 分页查询
        PageHelper.startPage(page, 20);
        List<Message> messages = messageMapper.selectByExample(example);
        PageInfo<Message> pageInfo = new PageInfo<>(messages);

        return new PageResult<>(pageInfo.getTotal(), pageInfo.getPageNum(), messages);
    }


}// end
