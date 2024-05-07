package roomescape.service.dto.output;

import roomescape.dao.dto.AvailableReservationTimeResult;

import java.util.List;

public record AvailableReservationTimeOutput(long timeId, String startAt, boolean alreadyBooked) {

    public static AvailableReservationTimeOutput toOutput(final AvailableReservationTimeResult availableReservationTimeResult) {

        return new AvailableReservationTimeOutput(availableReservationTimeResult.timeId(), availableReservationTimeResult.startAt(), availableReservationTimeResult.isBooked());
    }

    public static List<AvailableReservationTimeOutput> toOutputs(final List<AvailableReservationTimeResult> availableReservationTimeResult) {
        return availableReservationTimeResult.stream()
                .map(AvailableReservationTimeOutput::toOutput)
                .toList();
    }
}
