package roomescape.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import roomescape.service.dto.request.ServiceReservationTimeCreateRequest;

public record ControllerReservationTimeCreateRequest(
        @NotNull(message = "[ERROR] 시간은 비어 있을 수 없습니다.")
        LocalTime startAt
) {
    public ServiceReservationTimeCreateRequest toServiceReservationTimeRequest() {
        return new ServiceReservationTimeCreateRequest(startAt);
    }
}
