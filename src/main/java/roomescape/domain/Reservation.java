package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Reservation {

    @EqualsAndHashCode.Include
    private final Long id;
    private final LocalDate date;
    private final Member member;
    private final ReservationTime time;
    private final ReservationTheme theme;

    public Reservation(final Long id, final LocalDate date, final Member member, final ReservationTime time,
                       final ReservationTheme theme) {

        this.id = id;
        this.date = date;
        this.member = member;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(final LocalDate date, final Member member, final ReservationTime time,
                       final ReservationTheme theme) {
        this(null, date, member, time, theme);
    }

    public Reservation assignId(final Long id) {
        return new Reservation(id, date, member, time, theme);
    }

    public boolean isDuplicateReservation(Reservation reservation) {
        return this.toDateTime().equals(reservation.toDateTime());
    }

    public LocalDateTime toDateTime() {
        return LocalDateTime.of(date, time.getStartAt());
    }
}
