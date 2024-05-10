package roomescape.service.dto.output;

import java.util.List;
import roomescape.domain.ReservationTime;

public record ReservationTimeOutput(long id, String startAt) {

    public static ReservationTimeOutput from(final ReservationTime reservationTime) {
        return new ReservationTimeOutput(reservationTime.id(), reservationTime.getStartAtAsString());
    }

    public static List<ReservationTimeOutput> list(final List<ReservationTime> reservationTimes) {
        return reservationTimes.stream()
                .map(ReservationTimeOutput::from)
                .toList();
    }
}
