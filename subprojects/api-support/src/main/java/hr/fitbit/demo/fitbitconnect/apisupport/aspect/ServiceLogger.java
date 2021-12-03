package hr.fitbit.demo.fitbitconnect.apisupport.aspect;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class ServiceLogger {

    /**
     * Targets all methods in services tagged with @Transactional
     */
    private final static String SERVICE_TRANSACTIONAL_METHOD_POINTCUT_EXPRESSION =
            "execution(public * hr.fitbit.demo.fitbitconnect.users.service.UserService.*(..)) && @annotation(org.springframework.transaction.annotation.Transactional) || " +
                    "execution(public * hr.fitbit.demo.fitbitconnect.fitbitclient.service.FitbitService.*(..)) && @annotation(org.springframework.transaction.annotation.Transactional)";

    @Pointcut(SERVICE_TRANSACTIONAL_METHOD_POINTCUT_EXPRESSION)
    public void serviceTransactional() {
    }

    @Around("serviceTransactional()")
    public Object measureMethodExecutionTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        final long start = System.currentTimeMillis();
        // -----------------------------------------------//
        final Object result = proceedingJoinPoint.proceed();
        // -----------------------------------------------//
        final long end = System.currentTimeMillis() - start;
        final String methodName = proceedingJoinPoint.getSignature().getName();
        final String args = Arrays.toString(proceedingJoinPoint.getArgs());

        log.info("#{}({}): => {} in {} milliseconds", methodName, args, result, end);
        return result;
    }

}

