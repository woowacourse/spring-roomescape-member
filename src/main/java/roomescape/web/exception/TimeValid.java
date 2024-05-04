package roomescape.web.exception;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = TimeValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeValid {

    String message() default "올바른 시간 형태가 아닙니다.";

    String format() default "HH:mm";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
