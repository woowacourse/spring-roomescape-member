package roomescape.reservation.domain;

import java.time.LocalDate;
import java.util.Objects;
import roomescape.exception.BusinessException;
import roomescape.exception.ErrorType;

public class Reservation {
    private Long id;
    private LocalDate date;
    private ReservationTime time;
    private Theme theme;

    public Reservation(Long id, LocalDate date, ReservationTime time, Theme theme) {
        validate(date, time);
        this.id = id;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(LocalDate date, ReservationTime time, Theme theme) {
        this(null, date, time, theme);
    }

    private void validate(LocalDate date, ReservationTime time) {
        if (date == null || time == null) {
            throw new BusinessException(ErrorType.MISSING_REQUIRED_VALUE_ERROR);
        }
    }

    public boolean isSame(LocalDate date, ReservationTime time, Theme theme) {
        return this.date.equals(date) && this.time.equals(time) && this.theme.equals(theme);
    }

    public Long getId() {
        return id;
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
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reservation that = (Reservation) o;
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
                ", date=" + date +
                ", time=" + time +
                ", theme=" + theme +
                '}';
    }
}
