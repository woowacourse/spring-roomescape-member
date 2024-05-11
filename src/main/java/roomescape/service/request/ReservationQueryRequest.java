package roomescape.service.request;

import roomescape.web.exception.DateValid;

public record ReservationQueryRequest(
        Long themeId,
        Long memberId,
        @DateValid String fromDate,
        @DateValid String endDate
) {
}
