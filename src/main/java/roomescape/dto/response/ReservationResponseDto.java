package roomescape.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import roomescape.domain.Reservation;

public record ReservationResponseDto(
        Long id,
        String name,
        LocalDate date,
        @JsonProperty("theme") ThemeResponseDto themeResponseDto,
        @JsonProperty("timeResponseDto") TimeResponseDto timeResponseDto
) {
    public static ReservationResponseDto from(Reservation reservation) {
        return new ReservationResponseDto(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                ThemeResponseDto.from(reservation.getTheme()),
                TimeResponseDto.from(reservation.getTime())
        );
    }
}
