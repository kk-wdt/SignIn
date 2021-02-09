package com.kktt.jesus.utils;

import com.kktt.jesus.ThreadPoolTaskSchedulerCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

public class MultiTaskProcessorUtil {
    private static final Logger logger = LoggerFactory.getLogger(MultiTaskProcessorUtil.class);
    public static final int DEFAULT_POOL_SIZE = 100;
    static ThreadPoolTaskScheduler pool;
    static {
        pool = ThreadPoolTaskSchedulerCreator.create(DEFAULT_POOL_SIZE);
        pool.setWaitForTasksToCompleteOnShutdown(true);
        pool.setAwaitTerminationSeconds(10);
        pool.initialize();
    }

    public interface IThreadProcessor {
        void process() ;
    }

    public static void processTask(int size,IThreadProcessor processor) {
        size = size <= 0?DEFAULT_POOL_SIZE:size;
        for(int i= 0;i < size;i++){
            pool.submit(new ThreadTask(i+"",processor));
        }
    }

    public static class ThreadTask implements Runnable{
        private String name;
        private IThreadProcessor processor;

        public ThreadTask(String name,IThreadProcessor processor){
            this.name = name;
            this.processor = processor;
        }

        @Override
        public void run() {
            processor.process();
        }
    }
}