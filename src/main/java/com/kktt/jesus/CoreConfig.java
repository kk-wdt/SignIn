package com.kktt.jesus;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class CoreConfig {

    public static String getDefaultNotFoundImage() {
        return "https://images-na.ssl-images-amazon.com/images/G/01/x-site/icons/no-img-sm.gif";
    }

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}