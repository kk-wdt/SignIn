package com.kktt.jesus;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * Package com.tapcash.amazon
 *
 * @author Lancer He <lancer.he@gmail.com>
 */
public class ThreadPoolTaskSchedulerCreator {

    public static ThreadPoolTaskScheduler create(int poolSize) {
        ThreadPoolTaskScheduler scheduledExecutorService = new ThreadPoolTaskScheduler();
        scheduledExecutorService.setPoolSize(poolSize);
        scheduledExecutorService.setWaitForTasksToCompleteOnShutdown(true);
        scheduledExecutorService.setAwaitTerminationSeconds(10);
        return scheduledExecutorService;
    }
}
