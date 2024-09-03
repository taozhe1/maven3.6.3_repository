package com.redis.redis_springboot.aspect;


import com.alibaba.fastjson.JSONObject;
import com.redis.redis_springboot.annotation.ElapsedTimeAnnotation;
import com.redis.redis_springboot.util.AnnotationArgsUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


@Slf4j
//@Aspect注解就是告诉 Spring 这是一个aop类，AOP切面
@Aspect
@Component
public class ElapsedTimeAspect {

    /**
     * 定义
     */
    public static final String ANNOTATION_POINTCUT =
            "@annotation(com.redis.redis_springboot.annotation.ElapsedTimeAnnotation)";


    /**
     * 声明,@Pointcut注解 声明这是一个需要拦截的切面
     */
    @Pointcut(ANNOTATION_POINTCUT)
    public void annotationPointcut() {
    }

    /**
     * 此处进入到方法前  可以实现一些业务逻辑
     * @param joinPoint
     */
    @Before("annotationPointcut()")
    public void beforePointcut(JoinPoint joinPoint) {
    }

    /**
     * 环绕执行，就是在调用目标方法之前和调用之后，都会执行一定的逻辑
     * @param joinPoint
     * @return
     * @throws Throwable
     */

    @Around("annotationPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        ElapsedTimeAnnotation annotation = method.getAnnotation(ElapsedTimeAnnotation.class);

        if(annotation!=null){
            //动态传参
            String s = generateKeyBySpEL(annotation.arbitrarily(), joinPoint);
            log.info("动态参数："+s);

            if(annotation.needLog()){
                long start = System.currentTimeMillis();
                Object result = joinPoint.proceed();
                long end = System.currentTimeMillis();
                long elapsedTime = end - start;
                String stringBuffer = new StringBuffer()
                        .append("方法名：").append(method.getName())
                        .append("。方法路径地址名：").append(method.getDeclaringClass().getName())
                        .append("。方法入参请求结果：").append(AnnotationArgsUtils.argsArrayToString(joinPoint.getArgs()))
                        .append("。方法返回结果：").append(JSONObject.toJSONString(result))
                        .append("。耗时时长：").append(elapsedTime).append("ms").toString();
                log.info(stringBuffer);
                return result;
             }
            }

        return joinPoint.proceed();

    }

    /**
     * 在目标方法成功执行并返回结果之后执行。
     */
    @AfterReturning("annotationPointcut()")
    public void doAfterReturning(JoinPoint joinPoint) {
    }

    /**
     * 在抛出异常时使用
     */
    @AfterThrowing("annotationPointcut()")
    public void afterThrowing(JoinPoint joinPoint){
}

    /**
     *在目标方法执行完成之后执行，无论该方法是否成功返回或抛出异常。
     * @param joinPoint
     */
    @After("annotationPointcut()")
    public void after(JoinPoint joinPoint){
    }

    private SpelExpressionParser parserSpel = new SpelExpressionParser();
    private DefaultParameterNameDiscoverer parameterNameDiscoverer= new DefaultParameterNameDiscoverer();


    public String generateKeyBySpEL(String key, ProceedingJoinPoint pjp) {
        Expression expression = parserSpel.parseExpression(key);
        EvaluationContext context = new StandardEvaluationContext();
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Object[] args = pjp.getArgs();
        String[] paramNames = parameterNameDiscoverer.getParameterNames(methodSignature.getMethod());
        for(int i = 0 ; i < args.length ; i++) {
            context.setVariable(paramNames[i], args[i]);
        }
        return expression.getValue(context).toString();
    }



}
