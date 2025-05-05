package roomescape.domain;

import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Reservation {

    @EqualsAndHashCode.Include
    private Long id;
    @Size(max = 5, message = "이름은 다섯글자 이내로 입력해 주세요.")
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final ReservationTheme theme;

    public Reservation(final String name,
                       final LocalDate date,
                       final ReservationTime time,
                       final ReservationTheme theme) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation assignId(final Long id) {
        this.id = id;
        return this;
    }

    public boolean isDuplicateReservation(Reservation reservation) {
        return this.toDateTime().equals(reservation.toDateTime());
    }

    public LocalDateTime toDateTime() {
        return LocalDateTime.of(date, time.getStartAt());
    }
}
