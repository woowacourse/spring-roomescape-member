package roomescape.reservation.presentation.dto.response;

import java.time.LocalTime;

public record TimeInformation(
        Long id,
        LocalTime time
) {
}
