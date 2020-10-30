package hr.fitbit.demo.fitbitconnect.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class ServiceLogger {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceLogger.class);

    /**
     * Targets all methods in services tagged with @Transactional
     */
    private final static String SERVICE_TRANSACTIONAL_METHOD_POINTCUT_EXPRESSION =
            "execution(public * hr.fitbit.demo.fitbitconnect.service.UserService.*(..)) && @annotation(org.springframework.transaction.annotation.Transactional) || " +
                    "execution(public * hr.fitbit.demo.fitbitconnect.service.FitbitService.*(..)) && @annotation(org.springframework.transaction.annotation.Transactional)";

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

        LOG.info("#{}({}): => {} in {} milliseconds", methodName, args, result, end);
        return result;
    }

}

