package roomescape.controller.dto;

import roomescape.service.dto.input.ReservationTimeInput;

public record ReservationTimeRequest(String startAt) {

    public ReservationTimeInput toInput() {
        return new ReservationTimeInput(startAt);
    }
}
