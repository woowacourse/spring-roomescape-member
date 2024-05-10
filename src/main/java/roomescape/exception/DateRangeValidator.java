package roomescape.exception;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.temporal.ChronoUnit;
import roomescape.annotaions.ValidDateRange;
import roomescape.reservation.dto.ReservationSearchCondRequest;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, ReservationSearchCondRequest> {

    public static final int LIMIT_DAY = 30;

    @Override
    public boolean isValid(ReservationSearchCondRequest request, ConstraintValidatorContext context) {
        System.out.println(request);
        return ChronoUnit.DAYS.between(request.dateFrom(), request.dateTo()) <= LIMIT_DAY;
    }
}
