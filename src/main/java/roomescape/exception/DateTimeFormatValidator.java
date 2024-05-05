package roomescape.exception;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.format.DateTimeFormatter;

public class DateTimeFormatValidator implements ConstraintValidator<DateTimeFormat, String> {
    private DateTimeFormatter formatter;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        try {
            formatter.parse(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void initialize(DateTimeFormat constraintAnnotation) {
        formatter = DateTimeFormatter.ofPattern(constraintAnnotation.pattern());
    }
}
