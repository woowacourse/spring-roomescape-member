package roomescape.dto;

import java.time.format.DateTimeFormatter;
import roomescape.domain.ReservationTime;

public record AvailableReservationTimeResponseDto(String startAt, Long timeId, boolean alreadyBooked) {

    public static AvailableReservationTimeResponseDto from(ReservationTime time, boolean alreadyBooked) {
        return new AvailableReservationTimeResponseDto(
                time.getStartAt().format(DateTimeFormatter.ofPattern("HH:mm")),
                time.getId(),
                alreadyBooked
        );
    }
}
