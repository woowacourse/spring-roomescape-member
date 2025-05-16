package roomescape.reservation.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AdminReservationRequest(
        @NotNull(message = "예약 날짜를 입력해 주세요.")
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate date,
        @NotNull(message = "시간을 선택해 주세요.")
        Long timeId,
        @NotNull(message = "테마를 선택해 주세요.")
        Long themeId,
        @NotNull(message = "사용자를 선택해 주세요.")
        Long memberId
) {

    public ReservationCreate toReservationCreate() {
        return new ReservationCreate(date, timeId, themeId, memberId);
    }

}
