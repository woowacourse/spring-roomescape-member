package roomescape.reservation.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import roomescape.reservationtime.model.ReservationTime;
import roomescape.theme.model.Theme;

public class Reservation {
    private final Long id;
    private final ReservationName name;
    private final LocalDate date;
    private final ReservationTime reservationTime;
    private final Theme theme;

    public Reservation(final Long id,
                       final ReservationName name,
                       final LocalDate date,
                       final ReservationTime reservationTime,
                       final Theme theme) {
        validateReservationDateIsNull(date);
        validateReservationTimeIsNull(reservationTime);
        validateReservationThemeIsNull(theme);

        this.id = id;
        this.name = name;
        this.date = date;
        this.reservationTime = reservationTime;
        this.theme = theme;
    }

    public static Reservation of(final Long id,
                                 final String name,
                                 final LocalDate date,
                                 final ReservationTime reservationTime,
                                 final Theme theme) {
        return new Reservation(
                id,
                new ReservationName(name),
                date,
                reservationTime,
                theme);
    }

    public static Reservation of(final Long id,
                                 final Reservation reservation) {
        return new Reservation(
                id,
                reservation.getName(),
                reservation.getDate(),
                reservation.getReservationTime(),
                reservation.getTheme());
    }

    private void validateReservationDateIsNull(final LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("예약 생성 시 예약 날짜는 필수입니다.");
        }
    }

    private void validateReservationTimeIsNull(final ReservationTime reservationTime) {
        if (reservationTime == null) {
            throw new IllegalArgumentException("예약 생성 시 예약 시간은 필수입니다.");
        }
    }

    private void validateReservationThemeIsNull(final Theme theme) {
        if (theme == null) {
            throw new IllegalArgumentException("예약 생성 시 예약 테마는 필수입니다.");
        }
    }

    public boolean isSameId(Long id) {
        return Objects.equals(this.id, id);
    }

    public boolean isSameTime(final ReservationTime reservationTime) {
        return this.reservationTime.isSameTo(reservationTime.getId());
    }

    public boolean isSameTimeId(final Long timeId) {
        return this.reservationTime.isSameTo(timeId);
    }

    public boolean isSameTheme(final Long themeId) {
        return this.theme.isSameTo(themeId);
    }

    public boolean isSameDate(final LocalDate date) {
        return Objects.equals(this.date, date);
    }

    public boolean isBeforeDateTimeThanNow(final LocalDateTime now) {
        if (date.isBefore(now.toLocalDate())) {
            return true;
        }

        if (date.isAfter(now.toLocalDate())) {
            return false;
        }
        return reservationTime.isBefore(now.toLocalTime());
    }

    public Long getId() {
        return id;
    }

    public ReservationName getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getReservationTime() {
        return reservationTime;
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
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
