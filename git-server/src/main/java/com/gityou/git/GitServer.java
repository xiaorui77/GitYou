package com.gityou.git;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class GitServer {
    public static void main(String[] args) {
        SpringApplication.run(GitServer.class, args);
    }
}
