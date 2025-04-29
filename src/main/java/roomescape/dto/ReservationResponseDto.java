package roomescape.dto;

import java.time.LocalDate;

public record ReservationResponseDto(Long id, String name, LocalDate date, ReservationTimeResponseDto time) {

}
