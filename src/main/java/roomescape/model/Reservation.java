package roomescape.model;

import java.time.LocalDateTime;
import roomescape.dto.ReservationRequest;
import roomescape.model.user.UserName;

public class Reservation {
    private final Long id;
    private final UserName userName;
    private final ReservationDateTime reservationDateTime;
    private final Theme theme;

    public Reservation(Long id, UserName userName, ReservationDateTime reservationDateTime, Theme theme) {
        this.id = id;
        this.userName = userName;
        this.reservationDateTime = reservationDateTime;
        this.theme = theme;
    }

    public static Reservation createWithNoId(ReservationRequest reservationRequest, ReservationTime reservationTime,
                                             Theme theme) {
        validateFuture(reservationRequest, reservationTime);
        return new Reservation(null,
                new UserName(reservationRequest.name()),
                new ReservationDateTime(reservationRequest.date(), reservationTime),
                theme);
    }

    private static void validateFuture(ReservationRequest reservationRequest, ReservationTime reservationTime) {
        LocalDateTime dateTime = LocalDateTime.of(reservationRequest.date(),
                reservationTime.getStartAt());
        LocalDateTime now = LocalDateTime.now();
        if (dateTime.isBefore(now)) {
            throw new IllegalArgumentException("과거 예약은 불가능합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public UserName getUserName() {
        return userName;
    }

    public ReservationDateTime getReservationDateTime() {
        return reservationDateTime;
    }

    public Theme getTheme() {
        return theme;
    }
}
