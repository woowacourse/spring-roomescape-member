package roomescape.dto.reservation.response;

import java.time.format.DateTimeFormatter;
import roomescape.domain.reservationtime.ReservationTime;

public record AvailableReservationTimeResponseDto(String startAt, Long timeId, boolean alreadyBooked) {

    public static AvailableReservationTimeResponseDto from(final ReservationTime time, final boolean alreadyBooked) {
        return new AvailableReservationTimeResponseDto(
                time.getStartAt().format(DateTimeFormatter.ofPattern("HH:mm")),
                time.getId(),
                alreadyBooked
        );
    }
}
