package roomescape.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.service.dto.request.ServiceReservationUpdateRequest;

public record ControllerReservationUpdateRequest(
        @NotNull(message = "[ERROR] 날짜는 비어 있을 수 없습니다.")
        LocalDate date,

        @NotNull(message = "[ERROR] 예약 시간의 id는 비어 있을 수 없습니다.")
        Long timeId
) {
    public ServiceReservationUpdateRequest toServiceReservationRequest() {
        return new ServiceReservationUpdateRequest(date, timeId);
    }
}
