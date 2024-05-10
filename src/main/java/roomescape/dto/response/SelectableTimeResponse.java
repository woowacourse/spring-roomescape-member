package roomescape.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import roomescape.domain.reservation.ReservationTime;

import java.time.LocalTime;
import java.util.List;

public record SelectableTimeResponse(
        long id,

        @JsonFormat(pattern = "kk:mm")
        LocalTime startAt,

        boolean alreadyBooked
) {
    public static SelectableTimeResponse from(ReservationTime time, List<Long> usedTimeIds) {
        return new SelectableTimeResponse(
                time.getId(),
                time.getStartAt(),
                usedTimeIds.contains(time.getId())
        );
    }

    public static List<SelectableTimeResponse> listOf(List<ReservationTime> reservationTimes, List<Long> usedTimeIds) {
        return reservationTimes.stream()
                .map(time -> SelectableTimeResponse.from(time, usedTimeIds))
                .toList();
    }
}
