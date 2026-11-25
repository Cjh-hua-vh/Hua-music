package com.example.hua;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

@MapperScan("com.example.hua.mapper")
public class HuaMusicApplication {

    public static void main(String[] args) {

        SpringApplication.run(HuaMusicApplication.class, args);
    }

}
