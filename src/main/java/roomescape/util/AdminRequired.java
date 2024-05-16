package roomescape.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;
import roomescape.member.domain.Role;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminRequired {

    Role value() default Role.ADMIN;

    @AliasFor("value")
    Role role() default Role.ADMIN;
}
