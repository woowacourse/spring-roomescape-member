package roomescape.reservation.dto.request;

import java.time.LocalDate;

public record AvailableTimeRequest(Long themeId, LocalDate date) {

}
