package roomescape.reservationtime.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.reservationtime.domain.ReservationTime;

public record AvailableReservationTimeResDto(
    long id,
    @JsonFormat(pattern = "HH:mm") LocalTime startAt,
    boolean alreadyBooked
) {

    public static AvailableReservationTimeResDto from(ReservationTime reservationTime, boolean alreadyBooked) {
        return new AvailableReservationTimeResDto(
            reservationTime.getId(),
            reservationTime.getStartAt(),
            alreadyBooked
        );
    }
}
