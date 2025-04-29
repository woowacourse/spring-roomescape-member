package roomescape.business.dto;

import java.time.LocalDate;

public record ReservationRequestDto(String name, LocalDate date, Long timeId) {
}
