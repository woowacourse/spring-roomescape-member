package roomescape.domain;

import org.springframework.dao.DataRetrievalFailureException;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

public class ReservationTime {

    private static final Pattern TIME_PATTERN = Pattern.compile("^\\d{2}:\\d{2}$");

    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(Long id, LocalTime startAt) {
        this.id = id;
        this.startAt = Objects.requireNonNull(startAt, "시작 시간은 필수입니다.");
    }

    public ReservationTime(Long id, String startAt) {
        this.id = id;
        this.startAt = translateTime(startAt);
    }

    public ReservationTime(LocalTime startAt) {
        this(null, startAt);
    }

    public ReservationTime(String startAt) {
        this(null, startAt);
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    private LocalTime translateTime(String startAt) {
        if (!TIME_PATTERN.matcher(startAt).matches()) {
            throw new IllegalArgumentException("시간 형식이 HH:mm (예: 09:30) 형태여야 합니다: " + startAt);
        }

        try {
            return LocalTime.parse(startAt);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("유효하지 않은 시간 값입니다: " + startAt);
        }
    }

    public ReservationTime withId(Long id) {
        return new ReservationTime(id, this.startAt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationTime that = (ReservationTime) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
