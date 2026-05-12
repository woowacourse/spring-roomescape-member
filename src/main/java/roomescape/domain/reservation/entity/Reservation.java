package roomescape.domain.reservation.entity;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Objects;
import roomescape.domain.global.exception.ErrorCode;
import roomescape.domain.global.exception.UnprocessableEntityException;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.time.entity.Time;

public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final Time time;
    private final Theme theme;

    private Reservation(Long id, String name, LocalDate date, Time time, Theme theme, Clock clock) {
        if (clock != null) {
            validateDateTime(date, time, clock);
        }
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static Reservation create(String name, LocalDate date, Time time, Theme theme, Clock clock) {
        return new Reservation(null, name, date, time, theme, clock);
    }

    public static Reservation reconstruct(Long id, String name, LocalDate date, Time time, Theme theme) {
        return new Reservation(id, name, date, time, theme, null);
    }

    private void validateDateTime(LocalDate date, Time time, Clock clock) {
        LocalDate nowDate = LocalDate.now(clock);

        if (date.isBefore(nowDate) || (date.isEqual(nowDate) && time.isPast(clock))) {
            throw new UnprocessableEntityException(ErrorCode.RESERVATION_INVALID_DATETIME);
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

    public Time getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
