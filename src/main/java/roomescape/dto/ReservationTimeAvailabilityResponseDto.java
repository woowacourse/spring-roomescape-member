package roomescape.dto;

import roomescape.domain.ReservationTime;

public record ReservationTimeAvailabilityResponseDto(
        ReservationTimeResponseDto time,
        boolean available
) {
    public static ReservationTimeAvailabilityResponseDto from(ReservationTime time, boolean available) {
        return new ReservationTimeAvailabilityResponseDto(
                ReservationTimeResponseDto.from(time),
                available);
    }
}
