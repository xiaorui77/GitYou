package com.gityou.git.listener;

import com.gityou.common.utils.JsonUtils;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

import java.lang.reflect.ParameterizedType;


/*
 * @author: yongtao
 *
 * */
public abstract class AbstractMessageListener<T> implements ChannelAwareMessageListener {

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            byte[] body = message.getBody();
            Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            Object data = JsonUtils.parse(new String(body), entityClass);
            if (data == null)
                throw new Exception();
            MessageProperties properties = message.getMessageProperties();

            // 确认消息是否成功
            if (onMessage(data, properties, channel))
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            else
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 执行
    abstract boolean onMessage(Object message, MessageProperties properties, Channel channel);

}// end
