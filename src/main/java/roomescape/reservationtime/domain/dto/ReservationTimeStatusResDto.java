package roomescape.reservationtime.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.reservationtime.domain.ReservationTime;

public record ReservationTimeStatusResDto(
    long id,
    @JsonFormat(pattern = "HH:mm") LocalTime startAt,
    boolean alreadyBooked
) {

    public static ReservationTimeStatusResDto from(ReservationTime reservationTime, boolean alreadyBooked) {
        return new ReservationTimeStatusResDto(
            reservationTime.getId(),
            reservationTime.getStartAt(),
            alreadyBooked
        );
    }
}
