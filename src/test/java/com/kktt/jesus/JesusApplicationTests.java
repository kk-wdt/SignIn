package com.kktt.jesus;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JesusApplicationTests {

	@Test
	public void contextLoads() {

		Instant now = Instant.now();
		Instant startInstant = now.minus(1, ChronoUnit.DAYS);
		String xx = now.toString();
		String ss = startInstant.toString();

		System.out.println(1);
	}



}

