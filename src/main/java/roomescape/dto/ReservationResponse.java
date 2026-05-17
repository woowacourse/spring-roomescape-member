package roomescape.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

@Schema(description = "예약 응답")
public record ReservationResponse(
        @Schema(description = "예약 ID", example = "1")
        Long id,

        @Schema(description = "예약자 이름", example = "홍길동")
        String name,

        @Schema(description = "테마 ID", example = "1")
        Long themeId,

        @Schema(description = "테마 이름", example = "비밀의 방")
        String theme,

        @Schema(description = "예약 날짜 (yyyy-MM-dd)", example = "2025-08-05")
        LocalDate date,

        @Schema(description = "예약 시간 정보")
        ReservationTime time
) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getUser().getName(),
                reservation.getTheme().getId(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime()
        );
    }
}
