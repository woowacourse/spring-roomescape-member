package roomescape.web.exception;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateValidator implements ConstraintValidator<DateValid, String> {
    private DateTimeFormatter formatter;

    @Override
    public void initialize(DateValid constraintAnnotation) {
        formatter = DateTimeFormatter.ofPattern(constraintAnnotation.format());
    }

    @Override
    public boolean isValid(String strDate, ConstraintValidatorContext context) {
        try {
            LocalDate.parse(strDate, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
