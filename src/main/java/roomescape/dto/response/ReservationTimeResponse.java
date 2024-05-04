package roomescape.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import roomescape.domain.ReservationTime;

import java.time.LocalTime;
import java.util.List;

public record ReservationTimeResponse(
        long timeId,

        @JsonFormat(pattern = "kk:mm")
        LocalTime startAt
) {
    public ReservationTimeResponse(final ReservationTime reservationTime) {
        this(reservationTime.getId(), reservationTime.getStartAt());
    }

    public static List<ReservationTimeResponse> listOf(final List<ReservationTime> reservationTimes) {
        return reservationTimes.stream()
                .map(ReservationTimeResponse::new)
                .toList();
    }
}
