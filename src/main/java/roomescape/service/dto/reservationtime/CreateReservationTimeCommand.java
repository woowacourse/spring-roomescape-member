package roomescape.service.dto.reservationtime;

import java.time.LocalTime;
import roomescape.global.exception.InvalidReservationTimeException;

public record CreateReservationTimeCommand(
        LocalTime startAt
) {

    public CreateReservationTimeCommand {
        if (startAt == null) {
            throw new InvalidReservationTimeException("예약 시간은 필수입니다.");
        }
    }
}
