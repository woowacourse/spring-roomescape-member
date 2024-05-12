package roomescape.service.dto.output;

import java.util.List;
import roomescape.domain.AvailableReservationTime;

public record AvailableReservationTimeOutput(String startAt, long timeId, boolean alreadyBooked) {

    public static AvailableReservationTimeOutput from(final AvailableReservationTime reservationTime) {
        return new AvailableReservationTimeOutput(
                reservationTime.startAt(),
                reservationTime.timeId(),
                reservationTime.alreadyBooked()
        );
    }

    public static List<AvailableReservationTimeOutput> list(final List<AvailableReservationTime> availableTimes) {
        return availableTimes.stream()
                .map(AvailableReservationTimeOutput::from)
                .toList();
    }
}
