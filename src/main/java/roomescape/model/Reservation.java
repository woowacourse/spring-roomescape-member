package roomescape.model;

import java.time.LocalDateTime;
import roomescape.dto.UserReservationRequest;
import roomescape.model.user.Member;

public class Reservation {
    private final Long id;
    private final Member member;
    private final ReservationDateTime reservationDateTime;
    private final Theme theme;

    public Reservation(Long id, Member member, ReservationDateTime reservationDateTime, Theme theme) {
        this.id = id;
        this.member = member;
        this.reservationDateTime = reservationDateTime;
        this.theme = theme;
    }

    public static Reservation createWithNoId(Member member, UserReservationRequest userReservationRequest,
                                             ReservationTime reservationTime,
                                             Theme theme) {
        validateFuture(userReservationRequest, reservationTime);
        return new Reservation(null,
                member,
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

    public Member getMember() {
        return member;
    }

    public ReservationDateTime getReservationDateTime() {
        return reservationDateTime;
    }

    public Theme getTheme() {
        return theme;
    }
}
