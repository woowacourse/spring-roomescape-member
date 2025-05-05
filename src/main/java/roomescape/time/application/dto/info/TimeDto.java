package roomescape.time.application.dto.info;

import java.time.LocalTime;
import java.util.List;
import roomescape.time.domain.ReservationTime;

public record TimeDto(
        Long id,
        LocalTime startAt
) {
    public static TimeDto from(ReservationTime reservationTime) {
        return new TimeDto(reservationTime.getId(), reservationTime.getStartAt());
    }

    public static List<TimeDto> from(List<ReservationTime> reservationTimes) {
        return reservationTimes.stream()
                .map(TimeDto::from)
                .toList();
    }
}
