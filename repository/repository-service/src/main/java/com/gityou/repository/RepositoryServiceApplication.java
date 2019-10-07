package com.gityou.repository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.gityou.repository.mapper")
@EnableFeignClients
public class RepositoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RepositoryServiceApplication.class, args);
    }

}
