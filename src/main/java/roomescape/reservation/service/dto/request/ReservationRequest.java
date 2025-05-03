package roomescape.reservation.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import roomescape.reservation.entity.ReservationEntity;
import roomescape.time.entity.ReservationTimeEntity;

import java.time.LocalDate;

public record ReservationRequest(
        @NotNull(message = "날짜가 입력되지 않았습니다.")
        LocalDate date,
        @NotBlank(message = "예약자명이 입력되지 않았습니다.")
        String name,
        @NotNull(message = "시간이 선택되지 않았습니다.")
        Long timeId,
        @NotNull(message = "테마가 선택되지 않았습니다.")
        Long themeId
) {
    public ReservationEntity toEntity(ReservationTimeEntity timeEntity) {
        return new ReservationEntity(0L, name, date, timeEntity, themeId);
    }
}
