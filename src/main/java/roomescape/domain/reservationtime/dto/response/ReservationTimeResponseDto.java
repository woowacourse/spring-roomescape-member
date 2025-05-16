package roomescape.domain.reservationtime.dto.response;

import java.time.format.DateTimeFormatter;
import roomescape.domain.reservationtime.model.ReservationTime;

public record ReservationTimeResponseDto(long id, String startAt) {

    public static ReservationTimeResponseDto from(ReservationTime reservationTime) {
        return new ReservationTimeResponseDto(
            reservationTime.getId(),
            reservationTime.getStartAt().format(DateTimeFormatter.ofPattern("HH:mm")));
    }
}
