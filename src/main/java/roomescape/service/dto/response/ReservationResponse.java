package roomescape.service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.domain.Reservation;

public record ReservationResponse(
        @NotNull
        Long id,
        @NotBlank
        String name,
        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        ReservationTimeResponse time,
        RoomThemeResponse theme) {

    public static ReservationResponse fromReservation(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                ReservationTimeResponse.fromReservationTime(reservation.getTime()),
                RoomThemeResponse.fromRoomTheme(reservation.getTheme()));
    }
}
