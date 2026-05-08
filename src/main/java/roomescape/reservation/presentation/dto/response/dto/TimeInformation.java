package roomescape.reservation.presentation.dto.response.dto;

import java.time.LocalTime;

public record TimeInformation(
        Long id,
        LocalTime time
) {
}
