package roomescape.domain;

import roomescape.domain.exception.InvalidDateException;
import roomescape.domain.exception.InvalidRequestException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class Reservation {
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(final Long id, final String name, final LocalDate date,
                       final ReservationTime time, final Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(final Long id, final String name, final String date,
                       final ReservationTime time, final Theme theme) {
        validateNull(name);
        validateNull(date);
        this.id = id;
        this.name = name;
        this.date = validateFormatAndConvert(date);
        this.time = time;
        this.theme = theme;
    }

    private LocalDate validateFormatAndConvert(final String date) {
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException exception) {
            throw new InvalidDateException("유효하지 않은 날짜입니다.");
        }
    }

    private void validateNull(final String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidRequestException("공백일 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    @Override
    public boolean equals(final Object target) {
        if (this == target) {
            return true;
        }
        if (target == null || getClass() != target.getClass()) {
            return false;
        }
        final Reservation that = (Reservation) target;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", theme=" + theme +
                '}';
    }
}
