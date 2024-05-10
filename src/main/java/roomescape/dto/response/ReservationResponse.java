package roomescape.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.domain.Reservation;

import java.time.LocalDate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Member;

public record ReservationResponse(
        Long id,
        MemberResponse member,
        LocalDate date,
        TimeResponse time,
        ThemeResponse theme
) {
    public static ReservationResponse from(Reservation reservation) {
        Member member = reservation.getMember();
        return new ReservationResponse(
                reservation.getId(),
                MemberResponse.from(member),
                reservation.getDate(),
                TimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme())
        );
    }

    private record TimeResponse (
        Long id,
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt
    ) {
        public static TimeResponse from(ReservationTime reservationTime) {
            return new TimeResponse(
                    reservationTime.getId(),
                    reservationTime.getStartAt()
            );
        }
    }
}
