package roomescape.web.exception;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

class TimeValidator implements ConstraintValidator<TimeValid, String> {

    private DateTimeFormatter formatter;

    @Override
    public void initialize(TimeValid constraintAnnotation) {
        formatter = DateTimeFormatter.ofPattern(constraintAnnotation.format());
    }

    @Override
    public boolean isValid(String strDate, ConstraintValidatorContext context) {
        try {
            LocalTime.parse(strDate, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
