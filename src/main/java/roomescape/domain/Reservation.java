package roomescape.domain;

import java.time.LocalDate;
import java.util.Objects;
import org.springframework.util.StringUtils;
import roomescape.exception.InvalidReservationException;

public class Reservation {
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    private Reservation(
            Long id,
            String name,
            LocalDate date,
            ReservationTime time,
            Theme theme
    ) {
        validateName(name);
        validateDate(date);
        validateTime(time);
        validateTheme(theme);

        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static Reservation create(
            String name,
            LocalDate date,
            ReservationTime time,
            Theme theme
    ) {
        return new Reservation(
                null,
                name,
                date,
                time,
                theme
        );
    }

    public static Reservation retrieve(
            long id,
            String name,
            LocalDate date,
            ReservationTime time,
            Theme theme
    ) {
        return new Reservation(
                id,
                name,
                date,
                time,
                theme
        );
    }

    public Reservation with(long id) {
        return new Reservation(
                id,
                this.name,
                this.date,
                this.time,
                this.theme
        );
    }

    private void validateName(String name) {
        if (!StringUtils.hasText(name)) {
            throw new InvalidReservationException("예약엔 이름이 존재해야 합니다.");
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new InvalidReservationException("예약엔 날짜가 존재해야 합니다.");
        }
    }

    private void validateTime(ReservationTime time) {
        if (time == null) {
            throw new InvalidReservationException("예약엔 시간이 존재해야 합니다.");
        }
    }

    private void validateTheme(Theme theme) {
        if (theme == null) {
            throw new InvalidReservationException("예약엔 테마가 존재해야 합니다.");
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

    public Long getTimeId() {
        return time.getId();
    }

    public Long getThemeId() {
        return theme.getId();
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
        return Objects.equals(id, that.id) && Objects.equals(name, that.name)
                && Objects.equals(date, that.date) && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, date, time);
    }
}
