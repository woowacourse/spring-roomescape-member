package roomescape.reservation.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import roomescape.member.dto.MemberResponse;
import roomescape.reservation.domain.Reservation;

public record ReservationResponse(
        Long id,
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul") LocalDate date,
        ReservationTimeResponse time,
        ThemeResponse theme,
        MemberResponse member
) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime()), ThemeResponse.from(reservation.getTheme()),
                MemberResponse.from(reservation.getMember()));
    }

}
