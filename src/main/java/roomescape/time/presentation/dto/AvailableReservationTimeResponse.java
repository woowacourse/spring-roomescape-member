package roomescape.time.presentation.dto;

import java.util.List;
import lombok.Builder;
import roomescape.time.domain.ReservationTime;

@Builder
public record AvailableReservationTimeResponse(
        List<AvailableReservationTimeDto> times
) {
    public static AvailableReservationTimeResponse from(List<ReservationTime> times) {
        return AvailableReservationTimeResponse.builder()
                .times(times.stream()
                        .map(AvailableReservationTimeDto::from)
                        .toList())
                .build();
    }
}
