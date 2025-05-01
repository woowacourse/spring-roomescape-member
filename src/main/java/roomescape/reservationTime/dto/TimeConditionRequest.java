package roomescape.reservationTime.dto;

import java.time.LocalDate;

public record TimeConditionRequest(LocalDate date, Long themeId) {
}
