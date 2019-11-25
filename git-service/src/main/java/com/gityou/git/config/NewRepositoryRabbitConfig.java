package com.gityou.git.config;


import com.gityou.git.listener.NewRepositoryListener;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NewRepositoryRabbitConfig {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Value("${eureka.instance.metadata-map.machineId}")
    private String machineId;

    @Autowired
    private NewRepositoryListener newRepositoryListener;


    @Bean(name = "newRepositoryDirectExchange")
    public DirectExchange directExchange() {
        DirectExchange directExchange = new DirectExchange("git.service");
        directExchange.setAdminsThatShouldDeclare(rabbitAdmin);
        return directExchange;
    }

    @Bean(name = "newRepositoryDirectQueue")
    public Queue directQueue() {
        Queue queue = new Queue("git.new." + machineId);
        queue.setAdminsThatShouldDeclare(rabbitAdmin);
        return queue;
    }

    // 绑定
    @Bean(name = "newRepositoryDirectQueueBinding")
    public Binding directQueueBinding() {
        Binding binding = BindingBuilder.bind(directQueue()).to(directExchange()).with("new." + machineId);
        binding.setAdminsThatShouldDeclare(rabbitAdmin);
        return binding;
    }

    @Bean(name = "newRepositoryRabbit")
    public MessageListenerContainer messageContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);

        container.setAmqpAdmin(rabbitAdmin);
        container.setQueues(directQueue()); //设置要监听的队列
        container.setExposeListenerChannel(true);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); // 确认模式
        container.setMessageListener(newRepositoryListener);    // 仓库创建Listener
        return container;
    }


}// end
