package roomescape.time.service.dto.response;

import roomescape.time.repository.dto.ReservationTimeWithBookedDataResponse;

public record ReservationTimeWithBookedResponse(
        Long id,
        String startAt,
        boolean alreadyBooked
) {
    public static ReservationTimeWithBookedResponse of(ReservationTimeWithBookedDataResponse response) {
        return new ReservationTimeWithBookedResponse(
                response.id(),
                response.startAt().toString(),
                response.alreadyBooked()
        );
    }
}
