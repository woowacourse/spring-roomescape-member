package roomescape.service.dto.output;

import roomescape.domain.AvailableReservationTime;

import java.util.List;

public record AvailableReservationTimeOutput(long timeId, String startAt, boolean alreadyBooked) {

    public static AvailableReservationTimeOutput toOutput(final AvailableReservationTime availableReservationTime) {

        return new AvailableReservationTimeOutput(availableReservationTime.timeId(), availableReservationTime.startAt(), availableReservationTime.isBooked());
    }

    public static List<AvailableReservationTimeOutput> toOutputs(final List<AvailableReservationTime> availableReservationTime) {
        return availableReservationTime.stream()
                .map(AvailableReservationTimeOutput::toOutput)
                .toList();
    }
}
