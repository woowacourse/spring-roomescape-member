package roomescape.service.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class TimeFormatValidator implements ConstraintValidator<TimeFormat, String> {
    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        try {
            LocalTime.parse(value);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
