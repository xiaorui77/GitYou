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


    public List<Integer> getMachine(String user, String repositoryName) {
        List<Integer> result = new ArrayList<>();

        Long machine = repositoryMapper.queryMachine(user, repositoryName);
        if (machine == null)
            return result;

        if (machine > (0x1L << 60)) {
            machine &= 0xfffffffffffffffL;
        }

        for (; machine > 0; ) {
            result.add((int) (machine & 0xfff));
            machine >>= 12;
        }
        return result;
    }

}
