package com.kktt.jesus;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Package com.tapcash.amazon
 *
 * @author Lancer He <lancer.he@gmail.com>
 */
@Component
public class SpringContext implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        SpringContext.context = context;
    }

    public static ApplicationContext getContext() {
        return context;
    }

}