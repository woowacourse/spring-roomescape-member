package roomescape.reservation.dto.request;

import roomescape.reservation.entity.Reservation;
import roomescape.reservation.entity.Time;

import java.time.LocalDate;

public record ReservationRequest(
        LocalDate date,
        String name,
        Long timeId,
        Long themeId
) {

    public ReservationRequest {
        if (date == null || name == null || name.isBlank() || timeId == null || themeId == null) {
            throw new IllegalArgumentException("값이 모두 입력되지 않았습니다.");
        }
    }

    public Reservation toEntity(Time timeEntity) {
        return new Reservation(0L, name, date, timeEntity, themeId);
    }
}
