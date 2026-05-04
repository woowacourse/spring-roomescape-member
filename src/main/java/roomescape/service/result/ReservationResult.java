package roomescape.service.result;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import roomescape.domain.Reservation;

public record ReservationResult(long id, String name, LocalDate date, ReservationTimeResult time) {

    public static ReservationResult from(Reservation reservation) {
        requireNonNull(reservation, "변환할 예약 엔티티가 null입니다.");
        return new ReservationResult(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                ReservationTimeResult.from(reservation.getTime())
        );
    }
}
