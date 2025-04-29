package roomescape.dto;

import roomescape.entity.ReservationEntity;
import roomescape.entity.ReservationTimeEntity;

import java.time.LocalDate;

public record ReservationRequest(LocalDate date, String name, Long timeId) {
    public ReservationRequest {
        if (date == null || name == null || name.isBlank() || timeId == null) {
            throw new IllegalArgumentException("값이 모두 입력되지 않았습니다.");
        }
    }

    public ReservationEntity toEntity(ReservationTimeEntity timeEntity) {
        return new ReservationEntity(0L, name, date, timeEntity);
    }
}
