package roomescape.reservation.application.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import roomescape.reservation.domain.Reservation;

public record ReservationCreateCommand(
        String name,
        LocalDate date,
        Long themeId,
        Long timeId,
        LocalDateTime now
) {
    public Reservation toEntity(Long themeId, Long timeId) {
        return Reservation.builder()
                .name(name)
                .date(date)
                .themeId(themeId)
                .timeId(timeId)
                .build();
    }
}
