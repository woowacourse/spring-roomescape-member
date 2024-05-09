package roomescape.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AdminReservationCreateRequest(
        Long id,

        @NotNull(message = "[ERROR] 날짜는 비어있을 수 없습니다.")
        LocalDate date,

        @NotNull(message = "[ERROR] 예약 시간 Id는 비어있을 수 없습니다.")
        Long timeId,

        @NotNull(message = "[ERROR] 테마 Id는 비어있을 수 없습니다.")
        Long themeId,

        @NotNull(message = "[ERROR] 멤버 Id는 비어있을 수 없습니다.")
        Long memberId
) {
}
