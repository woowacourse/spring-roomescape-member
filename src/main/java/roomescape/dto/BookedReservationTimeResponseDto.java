package roomescape.dto;

import java.time.format.DateTimeFormatter;
import roomescape.domain.ReservationTime;

public record BookedReservationTimeResponseDto(String startAt, Long timeId,
                                               boolean alreadyBooked) {

    public static BookedReservationTimeResponseDto from(ReservationTime time,
        boolean alreadyBooked) {
        return new BookedReservationTimeResponseDto(
            time.getStartAt().format(DateTimeFormatter.ofPattern("HH:mm")),
            time.getId(),
            alreadyBooked
        );
    }
}
