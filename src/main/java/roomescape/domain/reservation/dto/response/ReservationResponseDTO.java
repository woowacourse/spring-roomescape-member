package roomescape.domain.reservation.dto.response;

import java.time.LocalDate;
import roomescape.domain.theme.dto.response.ThemeResponseDTO;
import roomescape.domain.time.dto.response.TimeResponseDTO;

public record ReservationResponseDTO(Long id, String name, LocalDate date, TimeResponseDTO time,
                                     ThemeResponseDTO theme) {
}
