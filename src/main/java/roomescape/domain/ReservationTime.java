package roomescape.domain;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ReservationTime {

    @EqualsAndHashCode.Include
    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(final Long id, final LocalTime startAt) {
        validateNotNulls(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTime(final LocalTime startAt) {
        this(null, startAt);
    }

    public ReservationTime(final String input) {
        this(null, parseTime(input));
    }

    public ReservationTime assignId(final Long id) {
        return new ReservationTime(id, startAt);
    }

    public boolean isSameTime(final ReservationTime other) {
        return this.startAt.equals(other.startAt);
    }

    private void validateNotNulls(LocalTime startAt) {
        Objects.requireNonNull(startAt, "시작 시간을 입력 해 주세요.");
    }

    private static LocalTime parseTime(final String input) {
        try {
            return LocalTime.parse(input);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("[ERROR] 시간 형식을 HH:mm 으로 수정해 주세요");
        }
    }
}
