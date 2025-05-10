package roomescape.presentation.request;

import java.time.LocalDate;
import roomescape.application.param.CreateReservationParam;

public record CreateAdminReservationRequest(
        LocalDate date,
        Long timeId,
        Long themeId,
        Long memberId
) {
    public CreateReservationParam toServiceParam() {
        return new CreateReservationParam(date, timeId, themeId, memberId);
    }
}
