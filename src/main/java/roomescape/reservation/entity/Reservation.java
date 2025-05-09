package roomescape.reservation.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Reservation {

    private final Long id;
    private String name;
    private LocalDate date;
    private ReservationTime time;
    private Long themeId;

    public boolean isDuplicatedWith(Reservation other) {
        return date.isEqual(other.date) && time.isDuplicatedWith(other.time);
    }

    public LocalDateTime getDateTime() {
        return LocalDateTime.of(date, time.getStartAt());
    }
}
