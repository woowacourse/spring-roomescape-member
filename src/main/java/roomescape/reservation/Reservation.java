package roomescape.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import roomescape.exception.ArgumentNullException;
import roomescape.exception.PastDateTimeReservationException;
import roomescape.member.Member;
import roomescape.reservationtime.ReservationTime;
import roomescape.theme.Theme;

public class Reservation {

    private Long id;
    private final Member member;
    private final LocalDate date;
    private final ReservationTime reservationTime;
    private final Theme theme;

    private Reservation(final Long id, final Member member, final LocalDate date, final ReservationTime reservationTime,
                        final Theme theme) {
        this.id = id;
        this.member = member;
        this.date = date;
        this.reservationTime = reservationTime;
        this.theme = theme;
    }

    private static void validateNull(Member member, LocalDate date, ReservationTime reservationTime, Theme theme) {
        if (member.getName() == null || member.getName().isBlank()) {
            throw new ArgumentNullException();
        }
        if (date == null) {
            throw new ArgumentNullException();
        }
        if (reservationTime == null) {
            throw new ArgumentNullException();
        }
        if (theme == null) {
            throw new ArgumentNullException();
        }
    }

    public static Reservation of(final Long id, final Member member, final LocalDate date,
                                 final ReservationTime reservationTime, final Theme theme) {
        validateNull(member, date, reservationTime, theme);
        return new Reservation(id, member, date, reservationTime, theme);
    }

    public static Reservation createWithoutId(final Member member, final LocalDate date,
                                              final ReservationTime reservationTime, final Theme theme) {
        validateNull(member, date, reservationTime, theme);
        validateDateTime(date, reservationTime);
        return Reservation.of(null, member, date, reservationTime, theme);
    }

    private static void validateDateTime(LocalDate date, ReservationTime reservationTime) {
        LocalDateTime dateTime = LocalDateTime.of(date, reservationTime.getStartAt());
        if (LocalDateTime.now().isAfter(dateTime)) {
            throw new PastDateTimeReservationException();
        }
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
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
