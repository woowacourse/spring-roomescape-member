package roomescape.controller.reservation.dto;

import java.time.LocalDate;

public record FilterReservationRequest(long themeId,
                                       long memberId,
                                       LocalDate dateFrom,
                                       LocalDate dateTo) {
}
