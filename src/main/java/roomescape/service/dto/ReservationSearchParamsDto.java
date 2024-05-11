package roomescape.service.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationSearchParamsDto(Long memberId, Long themeId, LocalDate dateFrom, LocalTime dateTo) {

}
