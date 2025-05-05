package roomescape.presentation.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

import roomescape.application.dto.request.CreateReservationServiceRequest;

public record CreateReservationRequest(
        @NotBlank(message = "예약자명을 빈 문자열이 아닌 값으로 입력해주세요.")
        String name,
        @NotNull(message = "날짜를 필수로 입력해야 합니다.")
        LocalDate date,
        @NotNull(message = "시간을 필수로 입력해야 합니다.")
        Long timeId,
        @NotNull(message = "테마를 필수로 입력해야 합니다.")
        Long themeId
) {

    public CreateReservationServiceRequest toServiceRequest() {
        return new CreateReservationServiceRequest(name, date, timeId, themeId);
    }
}
