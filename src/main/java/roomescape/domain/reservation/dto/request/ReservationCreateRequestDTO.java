package roomescape.domain.reservation.dto.request;

import java.time.LocalDate;

public record ReservationCreateRequestDTO(String name, LocalDate date, Long timeId) {

}
