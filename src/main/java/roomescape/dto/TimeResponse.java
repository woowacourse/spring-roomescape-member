package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import java.util.List;
import roomescape.domain.ReservationTime;

public record TimeResponse(
        Long id,
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt
) {

    public static List<TimeResponse> fromTimes(final List<ReservationTime> reservationTimes) {
        return reservationTimes.stream()
                .map(TimeResponse::fromTime)
                .toList();
    }

    public static TimeResponse fromTime(ReservationTime reservationTime) {
        return new TimeResponse(reservationTime.getId(), reservationTime.getStartAt());
    }
}
