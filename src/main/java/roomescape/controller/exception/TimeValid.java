package roomescape.controller.exception;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = TimeValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeValid {

    String message() default "올바른 시간 형식이 아닙니다.";

    String format() default "HH:mm";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
