package roomescape.controller.reservation.dto;

import java.time.LocalDate;

public record ReservationSearch(Long themeId,
                                Long memberId,
                                LocalDate dateFrom,
                                LocalDate dateTo) {
}
