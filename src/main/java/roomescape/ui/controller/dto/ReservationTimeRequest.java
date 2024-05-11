package roomescape.ui.controller.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import roomescape.application.dto.request.ReservationTimeCreationRequest;

public record ReservationTimeRequest(@NotNull(message = "시작 시간은 필수입니다.") LocalTime startAt) {
    public ReservationTimeCreationRequest toReservationCreationRequest() {
        return new ReservationTimeCreationRequest(startAt);
    }
}
