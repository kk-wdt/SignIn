package com.kktt.jesus.schedule;

import com.kktt.jesus.service.RedisQueueService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Clock;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.kktt.jesus.TaskConsumerComponent.PENDING_TASK_PREFIX;


/**
 * 重试定时器
 */
@Component
public class PendingTaskScheduler {

    @Resource
    private RedisQueueService redisQueueService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private Clock clock;

    private static final Map<String, Long> pendingTaskMaxTimeMap = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(PendingTaskScheduler.class);

    static {
    }

    @Scheduled(fixedDelay = 10000, initialDelay = 12000)
    private void process() {
        Set<String> pendingKeySets = stringRedisTemplate.keys(PENDING_TASK_PREFIX + "*");
        logger.info("正在处理重试队列, 当前Pending Sets数量: {}", pendingKeySets.size());
        for (String zSetKey : pendingKeySets) {
            String listKey = StringUtils.removeStart(zSetKey, PENDING_TASK_PREFIX);
            if(pendingTaskMaxTimeMap.get(listKey) != null && pendingTaskMaxTimeMap.get(listKey) == -1){
                logger.info("正在处理重试队列, 时间为-1 跳过: ({} => {})",  zSetKey, listKey);
                continue;
            }
            long pendingQueueTime = pendingTaskMaxTimeMap.get(listKey) == null ? 30 * 1000L : pendingTaskMaxTimeMap.get(listKey);
            long affected = redisQueueService.popZSetAndRightPush(zSetKey, listKey, Instant.now(clock).toEpochMilli() - pendingQueueTime);
            if (affected > 0)
                logger.info("正在处理重试队列, 时间差大于: {} ms 放回原队列数量: {} ({} => {})", pendingQueueTime, affected, zSetKey, listKey);
        }
    }
}