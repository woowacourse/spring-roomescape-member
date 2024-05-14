package roomescape.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import roomescape.domain.ReservationTime;

public record TimeCreateRequest(
        Long id,
        @NotNull(message = "[ERROR] 시간은 비어있을 수 없습니다.")
        LocalTime startAt
) {

    public static ReservationTime toTime(final TimeCreateRequest request) {
        return new ReservationTime(request.id(), request.startAt());
    }
}
