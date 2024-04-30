package roomescape.domain;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class ReservationTime {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, String startAt) {
        this.id = id;
        this.startAt = toLocalTime(startAt);
    }

    private ReservationTime(Long id, LocalTime startAt) {
        this.id = id;
        this.startAt = Objects.requireNonNull(startAt, "인자 중 null 값이 존재합니다.");
    }

    private static LocalTime toLocalTime(String time) {
        if (time == null) {
            return null;
        }

        try {
            return LocalTime.parse(time, TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("시간(%s)이 HH:mm 형식에 맞지 않습니다.".formatted(time));
        }
    }

    public ReservationTime changeId(Long id) {
        return new ReservationTime(id, this.startAt);
    }

    public boolean isAfter(LocalTime currentTime) {
        return startAt.isAfter(currentTime);
    }

    public Long getId() {
        return id;
    }

    public String getStartAt() {
        return TIME_FORMATTER.format(startAt);
    }
}
