package roomescape.reservation.service.dto;

import java.time.LocalDate;

public record ReservationUpdateServiceDto(
        Long id,
        String requesterName,
        LocalDate date,
        Long timeId,
        Long themeId
) {}
