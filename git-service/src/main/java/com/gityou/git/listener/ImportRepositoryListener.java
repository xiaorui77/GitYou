package com.gityou.git.listener;

import com.gityou.common.mapper.RepositoryMapper;
import com.gityou.common.pojo.Repository;
import com.gityou.git.utils.GitRepositoryUtils;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class ImportRepositoryListener extends AbstractMessageListener<Repository> {

    @Autowired
    private RepositoryMapper repositoryMapper;

    @Autowired
    private GitRepositoryUtils gitUtils;

    @Value("${eureka.instance.metadata-map.machineId}")
    private Integer machineId;


    @Override
    public boolean onMessage(Object message, MessageProperties properties, Channel channel) {
        Repository repository = (Repository) message;

        // 从url导入仓库
        if (!gitUtils.importRepository(repository.getUsername(), repository.getName(), repository.getImportFrom())) {
            return false;
        }

        // 设置machine标志
        long flag = 0x1L << (50 + (machineId - 1) * 2);
        repositoryMapper.setMachineFlag(repository.getId(), flag);
        return true;
    }

}// end
