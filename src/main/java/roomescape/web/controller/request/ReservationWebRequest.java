package roomescape.web.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import roomescape.service.request.ReservationRequest;
import roomescape.web.exception.DateValid;

public record ReservationWebRequest(
        @NotBlank(message = "예약 날짜는 필수입니다.") @DateValid String date,
        @NotNull(message = "예약 시간 ID는 필수입니다.") @Positive Long timeId,
        @NotNull(message = "예약 테마 ID는 필수입니다.") @Positive Long themeId
) {

    public ReservationRequest toServiceRequest(Long memberId) {
        return new ReservationRequest(date, memberId, timeId, themeId);
    }
}
