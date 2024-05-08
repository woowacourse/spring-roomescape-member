package roomescape.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.domain.Reservation;

import java.time.LocalDate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Member;

public record ReservationResponse(
        Long id,
        UserResponse member,
        LocalDate date,
        TimeResponse time,
        ThemeResponse theme
) {
    public static ReservationResponse from(Reservation reservation) {
        Member member = reservation.getUser();
        return new ReservationResponse(
                reservation.getId(),
                new UserResponse(member.getId(), member.getName(), member.getEmail(), member.getPassword()),
                reservation.getDate(),
                TimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme()));
    }

    private record TimeResponse (
        Long id,
        @JsonFormat(pattern = "HH:mm") LocalTime startAt
    ) {
        public static TimeResponse from(ReservationTime reservationTime) {
            return new TimeResponse(
                    reservationTime.getId(),
                    reservationTime.getStartAt());
        }
    }
}
