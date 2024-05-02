package roomescape.reservation.dto.response;

import java.time.LocalTime;

public record AvailableTimeResponse(LocalTime time, Long timeId, boolean isAvailable) {

}
