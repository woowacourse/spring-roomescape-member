package roomescape.presentation.dto.response;

import java.time.format.DateTimeFormatter;
import java.util.List;
import roomescape.domain.ReservationTime;

public record ReservationTimeResponse(Long id, String startAt) {

    public static ReservationTimeResponse from(ReservationTime reservationTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return new ReservationTimeResponse(
                reservationTime.getId(),
                reservationTime.getStartAt().format(formatter)
        );
    }

    public static List<ReservationTimeResponse> toList(List<ReservationTime> reservationTimes) {
        return reservationTimes.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }
}
