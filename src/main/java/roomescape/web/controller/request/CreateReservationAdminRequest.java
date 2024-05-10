package roomescape.web.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import roomescape.service.request.CreateReservationRequest;
import roomescape.web.exception.DateValid;

public record CreateReservationAdminRequest(
        @NotBlank(message = "예약 날짜는 필수입니다.") @DateValid String date,
        @NotNull(message = "예약 멤버 ID는 필수입니다.") @Positive Long memberId,
        @NotNull(message = "예약 시간 ID는 필수입니다.") @Positive Long timeId,
        @NotNull(message = "예약 테마 ID는 필수입니다.") @Positive Long themeId
) {

    public CreateReservationRequest toServiceRequest() {
        return new CreateReservationRequest(date, memberId, timeId, themeId);
    }
}
