package com.kktt.jesus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.kktt.jesus.dao")
public class JesusApplication {

	public static void main(String[] args) {
		SpringApplication.run(JesusApplication.class, args);
	}

}

