package roomescape.service.dto;

import java.util.List;
import roomescape.domain.ReservationTime;

public record ReservationTimeOutput(long id, String startAt) {

    public static ReservationTimeOutput toOutput(ReservationTime reservationTime) {
        return new ReservationTimeOutput(reservationTime.getId(), reservationTime.getStartAt());
    }

    public static List<ReservationTimeOutput> toOutputs(List<ReservationTime> reservationTimes) {
        return reservationTimes.stream()
                .map(ReservationTimeOutput::toOutput)
                .toList();
    }
}
