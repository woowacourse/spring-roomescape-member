package roomescape.auth.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import roomescape.auth.annotation.AuthenticatedUser;
import roomescape.auth.annotation.RequiredRoles;
import roomescape.auth.exception.ForbiddenException;
import roomescape.auth.resolver.UserSession;
import roomescape.user.domain.UserRole;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

@Aspect
@Component
@RequiredArgsConstructor
public class RequiredRoleAspect {

    @Around("@annotation(requiredRoles)")
    public Object checkMethodLevel(final ProceedingJoinPoint joinPoint,
                                   final RequiredRoles requiredRoles) throws Throwable {
        return check(joinPoint, requiredRoles.value());
    }

    @Around("@within(requiredRoles)")
    public Object checkClassLevel(final ProceedingJoinPoint joinPoint,
                                  final RequiredRoles requiredRoles) throws Throwable {
        return check(joinPoint, requiredRoles.value());
    }

    private Object check(final ProceedingJoinPoint joinPoint,
                         final UserRole[] requiredRoles) throws Throwable {

        final UserSession session = extractUserSession(joinPoint);

        final boolean authorized = Arrays.stream(requiredRoles)
                .anyMatch(session.role()::includes);

        if (authorized) {
            return joinPoint.proceed();
        }

        throw new ForbiddenException(session.id(), session.role(), List.of(requiredRoles));
    }

    private UserSession extractUserSession(final ProceedingJoinPoint joinPoint) {
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final Object[] args = joinPoint.getArgs();
        final Annotation[][] paramAnnotations = signature.getMethod().getParameterAnnotations();

        for (int i = 0; i < args.length; i++) {
            final Object arg = args[i];

            final boolean hasAnnotation = Arrays.stream(paramAnnotations[i])
                    .anyMatch(annotation -> annotation.annotationType().equals(AuthenticatedUser.class));

            if (hasAnnotation && arg instanceof final UserSession session) {
                return session;
            }
        }

        throw new IllegalStateException("Required @AuthenticatedUser UserSession not found. Check resolver or handler method.");
    }
}
