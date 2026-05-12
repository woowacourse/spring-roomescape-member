package roomescape.presentation.dto;

import java.time.LocalTime;
import org.springframework.http.HttpStatus;
import roomescape.global.exception.BusinessException;

public record ReservationTimeRequest(
        LocalTime startAt
) {
    public ReservationTimeRequest {
        validatStartAtNotEmpty(startAt);
    }

    private void validatStartAtNotEmpty(LocalTime startAt) {
        if (startAt == null) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "예약 시간이 비어있습니다.");
        }
    }
}
