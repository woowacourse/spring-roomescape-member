package roomescape.reservation.domain;

import java.time.LocalDate;
import java.util.Objects;

public class ReservationDate {

    private final LocalDate reservationDate;

    public ReservationDate(LocalDate reservationDate) {
        this.reservationDate = Objects.requireNonNull(
                reservationDate,
                "reservationDate는 null일 수 없습니다."
        );
    }

    public LocalDate getDate() {
        return reservationDate;
    }
}
