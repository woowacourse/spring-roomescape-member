package roomescape.reservation.domain.dto;

import java.time.LocalDate;

public record ReservationReqDto(String name, LocalDate date, Long timeId, Long themeId) {
}
