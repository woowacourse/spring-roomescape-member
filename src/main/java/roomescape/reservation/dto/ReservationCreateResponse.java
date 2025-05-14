package roomescape.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;

public record ReservationCreateResponse(
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        Long themeId,
        Long timeId,
        Long memberId
) {

    public static ReservationCreateResponse from(Reservation reservation) {
        return new ReservationCreateResponse(
                reservation.getDate(),
                reservation.getTheme().getId(),
                reservation.getTime().getId(),
                reservation.getMember().getId());
    }
}
