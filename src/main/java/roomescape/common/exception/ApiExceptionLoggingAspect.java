package roomescape.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ApiExceptionLoggingAspect {

    @AfterReturning(
            pointcut = "within(roomescape.common.exception.DomainExceptionHandler) || " +
                    "within(roomescape.common.exception.CustomSpringMvcExceptionHandler)",
            returning = "response"
    )
    public void logApiException(JoinPoint joinPoint, Object response) {
        if (!(response instanceof ResponseEntity<?> responseEntity)) {
            return;
        }
        if (!(responseEntity.getBody() instanceof ErrorResponse errorResponse)) {
            return;
        }

        HttpServletRequest request = findHttpServletRequest(joinPoint.getArgs());
        if (request == null) {
            return;
        }

        log.error("Exception in API: URI:{}, StatusCode:{}, Error Code:{}, ErrorMessage:{}",
                request.getMethod() + " " + request.getRequestURI(),
                responseEntity.getStatusCode(),
                errorResponse.getCode(),
                errorResponse.getMessage());
    }

    private HttpServletRequest findHttpServletRequest(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest request) {
                return request;
            }
        }
        return null;
    }
}
