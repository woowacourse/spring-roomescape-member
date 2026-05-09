package roomescape.reservation.entity;

import java.time.LocalDate;
import java.util.Objects;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.theme.entity.Theme;

public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    private Reservation(String name, LocalDate date, ReservationTime time, Theme theme) {
        this(null, name, date, time, theme);
    }

    private Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        validate(name, date, time, theme);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    private void validate(String name, LocalDate date, ReservationTime time, Theme theme) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("예약자 이름은 필수입니다.");
        }
        if (name.length() > 10) {
            throw new IllegalArgumentException("예약자 이름은 10자 이하여야 합니다.");
        }
        if (date == null) {
            throw new IllegalArgumentException("예약 날짜는 필수입니다.");
        }
        if (time == null) {
            throw new IllegalArgumentException("예약 시간은 필수입니다.");
        }
        if (theme == null) {
            throw new IllegalArgumentException("예약 테마는 필수입니다.");
        }
    }

    public static Reservation of(String name, LocalDate date, ReservationTime time, Theme theme) {
        return new Reservation(name, date, time, theme);
    }

    public static Reservation of(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        return new Reservation(id, name, date, time, theme);
    }

    public static Reservation toEntity(Reservation reservation, Long id) {
        return new Reservation(id, reservation.name, reservation.date, reservation.time, reservation.theme);
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
