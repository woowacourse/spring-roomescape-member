package roomescape.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import roomescape.domain.Reservation;

public record ReservationResponse(

        @Schema(description = "예약 ID", example = "1")
        long id,

        @Schema(type = "string")
        LocalDate date,

        ReservationTimeResponse time,
        ThemeResponse theme,
        MemberResponse member
) {

    public static ReservationResponse from(final Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme()),
                MemberResponse.from(reservation.getMember()));
    }
}
