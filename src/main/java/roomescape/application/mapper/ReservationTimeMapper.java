package roomescape.application.mapper;

import java.util.List;
import roomescape.application.dto.TimeDto;
import roomescape.domain.ReservationTime;
import roomescape.presentation.dto.request.TimeRequest;

public class ReservationTimeMapper {

    public static ReservationTime toDomain(TimeRequest request) {
        return ReservationTime.withoutId(request.startAt());
    }

    public static TimeDto toDto(ReservationTime reservationTime) {
        return new TimeDto(reservationTime.getId(), reservationTime.getStartAt());
    }

    public static List<TimeDto> toDtos(List<ReservationTime> reservationTimes) {
        return reservationTimes.stream()
                .map(ReservationTimeMapper::toDto)
                .toList();
    }
}
