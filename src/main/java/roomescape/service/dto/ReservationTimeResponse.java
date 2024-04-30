package roomescape.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import roomescape.domain.ReservationTime;

import java.time.LocalTime;
import java.util.List;

public record ReservationTimeResponse(Long id, @JsonFormat(pattern = "HH:mm") LocalTime startAt) {

    public static ReservationTimeResponse of(ReservationTime reservationTime) {
        return new ReservationTimeResponse(reservationTime.getId(), reservationTime.getStartAt());
    }

    public static List<ReservationTimeResponse> listOf(List<ReservationTime> reservationTimes) {
        return reservationTimes.stream()
                .map(ReservationTimeResponse::of)
                .toList();
    }
}
