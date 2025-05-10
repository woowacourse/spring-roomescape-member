package roomescape.service.dto;

import java.time.LocalDate;

public record ReservationCreateDto(String name, LocalDate date, long timeId, long themeId) {
}
