package roomescape.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AdminReservationSaveRequest(
        @NotNull(message = "예약 날짜는 널일 수 없습니다.") LocalDate date,
        @NotBlank(message = "사용자 id는 널일 수 없습니다.") Long memberId,
        @NotNull(message = "시간 id는 널일 수 없습니다.") Long timeId,
        @NotNull(message = "테마 id는 널일 수 없습니다.") Long themeId) {

    public ReservationSaveRequest toReservationSaveRequest() {
        return new ReservationSaveRequest(date, timeId, themeId);
    }
}
