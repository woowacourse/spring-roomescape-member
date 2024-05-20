package roomescape.controller.api.dto.request;

import roomescape.service.dto.input.ReservationInput;

public record ReservationCreateForUserRequest(String date, Long timeId, Long themeId) {

    public ReservationInput toInput(final Long memberId) {
        return ReservationInput.of(date, timeId, themeId, memberId);
    }
}
