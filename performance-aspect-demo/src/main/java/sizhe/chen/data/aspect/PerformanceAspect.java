package sizhe.chen.data.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @Author: sizhe.chen
 * @Date: Create in 6:13 下午 2022/4/23
 * @Description:
 * @Modified:
 * @Version:
 */
@Slf4j
@Aspect
@Component
public class PerformanceAspect {
    @Pointcut("execution(* sizhe.chen.data.repository..*(..))")
    public  void repositoryOps(){

    }

    @Around("repositoryOps()")
    public Object logPerformance(ProceedingJoinPoint pjp) throws  Throwable{
        long startTime = System.currentTimeMillis();
        String name = "-";
        String result = "Y";

        try{
            name = pjp.getSignature().toShortString();
            return pjp.proceed();
        }catch (Throwable t){
            result = "N";
            throw t;
        }finally {
            long endTime = System.currentTimeMillis();
            log.info("{};{};{}ms", name, result, endTime - startTime);
        }
    }
}
