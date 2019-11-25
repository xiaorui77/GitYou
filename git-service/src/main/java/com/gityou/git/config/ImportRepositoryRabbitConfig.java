package com.gityou.git.config;


import com.gityou.git.listener.ImportRepositoryListener;
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
public class ImportRepositoryRabbitConfig {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Value("${eureka.instance.metadata-map.machineId}")
    private String machineId;

    @Autowired
    private ImportRepositoryListener importRepositoryListener;


    @Bean(name = "importRepositoryDirectExchange")
    public DirectExchange directExchange() {
        DirectExchange directExchange = new DirectExchange("git.service");
        directExchange.setAdminsThatShouldDeclare(rabbitAdmin);
        return directExchange;
    }

    @Bean(name = "importRepositoryDirectQueue")
    public Queue directQueue() {
        Queue queue = new Queue("git.import." + machineId);
        queue.setAdminsThatShouldDeclare(rabbitAdmin);
        return queue;
    }

    // 绑定
    @Bean(name = "importRepositoryDirectQueueBinding")
    public Binding directQueueBinding() {
        Binding binding = BindingBuilder.bind(directQueue()).to(directExchange()).with("import." + machineId);
        binding.setAdminsThatShouldDeclare(rabbitAdmin);
        return binding;
    }

    @Bean(name = "importRepositoryRabbit")
    public MessageListenerContainer messageContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);

        container.setAmqpAdmin(rabbitAdmin);
        container.setQueues(directQueue()); //设置要监听的队列
        container.setExposeListenerChannel(true);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); // 确认模式
        container.setMessageListener(importRepositoryListener);    // 仓库创建Listener
        return container;
    }


}// end
