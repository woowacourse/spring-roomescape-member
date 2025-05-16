package roomescape.reservation.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record UserReservationRequest(
        @NotNull(message = "예약 날짜를 입력해 주세요.")
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate date,
        @NotNull(message = "시간을 선택해 주세요.")
        Long timeId,
        @NotNull(message = "테마를 선택해 주세요.")
        Long themeId
) {

    public ReservationCreate toReservationCreate(Long loginMemberId) {
        return new ReservationCreate(date, timeId, themeId, loginMemberId);
    }

}
