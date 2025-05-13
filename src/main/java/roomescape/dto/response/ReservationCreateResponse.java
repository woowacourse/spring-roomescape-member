package roomescape.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import roomescape.domain.Reservation;

public record ReservationCreateResponse(

        @Schema(description = "예약 ID", example = "1")
        long id,

        @Schema(type = "string")
        LocalDate date,
        ReservationTimeCreateResponse time,
        ThemeCreateResponse theme,
        MemberResponse member
) {
    public static ReservationCreateResponse from(final Reservation reservation) {
        return new ReservationCreateResponse(reservation.getId(), reservation.getDate(),
                ReservationTimeCreateResponse.from(reservation.getTime()),
                ThemeCreateResponse.from(reservation.getTheme()),
                MemberResponse.from(reservation.getMember()));
    }
}
