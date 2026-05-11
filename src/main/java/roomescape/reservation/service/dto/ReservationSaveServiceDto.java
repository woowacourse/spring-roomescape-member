package roomescape.reservation.service.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationSaveServiceDto(String name, LocalDate date, Long themeId, LocalTime time) {}
