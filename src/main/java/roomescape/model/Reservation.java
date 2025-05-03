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

    private static final int NAME_MAX_LENGTH = 5;

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final TimeSlot timeSlot;
    private final Theme theme;

    protected Reservation(final Long id, final String name, final LocalDate date, final TimeSlot timeSlot, final Theme theme) {
        validateNameLength(name);
        this.id = id;
        this.name = name;
        this.date = date;
        this.timeSlot = timeSlot;
        this.theme = theme;
    }

    public boolean isDateEquals(final LocalDate date) {
        return this.date.isEqual(date);
    }

    public boolean isTimeSlotEquals(final TimeSlot timeSlot) {
        return this.timeSlot.isSameAs(timeSlot);
    }

    public static Reservation of(final long id, final String name, final LocalDate date, final TimeSlot timeSlot, final Theme theme) {
        return new Reservation(id, name, date, timeSlot, theme);
    }

    public static Reservation reserveNewly(final String name, final LocalDate date, final TimeSlot timeSlot, final Theme theme) {
        return new NewReservation(name, date, timeSlot, theme);
    }

    private void validateNameLength(final String name) {
        if (name.isBlank() || name.length() > NAME_MAX_LENGTH) {
            throw new IllegalArgumentException(String.format("이름은 공백이거나 %d자를 넘길 수 없습니다.", NAME_MAX_LENGTH));
        }
    }

    private static final class NewReservation extends Reservation {

        private NewReservation(
            final String name,
            final LocalDate date,
            final TimeSlot timeSlot,
            final Theme theme
        ) {
            super(null, name, date, timeSlot, theme);
            if (isBeforeNow(date, timeSlot)) {
                throw new IllegalArgumentException("이전 날짜로 예약할 수 없습니다.");
            }
        }

        private boolean isBeforeNow(final LocalDate date, final TimeSlot timeSlot) {
            var now = LocalDateTime.now();
            var today = now.toLocalDate();
            var timeNow = now.toLocalTime();
            return date.isBefore(today)
                   || (date.isEqual(today) && timeSlot.isTimeBefore(timeNow));
        }
    }
}

