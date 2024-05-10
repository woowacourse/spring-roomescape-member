package roomescape.core.domain;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import roomescape.web.exception.BadRequestException;

public class ReservationTime {
    private static final String TIME_FORMAT = "HH:mm";

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(final String startAt) {
        this(null, startAt);
    }

    public ReservationTime(final Long id, final String startAt) {
        this(id, parseStartAt(startAt));
    }

    public ReservationTime(final Long id, final LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    private static LocalTime parseStartAt(final String startAt) {
        if (startAt == null) {
            throw new BadRequestException("시간은 null일 수 없습니다.");
        }
        try {
            return LocalTime.parse(startAt);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("시간 형식이 잘못되었습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    public String getStartAtString() {
        return startAt.format(DateTimeFormatter.ofPattern(TIME_FORMAT));
    }

    public boolean isPast() {
        return startAt.isBefore(LocalTime.now());
    }
}
