package com.example.demo.components.annotation;

import com.example.demo.util.common.TypeChangeUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author chen_bq
 * @description 日志aop
 * @create: 2019/9/27 14:09
 **/
@Aspect
@Component
public class OperationLogAspect {

    private final static Logger logger = LoggerFactory.getLogger(OperationLogAspect.class);

    @Pointcut("execution(* com.example.demo.controller..*(..))")
    public void pointCut() {

    }

    @Before("pointCut()")
    public void recordRestUseLog(JoinPoint joinPoint) {
        System.out.println("pointCut by regular expression in OperationLogAspect");
    }

    @Before(("@annotation(com.example.demo.components.annotation.OperationLog)"))
    public void receiveRequestLog(JoinPoint joinPoint) {
        logger.info("* * * receive {} by Aspect * * *", joinPoint.getSignature().getName());
    }

    @After("@annotation(com.example.demo.components.annotation.OperationLog)")
    public void recordOperationLog(JoinPoint joinPoint) {
        long endTime = System.currentTimeMillis();
        OperationLog operationLog = getOperationLogByJoinPoint(joinPoint);
        logger.info("rest completed operation Log {} : {}", operationLog.logtype(), operationLog.description());
    }

    @Around("@annotation(com.example.demo.components.annotation.OperationLog)")
    public Object aroundProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        long requestTime = System.currentTimeMillis();
        Object o = joinPoint.proceed();
        long responseTime = System.currentTimeMillis();
        // 仅为控制层方法的执行时间，不包括controller return之后的框架处理时间
//        logger.info("complete process {}, begintTime: {}, endTime: {}, duration: {}", joinPoint.getArgs(), requestTime,
//            responseTime, Duration.ofMillis(responseTime - requestTime));
        return o;
    }

    /**
     * @return com.example.demo.components.annotation.OperationLog
     * @Author chen_bq
     * @Description 根据切点获得注解实体
     * @Date 2019/10/15 10:01
     * @Param [joinPoint]
     */
    private OperationLog getOperationLogByJoinPoint(JoinPoint joinPoint) {
        // 获得切点对象名
        String targetName = joinPoint.getTarget().getClass().getName();
        // 获得切点方法名
        String methodName = joinPoint.getSignature().getName();
        // 获得方法入参值
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = null;
        try {
            // 反射对象
            targetClass = Class.forName(targetName);
            // 获得所有对象
            Method[] method = targetClass.getMethods();
            OperationLog operationLog;
            for (Method m : method) {
                // 找注解方法
                if (m.getName().equals(methodName)) {
                    Class[] parameterTypes = m.getParameterTypes();
                    // 过滤重载方法
                    if (!isOverride(parameterTypes, arguments)) {
                        return m.getAnnotation(OperationLog.class);
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isOverride(Class[] parameterTypes, Object[] arguments) {
        if (parameterTypes.length != arguments.length) {
            return true;
        }
        List list = TypeChangeUtils.objectArray2List(arguments);
        for (Class classType : parameterTypes) {
            for (int i = 0; i < list.size(); i++) {
                if (classType.isInstance(list.get(0))) {
                    list.remove(i);
                    break;
                }
                // 没有匹配到类型，直接返回false
                if (i == list.size() - 1) {
                    return true;
                }
            }
        }
        return false;
    }
}
