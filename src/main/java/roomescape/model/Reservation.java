package roomescape.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@EqualsAndHashCode(of = {"id"})
@Getter
@Accessors(fluent = true)
@ToString
public class Reservation {

    private static final int NAME_MAX_LENGTH = 5;

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final TimeSlot timeSlot;

    private Reservation(final Long id, final String name, final LocalDate date, final TimeSlot timeSlot) {
        validateNotNull(name, date, timeSlot);
        validateNameLength(name);
        this.id = id;
        this.name = name;
        this.date = date;
        this.timeSlot = timeSlot;
    }

    public boolean isBefore(final LocalDateTime dateTime) {
        var date = dateTime.toLocalDate();
        var time = dateTime.toLocalTime();
        return this.date.isBefore(date)
            || (this.date.isEqual(date) && this.timeSlot.isBefore(time));
    }

    public static Reservation register(final Long id, final String name, final LocalDate date, final TimeSlot timeSlot) {
        return new Reservation(id, name, date, timeSlot);
    }

    public static Reservation create(final String name, final LocalDate date, final TimeSlot timeSlot) {
        return new Reservation(null, name, date, timeSlot);
    }

    public boolean isSameDateTime(final Reservation reservation) {
        return this.date.isEqual(reservation.date()) && this.timeSlot.isSameTimeSlot(reservation.timeSlot());
    }

    private void validateNotNull(
        final String name,
        final LocalDate date,
        final TimeSlot timeSlot
    ) {
        if (name == null || date == null || timeSlot == null) {
            throw new IllegalArgumentException("모든 값들이 존재해야 합니다.");
        }
    }

    private void validateNameLength(final String name) {
        if (name.length() > NAME_MAX_LENGTH) {
            throw new IllegalArgumentException("이름은 5자를 넘길 수 없습니다.");
        }
    }
}

