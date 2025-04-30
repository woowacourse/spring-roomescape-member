package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import roomescape.globalException.BadRequestException;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime reservationTime;
    private final Theme theme;

    public Reservation(Long id, String name, LocalDate date, ReservationTime reservationTime, Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.reservationTime = reservationTime;
        this.theme = theme;
    }

    public static Reservation of(String name, LocalDate date, ReservationTime reservationTime, Theme theme) {
        LocalDateTime dateTime = LocalDateTime.of(date, reservationTime.getStartAt());
        validateTense(dateTime);
        return new Reservation(null, name, date, reservationTime, theme);
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
