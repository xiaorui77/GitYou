package com.gityou.git.listener;


import com.gityou.common.mapper.RepositoryMapper;
import com.gityou.common.pojo.Repository;
import com.gityou.common.utils.JsonUtils;
import com.gityou.git.utils.GitRepositoryUtils;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class NewRepositoryListener implements ChannelAwareMessageListener {

    @Autowired
    private RepositoryMapper repositoryMapper;

    @Autowired
    private GitRepositoryUtils gitUtils;

    @Value("${eureka.instance.metadata-map.machineId}")
    private Integer machineId;


    @Override
    public void onMessage(Message message, Channel channel) throws IOException {
        try {
            byte[] body = message.getBody();
            Repository repository = JsonUtils.parse(new String(body), Repository.class);
            if (repository == null)
                throw new Exception();
            MessageProperties properties = message.getMessageProperties();


            // Todo: 创建本地裸仓库
            if (!gitUtils.createNewRepository(repository.getUsername(), repository.getName())) {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
                return;
            }

            // 设置machine标志
            long flag = 0x1L << (50 + (machineId - 1) * 2);
            repositoryMapper.setMachineFlag(repository.getId(), flag);

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); //确认消息成功消费
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}// end
