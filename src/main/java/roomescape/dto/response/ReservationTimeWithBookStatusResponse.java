package roomescape.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record ReservationTimeWithBookStatusResponse(
        @NotNull
        Long id,
        @NotNull
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt,
        boolean booked)
{

    public static ReservationTimeWithBookStatusResponse fromReservationTime(ReservationTime reservationTime, boolean booked) {
        return new ReservationTimeWithBookStatusResponse(
                reservationTime.getId(),
                reservationTime.getStartAt(),
                booked);
    }
}
