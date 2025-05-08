package roomescape.domain.reservation;

import java.time.LocalDate;
import java.util.Objects;

public class ReservationDate {
    private final LocalDate date;

    public ReservationDate(LocalDate date) {
        this.date = Objects.requireNonNull(date, "date는 null일 수 없습니다.");
    }

    public ReservationDate(String date) {
        this.date = LocalDate.parse(Objects.requireNonNull(date, "date는 null일 수 없습니다."));
    }

    public LocalDate getDate() {
        return date;
    }
}
