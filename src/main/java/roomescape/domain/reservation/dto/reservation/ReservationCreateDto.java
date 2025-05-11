package roomescape.domain.reservation.dto.reservation;

import java.time.LocalDate;

public record ReservationCreateDto(Long userId, Long timeId, Long themeId, LocalDate date) {

}
