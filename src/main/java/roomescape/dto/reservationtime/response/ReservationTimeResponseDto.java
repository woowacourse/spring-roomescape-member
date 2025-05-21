package roomescape.dto.reservationtime.response;

import java.time.format.DateTimeFormatter;
import roomescape.domain.reservationtime.ReservationTime;

public record ReservationTimeResponseDto(Long id, String startAt) {

    public static ReservationTimeResponseDto from(final ReservationTime reservationTime) {
        return new ReservationTimeResponseDto(
                reservationTime.getId(),
                reservationTime.getStartAt().format(DateTimeFormatter.ofPattern("HH:mm")));
    }
}
