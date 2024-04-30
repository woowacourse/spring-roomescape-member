package roomescape.domain;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.function.UnaryOperator;

public class ReservationTime {

    private static final UnaryOperator<String> invalidErrorFormat =
            (value) -> String.format("%s 는 유효하지 않은 값입니다.(EX: 10:00)", value);
    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTime from(Long id, String startAt) {
        validate(startAt);
        try {
            return new ReservationTime(id, LocalTime.parse(startAt));
        } catch (DateTimeException exception) {
            throw new IllegalArgumentException(invalidErrorFormat.apply(startAt));
        }
    }


    private static void validate(final String startAt) {
        if (startAt.isEmpty()) {
            throw new IllegalArgumentException(invalidErrorFormat.apply(startAt));
        }
    }

    public Long getId() {
        return id;
    }

    public String getStartAt() {
        return startAt.toString();
    }
}
