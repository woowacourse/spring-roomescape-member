package roomescape.controller.api.dto.request;

import roomescape.service.dto.input.ReservationInput;

public record ReservationRequest(String name, String date, Long timeId, Long themeId) {

    public ReservationInput toInput() {
        return new ReservationInput(name, date, timeId, themeId);
    }
}
