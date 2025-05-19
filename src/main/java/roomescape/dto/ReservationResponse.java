package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import roomescape.domain.Reservation;

public record ReservationResponse(
        long id,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate date,
        MemberResponse member,
        ReservationTimeResponse time,
        ReservationThemeResponse theme) {

    public static ReservationResponse of(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getDate(),
                MemberResponse.of(reservation.getMember()),
                ReservationTimeResponse.of(reservation.getTime()),
                ReservationThemeResponse.of(reservation.getTheme()));
    }
}
