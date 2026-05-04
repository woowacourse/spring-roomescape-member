package roomescape.service.result;

import static java.util.Objects.requireNonNull;

import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record ReservationTimeResult(long id, LocalTime startAt) {

    public static ReservationTimeResult from(ReservationTime reservationTime) {
        requireNonNull(reservationTime, "변환할 예약 시간 엔티티가 null입니다.");
        return new ReservationTimeResult(reservationTime.getId(), reservationTime.getStartAt());
    }
}
