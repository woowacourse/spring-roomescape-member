package roomescape.domain.reservationtime.dto;

import java.time.LocalTime;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.support.exception.BadRequestException;
import roomescape.support.exception.ReservationTimeErrorCode;

public record CreateTimeRequest(
    LocalTime startAt
) {

    public void validate() {
        if (startAt == null) {
            throw new BadRequestException(ReservationTimeErrorCode.INVALID_RESERVATION_TIME);
        }
    }

    public ReservationTime toEntity() {
        return ReservationTime.createWithoutId(startAt);
    }
}
