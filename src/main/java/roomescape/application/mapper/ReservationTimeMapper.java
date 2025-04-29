package roomescape.application.mapper;

import java.util.List;
import roomescape.domain.ReservationTime;
import roomescape.presentation.dto.request.TimeRequest;
import roomescape.presentation.dto.response.TimeResponse;

public class ReservationTimeMapper {

    public static ReservationTime toDomain(TimeRequest request) {
        return ReservationTime.withoutId(request.startAt());
    }

    public static TimeResponse toDto(ReservationTime reservationTime) {
        return new TimeResponse(reservationTime.getId(), reservationTime.getStartAt());
    }

    public static List<TimeResponse> toDtos(List<ReservationTime> reservationTimes) {
        return reservationTimes.stream()
                .map(ReservationTimeMapper::toDto)
                .toList();
    }
}
