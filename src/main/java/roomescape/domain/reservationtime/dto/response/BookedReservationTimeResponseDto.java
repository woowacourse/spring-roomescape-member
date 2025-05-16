package roomescape.domain.reservationtime.dto.response;

import java.time.format.DateTimeFormatter;
import roomescape.domain.reservationtime.model.ReservationTime;

public record BookedReservationTimeResponseDto(String startAt, Long timeId, boolean alreadyBooked) {

    public static BookedReservationTimeResponseDto from(ReservationTime time,
        boolean alreadyBooked) {
        return new BookedReservationTimeResponseDto(
            time.getStartAt().format(DateTimeFormatter.ofPattern("HH:mm")),
            time.getId(),
            alreadyBooked
        );
    }
}
