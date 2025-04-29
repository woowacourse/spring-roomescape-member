package roomescape.dto;

import java.time.LocalDate;

public record ReservationRequestDto(LocalDate date, String name, Long timeId) {

}
