package roomescape.domain.reservation.dto.request;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import roomescape.domain.global.exception.BadRequestException;
import roomescape.domain.global.exception.ErrorCode;
import roomescape.domain.global.exception.ErrorDetail;

public record ReservationUpdateRequestDto(LocalDate date, Long timeId) {

    public ReservationUpdateRequestDto {
        validate(date, timeId);
    }

    private void validate(LocalDate date, Long timeId) {
        List<ErrorDetail> errors = new ArrayList<>();
        if (date == null) {
            errors.add(ErrorDetail.of("date", "date가 누락되었습니다."));
        }
        if (timeId == null) {
            errors.add(ErrorDetail.of("timeId", "timeId가 누락되었습니다."));
        }
        if (!errors.isEmpty()) {
            throw new BadRequestException(ErrorCode.COMMON_INVALID_REQUEST_BODY, errors);
        }
    }
}
