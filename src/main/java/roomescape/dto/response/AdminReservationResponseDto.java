package roomescape.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationStatus;

public record AdminReservationResponseDto(
        Long id,
        String name,
        LocalDate date,
        @JsonProperty("theme") ThemeResponseDto themeResponseDto,
        @JsonProperty("time") TimeResponseDto timeResponseDto,
        ReservationStatus status
) {
    public static AdminReservationResponseDto from(Reservation reservation) {
        return new AdminReservationResponseDto(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                ThemeResponseDto.from(reservation.getTheme()),
                TimeResponseDto.from(reservation.getTime()),
                reservation.getReservationStatus()
        );
    }
}
