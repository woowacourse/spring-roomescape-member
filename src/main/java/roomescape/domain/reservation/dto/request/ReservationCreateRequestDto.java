package roomescape.domain.reservation.dto.request;

import java.time.LocalDate;

public record ReservationCreateRequestDto(String name, LocalDate date, Long timeId, Long themeId) {

}
