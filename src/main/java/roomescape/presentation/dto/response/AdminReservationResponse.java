package roomescape.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.List;
import roomescape.application.dto.ReservationDto;

public record AdminReservationResponse(

        long id,

        String name,

        ThemeResponse theme,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        TimeResponse time
) {
    public static AdminReservationResponse from(ReservationDto reservationDto) {
        ThemeResponse themeResponse = ThemeResponse.from(reservationDto.theme());
        TimeResponse timeResponse = TimeResponse.from(reservationDto.time());
        return new AdminReservationResponse(reservationDto.id(), reservationDto.name(), themeResponse,
                reservationDto.date(), timeResponse);
    }

    public static List<AdminReservationResponse> from(List<ReservationDto> reservationDtos) {
        return reservationDtos.stream()
                .map(AdminReservationResponse::from)
                .toList();
    }
}
