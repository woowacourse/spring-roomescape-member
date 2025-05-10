package roomescape.auth.annotation;

import org.springframework.core.annotation.AliasFor;
import roomescape.user.domain.UserRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiredRoles {

    @AliasFor("userRole")
    UserRole[] value() default {};

    @AliasFor("value")
    UserRole[] userRole() default {};
}
