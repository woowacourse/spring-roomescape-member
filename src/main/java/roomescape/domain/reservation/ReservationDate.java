package roomescape.domain.reservation;

import java.time.LocalDate;
import java.util.Objects;

public record ReservationDate(LocalDate date) {

    public ReservationDate(LocalDate date) {
        this.date = Objects.requireNonNull(date, "date는 null일 수 없습니다.");
    }
}
