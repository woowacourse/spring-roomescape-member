package roomescape.reservation.service.dto.request;

import roomescape.reservation.entity.ReservationEntity;
import roomescape.time.entity.ReservationTimeEntity;

import java.time.LocalDate;

public record ReservationRequest(LocalDate date, String name, Long timeId, Long themeId) {
    public ReservationRequest {
        if (date == null || name == null || name.isBlank() || timeId == null || themeId == null) {
            throw new IllegalArgumentException("값이 모두 입력되지 않았습니다.");
        }
    }

    public ReservationEntity toEntity(ReservationTimeEntity timeEntity) {
        return new ReservationEntity(0L, name, date, timeEntity, themeId);
    }
}
