package roomescape.web.exception;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

class DateValidator implements ConstraintValidator<DateValid, String> {

    private DateTimeFormatter formatter;

    @Override
    public void initialize(DateValid constraintAnnotation) {
        formatter = DateTimeFormatter.ofPattern(constraintAnnotation.format());
    }

    @Override
    public boolean isValid(String strDate, ConstraintValidatorContext context) {
        if (strDate == null) {
            return true;
        }

        try {
            LocalDate.parse(strDate, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
