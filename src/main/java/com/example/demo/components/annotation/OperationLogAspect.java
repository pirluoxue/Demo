package com.example.demo.components.annotation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chen_bq
 * @description 日志aop
 * @create: 2019/9/27 14:09
 **/
@Aspect
@Component
public class OperationLogAspect {

    @Pointcut("execution(* com.example.demo.controller..*(..))")
    public void pointCut() {

    }

    @Before("pointCut()")
    public void recordRestUseLog(JoinPoint joinPoint) {
        System.out.println("OperationLogAspect before record");
    }

    @After("@annotation(com.example.demo.components.annotation.OperationLog)")
    public void recordOperationLog(JoinPoint joinPoint) {
        System.out.println("record operation log");
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
                        operationLog = m.getAnnotation(OperationLog.class);
                        System.out.println("rest completed operation Log" + operationLog.logtype() + " : " + operationLog.description());
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean isOverride(Class[] parameterTypes, Object[] arguments){
        if (parameterTypes.length != arguments.length){
            return true;
        }
        List list = objectArray2List(arguments);
        for (Class classType : parameterTypes){
            for (int i = 0 ; i < list.size() ; i++){
                if (classType.isInstance(list.get(0))){
                    list.remove(i);
                    break;
                }
                // 没有匹配到类型，直接返回false
                if (i == list.size() - 1){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @Author chen_bq
     * @Description object转list
     * @Date 2019/9/27 15:15
     * @Param [objects]
     * @return java.util.List
     */
    private List objectArray2List(Object[] objects){
        List list = new ArrayList();
        for (Object o : objects){
            list.add(o);
        }
        return list;
    }

}
