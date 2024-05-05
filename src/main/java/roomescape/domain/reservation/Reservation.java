package roomescape.domain.reservation;

import java.time.LocalDateTime;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;

public class Reservation {

    private final Long id;
    private final ReservationName reservationName;
    private final ReservationDate reservationDate;
    private final ReservationTime reservationTime;
    private final Theme theme;

    public Reservation(Long id,
                       ReservationName reservationName,
                       ReservationDate reservationDate,
                       ReservationTime reservationTime,
                       Theme theme) {
        this.id = id;
        this.reservationName = reservationName;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.theme = theme;
    }

    public void validatePast(ReservationTime reservationTime, LocalDateTime now) {
        if (reservationDate.isBefore(now) || reservationDate.isSame(now) && reservationTime.isBefore(now)) {
            throw new IllegalArgumentException("예약 일과 예약 시간은 과거일 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public ReservationName getName() {
        return reservationName;
    }

    public ReservationDate getDate() {
        return reservationDate;
    }

    public ReservationTime getReservationTime() {
        return reservationTime;
    }

    public Theme getTheme() {
        return theme;
    }
}
