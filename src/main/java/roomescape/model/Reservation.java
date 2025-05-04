package roomescape.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final String name;
    private final LocalDate date;
    private final TimeSlot timeSlot;
    private final Theme theme;

    public Reservation(final Long id, final String name, final LocalDate date, final TimeSlot timeSlot, final Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.timeSlot = timeSlot;
        this.theme = theme;
    }

    public Reservation(final String name, final LocalDate date, final TimeSlot timeSlot, final Theme theme) {
        this(null, name, date, timeSlot, theme);
    }

    public boolean isBefore(final LocalDateTime dateTime) {
        var date = dateTime.toLocalDate();
        var time = dateTime.toLocalTime();
        return this.date.isBefore(date)
                || (this.date.isEqual(date) && this.timeSlot.isBefore(time));
    }

    public boolean isSameDateTime(final Reservation reservation) {
        return this.date.isEqual(reservation.date()) && this.timeSlot.isSameTimeSlot(reservation.timeSlot());
    }
}
