package roomescape.controller.api.dto.request;

import roomescape.service.dto.input.ReservationTimeInput;

public record ReservationTimeCreateRequest(String startAt) {

    public ReservationTimeInput toInput() {
        return new ReservationTimeInput(startAt);
    }
}
