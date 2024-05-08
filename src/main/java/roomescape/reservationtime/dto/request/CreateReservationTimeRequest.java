package roomescape.reservationtime.dto.request;

import java.time.LocalTime;
import roomescape.reservationtime.model.ReservationTime;

public record CreateReservationTimeRequest(LocalTime startAt) {
    public CreateReservationTimeRequest {
        if (startAt == null) {
            throw new IllegalArgumentException("올바른 시간을 입력해주세요.");
        }
    }

    public ReservationTime toReservationTime() {
        return new ReservationTime(
                null,
                startAt);
    }
}
