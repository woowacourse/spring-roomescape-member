package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import roomescape.common.globalexception.BadRequestException;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime reservationTime;
    private final Theme theme;

    private Reservation(Long id, String name, LocalDate date, ReservationTime reservationTime, Theme theme) {
        validateId(id);
        validateName(name);
        validateDate(date);
        validateReservationTime(reservationTime);
        validateTheme(theme);
        this.id = id;
        this.name = name;
        this.date = date;
        this.reservationTime = reservationTime;
        this.theme = theme;
    }

    private Reservation(String name, LocalDate date, ReservationTime reservationTime, Theme theme) {
        validateName(name);
        validateDate(date);
        validateReservationTime(reservationTime);
        validateTheme(theme);
        this.id = null;
        this.name = name;
        this.date = date;
        this.reservationTime = reservationTime;
        this.theme = theme;
    }

    public static Reservation withId(
        Long id,
        String name,
        LocalDate date,
        ReservationTime reservationTime,
        Theme theme
    ) {

        return new Reservation(id, name, date, reservationTime, theme);
    }

    public static Reservation withoutId(String name, LocalDate date, ReservationTime reservationTime, Theme theme) {
        LocalDateTime dateTime = LocalDateTime.of(date, reservationTime.getStartAt());
        validateTense(dateTime);
        return new Reservation(name, date, reservationTime, theme);
    }

    private static void validateTense(LocalDateTime dateTime) {
        if (isPastTense(dateTime)) {
            throw new BadRequestException("과거시점으로 예약을 진행할 수 없습니다.");
        }
    }

    private static boolean isPastTense(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        return dateTime.isBefore(now);
    }

    private void validateId(Long id) {
        if (id == null) {
            throw new BadRequestException("예약 ID가 없습니다.");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("예약자 이름이 없습니다.");
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new BadRequestException("예약 날짜가 없습니다.");
        }
    }

    private void validateReservationTime(ReservationTime reservationTime) {
        if (reservationTime == null) {
            throw new BadRequestException("예약 시간이 없습니다.");
        }
    }

    private void validateTheme(Theme theme) {
        if (theme == null) {
            throw new BadRequestException("예약 테마가 없습니다.");
        }
    }

    public boolean isSameDateTime(Reservation compare) {
        return this.getDateTime().isEqual(compare.getDateTime());
    }

    public LocalDateTime getDateTime() {
        return LocalDateTime.of(date, reservationTime.getStartAt());
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

    public ReservationTime getReservationTime() {
        return reservationTime;
    }

    public Theme getTheme() {
        return theme;
    }
}
