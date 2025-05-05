package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

public class Reservation {
    private final Long id;
    private final Long memberId;
    private final Theme theme;
    private final LocalDate reservationDate;
    private final ReservationTime reservationTime;

    private Reservation(Long id, Long memberId, Theme theme, LocalDate reservationDate, ReservationTime reservationTime) {
        this.id = id;
        this.memberId = memberId;
        this.theme = theme;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
    }

    public static Reservation of(Long id, Long memberId, Theme theme, LocalDate date, ReservationTime time) {
        return new Reservation(id, memberId, theme, date, time);
    }

    public static Reservation createNew(Long memberId, Theme theme, LocalDate reservationDate,
                                        ReservationTime reservationTime) {
        return new Reservation(null, memberId, theme, reservationDate, reservationTime);
    }

    public static Reservation assignId(Long id, Reservation reservation) {
        return new Reservation(id, reservation.memberId, reservation.getTheme(), reservation.getReservationDate(),
                reservation.getReservationTime());
    }

    public boolean isPast() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationDateTime = LocalDateTime.of(reservationDate, reservationTime.getStartAt());
        return reservationDateTime.isBefore(now);
    }

    public boolean isDuplicatedWith(Reservation other) {
        return this.reservationDate.equals(other.reservationDate)
                && this.reservationTime.equals(other.reservationTime)
                && this.theme.equals(other.theme);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Theme getTheme() {
        return theme;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public ReservationTime getReservationTime() {
        return reservationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reservation that)) {
            return false;
        }
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
