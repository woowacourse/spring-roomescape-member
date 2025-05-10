package roomescape.dto.response;

import java.time.LocalDate;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record ReservationResponse(Long id,
                                  MemberResponse member,
                                  LocalDate date,
                                  ReservationTimeResponse time,
                                  ThemeResponse theme) {

    public static ReservationResponse from(Reservation reservation) {
        ReservationTime reservationTime = reservation.getTime();
        Theme theme = reservation.getTheme();
        Member member = reservation.getMember();
        return new ReservationResponse(
                reservation.getId(),
                MemberResponse.from(member),
                reservation.getDate(),
                ReservationTimeResponse.from(reservationTime),
                ThemeResponse.from(theme));
    }
}
