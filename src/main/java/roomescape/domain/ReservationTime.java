package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import roomescape.exceptions.UserException;

public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;
    private final Boolean alreadyBooked;

    public ReservationTime(Long id, LocalTime startAt, Boolean alreadyBooked) {
        validate(startAt);
        this.id = id;
        this.startAt = startAt;
        this.alreadyBooked = alreadyBooked;
    }

    public ReservationTime(Long id, LocalTime startAt) {
        this(id, startAt, null);
    }

    public ReservationTime(LocalTime startAt) {
        this(null, startAt, null);
    }

    private void validate(LocalTime startAt) {
        if (startAt == null) {
            throw new UserException("시작 시간은 필수 값입니다.");
        }
    }

    public boolean isBeforeNow(LocalDate date) {
        LocalDateTime dateTimeToReserve = LocalDateTime.of(date, startAt);
        LocalDateTime now = LocalDateTime.now();

        return dateTimeToReserve.isBefore(now);
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    public String getStartAt(DateTimeFormatter formatter) {
        return startAt.format(formatter);
    }

    public Boolean getAlreadyBooked() {
        return alreadyBooked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReservationTime that = (ReservationTime) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
