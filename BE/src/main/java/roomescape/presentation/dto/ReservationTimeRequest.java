package roomescape.presentation.dto;

import java.time.LocalTime;
import roomescape.global.exception.EntityNotFoundException;

public record ReservationTimeRequest(
        LocalTime startAt
) {
    public ReservationTimeRequest {
        validateStartAtNotEmpty(startAt);
    }

    private void validateStartAtNotEmpty(LocalTime startAt) {
        if (startAt == null) {
            throw new EntityNotFoundException("예약 시간을 입력해 주세요. startAt: %s"
                    .formatted(startAt)
            );
        }
    }
}
