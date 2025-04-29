package roomescape.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationResponseDto(Long id, String name, LocalTime time, LocalDate date) {

}
