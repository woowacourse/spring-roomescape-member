package roomescape.reservation.dto;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.dto.response.ThemeDetailDto;
import roomescape.time.dto.ReservationTimeResponse;

//TODO: status 필드 추가, time LocalTime으로 수정
public record ReservationResponse(
        Long id,
        String name,
        LocalDate date,
        ReservationTimeResponse time,
        ThemeDetailDto theme
) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.id(),
                reservation.name(),
                reservation.date(),
                ReservationTimeResponse.from(reservation.time()),
                ThemeDetailDto.from(reservation.theme())
        );
    }

}
