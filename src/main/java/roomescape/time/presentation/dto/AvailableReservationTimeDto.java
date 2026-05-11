package roomescape.time.presentation.dto;

import java.time.LocalTime;
import lombok.Builder;
import roomescape.time.domain.ReservationTime;

@Builder
public record AvailableReservationTimeDto(
        Long id,
        LocalTime startAt
) {
    public static AvailableReservationTimeDto from(ReservationTime time) {
        return AvailableReservationTimeDto.builder()
                .id(time.getId())
                .startAt(time.getStartAt())
                .build();
    }
}
