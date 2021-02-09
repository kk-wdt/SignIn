package com.kktt.jesus.schedule.controller;

import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RestController
public class RunSchedulerController {

    private static final String BasePackage = "com.tapcash.amazon.scheduler";

    @Resource
    private ApplicationContext applicationContext;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @GetMapping("/schedulers/run/{scheduler}/{method}")
    public String run(@PathVariable("scheduler") String scheduler, @PathVariable("method") String method) {
        try {
            Class className = Class.forName(BasePackage + "." + scheduler);
            Object bean = applicationContext.getBean(className);
            className.getDeclaredMethod(method).invoke(bean);
        } catch (NoSuchMethodException | ClassNotFoundException | InvocationTargetException | IllegalAccessException e) {
            return "Failed"+e.getMessage();
        }
        return "Success.";
    }

    @GetMapping("/schedulers")
    public String list() {
        try {
            String[] beans = applicationContext.getBeanDefinitionNames();
            StringBuilder response = new StringBuilder();
            for (String beanName : beans) {
                Class<?> beanType = applicationContext.getType(beanName);

                if (!beanType.getPackage().getName().equals(BasePackage))
                    continue;
                if (!beanName.endsWith("Scheduler"))
                    continue;

                Class<?> beanClass = Class.forName(beanType.getName());
                BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
                MethodDescriptor[] descriptors = beanInfo.getMethodDescriptors();

                response.append(beanClass.getSimpleName()).append("\n");
                for (MethodDescriptor pd : descriptors) {
                    Method declaredMethod;
                    try {
                        declaredMethod = beanClass.getDeclaredMethod(pd.getName());
                    } catch (NoSuchMethodException e) {
                        continue;
                    }
                    if (null == declaredMethod.getAnnotation(Scheduled.class))
                        continue;

                    Scheduled annotation = declaredMethod.getAnnotation(Scheduled.class);
                    StringBuilder description = new StringBuilder();

                    if (!annotation.cron().isEmpty())
                        description.append("cron").append("=").append(annotation.cron()).append(",");
                    if (!annotation.zone().isEmpty())
                        description.append("zone").append("=").append(annotation.zone()).append(",");
                    if (annotation.initialDelay() != -1)
                        description.append("initialDelay").append("=").append(annotation.initialDelay()).append(",");
                    if (!annotation.initialDelayString().isEmpty())
                        description.append("initialDelayString").append("=").append(annotation.initialDelayString()).append(",");
                    if (annotation.fixedDelay() != -1)
                        description.append("fixedDelay").append("=").append(annotation.fixedDelay()).append(",");
                    if (!annotation.fixedDelayString().isEmpty())
                        description.append("fixedDelayString").append("=").append(annotation.fixedDelayString()).append(",");
                    if (annotation.fixedRate() != -1)
                        description.append("fixedRate").append("=").append(annotation.fixedRate()).append(",").append(",");
                    if (!annotation.fixedRateString().isEmpty())
                        description.append("fixedRateString").append("=").append(annotation.fixedRateString()).append(",");

                    response.append("\t").append(pd.getName()).append(" (").append(description.substring(0, description.length() - 1)).append(")").append("\n");
                }
                response.append("\n");
            }
            return response.toString();
        } catch (Exception e) {
            return "Failed";
        }
    }
}
