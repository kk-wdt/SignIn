package com.kktt.jesus.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Clock;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class RedisQueueService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private Clock clock;

    private static final DefaultRedisScript<List<String>> leftPopAndAddIntoZSetRedisScript = new DefaultRedisScript<>("" +
            "local values = {}; " +
            "for i = 1, ARGV[1], 1 do " +
            "   local value = redis.call('LPOP', KEYS[1]); " +
            "   if not value then " +
            "       return values; " +
            "   end; " +
            "   redis.call('ZADD', KEYS[2], ARGV[2], value); " +
            "   table.insert(values, value); " +
            "end; " +
            "return values;", (Class<List<String>>) Arrays.<String>asList().getClass());

    private static final DefaultRedisScript<Long> popZSetAndRightPushRedisScript = new DefaultRedisScript<>("" +
            "local set = redis.call('ZRANGEBYSCORE', KEYS[1], '0', ARGV[1]); " +
            "for k, v in pairs(set) do " +
            "    redis.call('ZREM', KEYS[1], v); " +
            "    redis.call('RPUSH', KEYS[2], v); " +
            "end; " +
            "return table.getn(set); ", Long.class);

    public String leftPopAndAddIntoZSet(String listKey, String zSetKey) {
        List<String> values = leftPopAndAddIntoZSet(listKey, zSetKey, 1);
        return (values.isEmpty()) ? null : values.get(0);
    }

    public List<String> leftPopAndAddIntoZSet(String listKey, String zSetKey, int sizes) {
        return stringRedisTemplate.execute(leftPopAndAddIntoZSetRedisScript, Arrays.asList(listKey, zSetKey), String.format("%s", sizes), String.format("%s", Instant.now(clock).toEpochMilli()));
    }

    public long popZSetAndRightPush(String zSetKey, String listKey, long maxScore) {
        return stringRedisTemplate.execute(popZSetAndRightPushRedisScript, Arrays.asList(zSetKey, listKey), String.format("%s", maxScore));
    }

    public void removeZSetValue(String zSetKey, String value) {
        stringRedisTemplate.opsForZSet().remove(zSetKey, value);
    }

    public void removeZSetValue(String zSetKey, List<String> values) {
        values.forEach(value -> stringRedisTemplate.opsForZSet().remove(zSetKey, value));
    }

    public void createRun(String key, Instant instant) {
        stringRedisTemplate.opsForZSet().add(key, instant.toString(), instant.toEpochMilli());
    }

    public void removeRun(String key, int expireHours) {
        stringRedisTemplate.opsForZSet().removeRangeByScore(key, 0, System.currentTimeMillis() - expireHours * 60 * 60 * 1000L);
    }

    public boolean hasRun(String key, Instant instant) {
        return null != stringRedisTemplate.opsForZSet().score(key, instant.toString());
    }

    public void pushHeader(String queue, String value) {
        stringRedisTemplate.opsForList().rightPush(queue, value);
    }

    public void push(String queue, String value) {
        stringRedisTemplate.opsForList().leftPush(queue, value);
    }

    public void push(String queue, Collection<String> value) {
        stringRedisTemplate.opsForList().leftPushAll(queue, value);
    }

    public long size(String queue) {
        return stringRedisTemplate.opsForList().size(queue);
    }

    public long zCard(String queue) {
        return stringRedisTemplate.opsForZSet().size(queue);
    }
    public String popAndPush(String queue, String secQueue) {
        return stringRedisTemplate.opsForList().rightPopAndLeftPush(queue, secQueue);
    }

    public String pop(String queue){
        return stringRedisTemplate.opsForList().rightPop(queue);
    }

    public void delete(String queueName){
        stringRedisTemplate.delete(queueName);
    }
}
