package com.kktt.jesus;

import com.kktt.jesus.exception.*;
import com.kktt.jesus.service.RedisQueueService;
import com.kktt.jesus.utils.MWSExceptionIdentifyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Component
public class TaskConsumerComponent {

    public static final String PENDING_TASK_PREFIX = "GT_Pending_";

    private static final Logger logger = LoggerFactory.getLogger(TaskConsumerComponent.class);

    @Resource
    private RedisQueueService redisQueueService;

    public void consume(String queueName, int maxSize, IQueueMultiConsumer consumer) {
        String pendingSetName = PENDING_TASK_PREFIX + queueName;

        while (true) {
            List<String> values = redisQueueService.leftPopAndAddIntoZSet(queueName, pendingSetName, maxSize);
            if (values.isEmpty()) return;

            try {
                consumer.run(values);
                redisQueueService.removeZSetValue(pendingSetName, values);
            } catch (MWSAuthTokenInvalidException e) {
                MWSExceptionIdentifyUtil.MWSAuthTokenInvalidExceptionHandler(e);
                redisQueueService.removeZSetValue(pendingSetName, values);
            } catch (MWSTempAccessDeniedException e) {
                MWSExceptionIdentifyUtil.MWSTempAccessDeniedExceptionHandler(e);
                redisQueueService.removeZSetValue(pendingSetName, values);
            } catch (MWSConnectedStatusException e) {
                MWSExceptionIdentifyUtil.MWSConnectedStatusExceptionHandler(e);
                redisQueueService.removeZSetValue(pendingSetName, values);
            } catch (MWSQueueTaskContinueException e) {
                redisQueueService.removeZSetValue(pendingSetName, values);
            } catch (MWSInvalidParameterValueException e) {
                MWSExceptionIdentifyUtil.MWSInvalidParameterValueExceptionHandler(e);
                redisQueueService.removeZSetValue(pendingSetName, values);
            } catch (MWSRequestThrottledException e) {
                MWSExceptionIdentifyUtil.MWSRequestThrottledExceptionHandler(e);
            } catch (MWSQueueTaskRetryException e) {
                MWSExceptionIdentifyUtil.MWSQueueTaskRetryExceptionHandler(e);
            } catch (QueueExitException e) {
                break;
            } catch (Exception e) {
                logger.error(String.format("异常 - queue: %s, %s", queueName, values), e);
            }
        }
    }

    public void consume(String queueName, IQueueConsumer consumer) {
        String pendingSetName = PENDING_TASK_PREFIX + queueName;

        while (true) {
            String value = redisQueueService.leftPopAndAddIntoZSet(queueName, pendingSetName);
            if (StringUtils.isEmpty(value)) return;

            try {
                consumer.run(value);
                redisQueueService.removeZSetValue(pendingSetName, value);
            } catch (MWSAuthTokenInvalidException e) {
                MWSExceptionIdentifyUtil.MWSAuthTokenInvalidExceptionHandler(e);
                redisQueueService.removeZSetValue(pendingSetName, value);
            } catch (MWSTempAccessDeniedException e) {
                MWSExceptionIdentifyUtil.MWSTempAccessDeniedExceptionHandler(e);
                redisQueueService.removeZSetValue(pendingSetName, value);
            } catch (MWSConnectedStatusException e) {
                MWSExceptionIdentifyUtil.MWSConnectedStatusExceptionHandler(e);
                redisQueueService.removeZSetValue(pendingSetName, value);
            } catch (MWSQueueTaskContinueException e) {
                redisQueueService.removeZSetValue(pendingSetName, value);
            } catch (MWSInvalidParameterValueException e) {
                MWSExceptionIdentifyUtil.MWSInvalidParameterValueExceptionHandler(e);
                redisQueueService.removeZSetValue(pendingSetName, value);
            } catch (MWSRequestThrottledException e) {
                MWSExceptionIdentifyUtil.MWSRequestThrottledExceptionHandler(e);
            } catch (MWSQueueTaskRetryException e) {
                MWSExceptionIdentifyUtil.MWSQueueTaskRetryExceptionHandler(e);
            } catch (QueueExitException e) {
                break;
            } catch (Exception e) {
                logger.error(String.format("异常 - queue: %s, %s", queueName, value), e);
            }
        }
    }

    public interface IQueueConsumer {
        void run(String value) throws Exception;
    }

    public interface IQueueMultiConsumer {
        void run(List<String> value) throws Exception;
    }
}