package roomescape.domain.reservationtime.dto;

import java.time.LocalTime;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.support.exception.ReservationTimeErrorCode;
import roomescape.support.exception.RoomescapeException;

public record TimeCreationRequest(
    LocalTime startAt
) {

    public void validate() {
        if (startAt == null) {
            throw new RoomescapeException(ReservationTimeErrorCode.INVALID_RESERVATION_TIME);
        }
    }

    public ReservationTime toEntity() {
        return ReservationTime.createWithoutId(startAt);
    }
}
