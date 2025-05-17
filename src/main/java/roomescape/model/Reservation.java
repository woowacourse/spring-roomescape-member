package roomescape.model;

import java.time.LocalDate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@EqualsAndHashCode(of = {"id"})
@Getter
@Accessors(fluent = true)
@ToString
public class Reservation {

    private final Long id;
    private final Member member;
    private final LocalDate date;
    private final TimeSlot timeSlot;
    private final Theme theme;

    public Reservation(final Long id, final Member member, final LocalDate date, final TimeSlot timeSlot,
                       final Theme theme) {
        this.id = id;
        this.member = member;
        this.date = date;
        this.timeSlot = timeSlot;
        this.theme = theme;
    }

    public Reservation(final Member member, final LocalDate date, final TimeSlot timeSlot, final Theme theme) {
        this(null, member, date, timeSlot, theme);
    }

    public boolean isSameDateTime(final Reservation reservation) {
        return this.date.isEqual(reservation.date()) && this.timeSlot.isSameTimeSlot(reservation.timeSlot());
    }

    public boolean isSameTheme(final Reservation reservation) {
        return this.theme.equals(reservation.theme());
    }
}
