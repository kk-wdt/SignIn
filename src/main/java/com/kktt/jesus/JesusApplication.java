package com.kktt.jesus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.kktt.jesus.dao")
public class JesusApplication {

	public static void main(String[] args) {
		SpringApplication.run(JesusApplication.class, args);
	}

}

