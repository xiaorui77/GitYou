package com.gityou.git;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@ServletComponentScan
@MapperScan("com.gityou.common.mapper")
public class GitServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GitServerApplication.class, args);
    }
}
