package roomescape.model;

import java.time.LocalDateTime;
import roomescape.dto.UserReservationRequest;
import roomescape.model.user.Name;

public class Reservation {
    private final Long id;
    private final Name name;
    private final ReservationDateTime reservationDateTime;
    private final Theme theme;

    public Reservation(Long id, Name name, ReservationDateTime reservationDateTime, Theme theme) {
        this.id = id;
        this.name = name;
        this.reservationDateTime = reservationDateTime;
        this.theme = theme;
    }

    public static Reservation createWithNoId(Name name, UserReservationRequest userReservationRequest,
                                             ReservationTime reservationTime,
                                             Theme theme) {
        validateFuture(userReservationRequest, reservationTime);
        return new Reservation(null,
                name,
                new ReservationDateTime(userReservationRequest.date(), reservationTime),
                theme);
    }

    private static void validateFuture(UserReservationRequest userReservationRequest, ReservationTime reservationTime) {
        LocalDateTime dateTime = LocalDateTime.of(userReservationRequest.date(),
                reservationTime.getStartAt());
        LocalDateTime now = LocalDateTime.now();
        if (dateTime.isBefore(now)) {
            throw new IllegalArgumentException("과거 예약은 불가능합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public ReservationDateTime getReservationDateTime() {
        return reservationDateTime;
    }

    public Theme getTheme() {
        return theme;
    }
}
