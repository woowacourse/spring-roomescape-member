package roomescape.domain.reservation.validator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import roomescape.domain.global.exception.custom.BadRequestException;
import roomescape.domain.global.exception.error.ErrorCode;
import roomescape.domain.global.exception.error.ErrorDetail;

public final class ReservationUpdateRequestValidator {

    private ReservationUpdateRequestValidator() {
    }

    public static void validate(LocalDate date, Long timeId) {
        List<ErrorDetail> errors = new ArrayList<>();
        validateDate(date, errors);
        validateTimeId(timeId, errors);

        if (!errors.isEmpty()) {
            throw new BadRequestException(ErrorCode.COMMON_INVALID_REQUEST_BODY, errors);
        }
    }

    private static void validateTimeId(Long timeId, List<ErrorDetail> errors) {
        if (timeId == null) {
            errors.add(ErrorDetail.of("timeId", "timeId가 누락되었습니다."));
        }
    }

    private static void validateDate(LocalDate date, List<ErrorDetail> errors) {
        if (date == null) {
            errors.add(ErrorDetail.of("date", "date가 누락되었습니다."));
        }
    }
}
