package roomescape.exception;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

@Constraint(validatedBy = DateTimeFormatValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateTimeFormat {

    String message() default "날짜/시간 형식이 올바르지 않습니다.";

    @AliasFor("value")
    String pattern();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
