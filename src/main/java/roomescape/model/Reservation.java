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

    private static final int NAME_MAX_LENGTH = 5;

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final TimeSlot timeSlot;

    public Reservation(final Long id, final String name, final LocalDate date,
        final TimeSlot timeSlot) {
        validateNotNull(name, date, timeSlot);
        validateNameLength(name);
        this.id = id;
        this.name = name;
        this.date = date;
        this.timeSlot = timeSlot;
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

