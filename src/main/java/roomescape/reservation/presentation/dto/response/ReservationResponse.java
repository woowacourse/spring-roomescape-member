package roomescape.reservation.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.List;
import roomescape.reservation.application.dto.ReservationDto;

public record ReservationResponse(

        long id,

        String name,

        ThemeResponse theme,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        TimeResponse time
) {
    public static ReservationResponse from(ReservationDto reservationDto) {
        ThemeResponse themeResponse = ThemeResponse.from(reservationDto.theme());
        TimeResponse timeResponse = TimeResponse.from(reservationDto.time());
        return new ReservationResponse(reservationDto.id(), reservationDto.name(), themeResponse,
                reservationDto.date(), timeResponse);
    }

    public static List<ReservationResponse> from(List<ReservationDto> reservationDtos) {
        return reservationDtos.stream()
                .map(ReservationResponse::from)
                .toList();
    }
}
