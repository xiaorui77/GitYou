package com.gityou.gateway.service;


import com.gityou.common.mapper.RepositoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ZuulService {

    @Autowired
    RepositoryMapper repositoryMapper;


    // 获取某仓库所在的机器id
    public List<Integer> getMachine(String user, String repositoryName) {
        List<Integer> result = new ArrayList<>(5);

        Long machine = repositoryMapper.queryMachine(user, repositoryName);
        if (machine == null)
            return result;

        // 获取标志位
        long flag = (machine >> 50) & 0x3ff;

        // machine只保留50位
        machine &= 0x3ffffffffffffL;


        for (; machine > 0; ) {
            if ((flag & 0x1) == 0x1)
                result.add((int) (machine & 0x3ff));
            flag >>= 2;
            machine >>= 10;
        }
        return result;
    }

}
