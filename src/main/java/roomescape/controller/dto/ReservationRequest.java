package roomescape.controller.dto;

import roomescape.service.dto.ReservationInput;

public record ReservationRequest(String name, String date, Long timeId) {

    public ReservationInput toInput() {
        return new ReservationInput(name, date, timeId);
    }
}
