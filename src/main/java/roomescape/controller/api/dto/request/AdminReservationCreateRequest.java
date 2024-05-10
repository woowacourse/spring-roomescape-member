package roomescape.controller.api.dto.request;

import roomescape.service.dto.input.ReservationInput;

public record AdminReservationCreateRequest(String date, Long timeId, Long themeId, Long memberId) {

    public ReservationInput toInput() {
        return ReservationInput.of(date, timeId, themeId, memberId);
    }
}
