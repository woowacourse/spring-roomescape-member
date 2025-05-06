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

    private Long id;
    private final String name;
    private final LocalDate date;
    private final TimeSlot timeSlot;
    private final Theme theme;

    private Reservation(final Long id, final String name, final LocalDate date, final TimeSlot timeSlot, final Theme theme) {
        validateNameLength(name);
        this.id = id;
        this.name = name;
        this.date = date;
        this.timeSlot = timeSlot;
        this.theme = theme;
    }

    public Reservation withId(final long id) {
        if (this.id == null) {
            this.id = id;
            return this;
        }
        throw new IllegalStateException("예약 ID는 재할당할 수 없습니다. 현재 ID: " + this.id);
    }

    public boolean isDateEquals(final LocalDate date) {
        return this.date.isEqual(date);
    }

    public boolean isTimeSlotEquals(final TimeSlot timeSlot) {
        return this.timeSlot.isSameAs(timeSlot);
    }

    public static Reservation ofExisting(final long id, final String name, final LocalDate date, final TimeSlot timeSlot, final Theme theme) {
        return new Reservation(id, name, date, timeSlot, theme);
    }

    public static Reservation reserveNewly(final String name, final LocalDate date, final TimeSlot timeSlot, final Theme theme) {
        if (isBeforeNow(date, timeSlot)) {
            throw new IllegalArgumentException("이전 날짜로 예약할 수 없습니다.");
        }
        return new Reservation(null, name, date, timeSlot, theme);
    }

    private void validateNameLength(final String name) {
        if (name.isBlank() || name.length() > NAME_MAX_LENGTH) {
            throw new IllegalArgumentException(String.format("이름은 공백이거나 %d자를 넘길 수 없습니다.", NAME_MAX_LENGTH));
        }
    }

    private static boolean isBeforeNow(final LocalDate date, final TimeSlot timeSlot) {
        var now = LocalDateTime.now();
        var today = now.toLocalDate();
        var timeNow = now.toLocalTime();
        return date.isBefore(today)
               || (date.isEqual(today) && timeSlot.isTimeBefore(timeNow));
    }
}

