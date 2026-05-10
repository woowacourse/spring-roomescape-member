package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Getter;

@Getter
public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTime(LocalTime startAt) {
        this(null, startAt);
    }

    public boolean isPast(LocalDate date, LocalDateTime now) {
        LocalDateTime reservationDateTime = LocalDateTime.of(date, startAt);
        return reservationDateTime.isBefore(now);
    }
}
