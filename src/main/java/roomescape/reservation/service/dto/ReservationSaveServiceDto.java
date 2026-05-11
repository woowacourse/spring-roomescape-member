package roomescape.reservation.service.dto;

import java.time.LocalDate;

public record ReservationSaveServiceDto(String name, LocalDate date, Long themeId, Long timeId) {}
