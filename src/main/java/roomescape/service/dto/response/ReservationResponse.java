package roomescape.service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationResponse(
        Long id,
        Member member,
        LocalDate date,
        TimeResponse time,
        ThemeResponse theme
) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getMember(),
                reservation.getDate().getStartAt(),
                TimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme()));
    }

    private record TimeResponse(
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
