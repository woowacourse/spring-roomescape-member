package roomescape.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import roomescape.service.dto.request.ServiceReservationTimeRequest;

public record ControllerReservationTimeRequest(
        @NotNull(message = "[ERROR] 시간은 비어 있을 수 없습니다.")
        LocalTime startAt
) {
    public ServiceReservationTimeRequest toServiceReservationTimeRequest() {
        return new ServiceReservationTimeRequest(startAt);
    }
}
