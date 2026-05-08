package roomescape.reservation.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class Schedule {
    private final Long id;
    private LocalDate date;
    private ReservationTime time;
    private Theme theme;
}
