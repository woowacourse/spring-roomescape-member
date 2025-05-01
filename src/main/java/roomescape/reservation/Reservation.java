package roomescape.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import roomescape.globalException.BadRequestException;
import roomescape.reservationTime.ReservationTime;
import roomescape.theme.Theme;

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

    public Reservation(final String name, final LocalDate date) {
        this(null, name, date, null, null);
    }

    public static Reservation of(String name, LocalDate date, ReservationTime reservationTime, Theme theme) {
        LocalDateTime dateTime = LocalDateTime.of(date, reservationTime.getStartAt());
        validateTense(dateTime);
        return new Reservation(null, name, date, reservationTime, theme);
    }

    private static void validateTense(final LocalDateTime dateTime) {
        if (isPastTense(dateTime)) {
            throw new BadRequestException("과거시점으로 예약을 진행할 수 없습니다.");
        }
    }

    private static boolean isPastTense(final LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        return dateTime.isBefore(now);
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
