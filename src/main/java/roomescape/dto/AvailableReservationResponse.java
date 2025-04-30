package roomescape.dto;

import java.time.LocalTime;

// 완료 -> Reservation
// 가능한 예약 ->
public record AvailableReservationResponse(
        Long timeId,
        LocalTime startAt,
        Boolean isBooked
) {
    // localhost:8080/reservations?themeId=1&date=2024-12-13
}
