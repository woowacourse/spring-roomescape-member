package roomescape.dto;

import java.time.LocalDate;
import roomescape.model.Reservation;

public record ReservationResponseDto(
        Long id,
        String name,
        LocalDate date,
        ReservationTimeResponseDto time
) {
    public static ReservationResponseDto from(Reservation reservationInfo) {
        return new ReservationResponseDto(
                reservationInfo.getId(),
                reservationInfo.getName(),
                reservationInfo.getDate(),
                new ReservationTimeResponseDto(reservationInfo.getTimeId(), reservationInfo.getTime())
        );
    }
}
