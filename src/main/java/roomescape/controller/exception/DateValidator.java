package roomescape.controller.exception;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.format.annotation.DateTimeFormat;

public class DateValidator implements ConstraintValidator<DateValid, String> {

    private DateTimeFormatter formatter;

    @Override
    public boolean isValid(String stringDate, ConstraintValidatorContext constraintValidatorContext) {
        try {
            LocalDate.parse(stringDate, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void initialize(DateValid constraintAnnotation) {
        formatter = DateTimeFormatter.ofPattern(constraintAnnotation.format());
    }
}
