package com.imooc.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ServiceLogAspect {

    /* * AOP通知：
     * 1. 前置通知：在方法调用之前执行
     * 2．后置通知：在方法正常调用之后执行
     * 3. 环绕通知：在方法调用之前和之后，都分别可以执行的通知
     * 4. 异常通知：如果在方法调用过程中发生异常，则通知
     * 5. 最终通知：在方法调用之后执行 */

    /**
     * 切面表达式：
     * execution 代表所要执行的表达式主体
     * <pre>
     * 第一处 * 代表方法返回类型 *代表所有类型
     * 第二处 包名代表aop监控的类所在的包
     * 第三处 .. 代表该包以及其子包下的所有类方法
     * 第四处 * 代表类名，*代表所有类
     * 第五处 *(..) *代表类中的方法名，(..)表示方法中的任何参数
     */
    @Around("execution(* com.imooc.service.impl.*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();

        log.info(">>开始执行 {}.{}", className, methodName);

        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        long duration = end - start;

        if (duration > 3000) {
            log.error(">>执行时间过长，方法：{}，耗时：{}ms", className + "." + methodName, duration);
        } else if (duration > 2000) {
            log.warn(">>执行时间过长，方法：{}，耗时：{}ms", className + "." + methodName, duration);
        } else {
            log.info(">>执行结束 {}.{}，耗时：{}ms", className, methodName, duration);
        }

        return result;
    }
}
