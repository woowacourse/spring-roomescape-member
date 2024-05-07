package roomescape.controller.api.dto.response;

import roomescape.service.dto.output.AvailableReservationTimeOutput;

import java.util.List;

public record AvailableReservationTimesResponse(List<AvailReservationTimeResponse> data) {
    public static AvailableReservationTimesResponse toResponse(final List<AvailableReservationTimeOutput> outputs) {
        return new AvailableReservationTimesResponse(
                outputs.stream()
                        .map(AvailReservationTimeResponse::toResponse)
                        .toList()
        );
    }
}
