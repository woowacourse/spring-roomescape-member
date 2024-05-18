package roomescape.dto.reservation.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import roomescape.domain.reservation.Reservation;
import roomescape.dto.member.MemberResponse;
import roomescape.dto.theme.ThemeResponse;
import roomescape.dto.time.TimeResponse;

import java.time.LocalDate;

public record ReservationResponse(
        Long id,
        LocalDate date,
        @JsonProperty("time") TimeResponse timeResponse,
        @JsonProperty("theme") ThemeResponse themeResponse,
        @JsonProperty("member") MemberResponse memberResponse
) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getDate(),
                TimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme()),
                MemberResponse.from(reservation.getMember())
        );
    }
}
