package roomescape.reservation.presentation.dto.response;

import roomescape.reservation.presentation.dto.response.dto.TimeInformation;

public record AvailableTimeFindResponse(TimeInformation timeInformation,
                                        boolean isAvailable) {
}
