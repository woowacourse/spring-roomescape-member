package roomescape.domain.reservation.entity;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Objects;
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
            validateDate(date, clock);
            validateTime(date, time, clock);
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

    private void validateDate(LocalDate date, Clock clock) {
        LocalDate nowDate = LocalDate.now(clock);

        if (date.isBefore(nowDate)) {
            throw new IllegalArgumentException("이전 날짜로 예약할 수 없습니다.");
        }
    }

    private void validateTime(LocalDate date, Time time, Clock clock) {
        LocalDate nowDate = LocalDate.now(clock);

        if (date.isEqual(nowDate) && time.isPast(clock)) {
            throw new IllegalArgumentException("이전 시간으로 예약할 수 없습니다.");
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
