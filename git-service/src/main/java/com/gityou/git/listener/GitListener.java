package com.gityou.git.listener;


import com.gityou.common.pojo.Repository;
import com.gityou.common.utils.JsonUtils;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

@Component
public class GitListener implements ChannelAwareMessageListener {

    @Override
    public void onMessage(Message message, Channel channel) {
        try {
            byte[] body = message.getBody();
            Repository repository = JsonUtils.parse(new String(body), Repository.class);
            MessageProperties properties = message.getMessageProperties();

            System.out.println("receive properties : " + properties);
            System.out.println("receive msg : " + repository);

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); //确认消息成功消费
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}// end
