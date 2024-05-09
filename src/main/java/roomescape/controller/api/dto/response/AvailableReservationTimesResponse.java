package roomescape.controller.api.dto.response;

import java.util.List;
import roomescape.service.dto.output.AvailableReservationTimeOutput;

public record AvailableReservationTimesResponse(List<AvailableReservationTimeResponse> times) {

    private record AvailableReservationTimeResponse(String startAt, long timeId, boolean alreadyBooked) {

        public static AvailableReservationTimeResponse from(final AvailableReservationTimeOutput output) {
            return new AvailableReservationTimeResponse(
                    output.startAt(),
                    output.timeId(),
                    output.alreadyBooked()
            );
        }

        public static List<AvailableReservationTimeResponse> list(final List<AvailableReservationTimeOutput> outputs) {
            return outputs.stream()
                    .map(AvailableReservationTimeResponse::from)
                    .toList();
        }
    }

    public static AvailableReservationTimesResponse from(final List<AvailableReservationTimeOutput> outputs) {
        return new AvailableReservationTimesResponse(AvailableReservationTimeResponse.list(outputs));
    }
}
